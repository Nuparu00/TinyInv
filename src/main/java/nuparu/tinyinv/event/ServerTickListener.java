package nuparu.tinyinv.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import nuparu.tinyinv.TinyInv;

public class ServerTickListener implements ServerTickEvents.EndTick{
    @Override
    public void onEndTick(MinecraftServer minecraftServer){
        TinyInv.onServerTick(minecraftServer);
    }
}
