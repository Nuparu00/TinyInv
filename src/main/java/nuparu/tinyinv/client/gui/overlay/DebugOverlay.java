package nuparu.tinyinv.client.gui.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import nuparu.tinyinv.capabilities.CapabilityHelper;
import nuparu.tinyinv.capabilities.IExtendedPlayer;
import nuparu.tinyinv.world.entity.player.PlayerSlots;

public class DebugOverlay extends Gui implements IGuiOverlay {
    public DebugOverlay(Minecraft minecraft) {
        super(minecraft,minecraft.getItemRenderer());
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {

        if (!minecraft.options.hideGui && !minecraft.options.renderDebug && minecraft.getCameraEntity() instanceof Player player) {
            int line = 0;
            graphics.drawString(minecraft.font,"Slots " + PlayerSlots.getSlots(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Hotbar Slots " + PlayerSlots.getHotbarSlots(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Offhand Slot " + PlayerSlots.getOffhandSlot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Head Slot " + PlayerSlots.getHeadSlot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Chest Slot " + PlayerSlots.getChestSlot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Legs Slot " + PlayerSlots.getLegsSLot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Feet Slot " + PlayerSlots.getFeetSlot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Crafting TL Slot " + PlayerSlots.getCraftingTopLeftSlot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Crafting TR Slot " + PlayerSlots.getCraftingTopRightSlot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Crafting BR Slot " + PlayerSlots.getCraftingBottomRightSlot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Crafting BL Slot " + PlayerSlots.getCraftingBottomLeftSlot(player),0,10*(line++),0xffffff, true);
            graphics.drawString(minecraft.font,"Selected " + player.getInventory().selected + " " + player.getMainHandItem(),0,10*(line++),0xffffff, true);
        }
    }
}
