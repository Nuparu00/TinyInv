package nuparu.tinyinv.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Hand;
import nuparu.tinyinv.TinyInv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(at = @At("HEAD"), method = "onUpdateSelectedSlot(Lnet/minecraft/network/packet/s2c/play/UpdateSelectedSlotS2CPacket;)V", cancellable = true)
    public void onUpdateSelectedSlot(UpdateSelectedSlotS2CPacket packet, CallbackInfo ci) {
        ClientPlayNetworkHandler thys = ((ClientPlayNetworkHandler) (Object) this);
        NetworkThreadUtils.forceMainThread(packet, thys, MinecraftClient.getInstance());
        if (packet.getSlot() < thys.getWorld().getGameRules().getInt(TinyInv.HOTBAR_SIZE)) {
            MinecraftClient.getInstance().player.getInventory().selectedSlot = packet.getSlot();
        }
        ci.cancel();

    }
}
