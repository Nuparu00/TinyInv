package nuparu.tinyinv.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.capabilities.CapabilityHelper;
import nuparu.tinyinv.capabilities.IExtendedPlayer;
import nuparu.tinyinv.network.PacketManager;
import nuparu.tinyinv.network.packets.FixClientContainerPacket;
import nuparu.tinyinv.world.inventory.SlotUtils;

import java.util.HashSet;

@Mod.EventBusSubscriber(modid = TinyInv.MODID)
public class TickHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
        if(iep == null) return;
        if(player.level().isClientSide()){
            int i = 0;
        }
        if(player == null || player.level().isClientSide()) return;
        if (event.phase == TickEvent.Phase.START && iep.getGracePeriod() == 0) {
            boolean flag = false;
            for(int slot = 0; slot < player.getInventory().getContainerSize(); slot++){
                if(SlotUtils.shouldRemoveSlot(player, slot) && SlotUtils.replaceSlotStack(player,slot)){
                    flag = true;
                }
            }
            SlotUtils.fixCraftingSlots(player);
            if(flag){
                PlayerEventHandler.schedulePlayerForUpdate((ServerPlayer) player);
            }
        }
        if (event.phase == TickEvent.Phase.END) {
            iep.tick(player);
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            HashSet<ServerPlayer> players = new HashSet<>(PlayerEventHandler.playersToUpdate);
            PlayerEventHandler.playersToUpdate.removeAll(players);
            for(ServerPlayer player : players){
                SlotUtils.fixContainer(player.containerMenu, player);
                PacketManager.sendTo(PacketManager.fixClientContainer,new FixClientContainerPacket(),player);
            }
        }
    }
}
