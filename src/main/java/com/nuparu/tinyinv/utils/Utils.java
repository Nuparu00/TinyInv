package com.nuparu.tinyinv.utils;

import com.nuparu.tinyinv.config.ModConfig;
import com.nuparu.tinyinv.inventory.FakeSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import java.util.Iterator;

public class Utils {

    /*
    Replaces Vanilla player slots by "Fake slots"
     */
    public static void fixContainer(Container container, EntityPlayer player) {

        for (int i = 0; i < container.inventorySlots.size(); i++) {
            Slot slot = container.inventorySlots.get(i);
            if (shouldBeRemoved(slot, player)) {
                container.inventorySlots.set(i, new FakeSlot(slot.inventory, slot.getSlotIndex(), slot.xPos, slot.yPos));
            }
        }

    }

    public static boolean shouldBeRemoved(Slot slot, EntityPlayer player) {
        if (slot.inventory != player.inventory) return false;
        return shouldBeRemoved(slot.getSlotIndex(),player);
    }

    public static boolean shouldBeRemoved(int id, EntityPlayer player){
        if (ModConfig.common.disableOffhand && id == 40 && !player.isCreative()) return true;

        if (ModConfig.common.countSlotsFromStart) {
            return (id >= ModConfig.common.inventorySlots && id < ModConfig.common.armorStartID);
        }
        return (id < ModConfig.common.armorStartID) && ((id < 9 && id >= ModConfig.common.inventorySlots) ||
                (id >= 9 && id <= ModConfig.common.armorStartID - 1 - (Math.max(ModConfig.common.inventorySlots - 9, 0))));
    }

    public static int getHotbarSlots() {
        return Math.min(Math.min(ModConfig.common.inventorySlots, ModConfig.common.hotbarSlots), 9);
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
