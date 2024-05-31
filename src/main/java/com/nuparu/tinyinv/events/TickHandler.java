package com.nuparu.tinyinv.events;

import com.nuparu.tinyinv.TinyInv;
import com.nuparu.tinyinv.config.ModConfig;
import com.nuparu.tinyinv.utils.Utils;
import com.nuparu.tinyinv.utils.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TickHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == net.minecraftforge.fml.common.gameevent.TickEvent.Phase.START) {
            EntityPlayer player = event.player;
            if(player == null) return;
            if (player.inventory.currentItem >= Utils.getHotbarSlots()) {
                player.inventory.currentItem = 0;
            }
            if (ModConfig.common.disableOffhand && !player.getHeldItemOffhand().isEmpty()) {
                player.captureDrops = true;
                EntityItem entity = player.dropItem(player.getHeldItemOffhand(), false, false);
                player.capturedDrops.clear();
                player.captureDrops = false;

                if (!player.world.isRemote) {
                    player.getEntityWorld().spawnEntity(entity);
                }
                player.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
            }
            InventoryPlayer inv = player.inventory;
            for (int i = 0; i < ModConfig.common.armorStartID; i++) {
                if(!Utils.shouldBeRemoved(i,player)) continue;
                ItemStack stack = inv.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    if(stack.getItem() == TinyInv.fakeItem) continue;
                    player.captureDrops = true;
                    EntityItem entity = player.dropItem(stack, false, false);
                    if(entity == null) continue;
                    player.capturedDrops.clear();
                    player.captureDrops = false;

                    if (player.world != null && !player.world.isRemote) {
                        player.world.spawnEntity(entity);
                        inv.setInventorySlotContents(i, new ItemStack(TinyInv.fakeItem));
                    }
                }else{
                    inv.setInventorySlotContents(i, new ItemStack(TinyInv.fakeItem));
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if(!ModConfig.client.fakeSlotOverlay)return;
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            GuiScreen screen = mc.currentScreen;
            if (screen == null) return;
            if (screen instanceof GuiContainer) {
                GuiContainer guiContainer = (GuiContainer) screen;
                Container container = guiContainer.inventorySlots;
                //Just to iterate safely
                List<Slot> slots = new ArrayList<Slot>(container.inventorySlots);
                EntityPlayer player = mc.player;
                if (player == null) return;
                for (Slot slot : slots) {
                    if (slot.inventory == player.inventory && Utils.shouldBeRemoved(slot, player)) {
                        GlStateManager.pushMatrix();
                        GlStateManager.disableAlpha();
                        GlStateManager.disableBlend();
                        RenderUtils.drawColoredRect(ModConfig.client.fakeSlotOverlayColor, guiContainer.getGuiLeft() + slot.xPos - 1, guiContainer.getGuiTop() + slot.yPos - 1, 18, 18, 0);
                        GlStateManager.enableBlend();
                        GlStateManager.enableAlpha();
                        GlStateManager.popMatrix();
                    }
                }
                while(ClientEventHandler.TOOLTIPS.size() > 0){
                    RenderTooltipEvent.Pre e = ClientEventHandler.TOOLTIPS.get(0);
                    ClientEventHandler.TOOLTIPS.remove(e);
                    RenderUtils.drawHoveringText(e.getStack(),e.getLines(),e.getX(),e.getY(),e.getScreenWidth(),e.getScreenHeight(),e.getMaxWidth(),e.getFontRenderer());
                }
            }
        }
    }
}
