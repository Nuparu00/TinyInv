package nuparu.tinyinv.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.capabilities.CapabilityHelper;
import nuparu.tinyinv.capabilities.IExtendedPlayer;
import nuparu.tinyinv.world.inventory.SlotUtils;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber
public class PlayerEventHandler {
    public static final Set<ServerPlayer> playersToUpdate = new HashSet<>();

    @SubscribeEvent
    public static void onContainerEvent(PlayerContainerEvent.Open event){
        AbstractContainerMenu container = event.getContainer();
        Player player = event.getEntity();
        SlotUtils.fixContainer(container,player);
    }
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
        if(iep == null) return;
        iep.setGracePeriod(40);
    }

    @SubscribeEvent
    public static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
        if(event.getEntity() instanceof ServerPlayer player) {
            schedulePlayerForUpdate(player);
        }
    }

    public static void schedulePlayerForUpdate(ServerPlayer player){
        playersToUpdate.add(player);
    }
}
