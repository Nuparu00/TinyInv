package nuparu.tinyinv.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.tinyinv.config.ServerConfig;

public class FakeSlot extends Slot {
    public Player player;

    public FakeSlot(Container inventoryIn, int index, int xPosition, int yPosition, Player player) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public int getMaxStackSize() {
        if (player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return super.getMaxStackSize();
        return 0;
    }

    @Override
    public boolean mayPickup(Player p_82869_1_) {

        if (player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return super.mayPickup(p_82869_1_);
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isActive() {

        if (player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return super.isActive();
        return false;
    }


    @Override
    public boolean mayPlace(ItemStack p_75214_1_) {

        if (player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) return super.mayPlace(p_75214_1_);
        return false;
    }
}
