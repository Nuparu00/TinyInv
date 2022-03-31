package nuparu.tinyinv.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.tinyinv.utils.Utils;

public class FakeItem extends Item {
    public FakeItem() {
        super(new FabricItemSettings().maxCount(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!Utils.shouldBeRemoved(slot, player, player.playerScreenHandler)) {
                player.getInventory().setStack(slot, ItemStack.EMPTY);
            }
        }
    }

}
