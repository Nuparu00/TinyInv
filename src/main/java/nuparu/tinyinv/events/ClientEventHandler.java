package nuparu.tinyinv.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.utils.Utils;
import nuparu.tinyinv.utils.client.RenderUtils;

@Mod.EventBusSubscriber
public class ClientEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onOverlayRenderPre(RenderGameOverlayEvent.PreLayer event) {
        if (event.getOverlay() == ForgeIngameGui.HOTBAR_ELEMENT) {
            event.setCanceled(true);
            RenderUtils.renderHotbar(event.getWindow(), event.getPartialTicks(), event.getMatrixStack());
        }
        int rows = RenderUtils.getHotbarRows(event.getWindow());
        if (event.getOverlay() == ForgeIngameGui.PLAYER_HEALTH_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.EXPERIENCE_BAR_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.ARMOR_LEVEL_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.FOOD_LEVEL_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.MOUNT_HEALTH_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.HUD_TEXT_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.ITEM_NAME_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.AIR_LEVEL_ELEMENT) {
            event.getMatrixStack().translate(0, -20 * (rows), 0);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onOverlayRenderPost(RenderGameOverlayEvent.PostLayer event) {
        int rows = RenderUtils.getHotbarRows(event.getWindow());
        if (event.getOverlay() == ForgeIngameGui.PLAYER_HEALTH_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.EXPERIENCE_BAR_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.ARMOR_LEVEL_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.FOOD_LEVEL_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.MOUNT_HEALTH_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.HUD_TEXT_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.ITEM_NAME_ELEMENT ||
                event.getOverlay() == ForgeIngameGui.AIR_LEVEL_ELEMENT) {
            event.getMatrixStack().translate(0, 20 * (rows), 0);
        }
    }


    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGuiOpen(ScreenOpenEvent event) {
        Screen gui = event.getScreen();
        if (gui instanceof AbstractContainerScreen) {
            AbstractContainerScreen guiContainer = (AbstractContainerScreen) gui;
            AbstractContainerMenu container = guiContainer.getMenu();
            Utils.fixContainer(container, Minecraft.getInstance().player);
        }
    }
}
