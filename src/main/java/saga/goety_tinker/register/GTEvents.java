package saga.goety_tinker.register;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import saga.goety_tinker.event.GTSpellHandler;
import saga.goety_tinker.event.SummonStrengthHandler;

public class GTEvents {
    public static void register(IEventBus modEventBus) {
        // 今後イベントハンドラが増えたらここに追記していく
        MinecraftForge.EVENT_BUS.register(new SummonStrengthHandler());
        MinecraftForge.EVENT_BUS.register(new SummonStrengthHandler());
        MinecraftForge.EVENT_BUS.register(new GTSpellHandler()); // 詠唱処理を追加
        // 例: MinecraftForge.EVENT_BUS.register(new MagicAttackHandler());
    }
}
