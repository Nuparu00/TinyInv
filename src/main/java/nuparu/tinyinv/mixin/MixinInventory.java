package nuparu.tinyinv.mixin;


import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class MixinInventory {

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V", cancellable = true)
    public void swapPaint(double p_35989_, CallbackInfo ci) {
        Inventory thys = ((Inventory) (Object) this);
        if (p_35989_ > 0.0D) {
            p_35989_ = 1.0D;
        }

        if (p_35989_ < 0.0D) {
            p_35989_ = -1.0D;
        }

        for(thys.selected = (int)((double)thys.selected - p_35989_); thys.selected < 0; thys.selected += Utils.getHotbarSlots(thys.player)) {
        }

        while(thys.selected >= Utils.getHotbarSlots(thys.player)) {
            thys.selected -= Utils.getHotbarSlots(thys.player);
        }
        ci.cancel();
    }

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/world/entity/player/Inventory;getSuitableHotbarSlot()I", cancellable = true)
    public void getSuitableHotbarSlot(CallbackInfoReturnable<Integer> cir) {
        Inventory thys = ((Inventory) (Object) this);
        for(int i = 0; i < Utils.getHotbarSlots(thys.player); ++i) {
            int j = (thys.selected + i) % ServerConfig.hotbarSlots.get();
            if (thys.items.get(j).isEmpty()) {
                cir.setReturnValue(j);
                cir.cancel();
                return;
            }
        }

        for(int k = 0; k < Utils.getHotbarSlots(thys.player); ++k) {
            int l = (thys.selected + k) % ServerConfig.hotbarSlots.get();
            if (!thys.items.get(l).isEnchanted()) {
                cir.setReturnValue(l);
                cir.cancel();
                return;
            }
        }

        cir.setReturnValue(thys.selected);
        cir.cancel();
    }

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/world/entity/player/Inventory;getSelectionSize()I", cancellable = true)
    private static void getSelectionSize(CallbackInfoReturnable<Integer> cir) {
        if(ServerConfig.hotbarSlots != null) {
            cir.setReturnValue(Utils.getHotbarSlots(null));
            cir.cancel();
        }
    }
    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/world/entity/player/Inventory;isHotbarSlot(I)Z", cancellable = true)
    private static void isHotbarSlot(int slot, CallbackInfoReturnable<Boolean> cir) {
        if(ServerConfig.hotbarSlots != null) {
            cir.setReturnValue(slot >= 0 && slot < Utils.getHotbarSlots(null));
            cir.cancel();
        }
    }
}
