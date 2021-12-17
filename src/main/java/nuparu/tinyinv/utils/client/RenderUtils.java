package nuparu.tinyinv.utils.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import nuparu.tinyinv.utils.Utils;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.AttackIndicatorStatus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.gui.GuiUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;


@OnlyIn(Dist.CLIENT)
public class RenderUtils {

    public static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");

    public static void renderHotbar(MainWindow sr, float partialTicks, MatrixStack stack) {
        Minecraft mc = Minecraft.getInstance();
        float zLevel = 0;

        if (mc.cameraEntity instanceof PlayerEntity) {
            mc.getTextureManager().bind(WIDGETS_TEX_PATH);
            PlayerEntity entityplayer = (PlayerEntity) mc.cameraEntity;
            ItemStack itemstack = entityplayer.getOffhandItem();
            HandSide enumhandside = entityplayer.getMainArm().getOpposite();
            int i = sr.getGuiScaledWidth() / 2;
            float f = zLevel;
            int j = 182;
            int k = 91;
            zLevel = -90.0F;

            int slots = Utils.getHotbarSlots();
            int width = 20*slots+2;
            int x = i - width/2;

            for (int l = 0; l < slots; ++l) {
                int w = (l==0 || l == slots-1) ? 21 : 20;
                //x,y,
                drawTexturedModalRect(stack.last().pose(), x, sr.getGuiScaledHeight() - 22, l==0 ? 0 : (l==slots-1 ? 161 : 21+20*(l-1)), 0, w, 22, zLevel);
                x+=w;
            }


            //drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22, zLevel);
            drawTexturedModalRect(stack.last().pose(),i - 91 - 1 + entityplayer.inventory.selected * 20 - (slots-9)*10, sr.getGuiScaledHeight() - 22 - 1, 0, 22, 24, 22, zLevel);

            if (!itemstack.isEmpty()) {
                if (enumhandside == HandSide.LEFT) {
                    drawTexturedModalRect(stack.last().pose(),i - 91 - 29, sr.getGuiScaledHeight() - 23, 24, 22, 29, 24, zLevel);
                } else {
                    drawTexturedModalRect(stack.last().pose(),i + 91, sr.getGuiScaledHeight() - 23, 53, 22, 29, 24, zLevel);
                }
            }

            zLevel = f;
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
           // RenderHelper.setupForFlatItems();

            for (int l = 0; l < slots; ++l) {
                int i1 = i - 90 + l * 20 + 2 - (slots-9)*10;
                int j1 = sr.getGuiScaledHeight() - 16 - 3;
                renderHotbarItem(i1, j1, partialTicks, entityplayer, entityplayer.inventory.items.get(l), mc);
            }

            if (!itemstack.isEmpty()) {
                int l1 = sr.getGuiScaledHeight() - 16 - 3;

                if (enumhandside == HandSide.LEFT) {
                    renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack, mc);
                } else {
                    renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack, mc);
                }
            }

            if (mc.options.attackIndicator == AttackIndicatorStatus.HOTBAR) {
                float f1 = mc.player.getAttackStrengthScale(0.0F);

                if (f1 < 1.0F) {
                    int i2 = sr.getGuiScaledHeight() - 20;
                    int j2 = i + 91 + 6;

                    if (enumhandside == HandSide.RIGHT) {
                        j2 = i - 91 - 22;
                    }

                    mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
                    int k1 = (int) (f1 * 19.0F);
                    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                    drawTexturedModalRect(stack.last().pose(),j2, i2, 0, 94, 18, 18, zLevel);
                    drawTexturedModalRect(stack.last().pose(),j2, i2 + 18 - k1, 18, 112 - k1, 18, k1, zLevel);
                }
            }
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableBlend();
        }
    }

    public static void renderHotbarItem(int p_184044_1_, int p_184044_2_, float p_184044_3_, PlayerEntity player, ItemStack stack, Minecraft mc) {

        if (!stack.isEmpty()) {
            float f = (float)stack.getPopTime() - p_184044_3_;
            if (f > 0.0F) {
                RenderSystem.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                RenderSystem.translatef((float)(p_184044_1_ + 8), (float)(p_184044_2_ + 12), 0.0F);
                RenderSystem.scalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                RenderSystem.translatef((float)(-(p_184044_1_ + 8)), (float)(-(p_184044_2_ + 12)), 0.0F);
            }

            mc.getItemRenderer().renderAndDecorateItem(player, stack, p_184044_1_, p_184044_2_);
            if (f > 0.0F) {
                RenderSystem.popMatrix();
            }

            mc.getItemRenderer().renderGuiItemDecorations(mc.font, stack, p_184044_1_, p_184044_2_);
        }
    }

    public static void drawTexturedModalRect(Matrix4f matrix, int x, int y, int textureX, int textureY, int width, int height, double zLevel) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + height), (float) zLevel).uv(((float) (textureX + 0) * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + height), (float) zLevel).uv(((float) (textureX + width) * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + 0), (float) zLevel).uv(((float) (textureX + width) * 0.00390625F), ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + 0), (float) zLevel).uv( ((float) (textureX + 0) * 0.00390625F), ((float) (textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.end();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.end(bufferbuilder);
    }

    public static void drawColoredRect(Matrix4f matrix, int color, double x, double y, double width, double height,
                                       double zLevel) {
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;

        RenderSystem.disableTexture();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + height), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + height), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + 0), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + 0), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.end();
        RenderSystem.enableTexture();
        WorldVertexBufferUploader.end(bufferbuilder);
    }

    public static void drawHoveringText(@Nonnull final ItemStack stack, MatrixStack mStack, List<? extends ITextProperties> textLines, int mouseX, int mouseY,
                                        int screenWidth, int screenHeight, int maxTextWidth,
                                        int backgroundColor, int borderColorStart, int borderColorEnd, FontRenderer font)
    {
        if (!textLines.isEmpty())
        {
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableDepthTest();
            int tooltipTextWidth = 0;

            for (ITextProperties textLine : textLines)
            {
                int textLineWidth = font.width(textLine);
                if (textLineWidth > tooltipTextWidth)
                    tooltipTextWidth = textLineWidth;
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth)
            {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2)
                        tooltipTextWidth = mouseX - 12 - 8;
                    else
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth)
            {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap)
            {
                int wrappedTooltipWidth = 0;
                List<ITextProperties> wrappedTextLines = new ArrayList<>();
                for (int i = 0; i < textLines.size(); i++)
                {
                    ITextProperties textLine = textLines.get(i);
                    List<ITextProperties> wrappedLine = font.getSplitter().splitLines(textLine, tooltipTextWidth, Style.EMPTY);
                    if (i == 0)
                        titleLinesCount = wrappedLine.size();

                    for (ITextProperties line : wrappedLine)
                    {
                        int lineWidth = font.width(line);
                        if (lineWidth > wrappedTooltipWidth)
                            wrappedTooltipWidth = lineWidth;
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2)
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                else
                    tooltipX = mouseX + 12;
            }

            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1)
            {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount)
                    tooltipHeight += 2; // gap between title lines and next lines
            }

            if (tooltipY < 4)
                tooltipY = 4;
            else if (tooltipY + tooltipHeight + 4 > screenHeight)
                tooltipY = screenHeight - tooltipHeight - 4;

            final int zLevel = 400;
            RenderTooltipEvent.Color colorEvent = new RenderTooltipEvent.Color(stack, textLines, mStack, tooltipX, tooltipY, font, backgroundColor, borderColorStart, borderColorEnd);
            MinecraftForge.EVENT_BUS.post(colorEvent);
            backgroundColor = colorEvent.getBackground();
            borderColorStart = colorEvent.getBorderStart();
            borderColorEnd = colorEvent.getBorderEnd();

            mStack.pushPose();
            Matrix4f mat = mStack.last().pose();
            //TODO, lots of unnessesary GL calls here, we can buffer all these together.
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            GuiUtils.drawGradientRect(mat, zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostBackground(stack, textLines, mStack, tooltipX, tooltipY, font, tooltipTextWidth, tooltipHeight));

            IRenderTypeBuffer.Impl renderType = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            mStack.translate(0.0D, 0.0D, zLevel);

            int tooltipTop = tooltipY;

            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber)
            {
                ITextProperties line = textLines.get(lineNumber);
                if (line != null)
                    font.drawInBatch(LanguageMap.getInstance().getVisualOrder(line), (float)tooltipX, (float)tooltipY, -1, true, mat, renderType, false, 0, 15728880);

                if (lineNumber + 1 == titleLinesCount)
                    tooltipY += 2;

                tooltipY += 10;
            }

            renderType.endBatch();
            mStack.popPose();

            MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostText(stack, textLines, mStack, tooltipX, tooltipTop, font, tooltipTextWidth, tooltipHeight));

            RenderSystem.enableDepthTest();
            RenderSystem.enableRescaleNormal();
        }
    }

}
