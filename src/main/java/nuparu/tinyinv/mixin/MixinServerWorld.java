package nuparu.tinyinv.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class MixinServerWorld {

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/server/world/ServerWorld;addPlayer(Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    public void addPlayer(ServerPlayerEntity player, CallbackInfo ci) {
        if(player != null){
            Utils.syncGameruleInt(0,TinyInv.HOTBAR_SIZE,player);
            Utils.syncGameruleInt(1,TinyInv.INVENTORY_SIZE,player);
            Utils.syncGameruleInt(2,TinyInv.ARMOR_START_ID,player);
            Utils.syncGameruleBoolean(3,TinyInv.DISABLE_OFFHAND,player);
            Utils.syncGameruleBoolean(4,TinyInv.COUNT_FROM_START,player);
            Utils.syncGameruleBoolean(5,TinyInv.EXCLUDE_CREATIVE,player);
            Utils.fixContainer(player.playerScreenHandler,player);
        }
    }
}
