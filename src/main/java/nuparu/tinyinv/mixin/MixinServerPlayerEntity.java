package nuparu.tinyinv.mixin;

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

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/server/network/ServerPlayerEntity;onScreenHandlerOpened(Lnet/minecraft/screen/ScreenHandler;)V")
    public void onScreenHandlerOpened(ScreenHandler screenHandler, CallbackInfo ci) {
        ServerPlayerEntity thys = ((ServerPlayerEntity) (Object) this);
        Utils.fixContainer(screenHandler,thys);
    }

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/server/network/ServerPlayerEntity;tick()V")
    public void tick(CallbackInfo ci) {
        ServerPlayerEntity thys = ((ServerPlayerEntity) (Object) this);
        if (thys.getInventory().selectedSlot >= Utils.getHotbarSlots(thys)) {
            thys.getInventory().selectedSlot = 0;
        }
        if (thys.world.getGameRules().getBoolean(TinyInv.DISABLE_OFFHAND) && !thys.getOffHandStack().isEmpty()) {
            //player.captureDrops = true;
            ItemEntity entity = thys.dropItem(thys.getOffHandStack(), false, false);
            //player.capturedDrops.clear();
            //player.captureDrops = false;
                thys.world.spawnEntity(entity);
            thys.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
        PlayerInventory inv = thys.getInventory();
        for (int i = 0; i < thys.world.getGameRules().getInt(TinyInv.ARMOR_START_ID); i++) {
            if(!Utils.shouldBeRemoved(i,thys, thys.playerScreenHandler)) continue;
            ItemStack stack = inv.getStack(i);
            if (!stack.isEmpty()) {
                if(stack.getItem() == TinyInv.FAKE_ITEM) continue;
                //player.captureDrops = true;
                ItemEntity entity = thys.dropItem(stack, false, false);
                if(entity == null) continue;
                //player.capturedDrops.clear();
                //player.captureDrops = false;
                thys.world.spawnEntity(entity);
                inv.setStack(i, new ItemStack(TinyInv.FAKE_ITEM));

            }else{
                inv.setStack(i, new ItemStack(TinyInv.FAKE_ITEM));
            }
        }
    }
}
