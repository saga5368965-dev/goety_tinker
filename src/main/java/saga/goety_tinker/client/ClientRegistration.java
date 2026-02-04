package saga.goety_tinker.client;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import saga.goety_tinker.Goety_tinker;

@Mod.EventBusSubscriber(modid = Goety_tinker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        event.register(new ModelResourceLocation(Goety_tinker.MODID, "staff_handle", "inventory"));
        event.register(new ModelResourceLocation(Goety_tinker.MODID, "staff_core", "inventory"));
        event.register(new ModelResourceLocation(Goety_tinker.MODID, "staff_head", "inventory"));
    }
}
