package nuparu.tinyinv.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import nuparu.tinyinv.world.inventory.SlotUtils;
import org.joml.Matrix4f;

public class RenderUtils {

    public static void drawColoredRect(Matrix4f matrix, int color, double x, double y, double width, double height,
                                       double zLevel) {
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + height), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + height), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + 0), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + 0), (float) zLevel).color(r, g, b, 255).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public static void slotDebug(AbstractContainerScreen<?> self, GuiGraphics guiGraphics, Slot slot) {
        Player playerEntity = Minecraft.getInstance().player;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
        String s = "" + SlotUtils.normalizeSlotId(slot.getSlotIndex());
        guiGraphics.drawString(self.getMinecraft().font, s, slot.x+8-self.getMinecraft().font.width(s)/2,slot.y+self.getMinecraft().font.lineHeight/2,0xffffff,true);
        guiGraphics.pose().popPose();
    }
}
