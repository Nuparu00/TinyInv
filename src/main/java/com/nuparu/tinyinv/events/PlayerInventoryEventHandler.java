package com.nuparu.tinyinv.events;

import com.nuparu.tinyinv.config.ModConfig;
import com.nuparu.tinyinv.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class PlayerInventoryEventHandler {

    @SubscribeEvent
    public static void onContainerEvent(PlayerContainerEvent.Open event){
        Container container = event.getContainer();
        EntityPlayer player = event.getEntityPlayer();
        Utils.fixContainer(container,player);
    }
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer)event.getEntity();
        Utils.fixContainer(player.inventoryContainer,player);
    }
}
