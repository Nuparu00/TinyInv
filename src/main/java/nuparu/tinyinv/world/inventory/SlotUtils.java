package nuparu.tinyinv.world.inventory;

import net.minecraft.server.level.ServerPlayer;
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
     * @param slot Container slot
     * @param player Player
     * @param container Container
     * @return should slot be hidden?
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
     * @param player Player
     * @param slot slot ID
     * @return  should slot be removed?
     */
    public static boolean shouldRemoveSlot(Player player, int slot) {

        int normalizedSlotId = normalizeSlotId(slot);

        return isAndShouldRemoveInventorySlot(player, normalizedSlotId) ||
                isAndShouldRemoveOffhandSlot(player, slot) ||
                isAndShouldRemoveArmorSlot(player, slot);
    }

    /**
     * Determines whether a crafting slot should be removed
     * @param player Player
     * @param slot Crafting slot ID
     * @return should slot be removed?
     */
    public static boolean shouldRemoveCraftingSlot(Player player, int slot) {
        return switch (slot) {
            case 0 -> !PlayerSlots.getCraftingTopLeftSlot(player);
            case 1 -> !PlayerSlots.getCraftingTopRightSlot(player);
            case 2 -> !PlayerSlots.getCraftingBottomLeftSlot(player);
            case 3 -> !PlayerSlots.getCraftingBottomRightSlot(player);
            default -> false;
        };
    }

    /**
     * Determines whether the crafting result slot should be removed
     * @param player Player
     * @param slot Crafting slot ID
     * @return should slot be removed?
     */
    public static boolean shouldRemoveCraftingResultSlot(Player player, int slot) {
        return !PlayerSlots.getCraftingTopLeftSlot(player) && !PlayerSlots.getCraftingTopRightSlot(player) && !PlayerSlots.getCraftingBottomLeftSlot(player) && !PlayerSlots.getCraftingBottomRightSlot(player);
    }

    /**
     * Determines whether the given slot is a regular inventory slot, that should be removed
     * @param player Player
     * @param slot slot ID
     * @return Is the given slot a regular inventory slot, that should be removed?
     */
    public static boolean isAndShouldRemoveInventorySlot(Player player, int slot) {
        return slot < Inventory.INVENTORY_SIZE && PlayerSlots.getSlots(player) <= slot;
    }

    /**
     * Determines whether the given slot is the offhand slot and should be removed
     * @param player Player
     * @param slot slot ID
     * @return Is the given slot the offhand slot and be removed?
     */
    public static boolean isAndShouldRemoveOffhandSlot(Player player, int slot) {
        return isOffhandSlot(slot) && shouldRemoveOffhandSlot(player);
    }

    /**
     * Determines whether the given slot is an armor slot, that should be removed
     * @param player Player
     * @param slot slot ID
     * @return Is the given slot an armor slot, that should be removed?
     */
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

    public static boolean shouldRemoveOffhandSlot(Player player) {
        return !PlayerSlots.getOffhandSlot(player);
    }

    /**
     * Should the player be ignored by the mod
     * @param player Player
     * @return Should the player be ignored by the mod
     */
    public static boolean shouldPlayerBeExcluded(Player player) {
        return player.isSpectator() || (player.isCreative() && ServerConfig.excludeCreativeModePlayers.get());
    }

    /**
     * Converts the ids chosen by Mojang (where the non-hotbar inventory is number from top to bottom) to a more logical numbering from bottom to top (which fits better with the hotbar row ids)
     * @see <a href="https://github.com/Nuparu00/TinyInv/wiki#server-config">Wiki</a>
     * @param slot Mojang slot ID
     * @return Normalized ID
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
     * Converts TinyInv's slot indexes back into Mojang's indexes, inverse of {@link #normalizeSlotId(int)}
     * @param slot Normalized slot ID
     * @return Mojang ID
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

    /**
     * Basically, there is this one part of Vanilla code that shifts the IDs for some reason, and it wasn't made with 10+ hotbar slots in mind
     * @param slot Mojang slot ID
     * @return Weird Mojang slot ID
     */
    public static int unnormalizedToMenuId(int slot) {
        if (slot >= 9 && slot < 36) {
            return slot;
        }
        return 36 + slot;
    }

    /**
     * Puts the "Fake Item" inside the given slot. If there already was a non-fake item, it is dropped
     *
     * @param player Player
     * @param slot Slot ID
     * @return Did we have to put a new "Fake Item" into the slot?
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
     * Replaces the slots (the entities in the Container) that should be removed by a fake (non-active) variant,
     * also replaces the fake slots, that should not be fake by the originals
     * @param container Container
     * @param player Player
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

    /**
     * Replaces the slot by a fake (non-active) variant - if it was meant to be removed,
     * otherwise if it is a fake slot, and therer should not be one, it replaces it by the original
     * @param player Player
     * @param slot Slot (the entity in Container)
     * @param container Container
     * @param slotPos Position/index of the slot in the container
     * @param test Function used to test whether there should be a fake slot
     */
    public static void updateSlotState(Player player, Slot slot, AbstractContainerMenu container, int slotPos, BiFunction<Player, Integer, Boolean> test) {
        if (slot instanceof FakeSlot fakeSlot) {
            if (!test.apply(player, slot.getSlotIndex())) {
                container.slots.set(slotPos, fakeSlot.originalSlot);
            }
        } else if (test.apply(player, slot.getSlotIndex())) {
            container.slots.set(slotPos, new FakeSlot(slot.container, slot.getSlotIndex(), slot.x, slot.y, player, slot));
        }
    }

    /**
     * Determines whether the slot is a part of the player's inventory (excluding the crafting slots)
     * @param menu Container
     * @param slot Slot
     * @param player Player
     * @return is the slot a part of the player's inventory
     */
    public static boolean isInventorySlot(AbstractContainerMenu menu, Slot slot, Player player) {
        return (slot.container == player.getInventory());
    }

    /**
     * Determines whether the slot one of the player's crafting input slots
     * @param menu Container
     * @param slot Slot
     * @param player Player
     * @return is the slot one of the player's crafting input slots
     */
    public static boolean isCraftingSlot(AbstractContainerMenu menu, Slot slot, Player player) {
        if (menu instanceof InventoryMenu inventoryMenu) {
            return slot.container == inventoryMenu.getCraftSlots() && menu == player.inventoryMenu;
        }
        return false;
    }

    /**
     * Determines whether the slot one is the player's crafting result slot
     * @param menu Container
     * @param slot Slot
     * @param player Player
     * @return is the slot the player's crafting result slot
     */
    public static boolean isCraftingResultSlot(AbstractContainerMenu menu, Slot slot, Player player) {
        if (menu instanceof InventoryMenu inventoryMenu) {
            return slot.container == inventoryMenu.resultSlots && menu == player.inventoryMenu;
        }
        return false;
    }

    /**
     * Ensures the player's selected slot is a valid (hotbar) one
     * @param player Player
     */
    public static void fixSelectedSlot(Player player) {
        int hotbarSlots = PlayerSlots.getHotbarSlots(player);
        if (hotbarSlots == 0) return;
        if (normalizeSlotId(player.getInventory().selected) >= hotbarSlots) {
            player.getInventory().selected = unnormalizeSlotId(normalizeSlotId(player.getInventory().selected) % hotbarSlots);
        }
    }

    /**
     * Is the slot player's hotbar slot
     * @param slot slot
     * @param player Player
     * @return Is the slot player's hotbar slot
     */
    public static boolean isHotbarSlot(int slot, Player player) {
        return slot >= 0 && normalizeSlotId(slot) < PlayerSlots.getHotbarSlots(player);
    }

    /**
     * Fixes the player's crafting slots (
     * @param player Player
     */
    public static void fixCraftingSlots(Player player){
        for(int i = 1; i < 5; i++){
            if(SlotUtils.shouldRemoveCraftingSlot(player, i - 1) != (player.inventoryMenu.slots.get(i) instanceof FakeSlot)){
                PlayerEventHandler.schedulePlayerForUpdate((ServerPlayer) player);
            }
        }
    }
}
