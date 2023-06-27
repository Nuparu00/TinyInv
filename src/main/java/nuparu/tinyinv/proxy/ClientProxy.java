package nuparu.tinyinv.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientProxy extends CommonProxy {
    public Player getPlayerEntityFromContext(Supplier<NetworkEvent.Context> ctx) {
        return (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT ? Minecraft.getInstance().player
                : super.getPlayerEntityFromContext(ctx));
    }

    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }


    public Level getLevel() {
        return Minecraft.getInstance().level;
    }

}
