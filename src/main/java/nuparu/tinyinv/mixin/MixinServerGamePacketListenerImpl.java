package nuparu.tinyinv.mixin;


import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import nuparu.tinyinv.world.entity.player.PlayerSlots;
import nuparu.tinyinv.world.inventory.ServerGamePacketListenerImplMixinHelper;
import nuparu.tinyinv.world.inventory.SlotUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {
    @Shadow public ServerPlayer player;

    @Shadow @Final
    static Logger LOGGER;

    @Inject(at = @At("HEAD") ,method = "handleSetCarriedItem(Lnet/minecraft/network/protocol/game/ServerboundSetCarriedItemPacket;)V", cancellable = true)
    private void handleSetCarriedItem(ServerboundSetCarriedItemPacket p_9909_, CallbackInfo ci) {
        ServerGamePacketListenerImplMixinHelper.handleSetCarriedItem(p_9909_, (ServerGamePacketListenerImpl)(Object)this, LOGGER);
        ci.cancel();
    }
    @Inject(at = @At("HEAD") ,method = "handleSetCreativeModeSlot(Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;)V", cancellable = true)
    private void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket p_9915_, CallbackInfo ci) {
        ServerGamePacketListenerImplMixinHelper.handleSetCreativeModeSlot(p_9915_, (ServerGamePacketListenerImpl)(Object)this);
        ci.cancel();
    }
}
