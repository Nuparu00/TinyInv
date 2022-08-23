package nuparu.tinyinv.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.utils.Utils;

@Mod.EventBusSubscriber
public class PlayerInventoryEventHandler {

    @SubscribeEvent
    public static void onContainerEvent(PlayerContainerEvent.Open event){
        AbstractContainerMenu container = event.getContainer();
        Player player = event.getPlayer();
        Utils.fixContainer(container,player);
    }
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        Utils.fixContainer(player.containerMenu,player);
    }
}
