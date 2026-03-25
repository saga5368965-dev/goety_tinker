package saga.goety_tinker.register;

import com.Polarice3.Goety.api.magic.SpellType;
import saga.goety_tinker.Goety_tinker;
import saga.goety_tinker.modifier.NecromancyModifier;
import saga.goety_tinker.modifier.SpellTypeModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class GTModifiers {
    public static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(Goety_tinker.MODID);

    // 各属性を個別に登録（これで「Frost」と「Necromancy」を両方付けられるようになる）
    public static final StaticModifier<NecromancyModifier> NECROMANCY = MODIFIERS.register("necromancy", NecromancyModifier::new);
    public static final StaticModifier<SpellTypeModifier> FROST = MODIFIERS.register("frost", () -> new SpellTypeModifier(SpellType.FROST));
    public static final StaticModifier<SpellTypeModifier> NETHER = MODIFIERS.register("nether", () -> new SpellTypeModifier(SpellType.NETHER));
    public static final StaticModifier<SpellTypeModifier> STORM = MODIFIERS.register("storm", () -> new SpellTypeModifier(SpellType.STORM));
    public static final StaticModifier<SpellTypeModifier> VOID = MODIFIERS.register("void", () -> new SpellTypeModifier(SpellType.VOID));
    public static final StaticModifier<SpellTypeModifier> WIND = MODIFIERS.register("wind", () -> new SpellTypeModifier(SpellType.WIND));
    public static final StaticModifier<SpellTypeModifier> WILD = MODIFIERS.register("wild", () -> new SpellTypeModifier(SpellType.WILD));
    public static final StaticModifier<SpellTypeModifier> ABYSS = MODIFIERS.register("abyss", () -> new SpellTypeModifier(SpellType.ABYSS));
    public static final StaticModifier<SpellTypeModifier> GEOMANCY = MODIFIERS.register("geomancy", () -> new SpellTypeModifier(SpellType.GEOMANCY));

    public static void register(net.minecraftforge.eventbus.api.IEventBus bus) {
        MODIFIERS.register(bus);
    }
}
