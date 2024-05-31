package com.nuparu.tinyinv.item;

import com.nuparu.tinyinv.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemFake extends Item {
    public ItemFake() {
        this.setUnlocalizedName("fake_item");
        this.setRegistryName("fake_item");
    }

    @Override
    public boolean onEntityItemUpdate(net.minecraft.entity.item.EntityItem entityItem) {
        entityItem.setDead();
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityIn;
            if (!Utils.shouldBeRemoved(itemSlot, player)) {
                player.inventory.setInventorySlotContents(itemSlot,ItemStack.EMPTY);
            }
        }
    }
}
