package nuparu.tinyinv.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.client.RenderUtils;
import nuparu.tinyinv.utils.Utils;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onOverlayRenderPre(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id() == VanillaGuiOverlay.HOTBAR.id()) {
            event.setCanceled(true);
            RenderUtils.renderHotbar(event.getWindow(), event.getPartialTick(), event.getPoseStack());
        }
        int rows = RenderUtils.getHotbarRows(event.getWindow());
        ResourceLocation overlayID = event.getOverlay().id();
        if (overlayID == VanillaGuiOverlay.PLAYER_HEALTH.id() ||
        		overlayID == VanillaGuiOverlay.EXPERIENCE_BAR.id() ||
        		overlayID ==  VanillaGuiOverlay.ARMOR_LEVEL.id() ||
				overlayID == VanillaGuiOverlay.FOOD_LEVEL.id() ||
				overlayID == VanillaGuiOverlay.MOUNT_HEALTH.id() ||
				overlayID == VanillaGuiOverlay.DEBUG_TEXT.id() ||
				overlayID == VanillaGuiOverlay.ITEM_NAME.id() ||
				overlayID == VanillaGuiOverlay.AIR_LEVEL.id()) {
            event.getPoseStack().translate(0, -20 * (rows), 0);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onOverlayRenderPost(RenderGuiOverlayEvent.Post event) {
        int rows = RenderUtils.getHotbarRows(event.getWindow());
        ResourceLocation overlayID = event.getOverlay().id();
        if (overlayID == VanillaGuiOverlay.PLAYER_HEALTH.id() ||
        		overlayID == VanillaGuiOverlay.EXPERIENCE_BAR.id() ||
        		overlayID ==  VanillaGuiOverlay.ARMOR_LEVEL.id() ||
				overlayID == VanillaGuiOverlay.FOOD_LEVEL.id() ||
				overlayID == VanillaGuiOverlay.MOUNT_HEALTH.id() ||
				overlayID == VanillaGuiOverlay.DEBUG_TEXT.id() ||
				overlayID == VanillaGuiOverlay.ITEM_NAME.id() ||
				overlayID == VanillaGuiOverlay.AIR_LEVEL.id()) {
            event.getPoseStack().translate(0, 20 * (rows), 0);
        }
    }


    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGuiOpen(ScreenEvent.Init event) {
        Screen gui = event.getScreen();
        if (gui instanceof AbstractContainerScreen) {
            AbstractContainerScreen guiContainer = (AbstractContainerScreen) gui;
            AbstractContainerMenu container = guiContainer.getMenu();
            Utils.fixContainer(container, Minecraft.getInstance().player);
        }
    }
}
