package saga.goety_tinker.event;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import saga.goety_tinker.item.SpecialStaffItem;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

// MOD_IDの解決ができない場合は直接文字列で指定
@Mod.EventBusSubscriber(modid = "goety_tinker")
public class GTSpellHandler {

    @SubscribeEvent
    public static void onUsingTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof Player player && event.getItem().getItem() instanceof SpecialStaffItem staff) {
            ItemStack stack = event.getItem();
            ISpell spell = staff.getSpell(stack);
            if (spell != null) {
                staff.useParticles(player.level(), player, stack, spell);
                if (player.level() instanceof ServerLevel serverLevel) {
                    int useTime = event.getItem().getUseDuration() - event.getDuration();
                    spell.useSpell(serverLevel, player, stack, useTime, WandUtil.getStats(player, spell));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onFinishCast(LivingEntityUseItemEvent.Stop event) {
        if (event.getEntity() instanceof Player player && event.getItem().getItem() instanceof SpecialStaffItem staff) {
            ItemStack stack = event.getItem();
            ISpell spell = staff.getSpell(stack);

            if (spell != null && !player.level().isClientSide) {
                int useTime = event.getItem().getUseDuration() - event.getDuration();
                if (useTime >= spell.castDuration(player, stack)) {
                    ToolStack tool = ToolStack.from(stack);
                    float toolDamage = tool.getStats().get(ToolStats.ATTACK_DAMAGE);
                    SpellStat spellStat = new SpellStat(useTime, 0, 0, 1.0D + (toolDamage * 0.15D), 0, 0.0F);

                    // 修正点: spell.SpellResult が void のバージョンに対応
                    spell.SpellResult((ServerLevel) player.level(), player, stack, spellStat);

                    if (!player.getAbilities().instabuild) {
                        ToolDamageUtil.damage(tool, 1, player, stack);
                    }
                }
            }
        }
    }
}