package nuparu.tinyinv.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientSafeUtils {
    public static Player getPlayer(){
        return Minecraft.getInstance().player;
    }
}
