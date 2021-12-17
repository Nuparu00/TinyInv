package nuparu.tinyinv.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.utils.ModConstants;
import nuparu.tinyinv.utils.Utils;
import nuparu.tinyinv.utils.client.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerScreen.class)
public class MixinContainerScreen {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;color4f(FFFF)V", ordinal = 0),method = "Lnet/minecraft/client/gui/screen/inventory/ContainerScreen;render(Lcom/mojang/blaze3d/matrix/MatrixStack;IIF)V", remap = ModConstants.REMAP)
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
            ContainerScreen thys = ((ContainerScreen) (Object) this);
            for(int i1 = 0; i1 < thys.getMenu().slots.size(); ++i1) {
                Slot slot = thys.getMenu().slots.get(i1);
                PlayerEntity playerEntity = Minecraft.getInstance().player;
                if(playerEntity == null) return;
                if (ClientConfig.fakeSlotOverlay.get() && Utils.shouldBeHidden(slot,playerEntity,thys.getMenu())) {
                    int i = slot.x;
                    int j = slot.y;

                    if (thys != null && thys.itemRenderer != null) {
                        thys.setBlitOffset(100);
                        thys.itemRenderer.blitOffset = 100.0F;
                        Minecraft.getInstance().getTextureManager().bind(new ResourceLocation(TinyInv.MODID, "textures/gui/1x1.png"));
                        RenderUtils.drawColoredRect(matrixStack.last().pose(), ClientConfig.fakeSlotOverlayColor.get(), i-1, j-1, 18, 18, thys.getBlitOffset());
                        thys.itemRenderer.blitOffset = 0.0F;
                        thys.setBlitOffset(0);
                    }
                }
            }
    }

    @Inject(at = @At("HEAD") ,method = "Lnet/minecraft/client/gui/screen/inventory/ContainerScreen;renderSlot(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/inventory/container/Slot;)V", remap = ModConstants.REMAP, cancellable = true)
    public void renderSlot(MatrixStack matrixStack, Slot slot, CallbackInfo ci) {
        ContainerScreen thys = ((ContainerScreen) (Object) this);
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if(playerEntity == null) return;
        if (ClientConfig.fakeSlotOverlay.get() && Utils.shouldBeHidden(slot,playerEntity,thys.getMenu())) {
            ci.cancel();
        }
    }
}
