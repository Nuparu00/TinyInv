package nuparu.tinyinv.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

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

    /**
     * It takes a while after joining for the game to recognize attribute modifiers added by armor, this prevents unwanted temporary inventory shrinking
     * @return The remaining grace period
     */
    int getGracePeriod();
}
