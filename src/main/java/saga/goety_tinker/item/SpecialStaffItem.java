package saga.goety_tinker;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import java.util.ArrayList;

public class SpecialStaffItem extends ModifiableItem {
    public SpecialStaffItem(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    // 攻撃時にデバフを付与する（絶対解除不能仕様）
    @Override
    public boolean dealDamage(IToolStackView tool, LivingEntity player, LivingEntity target, float damage, boolean isCritical, boolean fullyCharged) {
        boolean result = super.dealDamage(tool, player, target, damage, isCritical, fullyCharged);

        if (result && !target.level().isClientSide) {
            // 例：移動速度低下を付与
            MobEffectInstance effect = new MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN, 200, 2);

            // 【重要】時間切れ以外に絶対解除できない仕様にする
            // curativeItemsを空にすることで、ミルクなどでの解除を封じます
            effect.setCurativeItems(new ArrayList<>());

            target.addEffect(effect);
        }
        return result;
    }

    // Goetyのフォーカス機能などはここに追加のロジックを実装します
}
