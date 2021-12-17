package nuparu.tinyinv.item;

import nuparu.tinyinv.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
    public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean selected) {
        if (entityIn instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entityIn;
            if (!Utils.shouldBeRemoved(itemSlot, player, player.inventoryMenu)) {
                player.inventory.setItem(itemSlot,ItemStack.EMPTY);
            }
        }
    }
}
