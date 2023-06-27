package nuparu.tinyinv.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.world.inventory.SlotUtils;

import java.util.function.Supplier;

public class FixClientContainerPacket {
    public FixClientContainerPacket() {
    }
    public static void encode(FixClientContainerPacket msg, FriendlyByteBuf buf) {
    }

    public static FixClientContainerPacket decode(FriendlyByteBuf buf) {
        return new FixClientContainerPacket();
    }

    public static class Handler {

        public static void handle(FixClientContainerPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Player player = TinyInv.proxy.getPlayerEntityFromContext(ctx);
                SlotUtils.fixContainer(player.inventoryMenu, player);
                SlotUtils.fixContainer(player.containerMenu, player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
