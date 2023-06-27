package nuparu.tinyinv.network;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.network.packets.FixClientContainerPacket;
import nuparu.tinyinv.network.packets.SyncItemAttributeDataToClient;

import java.util.Optional;
import java.util.function.Supplier;

public class PacketManager {

    private static final String PROTOCOL_VERSION = Integer.toString(1);

    public static final SimpleChannel fixClientContainer = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(TinyInv.MODID, "fix_client_container"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    public static final SimpleChannel syncItemAttributeData = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(TinyInv.MODID, "sync_item_attribute_data"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    private static int discriminator = 0;
    public static void setup() {
        PacketManager.fixClientContainer.registerMessage(PacketManager.discriminator++, FixClientContainerPacket.class, FixClientContainerPacket::encode, FixClientContainerPacket::decode, FixClientContainerPacket.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        PacketManager.syncItemAttributeData.registerMessage(PacketManager.discriminator++, SyncItemAttributeDataToClient.class, SyncItemAttributeDataToClient::encode, SyncItemAttributeDataToClient::decode, SyncItemAttributeDataToClient.Handler::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendTo(SimpleChannel channel, Object message, ServerPlayer player) {
        channel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToChunk(SimpleChannel channel, Object message, Supplier<LevelChunk> chunk) {
        channel.send(PacketDistributor.TRACKING_CHUNK.with(chunk), message);
    }

    public static void sendToDimension(SimpleChannel channel, Object message, Supplier<ResourceKey<Level>> world) {
        channel.send(PacketDistributor.DIMENSION.with(world), message);
    }

    public static void sendToTrackingEntity(SimpleChannel channel, Object message, Supplier<Entity> entity) {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(entity), message);
    }

    public static void sendToAll(SimpleChannel channel, Object message) {
        channel.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToServer(SimpleChannel channel, Object message) {
        channel.sendToServer(message);
    }
}
