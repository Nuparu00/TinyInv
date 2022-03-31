package nuparu.tinyinv.mixin;

import net.minecraft.entity.player.PlayerInventory;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory {

    //TinyInv.server.getOverworld().getGameRules().getInt(TinyInv.HOTBAR_SIZE)
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/player/PlayerInventory;getHotbarSize()I", cancellable = true)
    private static void getHotbarSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Utils.getHotbarSlots(null));
        cir.cancel();

    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/player/PlayerInventory;isValidHotbarIndex(I)Z", cancellable = true)
    private static void isHotbarSlot(int slot, CallbackInfoReturnable<Boolean> cir) {

        cir.setReturnValue(slot >= 0 && slot < Utils.getHotbarSlots(null));
        cir.cancel();
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V", cancellable = true)
    public void scrollInHotbar(double scrollAmount, CallbackInfo ci) {
        PlayerInventory thys = ((PlayerInventory) (Object) this);
        if (scrollAmount > 0.0D) {
            scrollAmount = 1.0D;
        }

        if (scrollAmount < 0.0D) {
            scrollAmount = -1.0D;
        }

        for (thys.selectedSlot = (int) ((double) thys.selectedSlot - scrollAmount); thys.selectedSlot < 0; thys.selectedSlot += Utils.getHotbarSlots(thys.player)) {
        }

        while (thys.selectedSlot >= thys.player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE)) {
            thys.selectedSlot -= thys.player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE);
        }
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/player/PlayerInventory;getSwappableHotbarSlot()I", cancellable = true)
    public void getSwappableHotbarSlot(CallbackInfoReturnable<Integer> cir) {
        PlayerInventory thys = ((PlayerInventory) (Object) this);
        for (int i = 0; i < thys.player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE); ++i) {
            int j = (thys.selectedSlot + i) % thys.player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE);
            if (thys.main.get(j).isEmpty()) {
                cir.setReturnValue(j);
                cir.cancel();
                return;
            }
        }

        for (int k = 0; k < thys.player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE); ++k) {
            int l = (thys.selectedSlot + k) % thys.player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE);
            if (!thys.main.get(l).hasEnchantments()) {
                cir.setReturnValue(l);
                cir.cancel();
                return;
            }
        }

        cir.setReturnValue(thys.selectedSlot);
        cir.cancel();
    }
}
