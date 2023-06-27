package nuparu.tinyinv;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import nuparu.tinyinv.config.ConfigHelper;
import nuparu.tinyinv.init.ModItems;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TinyInv.MODID)
public class TinyInv
{
    public static final String MODID = "tinyinv";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TinyInv()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHelper.serverConfig);
        ConfigHelper.loadConfig(ConfigHelper.serverConfig,
                FMLPaths.CONFIGDIR.get().resolve("tinyinv-server.toml").toString());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHelper.clientConfig);
        ConfigHelper.loadConfig(ConfigHelper.clientConfig,
                FMLPaths.CONFIGDIR.get().resolve("tinyinv-client.toml").toString());

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);
    }

}
