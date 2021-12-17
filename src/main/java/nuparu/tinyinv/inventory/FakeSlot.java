package nuparu.tinyinv.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FakeSlot extends Slot {
    public FakeSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public boolean mayPickup(PlayerEntity p_82869_1_) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isActive() {
        return false;
    }


    @Override
    public boolean mayPlace(ItemStack p_75214_1_) {
        return false;
    }
}
