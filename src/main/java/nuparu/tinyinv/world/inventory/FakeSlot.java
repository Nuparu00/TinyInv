package nuparu.tinyinv.world.inventory;


import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class FakeSlot extends Slot {
    public final Player player;
    public final Slot originalSlot;

    public FakeSlot(Container inventoryIn, int index, int xPosition, int yPosition, Player player, Slot originalSLot) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
        this.originalSlot = originalSLot;
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public boolean mayPickup(@NotNull Player p_82869_1_) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isActive() {
        return false;
    }


    @Override
    public boolean mayPlace(@NotNull ItemStack p_75214_1_) {
        return false;
    }
}
