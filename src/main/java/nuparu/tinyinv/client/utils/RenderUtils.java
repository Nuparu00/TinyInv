package nuparu.tinyinv.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.utils.Utils;

public class RenderUtils {
    public static final Identifier PIXEL = new Identifier("tinyinv", "textures/gui/1x1.png");

    protected static final Identifier WIDGETS_LOCATION = new Identifier("textures/gui/widgets.png");

    public static void drawTexturedModalRect(Matrix4f matrix, int x, int y, int textureX, int textureY, int width, int height, double zLevel) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + height), (float) zLevel).texture(((float) (textureX + 0) * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).next();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + height), (float) zLevel).texture(((float) (textureX + width) * 0.00390625F), ((float) (textureY + height) * 0.00390625F)).next();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + 0), (float) zLevel).texture(((float) (textureX + width) * 0.00390625F), ((float) (textureY + 0) * 0.00390625F)).next();
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + 0), (float) zLevel).texture( ((float) (textureX + 0) * 0.00390625F), ((float) (textureY + 0) * 0.00390625F)).next();
        bufferbuilder.end();
        BufferRenderer.draw(bufferbuilder);
    }

    public static void drawColoredRect(Matrix4f matrix, int color, double x, double y, double width, double height,
                                       double zLevel) {
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;

        RenderSystem.disableTexture();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + height), (float) zLevel).color(r, g, b, 255).next();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + height), (float) zLevel).color(r, g, b, 255).next();
        bufferbuilder.vertex(matrix, (float) (x + width), (float) (y + 0), (float) zLevel).color(r, g, b, 255).next();
        bufferbuilder.vertex(matrix, (float) (x + 0), (float) (y + 0), (float) zLevel).color(r, g, b, 255).next();
        bufferbuilder.end();
        RenderSystem.enableTexture();
        BufferRenderer.draw(bufferbuilder);
        //WorldVertexBufferUploader.end(bufferbuilder);
    }

    private static PlayerEntity getCameraPlayer() {
        return !(MinecraftClient.getInstance().getCameraEntity() instanceof PlayerEntity) ? null : (PlayerEntity)MinecraftClient.getInstance().getCameraEntity();
    }


    public static void renderHotbar(Window window, float partialTicks, MatrixStack poseStack) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        int screenHeight = window.getScaledHeight();
        int screenWidth = window.getScaledWidth();
        int blitOffset = 0;

        PlayerEntity player = getCameraPlayer();
        if (player != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            ItemStack itemstack = player.world.getGameRules().getBoolean(TinyInv.DISABLE_OFFHAND) ? ItemStack.EMPTY : player.getOffHandStack();
            Arm humanoidarm = player.getMainArm().getOpposite();
            int i = screenWidth / 2;
            int j = blitOffset;
            int k = 182;
            //int l = 91;
            blitOffset = -90;
            //this.blit(poseStack, i - 91, screenHeight - 22, 0, 0, 182, 22);

            int slots = Utils.getHotbarSlots(player);

            //int maxRowLength = screenWidth / 21;
            int maxRowLength = ClientConfig.maxSlotsInHotbarRow == 0 ? screenWidth / 21 : ClientConfig.maxSlotsInHotbarRow;
            int rows = (int) Math.ceil(slots/(double)maxRowLength);

            for(int r = 0; r < rows; r++) {
                int slotsInRow = Math.min(slots-maxRowLength*r,maxRowLength);
                int width = 20*slotsInRow+2;
                int x = i - width/2;
                for (int l = 0; l < slotsInRow; ++l) {
                    int w = (l == 0 || l == slotsInRow - 1) ? 21 : 20;
                    //x,y,
                    drawTexturedModalRect(poseStack.peek().getPositionMatrix(), x, screenHeight - 22*(r+1), l == 0 ? 0 : (l == slotsInRow - 1 ? 161 : 21 + 20 * (l % 7)), 0, w, 22, blitOffset);
                    x += w;
                }
            }

            int selected = Math.min(player.getInventory().selectedSlot,slots);
            int selectedRow = (int) Math.floor(selected/maxRowLength);
            int slotsInSelectedRow = Math.min(slots-maxRowLength*selectedRow,maxRowLength);

            drawTexturedModalRect(poseStack.peek().getPositionMatrix(),i - 91 - 1 + (selected % maxRowLength) * 20 - (slotsInSelectedRow-9)*10, screenHeight - 22*(selectedRow+1) - 1, 0, 22, 24, 22, blitOffset);
            //this.blit(poseStack, i - 91 - 1 + player.getInventory().selected * 20, screenHeight - 22 - 1, 0, 22, 24, 22);

            if (!itemstack.isEmpty()) {
                double offset = 0.5;
                if(slots > 9){
                    offset = Math.floor(1 + (((slots-1) % maxRowLength) - 9)/2d)-0.5f;
                }
                if (humanoidarm == Arm.LEFT) {
                    drawTexturedModalRect(poseStack.peek().getPositionMatrix(),i - 91 - 29 - (int)(20 * offset), screenHeight - 23 - (rows-1)*22, 24, 22, 29, 24, blitOffset);
                    //this.blit(poseStack, i - 91 - 29, screenHeight - 23, 24, 22, 29, 24);
                } else {
                    drawTexturedModalRect(poseStack.peek().getPositionMatrix(),i + 91 + (int)(20 * offset), screenHeight - 23 - (rows-1)*22, 53, 22, 29, 24, blitOffset);
                    //this.blit(poseStack, i + 91, screenHeight - 23, 53, 22, 29, 24);
                }
            }

            blitOffset = j;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            int i1 = 1;

            for(int j1 = 0; j1 < slots; ++j1) {
                int k1 = i - 90 + j1 * 20 + 2 - (slots-9)*10;
                int l1 = screenHeight - 16 - 3;
            }

            for(int r = 0; r < rows; r++) {
                int slotsInRow = Math.min(slots-maxRowLength*r,maxRowLength);
                int width = 20*slotsInRow+2;
                int x = i - width/2;
                for (int l = 0; l < slotsInRow; ++l) {
                    int w = 20;
                    //x,y,
                    //drawTexturedModalRect(poseStack.last().pose(), x, screenHeight - 22*(r+1), l == 0 ? 0 : (l == slotsInRow - 1 ? 161 : 21 + 20 * (l % 7)), 0, w, 22, blitOffset);
                    renderSlot(x+3, screenHeight - 22*(r+1)+3, partialTicks, player, player.getInventory().main.get(r*maxRowLength+l), i1++);
                    x += w;
                }
            }

            if (!itemstack.isEmpty()) {
                int j2 = screenHeight - 16 - 3 - (rows-1)*22;
                double offset = 0.5;
                if(slots > 9){
                    offset = Math.floor(1 + (((slots-1) % maxRowLength) - 9)/2d)-0.5f;
                }
                if (humanoidarm == Arm.LEFT) {
                    renderSlot(i - 91 - 26 - (int) (20 * offset), j2, partialTicks, player, itemstack, i1++);
                } else {
                    renderSlot(i + 91 + 10 + (int) (20 * offset), j2, partialTicks, player, itemstack, i1++);
                }
            }

            if (minecraft.options.attackIndicator == AttackIndicator.HOTBAR) {
                float f = minecraft.player.getAttackCooldownProgress(0.0F);
                if (f < 1.0F) {
                    int k2 = screenHeight - 20;
                    int l2 = i + 91 + 6;
                    if (humanoidarm == Arm.RIGHT) {
                        l2 = i - 91 - 22;
                    }

                    RenderSystem.setShaderTexture(0, DrawableHelper.GUI_ICONS_TEXTURE);
                    int i2 = (int)(f * 19.0F);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    //this.blit(poseStack, l2, k2, 0, 94, 18, 18);
                    //this.blit(poseStack, l2, k2 + 18 - i2, 18, 112 - i2, 18, i2);

                    drawTexturedModalRect(poseStack.peek().getPositionMatrix(),l2, k2, 0, 94, 18, 18, blitOffset);
                    drawTexturedModalRect(poseStack.peek().getPositionMatrix(),l2, k2 + 18 - i2, 18, 112 - i2, 18, i2, blitOffset);
                }
            }

            RenderSystem.disableBlend();
        }
    }

    private static void renderSlot(int p_168678_, int p_168679_, float p_168680_, PlayerEntity p_168681_, ItemStack p_168682_, int p_168683_) {
        if (!p_168682_.isEmpty()) {
            MatrixStack posestack = RenderSystem.getModelViewStack();
            float f = (float)p_168682_.getBobbingAnimationTime() - p_168680_;
            if (f > 0.0F) {
                float f1 = 1.0F + f / 5.0F;
                posestack.push();
                posestack.translate((double)(p_168678_ + 8), (double)(p_168679_ + 12), 0.0D);
                posestack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                posestack.translate((double)(-(p_168678_ + 8)), (double)(-(p_168679_ + 12)), 0.0D);
                RenderSystem.applyModelViewMatrix();
            }

            MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(p_168681_, p_168682_, p_168678_, p_168679_, p_168683_);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            if (f > 0.0F) {
                posestack.pop();
                RenderSystem.applyModelViewMatrix();
            }

            MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, p_168682_, p_168678_, p_168679_);
        }
    }

    public static int getHotbarRows(Window window){
        PlayerEntity player = getCameraPlayer();
        int slots = Utils.getHotbarSlots(player);
        int maxRowLength = ClientConfig.maxSlotsInHotbarRow== 0 ? window.getWidth() / 21 : ClientConfig.maxSlotsInHotbarRow;
        return (int) Math.ceil(slots/(double)maxRowLength) - 1;
    }
}
