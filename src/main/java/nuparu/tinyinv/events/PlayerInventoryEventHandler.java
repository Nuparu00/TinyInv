package nuparu.tinyinv.events;

import nuparu.tinyinv.utils.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerInventoryEventHandler {

    @SubscribeEvent
    public static void onContainerEvent(PlayerContainerEvent.Open event){
        Container container = event.getContainer();
        PlayerEntity player = event.getPlayer();
        Utils.fixContainer(container,player);
    }
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity)event.getEntity();
        Utils.fixContainer(player.containerMenu,player);
    }
}
