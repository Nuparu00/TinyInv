package com.nuparu.tinyinv.events;

import com.nuparu.tinyinv.config.ModConfig;
import com.nuparu.tinyinv.utils.client.RenderUtils;
import com.nuparu.tinyinv.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.LinkedList;

@Mod.EventBusSubscriber
public class ClientEventHandler {

    public static final ArrayList<RenderTooltipEvent.Pre> TOOLTIPS = new ArrayList<RenderTooltipEvent.Pre>();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onHotbarRender(RenderGameOverlayEvent.Pre event){
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            event.setCanceled(true);
            RenderUtils.renderHotbar(event.getResolution(),event.getPartialTicks());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onGuiOpen(GuiOpenEvent event) {
        Gui gui = event.getGui();
        if(gui instanceof GuiContainer){
            GuiContainer guiContainer = (GuiContainer)gui;
            Container container = guiContainer.inventorySlots;
            Utils.fixContainer(container, Minecraft.getMinecraft().player);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onTooltipPre(RenderTooltipEvent.Pre event){
        if(ModConfig.client.fakeSlotOverlay) {
            event.setCanceled(true);
            TOOLTIPS.add(event);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onTooltipPost(RenderTooltipEvent.PostText event){

    }
}
