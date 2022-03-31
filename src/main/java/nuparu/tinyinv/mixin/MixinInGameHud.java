package nuparu.tinyinv.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.client.utils.RenderUtils;
import nuparu.tinyinv.utils.ModGameRules;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V", cancellable = true)
    public void renderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        if(MinecraftClient.getInstance().world != null && MinecraftClient.getInstance().world.getGameRules().getInt(TinyInv.HOTBAR_SIZE) != 9){
            MixinInGameHud thys = ((MixinInGameHud) (Object) this);
            RenderUtils.renderHotbar(MinecraftClient.getInstance().getWindow(), tickDelta, matrices);
            ci.cancel();
        }
    }
}
