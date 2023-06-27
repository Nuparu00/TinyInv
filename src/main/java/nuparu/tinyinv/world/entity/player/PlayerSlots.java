package nuparu.tinyinv.world.entity.player;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.init.ModAttributes;
import nuparu.tinyinv.world.inventory.SlotUtils;

public class PlayerSlots {
    public static int getSlots(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return Inventory.INVENTORY_SIZE;
        return (int) (ServerConfig.inventorySlots.get() + player.getAttributeValue(ModAttributes.SLOTS.get()));
    }
    public static int getHotbarSlots(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return Inventory.getSelectionSize();
        return Math.min(getSlots(player), (int) (ServerConfig.hotbarSlots.get() + player.getAttributeValue(ModAttributes.HOTBAR_SLOTS.get())));
    }
    public static boolean getOffhandSlot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.offhandSlot.get()) + player.getAttributeValue(ModAttributes.OFFHAND_SLOT.get())));
    }
    public static boolean getHeadSlot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.headSlot.get()) + player.getAttributeValue(ModAttributes.HEAD_SLOT.get())));
    }
    public static boolean getChestSlot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.chestSlot.get()) + player.getAttributeValue(ModAttributes.CHEST_SLOT.get())));
    }
    public static boolean getLegsSLot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.legsSlot.get()) + player.getAttributeValue(ModAttributes.LEGS_SLOT.get())));
    }
    public static boolean getFeetSlot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.feetSlot.get()) + player.getAttributeValue(ModAttributes.FEET_SLOT.get())));
    }
    public static boolean getCraftingTopLeftSlot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.craftingTLSlot.get()) + player.getAttributeValue(ModAttributes.CRAFT_TOP_LEFT_SLOT.get())));
    }
    public static boolean getCraftingTopRightSlot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.craftingTRSlot.get()) + player.getAttributeValue(ModAttributes.CRAFT_TOP_RIGHT_SLOT.get())));
    }
    public static boolean getCraftingBottomRightSlot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.craftingBRSlot.get()) + player.getAttributeValue(ModAttributes.CRAFT_BOTTOM_RIGHT_SLOT.get())));
    }
    public static boolean getCraftingBottomLeftSlot(Player player){
        if (SlotUtils.shouldPlayerBeExcluded(player)) return true;
        return intToBoolean((int) (booleanToInt(ServerConfig.craftingBLSlot.get()) + player.getAttributeValue(ModAttributes.CRAFT_BOTTOM_LEFT_SLOT.get())));
    }

    private static int booleanToInt(boolean bool){
        return bool ? 1 : 0;
    }

    private static boolean intToBoolean(int i){
        return i > 0;
    }


}
