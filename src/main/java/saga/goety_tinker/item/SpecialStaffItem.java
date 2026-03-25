package saga.goety_tinker.item;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.items.capability.SoulUsingItemCapability;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import saga.goety_tinker.modifier.SpellTypeModifier;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.MaterialNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SpecialStaffItem extends ModifiableItem implements IWand, IModifiable {

    public SpecialStaffItem(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    // --- 1. TiC 道具認識の強化 ---

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        // NBTが空の場合、強制的に初期化を試みて「Missing Tool Data」を回避する
        if (!world.isClientSide && stack.getTag() == null) {
            ToolStack.ensureInitialized(stack, getToolDefinition());
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        ToolStack.ensureInitialized(stack, getToolDefinition());
    }

    // --- 2. Goety 属性システム ---

    @Override
    public SpellType getSpellType() {
        return SpellType.NONE;
    }

    public SpellType getSpellType(ItemStack stack) {
        return this.getCustomSpellType(stack);
    }

    public SpellType getCustomSpellType(ItemStack stack) {
        try {
            ToolStack tool = ToolStack.from(stack);
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() instanceof SpellTypeModifier typeMod) {
                    return typeMod.getSpellType();
                }
            }
        } catch (Exception ignored) {}
        return this.getSpellType();
    }

    // --- 3. 詠唱・発動ロジック ---

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(stack);

        // TiCのツールとして壊れている場合は使用不可
        if (tool.isBroken()) {
            return InteractionResultHolder.fail(stack);
        }

        ISpell spell = this.getSpell(stack);
        if (spell != null && !this.cannotCast(player, stack, spell)) {
            if (spell.CastingSound(player) != null) {
                player.playSound(spell.CastingSound(player), spell.castingVolume(), spell.castingPitch());
            }
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(world, player, hand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (!world.isClientSide && world instanceof ServerLevel serverLevel && entity instanceof Player player) {
            ISpell spell = this.getSpell(stack);
            if (spell != null && !this.cannotCast(player, stack, spell)) {
                com.Polarice3.Goety.common.magic.SpellStat stats = WandUtil.getStats(player, spell);
                spell.SpellResult(serverLevel, player, stack, stats);

                if (!player.getAbilities().instabuild) {
                    ItemStack focus = IWand.getFocus(stack);
                    if (!focus.isEmpty()) {
                        player.getCooldowns().addCooldown(focus.getItem(), 20);
                    }
                }
            }
        }
    }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BOW; }
    @Override public int getUseDuration(ItemStack stack) { return 72000; }

    // --- 4. Capability 合体 (最重要修正箇所) ---

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        // super.initCapabilitiesを呼び、TiC公式のProviderを確実に取得する
        ICapabilityProvider parent = super.initCapabilities(stack, nbt);
        return new CombinedProvider(stack, parent);
    }

    private static class CombinedProvider implements ICapabilityProvider {
        private final ICapabilityProvider ticParent;
        private final SoulUsingItemCapability goety;

        public CombinedProvider(ItemStack stack, ICapabilityProvider ticParent) {
            this.ticParent = ticParent;
            this.goety = new SoulUsingItemCapability(stack);
        }

        @Override
        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            // 1. TiC本来の能力(ToolStack/NBT操作)を優先して返す
            // これをしないと、組み立て台でツールとして認識されない
            LazyOptional<T> t = ticParent.getCapability(cap, side);
            if (t.isPresent()) return t;

            // 2. 次にGoety側の能力(Soul使用など)を返す
            return goety.getCapability(cap, side);
        }
    }

    // --- 5. 素材色反映ロジック ---

    public int getColor(@NotNull ItemStack stack, int tintIndex) {
        // tintIndex 0はツールのベース、1以降が各パーツの色
        if (tintIndex > 0) {
            try {
                ToolStack ts = ToolStack.from(stack);
                MaterialNBT materials = ts.getMaterials();
                // パーツ数に応じて色を取得
                if (tintIndex - 1 < materials.size()) {
                    MaterialVariantId material = materials.get(tintIndex - 1).getVariant();
                    Optional<MaterialRenderInfo> info = MaterialRenderInfoLoader.INSTANCE.getRenderInfo(material);
                    if (info.isPresent()) {
                        // 頂点カラーからRGB値を抽出
                        return info.get().vertexColor() & 0x00FFFFFF;
                    }
                }
            } catch (Exception ignored) {}
        }
        return 0xFFFFFF; // デフォルトは白
    }
}