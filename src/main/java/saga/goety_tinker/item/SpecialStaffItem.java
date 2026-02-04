package saga.goety_tinker.item;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.items.capability.SoulUsingItemCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import saga.goety_tinker.client.renderer.SpecialStaffRenderer;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SpecialStaffItem extends ModifiableItem implements IWand, IModifiable {

    public SpecialStaffItem(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NONE;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new CombinedProvider(stack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new SpecialStaffRenderer(
                        Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                        Minecraft.getInstance().getEntityModels()
                );
            }
        });
    }

    private static class CombinedProvider implements ICapabilityProvider {
        private final ToolCapabilityProvider tic;
        private final SoulUsingItemCapability goety;

        public CombinedProvider(ItemStack stack) {
            this.tic = new ToolCapabilityProvider(stack);
            this.goety = new SoulUsingItemCapability(stack);
        }

        @Override
        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            LazyOptional<T> g = goety.getCapability(cap, side);
            if (g.isPresent()) return g;
            return tic.getCapability(cap, side);
        }
    }

    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        return super.getShareTag(stack);
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
    }
}