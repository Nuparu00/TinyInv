package nuparu.tinyinv;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nuparu.tinyinv.config.ConfigHelper;
import nuparu.tinyinv.init.ModAttributes;
import nuparu.tinyinv.init.ModCreativeTabs;
import nuparu.tinyinv.init.ModItems;
import nuparu.tinyinv.network.PacketManager;
import nuparu.tinyinv.proxy.ClientProxy;
import nuparu.tinyinv.proxy.CommonProxy;
import org.slf4j.Logger;

@Mod(TinyInv.MODID)
public class TinyInv
{
    public static final String MODID = "tinyinv";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new,
            () -> CommonProxy::new);



    public TinyInv()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHelper.serverConfig);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHelper.clientConfig);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);

        ModItems.ITEMS.register(eventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(eventBus);
        ModAttributes.ATTRIBUTES.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketManager.setup();
    }
}
