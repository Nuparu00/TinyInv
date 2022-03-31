package nuparu.tinyinv.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class MixinItemEntity {
    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/entity/ItemEntity;tick()V")
    public void tick(CallbackInfo ci) {
        ItemEntity thys = ((ItemEntity) (Object) this);
        if(thys.getStack().getItem() == TinyInv.FAKE_ITEM){
            thys.kill();
        }

    }
}
