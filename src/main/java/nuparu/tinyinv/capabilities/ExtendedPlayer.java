package nuparu.tinyinv.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import nuparu.tinyinv.world.inventory.SlotUtils;

public class ExtendedPlayer implements IExtendedPlayer {
    private Player owner;

    private int gracePeriod;
    @Override
    public IExtendedPlayer setOwner(Player player) {
        this.owner = player;
        return this;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        if(nbt.contains("GracePeriod",CompoundTag.TAG_INT)){
            gracePeriod = nbt.getInt("GracePeriod");
        }
    }

    @Override
    public CompoundTag writeNBT(CompoundTag nbt) {
        nbt.putInt("GracePeriod", gracePeriod);
        return nbt;
    }

    @Override
    public void copy(IExtendedPlayer iep) {
        readNBT(iep.writeNBT(new CompoundTag()));
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onStartedTracking(Player tracker) {

    }

    @Override
    public void tick(Player player) {
        if(player.level().isClientSide()) return;
        int grace = getGracePeriod();
        if(grace > 0){
            setGracePeriod(grace - 1);
            if(gracePeriod == 0){
                SlotUtils.fixContainer(player.containerMenu,player);
            }
        }
    }

    @Override
    public IExtendedPlayer setGracePeriod(int time) {
        this.gracePeriod = time;
        return this;
    }

    @Override
    public int getGracePeriod() {
        return gracePeriod;
    }
}
