package saga.goety_tinker.event;

import com.Polarice3.Goety.api.entities.IOwned;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import saga.goety_tinker.item.SpecialStaffItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;

public class SummonStrengthHandler {

    // 1. スポーン時のステータス底上げ（Funnelの handleTinkersAttack のような基礎強化）
    @SubscribeEvent
    public void onSummonJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity summoned && summoned instanceof IOwned owned) {
            LivingEntity owner = owned.getTrueOwner();
            if (owner != null && owner.getMainHandItem().getItem() instanceof SpecialStaffItem stack) {
                ToolStack tool = ToolStack.from(owner.getMainHandItem());

                // 基礎攻撃力と体力を反映
                float staffDamage = tool.getStats().get(ToolStats.ATTACK_DAMAGE);
                summoned.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(
                        summoned.getAttributeValue(Attributes.ATTACK_DAMAGE) + (staffDamage * 0.5)
                );
                summoned.getAttribute(Attributes.MAX_HEALTH).setBaseValue(
                        summoned.getAttributeValue(Attributes.MAX_HEALTH) + staffDamage
                );
                summoned.setHealth(summoned.getMaxHealth());
            }
        }
    }

    // 2. 攻撃時の特性反映（Funnelの executeAttack と同じ考え方）
    @SubscribeEvent
    public void onServantAttack(LivingAttackEvent event) {
        // 攻撃者がGoetyの配下モブかチェック
        if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker instanceof IOwned owned) {
            LivingEntity owner = owned.getTrueOwner();
            if (owner != null && owner.getMainHandItem().getItem() instanceof SpecialStaffItem) {
                ToolStack tool = ToolStack.from(owner.getMainHandItem());
                LivingEntity target = event.getEntity();

                // --- 特性の反映ループ ---
                for (ModifierEntry entry : tool.getModifierList()) {
                    // 例: Fiery(火属性)が付いていたら相手を燃やす
                    // entry.getModifier().getId().getPath().equals("fiery")
                    // もしくは各特性のIDで判定
                    if (entry.getModifier().getDisplayName().getString().contains("Fiery")) {
                        target.setSecondsOnFire(3 * entry.getLevel());
                    }

                    // 例: Lifesteal(吸血)が付いていたら召喚モブを回復させる
                    if (entry.getModifier().getDisplayName().getString().contains("Lifesteal")) {
                        attacker.heal(entry.getLevel() * 0.5f);
                    }
                }
            }
        }
    }
}