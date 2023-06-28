package nuparu.tinyinv.world.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nuparu.tinyinv.world.entity.player.PlayerSlots;

public class InventoryMixinHelper {
    public static void swapPaint(double p_35989_, Player player, Inventory inventory) {
        int i = (int) Math.signum(p_35989_);
        int hotbarSlots = PlayerSlots.getHotbarSlots(player);

        if (hotbarSlots == 0) return;
        int normalized = SlotUtils.normalizeSlotId(inventory.selected);

        for (normalized -= i; normalized < 0; normalized += hotbarSlots) {
        }

        while (normalized >= hotbarSlots) {
            normalized -= hotbarSlots;
        }

        inventory.selected = SlotUtils.unnormalizeSlotId(normalized);
    }
    public static int getSuitableHotbarSlot(Inventory inventory){

        int hotbarSlots = PlayerSlots.getHotbarSlots(inventory.player);
        if(hotbarSlots == 0){
            return inventory.selected;
        }
        for(int i = 0; i < hotbarSlots; ++i) {
            int j = SlotUtils.unnormalizeSlotId((SlotUtils.normalizeSlotId(inventory.selected) + i) % hotbarSlots);
            if (inventory.items.get(j).isEmpty()) {
                return j;
            }
        }

        for(int k = 0; k < hotbarSlots; ++k) {
            int l = SlotUtils.unnormalizeSlotId((SlotUtils.normalizeSlotId(inventory.selected) + k) % hotbarSlots);
            if (!inventory.items.get(l).isNotReplaceableByPickAction(inventory.player, l)) {
                return l;
            }
        }

        return inventory.selected;
    }

    public static ItemStack getSelected(Inventory self) {
        return SlotUtils.isHotbarSlot(self.selected, self.player) ? self.items.get(self.selected) : ItemStack.EMPTY;
    }

    public static void setPickedItem(ItemStack p_36013_, Inventory self) {
        int i = self.findSlotMatchingItem(p_36013_);
        if (SlotUtils.isHotbarSlot(i, self.player)) {
            self.selected = i;
        } else {
            if (i == -1) {
                self.selected = self.getSuitableHotbarSlot();
                if (!self.items.get(self.selected).isEmpty()) {
                    int j = self.getFreeSlot();
                    if (j != -1) {
                        self.items.set(j, self.items.get(self.selected));
                    }
                }
                self.items.set(self.selected, p_36013_);
            } else {
                self.pickSlot(i);
            }

        }
    }
}
