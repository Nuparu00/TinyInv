package nuparu.tinyinv.capabilities;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public class CapabilityHelper {
    public static @Nullable IExtendedPlayer getExtendedPlayer(Player player) {
        return player.getCapability(ExtendedPlayerProvider.EXTENDED_PLAYER_CAPABILITY, null).orElse(null);
    }
}
