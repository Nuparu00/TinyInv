package nuparu.tinyinv.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/client/network/ClientPlayerEntity;tick()V")
    public void tickPre(CallbackInfo ci) {
        ClientPlayerEntity thys = ((ClientPlayerEntity) (Object) this);
        /*if (thys.getInventory().selectedSlot >= Utils.getHotbarSlots(thys)) {
            thys.getInventory().selectedSlot = 0;
        }*/
    }

    @Inject(at = @At("RETURN") ,method = "Lnet/minecraft/client/network/ClientPlayerEntity;tick()V")
    public void tickPost(CallbackInfo ci) {
        ClientPlayerEntity thys = ((ClientPlayerEntity) (Object) this);
        /*if (thys.getInventory().selectedSlot >= Utils.getHotbarSlots(thys)) {
            thys.getInventory().selectedSlot = 0;
        }*/
    }
}
