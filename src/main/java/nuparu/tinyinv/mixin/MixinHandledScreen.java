package nuparu.tinyinv.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import nuparu.tinyinv.client.utils.RenderUtils;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class MixinHandledScreen {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0),method = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        HandledScreen thys = ((HandledScreen) (Object) this);
        for(int i1 = 0; i1 < thys.getScreenHandler().slots.size(); ++i1) {
            Slot slot = thys.getScreenHandler().slots.get(i1);
            PlayerEntity playerEntity = MinecraftClient.getInstance().player;
            if(playerEntity == null) return;
            if (ClientConfig.fakeSlotOverlay && Utils.shouldBeHidden(slot,playerEntity,thys.getScreenHandler())) {
                int i = slot.x;
                int j = slot.y;

                ItemRenderer renderer = ((MixinScreen)thys).tinyinv_getItermRenderer();
                if (thys != null && renderer != null) {
                    thys.setZOffset(100);
                    renderer.zOffset = 100.0F;
                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
                    //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShaderTexture(0, RenderUtils.PIXEL);
                    RenderUtils.drawColoredRect(matrices.peek().getPositionMatrix(), ClientConfig.fakeSlotOverlayColor, i-1, j-1, 18, 18, thys.getZOffset());
                    renderer.zOffset = 0.0F;
                    thys.setZOffset(0);
                }
            }
        }
    }

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlot(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/screen/slot/Slot;)V", cancellable = true)
    public void renderSlot(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        HandledScreen thys = ((HandledScreen) (Object) this);
        PlayerEntity playerEntity = MinecraftClient.getInstance().player;
        if(playerEntity == null) return;
        if (ClientConfig.fakeSlotOverlay && Utils.shouldBeHidden(slot,playerEntity,thys.getScreenHandler())) {
            ci.cancel();
        }
    }
}
