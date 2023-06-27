package nuparu.tinyinv.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.chunk.LevelChunk;

public class CapabilityHelper {
    public static IExtendedPlayer getExtendedPlayer(Player player) {
        return player.getCapability(ExtendedPlayerProvider.EXTENDED_PLAYER_CAPABILITY, null).orElse(null);
    }
}
