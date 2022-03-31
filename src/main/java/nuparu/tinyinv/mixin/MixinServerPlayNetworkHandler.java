package nuparu.tinyinv.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Hand;
import nuparu.tinyinv.TinyInv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class MixinServerPlayNetworkHandler {
    @Inject(at = @At("HEAD"), method = "onUpdateSelectedSlot(Lnet/minecraft/network/packet/c2s/play/UpdateSelectedSlotC2SPacket;)V", cancellable = true)
    public void scrollInHotbar(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        ServerPlayNetworkHandler thys = ((ServerPlayNetworkHandler) (Object) this);
        NetworkThreadUtils.forceMainThread(packet, thys, thys.player.getWorld());
        if (packet.getSelectedSlot() >= 0 && packet.getSelectedSlot() < thys.player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE)) {
            if (thys.player.getInventory().selectedSlot != packet.getSelectedSlot() && thys.player.getActiveHand() == Hand.MAIN_HAND) {
                thys.player.clearActiveItem();
            }

            thys.player.getInventory().selectedSlot = packet.getSelectedSlot();
            thys.player.updateLastActionTime();
        } else {
            TinyInv.LOGGER.warn("{} tried to set an invalid carried item", thys.player.getName().getString());
        }
        ci.cancel();

    }
}
