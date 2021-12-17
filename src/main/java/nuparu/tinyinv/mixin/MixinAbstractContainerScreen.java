package nuparu.tinyinv.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.utils.ModConstants;
import nuparu.tinyinv.utils.Utils;
import nuparu.tinyinv.utils.client.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0),method = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", remap = ModConstants.REMAP)
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        AbstractContainerScreen thys = ((AbstractContainerScreen) (Object) this);
            for(int i1 = 0; i1 < thys.getMenu().slots.size(); ++i1) {
                Slot slot = thys.getMenu().slots.get(i1);
                Player playerEntity = Minecraft.getInstance().player;
                if(playerEntity == null) return;
                if (ClientConfig.fakeSlotOverlay.get() && Utils.shouldBeHidden(slot,playerEntity,thys.getMenu())) {
                    int i = slot.x;
                    int j = slot.y;

                    if (thys != null && thys.itemRenderer != null) {
                        thys.setBlitOffset(100);
                        thys.itemRenderer.blitOffset = 100.0F;
                        RenderSystem.setShader(GameRenderer::getPositionColorShader);
                        //RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        RenderSystem.setShaderTexture(0, RenderUtils.PIXEL);
                        RenderUtils.drawColoredRect(matrixStack.last().pose(), ClientConfig.fakeSlotOverlayColor.get(), i-1, j-1, 18, 18, thys.getBlitOffset());
                        thys.itemRenderer.blitOffset = 0.0F;
                        thys.setBlitOffset(0);
                    }
                }
            }
    }

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderSlot(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/inventory/Slot;)V", remap = ModConstants.REMAP, cancellable = true)
    public void renderSlot(PoseStack matrixStack, Slot slot, CallbackInfo ci) {
        AbstractContainerScreen thys = ((AbstractContainerScreen) (Object) this);
        Player playerEntity = Minecraft.getInstance().player;
        if(playerEntity == null) return;
        if (ClientConfig.fakeSlotOverlay.get() && Utils.shouldBeHidden(slot,playerEntity,thys.getMenu())) {
            ci.cancel();
        }
    }
}
