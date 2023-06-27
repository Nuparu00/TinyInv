package nuparu.tinyinv.client;

import org.joml.Matrix4f;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ScreenUtils;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.utils.Utils;


@OnlyIn(Dist.CLIENT)
public class RenderUtils {
    public static final ResourceLocation PIXEL = new ResourceLocation(TinyInv.MODID, "textures/gui/1x1.png");

    protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public static void drawColoredRect(Matrix4f matrix, int color, double x, double y, double width, double height,
                                       double zLevel) {
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;

        RenderSystem.disableTexture();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        bufferbuilder.m_252986_(matrix, (float) (x + 0), (float) (y + height), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.m_252986_(matrix, (float) (x + width), (float) (y + height), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.m_252986_(matrix, (float) (x + width), (float) (y + 0), (float) zLevel).color(r, g, b, 255).endVertex();
        bufferbuilder.m_252986_(matrix, (float) (x + 0), (float) (y + 0), (float) zLevel).color(r, g, b, 255).endVertex();
        RenderSystem.enableTexture();
        BufferUploader.drawWithShader(bufferbuilder.end());
        //WorldVertexBufferUploader.end(bufferbuilder);
    }

    private static Player getCameraPlayer() {
        return !(Minecraft.getInstance().getCameraEntity() instanceof Player) ? null : (Player)Minecraft.getInstance().getCameraEntity();
    }


    public static void renderHotbar(Window window,float partialTicks, PoseStack poseStack) {
        Minecraft minecraft = Minecraft.getInstance();
        int screenHeight = window.getGuiScaledHeight();
        int screenWidth = window.getGuiScaledWidth();
        int blitOffset = 0;

        Player player = getCameraPlayer();
        if (player != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            ItemStack itemstack = ServerConfig.disableOffhand.get() ? ItemStack.EMPTY : player.getOffhandItem();
            HumanoidArm humanoidarm = player.getMainArm().getOpposite();
            int i = screenWidth / 2;
            int j = blitOffset;
            int k = 182;
            //int l = 91;
            blitOffset = -90;
            //this.blit(poseStack, i - 91, screenHeight - 22, 0, 0, 182, 22);

            int slots = Utils.getHotbarSlots(player);

            //int maxRowLength = screenWidth / 21;
            int maxRowLength = ClientConfig.maxSlotsInHotbarRow.get() == 0 ? screenWidth / 21 : ClientConfig.maxSlotsInHotbarRow.get();
            int rows = (int) Math.ceil(slots/(double)maxRowLength);

            for(int r = 0; r < rows; r++) {
                int slotsInRow = Math.min(slots-maxRowLength*r,maxRowLength);
                int width = 20*slotsInRow+2;
                int x = i - width/2;
                for (int l = 0; l < slotsInRow; ++l) {
                    int w = (l == 0 || l == slotsInRow - 1) ? 21 : 20;
                    //x,y,
                    ScreenUtils.drawTexturedModalRect(poseStack, x, screenHeight - 22*(r+1), l == 0 ? 0 : (l == slotsInRow - 1 ? 161 : 21 + 20 * (l % 7)), 0, w, 22, blitOffset);
                    x += w;
                }
            }

            int selected = Math.min(player.getInventory().selected,slots);
            int selectedRow = (int) Math.floor(selected/maxRowLength);
            int slotsInSelectedRow = Math.min(slots-maxRowLength*selectedRow,maxRowLength);

            ScreenUtils.drawTexturedModalRect(poseStack, i - 91 - 1 + (selected % maxRowLength) * 20 - (slotsInSelectedRow-9)*10, screenHeight - 22*(selectedRow+1) - 1, 0, 22, 24, 22, blitOffset);
            //this.blit(poseStack, i - 91 - 1 + player.getInventory().selected * 20, screenHeight - 22 - 1, 0, 22, 24, 22);

            if (!itemstack.isEmpty()) {
                double offset = 0.5;
                if(slots > 9){
                    offset = Math.floor(1 + (((slots-1) % maxRowLength) - 9)/2d)-0.5f;
                }
                if (humanoidarm == HumanoidArm.LEFT) {
                	ScreenUtils.drawTexturedModalRect(poseStack, i - 91 - 29 - (int)(20 * offset), screenHeight - 23 - (rows-1)*22, 24, 22, 29, 24, blitOffset);
                    //this.blit(poseStack, i - 91 - 29, screenHeight - 23, 24, 22, 29, 24);
                } else {
                	ScreenUtils.drawTexturedModalRect(poseStack, i + 91 + (int)(20 * offset), screenHeight - 23 - (rows-1)*22, 53, 22, 29, 24, blitOffset);
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
                    renderSlot(x+3, screenHeight - 22*(r+1)+3, partialTicks, player, player.getInventory().items.get(r*maxRowLength+l), i1++);
                    x += w;
                }
            }

            if (!itemstack.isEmpty()) {
                int j2 = screenHeight - 16 - 3 - (rows-1)*22;
                double offset = 0.5;
                if(slots > 9){
                    offset = Math.floor(1 + (((slots-1) % maxRowLength) - 9)/2d)-0.5f;
                }
                if (humanoidarm == HumanoidArm.LEFT) {
                    renderSlot(i - 91 - 26 - (int) (20 * offset), j2, partialTicks, player, itemstack, i1++);
                } else {
                    renderSlot(i + 91 + 10 + (int) (20 * offset), j2, partialTicks, player, itemstack, i1++);
                }
            }

            if (minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                float f = minecraft.player.getAttackStrengthScale(0.0F);
                if (f < 1.0F) {
                    int k2 = screenHeight - 20;
                    int l2 = i + 91 + 6;
                    if (humanoidarm == HumanoidArm.RIGHT) {
                        l2 = i - 91 - 22;
                    }

                    RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
                    int i2 = (int)(f * 19.0F);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    //this.blit(poseStack, l2, k2, 0, 94, 18, 18);
                    //this.blit(poseStack, l2, k2 + 18 - i2, 18, 112 - i2, 18, i2);

                    ScreenUtils.drawTexturedModalRect(poseStack, l2, k2, 0, 94, 18, 18, blitOffset);
                    ScreenUtils.drawTexturedModalRect(poseStack, l2, k2 + 18 - i2, 18, 112 - i2, 18, i2, blitOffset);
                }
            }

            RenderSystem.disableBlend();
        }
    }

    private static void renderSlot(int p_168678_, int p_168679_, float p_168680_, Player p_168681_, ItemStack p_168682_, int p_168683_) {
        if (!p_168682_.isEmpty()) {
            PoseStack posestack = RenderSystem.getModelViewStack();
            float f = p_168682_.getPopTime() - p_168680_;
            if (f > 0.0F) {
                float f1 = 1.0F + f / 5.0F;
                posestack.pushPose();
                posestack.translate(p_168678_ + 8, p_168679_ + 12, 0.0D);
                posestack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                posestack.translate((-(p_168678_ + 8)), (-(p_168679_ + 12)), 0.0D);
                RenderSystem.applyModelViewMatrix();
            }

            Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(p_168681_, p_168682_, p_168678_, p_168679_, p_168683_);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            if (f > 0.0F) {
                posestack.popPose();
                RenderSystem.applyModelViewMatrix();
            }

            Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, p_168682_, p_168678_, p_168679_);
        }
    }

    public static int getHotbarRows(Window window){
        Player player = getCameraPlayer();
        int slots = Utils.getHotbarSlots(player);
        int maxRowLength = ClientConfig.maxSlotsInHotbarRow.get() == 0 ? window.getScreenWidth() / 21 : ClientConfig.maxSlotsInHotbarRow.get();
        return (int) Math.ceil(slots/(double)maxRowLength) - 1;
    }
}
