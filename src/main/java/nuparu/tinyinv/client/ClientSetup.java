package nuparu.tinyinv.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.client.gui.overlay.DebugOverlay;

@Mod.EventBusSubscriber(modid= TinyInv.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        //event.registerAboveAll(TinyInv.MODID+"__upgrade", new DebugOverlay(Minecraft.getInstance()));
    }
}
