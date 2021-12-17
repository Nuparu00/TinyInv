package nuparu.tinyinv.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.utils.client.RenderUtils;
import nuparu.tinyinv.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ClientEventHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onHotbarRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            event.setCanceled(true);
            RenderUtils.renderHotbar(event.getWindow(), event.getPartialTicks(), event.getMatrixStack());
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGuiOpen(GuiOpenEvent event) {
        Screen gui = event.getGui();
        if (gui instanceof ContainerScreen) {
            ContainerScreen guiContainer = (ContainerScreen) gui;
            Container container = guiContainer.getMenu();
            Utils.fixContainer(container, Minecraft.getInstance().player);
        }
    }
}
