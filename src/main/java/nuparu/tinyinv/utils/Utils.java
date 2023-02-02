package nuparu.tinyinv.utils;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.inventory.FakeSlot;

public class Utils {
    /*
    Replaces Vanilla player slots by "Fake slots"
     */
    public static void fixContainer(AbstractContainerMenu container, Player player) {

        for (int i = 0; i < container.slots.size(); i++) {
            Slot slot = container.slots.get(i);
            if (shouldBeRemoved(slot, player,container)) {
                System.out.println("shouldBeRemoved " + player
                        .isCreative() + " " + ServerConfig.excludeCreativeModePlayers.get());
                container.slots.set(i, new FakeSlot(slot.container, slot.getSlotIndex(), slot.x, slot.y,player));
            }
        }

    }

    public static boolean shouldBeRemoved(Slot slot, Player player, AbstractContainerMenu container) {
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return false;
        if (slot.container != player.getInventory()) return false;
        return shouldBeRemoved(slot.getSlotIndex(),player,container);
    }

    public static boolean shouldBeHidden(Slot slot, Player player, AbstractContainerMenu container) {
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return false;
        if (slot.container != player.getInventory()) return false;
        int id = slot.getSlotIndex();
        return shouldBeRemoved(id,player,slot.container) || (ClientConfig.hideOffhand.get() && isOffhandSlot(slot,player));
    }

    public static boolean shouldBeRemoved(int id, Player player, Object container){
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return false;
        if (ServerConfig.disableOffhand.get() && isOffhandSlot(id,player,container)) return true;

        if (ServerConfig.countSlotsFromStart.get()) {
            return (id >= ServerConfig.inventorySlots.get() && id < ServerConfig.armorStartID.get());
        }
        return (id < ServerConfig.armorStartID.get()) && ((id < 9 && id >= ServerConfig.inventorySlots.get()) ||
                (id >= 9 && id <= ServerConfig.armorStartID.get() - 1 - (Math.max(ServerConfig.inventorySlots.get() - 9, 0))));
    }


    public static boolean isOffhandSlot(Slot slot, Player player) {
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return false;
        return slot.getSlotIndex() == Inventory.SLOT_OFFHAND && (slot.container instanceof InventoryMenu || slot.container instanceof Inventory);
    }

    public static boolean isOffhandSlot(int id, Player player, Object container) {
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return false;
        return id == Inventory.SLOT_OFFHAND && (container instanceof InventoryMenu || container instanceof Inventory);
    }

    public static int getHotbarSlots(@Nullable  Player player) {
        //if(player != null && player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return 9;
        return Math.min(ServerConfig.inventorySlots.get(), ServerConfig.hotbarSlots.get());
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
