package nuparu.tinyinv.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import nuparu.tinyinv.client.gui.RenderUtils;
import nuparu.tinyinv.client.gui.Textures;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.world.inventory.SlotUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderLabels(Lnet/minecraft/client/gui/GuiGraphics;II)V", ordinal = 0), method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V")
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        AbstractContainerScreen<?> self = ((AbstractContainerScreen<?>) (Object) this);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 232.0F);
        for (int i1 = 0; i1 < self.getMenu().slots.size(); ++i1) {
            Slot slot = self.getMenu().slots.get(i1);
            Player playerEntity = Minecraft.getInstance().player;
            if (playerEntity == null) return;
            if (ClientConfig.disabledSlotOverlay.get() && SlotUtils.shouldHideSlot(slot, playerEntity, self.getMenu())) {
                int i = slot.x;
                int j = slot.y;

                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                RenderSystem.setShaderTexture(0, Textures.PIXEL);
                RenderUtils.drawColoredRect(guiGraphics.pose().last().pose(), ClientConfig.disabledSlotOverlayColor.get(), i - 1, j - 1, 18, 18, 0);

            }
        }
        guiGraphics.pose().popPose();
    }

    @Inject(at = @At("HEAD"), method = "renderSlot(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/inventory/Slot;)V", cancellable = true)
    public void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        AbstractContainerScreen<?> self = ((AbstractContainerScreen<?>) (Object) this);
        Player playerEntity = Minecraft.getInstance().player;
        if (playerEntity == null) return;
        if (ClientConfig.disabledSlotOverlay.get() && SlotUtils.shouldHideSlot(slot, playerEntity, self.getMenu())) {
            ci.cancel();
        }
    }
}
