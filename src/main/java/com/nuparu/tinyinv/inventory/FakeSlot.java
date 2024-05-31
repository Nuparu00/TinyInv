package com.nuparu.tinyinv.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FakeSlot extends Slot {
    public FakeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public int getSlotStackLimit() {
        return 0;
    }
    @Override
    public boolean canTakeStack(EntityPlayer playerIn)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isEnabled()
    {
        return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }
}
