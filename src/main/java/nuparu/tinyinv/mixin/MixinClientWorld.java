package nuparu.tinyinv.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/client/world/ClientWorld;addPlayer(ILnet/minecraft/client/network/AbstractClientPlayerEntity;)V")
    public void addPlayer(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
        if(player != null){
            Utils.fixContainer(player.playerScreenHandler,player);
        }
    }
}
