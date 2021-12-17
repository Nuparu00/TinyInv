package nuparu.tinyinv;

import nuparu.tinyinv.config.ConfigHelper;
import nuparu.tinyinv.events.ClientEventHandler;
import nuparu.tinyinv.events.PlayerInventoryEventHandler;
import nuparu.tinyinv.events.TickHandler;
import nuparu.tinyinv.item.ItemFake;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;


@Mod(TinyInv.MODID)
public class TinyInv {
    public static final String MODID = "tinyinv";

    private static Logger logger;

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TinyInv.MODID);
    public static final RegistryObject<Item> FAKE_ITEM = ITEMS.register("fake_item", () -> new ItemFake());

    public TinyInv() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHelper.commonConfig);
        ConfigHelper.loadConfig(ConfigHelper.commonConfig,
                FMLPaths.CONFIGDIR.get().resolve("tinyinv-common.toml").toString());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHelper.clientConfig);
        ConfigHelper.loadConfig(ConfigHelper.clientConfig,
                FMLPaths.CONFIGDIR.get().resolve("tinyinv-client.toml").toString());

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        ITEMS.register(bus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerInventoryEventHandler());

    }


    private void setup(final FMLCommonSetupEvent event) {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }

}
