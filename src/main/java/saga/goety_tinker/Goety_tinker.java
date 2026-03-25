package saga.goety_tinker;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import saga.goety_tinker.register.GTEvents;
import saga.goety_tinker.register.GTItems;
import saga.goety_tinker.register.GTModifiers;

@Mod(Goety_tinker.MODID)
public class Goety_tinker {
    public static final String MODID = "goety_tinker";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Goety_tinker() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 過去の成功例に基づき、2つのレジストリを個別に登録する
        GTItems.ITEMS.register(modEventBus);
        GTItems.TINKER_ITEMS.register(modEventBus);

        // Modifierの登録
        GTModifiers.MODIFIERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            GTEvents.register(modEventBus);
        });

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Goety Tinker: Common Setup Initializing...");
        });
    }

    public static ResourceLocation resource(String name) {
        return new ResourceLocation(MODID, name);
    }
}