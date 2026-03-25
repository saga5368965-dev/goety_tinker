package saga.goety_tinker.modifier;

import com.Polarice3.Goety.api.magic.SpellType;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import saga.goety_tinker.register.GTModifiers;

public class NecromancyModifier extends SpellTypeModifier {
    public NecromancyModifier() {
        super(SpellType.NECROMANCY);
    }

    public static int getNecroLevel(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        try {
            ToolStack tool = ToolStack.from(stack);
            // GTModifiersに登録した自身のStaticModifierから現在のレベルを取得
            return tool.getModifierLevel(GTModifiers.NECROMANCY.get());
        } catch (Exception e) {
            return 0;
        }
    }
}