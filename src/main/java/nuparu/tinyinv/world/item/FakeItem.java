package nuparu.tinyinv.world.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nuparu.tinyinv.world.inventory.SlotUtils;

public class FakeItem extends Item {
    public FakeItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        entity.kill();
        return false;
    }

    public static boolean checkValidity(ItemStack stack, Player player, int itemSlot) {
        if(player.level().isClientSide()) return false;
        if (!SlotUtils.shouldRemoveSlot(player, itemSlot)) {
            player.getInventory().setItem(itemSlot, ItemStack.EMPTY);
            return true;
        }
        return false;
    }
}
