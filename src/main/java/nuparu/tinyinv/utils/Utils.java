package nuparu.tinyinv.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.config.CommonConfig;
import nuparu.tinyinv.inventory.FakeSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Utils {

    /*
    Replaces Vanilla player slots by "Fake slots"
     */
    public static void fixContainer(Container container, PlayerEntity player) {

        for (int i = 0; i < container.slots.size(); i++) {
            Slot slot = container.slots.get(i);
            if (shouldBeRemoved(slot, player,container)) {
                container.slots.set(i, new FakeSlot(slot.container, slot.getSlotIndex(), slot.x, slot.y));
            }
        }

    }

    public static boolean shouldBeRemoved(Slot slot, PlayerEntity player, Container container) {
        if (slot.container != player.inventory) return false;
        return shouldBeRemoved(slot.getSlotIndex(),player,container);
    }

    public static boolean shouldBeHidden(Slot slot, PlayerEntity player, Container container) {
        if (slot.container != player.inventory) return false;
        int id = slot.getSlotIndex();
        return shouldBeRemoved(id,player,slot.container) || (ClientConfig.hideOffhand.get() && isOffhandSlot(slot,player));
    }

    public static boolean shouldBeRemoved(int id, PlayerEntity player, Object container){
        if (CommonConfig.disableOffhand.get() && isOffhandSlot(id,player,container)) return true;

        if (CommonConfig.countSlotsFromStart.get()) {
            return (id >= CommonConfig.inventorySlots.get() && id < CommonConfig.armorStartID.get());
        }
        return (id < CommonConfig.armorStartID.get()) && ((id < 9 && id >= CommonConfig.inventorySlots.get()) ||
                (id >= 9 && id <= CommonConfig.armorStartID.get() - 1 - (Math.max(CommonConfig.inventorySlots.get() - 9, 0))));
    }


    public static boolean isOffhandSlot(Slot slot, PlayerEntity player) {
        return slot.getSlotIndex() == 40 && (slot.container instanceof PlayerContainer || slot.container instanceof PlayerInventory);
    }

    public static boolean isOffhandSlot(int id, PlayerEntity player, Object container) {
        return id == 40 && (container instanceof PlayerContainer || container instanceof PlayerInventory);
    }

    public static int getHotbarSlots() {
        return Math.min(Math.min(CommonConfig.inventorySlots.get(), CommonConfig.hotbarSlots.get()), 9);
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}
