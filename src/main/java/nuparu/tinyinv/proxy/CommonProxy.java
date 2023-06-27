package nuparu.tinyinv.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CommonProxy {
    public Player getPlayerEntityFromContext(Supplier<NetworkEvent.Context> ctx) {
        return ctx.get().getSender();
    }
    @Nullable
    public Player getPlayer() {
        return null;
    }

    @Nullable
    public Level getLevel() {
        return null;
    }
}
