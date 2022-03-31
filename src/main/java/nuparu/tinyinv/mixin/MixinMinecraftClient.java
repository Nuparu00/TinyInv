package nuparu.tinyinv.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.utils.Utils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V")
    public void setScreen(Screen screen, CallbackInfo ci) {
        MinecraftClient thys = ((MinecraftClient) (Object) this);
        if (screen instanceof HandledScreen) {
            HandledScreen handledScreen = (HandledScreen)screen;
            Utils.fixContainer(handledScreen.getScreenHandler(), thys.player);
        }
    }
    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/client/MinecraftClient;render(Z)V")
    public void tick(CallbackInfo ci) {
        if(player != null){
            while(player.getInventory().selectedSlot >= player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE)){
                player.getInventory().selectedSlot-=player.world.getGameRules().getInt(TinyInv.HOTBAR_SIZE);
            }
        }
    }
}
