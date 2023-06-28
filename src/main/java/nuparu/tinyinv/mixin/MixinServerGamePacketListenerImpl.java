package nuparu.tinyinv.mixin;


import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import nuparu.tinyinv.world.inventory.ServerGamePacketListenerImplMixinHelper;
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
