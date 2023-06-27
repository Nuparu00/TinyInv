package nuparu.tinyinv.event;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.data.attributes.AttributeDataManager;
import nuparu.tinyinv.network.PacketManager;
import nuparu.tinyinv.network.packets.SyncItemAttributeDataToClient;

@Mod.EventBusSubscriber(modid = TinyInv.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DataEventHandler {
    @SubscribeEvent
    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(AttributeDataManager.INSTANCE);
    }
    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        System.out.println("SYNC PLAYER");
        if(event.getPlayer() != null){
            PacketManager.sendTo(PacketManager.syncItemAttributeData,new SyncItemAttributeDataToClient(), event.getPlayer());
        }
        else{
            event.getPlayerList().getPlayers()
                    .forEach(player -> PacketManager.sendTo(PacketManager.syncItemAttributeData,new SyncItemAttributeDataToClient(), player));
        }
    }
}
