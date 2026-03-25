package saga.goety_tinker.modifier;

import com.Polarice3.Goety.api.magic.SpellType;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SpellTypeModifier extends Modifier {
    private final SpellType type;

    public SpellTypeModifier(SpellType type) {
        super();
        this.type = type;
    }

    public SpellType getSpellType() {
        return this.type;
    }

    // TiC 3.x系での正しいオーバーライド
    // どのツールにもこの属性モディファイアを付けられるようにします
    @Override
    public boolean shouldDisplay(boolean advanced) {
        return true;
    }

    // もし「特定のツールにしか付けたくない」という制限が不要なら、
    // allowedOn メソッド自体を書かなくてもデフォルトで「許可」になります。
}
