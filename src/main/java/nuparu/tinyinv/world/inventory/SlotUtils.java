package nuparu.tinyinv.world.inventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import nuparu.tinyinv.config.Indexing;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.event.PlayerEventHandler;
import nuparu.tinyinv.init.ModItems;
import nuparu.tinyinv.world.entity.player.PlayerSlots;

import java.util.function.BiFunction;

public class SlotUtils {

    /**
     * Determines whether a slot (the entity inside a container/gui) should be hidden
     *
     * @param slot
     * @param player
     * @param container
     * @return
     */
    public static boolean shouldHideSlot(Slot slot, Player player, AbstractContainerMenu container) {
        if (isInventorySlot(container, slot, player)) {
            return shouldRemoveSlot(player, slot.getSlotIndex());
        } else if (isCraftingSlot(container, slot, player)) {
            return shouldRemoveCraftingSlot(player, slot.getSlotIndex());
        } else if (isCraftingResultSlot(container, slot, player)) {
            return shouldRemoveCraftingResultSlot(player, slot.getSlotIndex());
        }
        return false;
    }

    /**
     * Determines whether a slot (logically the place in the list of stacks) should be considered removed
     *
     * @param player
     * @param slot
     * @return
     */
    public static boolean shouldRemoveSlot(Player player, int slot) {

        int normalizedSlotId = normalizeSlotId(slot);

        return isAndShouldRemoveInventorySlot(player, normalizedSlotId) ||
                isAndShouldRemoveOffhandSlot(player, slot) ||
                isAndShouldRemoveArmorSlot(player, slot);
    }

    public static boolean shouldRemoveCraftingSlot(Player player, int slot) {
        return switch (slot) {
            case 0 -> !PlayerSlots.getCraftingTopLeftSlot(player);
            case 1 -> !PlayerSlots.getCraftingTopRightSlot(player);
            case 2 -> !PlayerSlots.getCraftingBottomLeftSlot(player);
            case 3 -> !PlayerSlots.getCraftingBottomRightSlot(player);
            default -> false;
        };
    }

    public static boolean shouldRemoveCraftingResultSlot(Player player, int slot) {
        return !PlayerSlots.getCraftingTopLeftSlot(player) && !PlayerSlots.getCraftingTopRightSlot(player) && !PlayerSlots.getCraftingBottomLeftSlot(player) && !PlayerSlots.getCraftingBottomRightSlot(player);
    }

    public static boolean isAndShouldRemoveInventorySlot(Player player, int slot) {
        return slot < Inventory.INVENTORY_SIZE && PlayerSlots.getSlots(player) <= slot;
    }

    public static boolean isAndShouldRemoveOffhandSlot(Player player, int slot) {
        return isOffhandSlot(slot) && shouldRemovedOffhandSlot(player);
    }

    public static boolean isAndShouldRemoveArmorSlot(Player player, int slot) {
        return switch (slot) {
            default -> false;
            case 36 -> !PlayerSlots.getFeetSlot(player);
            case 37 -> !PlayerSlots.getLegsSLot(player);
            case 38 -> !PlayerSlots.getChestSlot(player);
            case 39 -> !PlayerSlots.getHeadSlot(player);
        };
    }

    public static boolean isOffhandSlot(int slot) {
        return Inventory.SLOT_OFFHAND == slot;
    }

    public static boolean shouldRemovedOffhandSlot(Player player) {
        return !PlayerSlots.getOffhandSlot(player);
    }

    public static boolean shouldPlayerBeExcluded(Player player) {
        return player.isSpectator() || (player.isCreative() && ServerConfig.excludeCreativeModePlayers.get());
    }

    /**
     * Converts the ids chosen by Mojang (where the non-hotbar inventory is number from top to bottom) to a more logical numbering from bottom to top (which fits better with the hotbar row ids)
     *
     * @param slot
     * @return
     */
    public static int normalizeSlotId(int slot) {
        if(ServerConfig.indexing.get() == Indexing.MOJANG) {
            return slot;
        }
        if (slot >= 9 && slot <= 17) {
            return slot + 18;
        }
        if (slot >= 27 && slot <= 35) {
            return slot - 18;
        }
        return slot;
    }

    /**
     * Converts TinyInv's slot indexes back into Mojang's indexes
     *
     * @param slot
     * @return
     */
    public static int unnormalizeSlotId(int slot) {
        if(ServerConfig.indexing.get() == Indexing.MOJANG) {
            return slot;
        }
        if (slot >= 27 && slot <= 35) {
            return slot - 18;
        }
        if (slot >= 9 && slot <= 17) {
            return slot + 18;
        }
        return slot;
    }

    public static int unnormalizedToMenuId(int slot) {
        if (slot >= 9 && slot < 36) {
            return slot;
        }
        return 36 + slot;
    }

    /**
     * Puts the "Fake Item" inside the given slot. If there already was a non-fake item, it is dropped
     *
     * @param player
     * @param slot
     * @return
     */
    public static boolean replaceSlotStack(Player player, int slot) {
        if (player.level().isClientSide()) return false;

        ItemStack stack = player.getInventory().getItem(slot);
        if (!stack.isEmpty()) {
            if (stack.is(ModItems.FAKE_ITEM.get())) return false;
            player.drop(stack, false, false);
        }
        player.getInventory().setItem(slot, new ItemStack(ModItems.FAKE_ITEM.get()));
        return true;
    }

    /**
     * Replaces the slots that should be removed by a fake (non-active) variant
     *
     * @param container
     * @param player
     */
    public static void fixContainer(AbstractContainerMenu container, Player player) {
        for (int i = 0; i < container.slots.size(); i++) {
            Slot slot = container.slots.get(i);
            if (isInventorySlot(container, slot, player)) {
                updateSlotState(player, slot, container, i, SlotUtils::shouldRemoveSlot);
            } else if (isCraftingSlot(container, slot, player)) {
                updateSlotState(player, slot, container, i, SlotUtils::shouldRemoveCraftingSlot);
            } else if (isCraftingResultSlot(container, slot, player)) {
                updateSlotState(player, slot, container, i, SlotUtils::shouldRemoveCraftingResultSlot);
            }
        }

    }

    public static void updateSlotState(Player player, Slot slot, AbstractContainerMenu container, int i, BiFunction<Player, Integer, Boolean> test) {
        if (slot instanceof FakeSlot fakeSlot) {
            if (!test.apply(player, slot.getSlotIndex())) {
                container.slots.set(i, fakeSlot.originalSlot);
            }
        } else if (test.apply(player, slot.getSlotIndex())) {
            container.slots.set(i, new FakeSlot(slot.container, slot.getSlotIndex(), slot.x, slot.y, player, slot));
        }
    }

    public static boolean isRemovableSlot(AbstractContainerMenu container, Slot slot, Player player) {
        return (slot.container == player.getInventory()) || (isCraftingSlot(container, slot, player)) || (isCraftingResultSlot(container, slot, player));
    }

    public static boolean isInventorySlot(AbstractContainerMenu menu, Slot slot, Player player) {
        return (slot.container == player.getInventory());
    }

    public static boolean isCraftingSlot(AbstractContainerMenu menu, Slot slot, Player player) {
        if (menu instanceof InventoryMenu inventoryMenu) {
            return slot.container == inventoryMenu.getCraftSlots() && menu == player.inventoryMenu;
        }
        return false;
    }

    public static boolean isCraftingResultSlot(AbstractContainerMenu menu, Slot slot, Player player) {
        if (menu instanceof InventoryMenu inventoryMenu) {
            return slot.container == inventoryMenu.resultSlots && menu == player.inventoryMenu;
        }
        return false;
    }

    public static void fixSelectedSlot(Player player) {
        int hotbarSlots = PlayerSlots.getHotbarSlots(player);
        if (hotbarSlots == 0) return;
        if (normalizeSlotId(player.getInventory().selected) >= hotbarSlots) {
            player.getInventory().selected = unnormalizeSlotId(normalizeSlotId(player.getInventory().selected) % hotbarSlots);
        }
    }

    public static boolean isHotbarSlot(int slot, Player player) {
        return slot >= 0 && normalizeSlotId(slot) < PlayerSlots.getHotbarSlots(player);
    }

    public static void fixCraftingSlots(Player player){
        //System.out.println();
        for(int i = 1; i < 5; i++){
            //System.out.println(i + " " + SlotUtils.shouldRemoveSlot(player, i - 1) + " " + (player.inventoryMenu.slots.get(i) instanceof FakeSlot));

            if(SlotUtils.shouldRemoveCraftingSlot(player, i - 1) != (player.inventoryMenu.slots.get(i) instanceof FakeSlot)){
                PlayerEventHandler.schedulePlayerForUpdate((ServerPlayer) player);
            }
        }
        //System.out.println();
    }
}
