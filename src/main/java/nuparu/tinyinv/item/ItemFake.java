package nuparu.tinyinv.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import nuparu.tinyinv.utils.Utils;

public class ItemFake extends Item {
    public ItemFake() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity)
    {
        entity.kill();
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entityIn, int itemSlot, boolean selected) {
        if (entityIn instanceof Player) {
            Player player = (Player)entityIn;
            if (!Utils.shouldBeRemoved(itemSlot, player, player.inventoryMenu)) {
                player.getInventory().setItem(itemSlot,ItemStack.EMPTY);
            }
        }
    }
}
