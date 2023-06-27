package nuparu.tinyinv.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;

public interface IExtendedPlayer {

    IExtendedPlayer setOwner(Player player);
    Player getOwner();

    void readNBT(CompoundTag nbt);
    CompoundTag writeNBT(CompoundTag nbt);
    void copy(IExtendedPlayer iep);
    void onDataChanged();
    void onStartedTracking(Player tracker);
    void tick(Player player);

    IExtendedPlayer setGracePeriod(int time);
    int getGracePeriod();
}
