package nuparu.tinyinv.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.client.ClientSlotUtils;
import nuparu.tinyinv.client.gui.InGameOverlayHelper;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.world.entity.player.PlayerSlots;
import nuparu.tinyinv.world.inventory.SlotUtils;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {

    private static Queue<Supplier<?>> actions = new LinkedList<>();
    private static int rows;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onOverlayRenderPre(RenderGuiOverlayEvent.Pre event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSpectator()) return;
        rows = ClientSlotUtils.getHotbarRows(event.getWindow()) - 1;
        if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
            event.setCanceled(true);
            InGameOverlayHelper.renderHotbar(event.getWindow(), event.getPartialTick(), event.getGuiGraphics());
            Gui gui = Minecraft.getInstance().gui;
            if (gui instanceof ForgeGui forgeGui) {
                forgeGui.leftHeight += (InGameOverlayHelper.HOTBAR_SLOT_HEIGHT - 1) * (rows);
                forgeGui.rightHeight += (InGameOverlayHelper.HOTBAR_SLOT_HEIGHT - 1) * (rows);
            }
        }
        if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type() ||
                event.getOverlay() == VanillaGuiOverlay.TITLE_TEXT.type() ||
                event.getOverlay() == VanillaGuiOverlay.JUMP_BAR.type() ||
                event.getOverlay() == VanillaGuiOverlay.ITEM_NAME.type() ||
                event.getOverlay() == VanillaGuiOverlay.RECORD_OVERLAY.type() ||
                (ClientConfig.transformChat.get() && event.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type())) {
            event.getGuiGraphics().pose().translate(0, -(InGameOverlayHelper.HOTBAR_SLOT_HEIGHT - 1) * (rows), 0);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onOverlayRenderPost(RenderGuiOverlayEvent.Post event) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSpectator()) return;
        if (event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type() ||
                event.getOverlay() == VanillaGuiOverlay.TITLE_TEXT.type() ||
                event.getOverlay() == VanillaGuiOverlay.JUMP_BAR.type() ||
                event.getOverlay() == VanillaGuiOverlay.ITEM_NAME.type() ||
                event.getOverlay() == VanillaGuiOverlay.RECORD_OVERLAY.type() ||
                (ClientConfig.transformChat.get() && event.getOverlay() == VanillaGuiOverlay.CHAT_PANEL.type())) {
            event.getGuiGraphics().pose().translate(0, (InGameOverlayHelper.HOTBAR_SLOT_HEIGHT - 1) * (rows), 0);
        }
    }

    @SubscribeEvent
    public static void onGuiOpen(ScreenEvent.Opening event) {
        Screen gui = event.getScreen();
        if (gui instanceof AbstractContainerScreen<?> guiContainer) {
            AbstractContainerMenu container = guiContainer.getMenu();
            SlotUtils.fixContainer(container, Minecraft.getInstance().player);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            //PlayerSlots.fixSelectedSlot(player);
        }
        if (event.phase == TickEvent.Phase.END) return;
        Optional<Supplier<?>> action;
        while ((action = retrieveAction()).isPresent()) {
            action.get().get();
        }
    }

    public static synchronized void scheduleAction(Supplier<?> action) {
        actions.add(action);
    }

    public static synchronized Optional<Supplier<?>> retrieveAction() {
        if (actions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(actions.remove());
    }

    @SubscribeEvent
    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isPickBlock() && event.getHand() == InteractionHand.MAIN_HAND) {
            event.setCanceled(true);
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            boolean flag = player.getAbilities().instabuild;
            BlockEntity blockentity = null;
            HitResult.Type hitresult$type = minecraft.hitResult.getType();
            ItemStack itemstack;
            if (hitresult$type == HitResult.Type.BLOCK) {
                BlockPos blockpos = ((BlockHitResult) minecraft.hitResult).getBlockPos();
                BlockState blockstate = minecraft.level.getBlockState(blockpos);
                if (blockstate.isAir()) {
                    return;
                }

                Block block = blockstate.getBlock();
                itemstack = blockstate.getCloneItemStack(minecraft.hitResult, minecraft.level, blockpos, minecraft.player);

                if (flag && Screen.hasControlDown() && blockstate.hasBlockEntity()) {
                    blockentity = minecraft.level.getBlockEntity(blockpos);
                }
            } else {
                if (hitresult$type != HitResult.Type.ENTITY || !flag) {
                    return;
                }

                Entity entity = ((EntityHitResult) minecraft.hitResult).getEntity();
                itemstack = entity.getPickedResult(minecraft.hitResult);
                if (itemstack == null) {
                    return;
                }
            }

            if (itemstack.isEmpty()) {
                String s = "";
                if (hitresult$type == HitResult.Type.BLOCK) {
                    s = BuiltInRegistries.BLOCK.getKey(minecraft.level.getBlockState(((BlockHitResult) minecraft.hitResult).getBlockPos()).getBlock()).toString();
                } else if (hitresult$type == HitResult.Type.ENTITY) {
                    s = BuiltInRegistries.ENTITY_TYPE.getKey(((EntityHitResult) minecraft.hitResult).getEntity().getType()).toString();
                }

                minecraft.LOGGER.warn("Picking on: [{}] {} gave null item", hitresult$type, s);
            } else {
                Inventory inventory = minecraft.player.getInventory();
                if (blockentity != null) {
                    minecraft.addCustomNbtData(itemstack, blockentity);
                }

                int i = inventory.findSlotMatchingItem(itemstack);
                if (flag) {
                    inventory.setPickedItem(itemstack);
                    System.out.println("SSSSSSSSS " + inventory.selected + " " + SlotUtils.unnormalizedToMenuId(inventory.selected));
                    minecraft.gameMode.handleCreativeModeItemAdd(minecraft.player.getItemInHand(InteractionHand.MAIN_HAND), SlotUtils.unnormalizedToMenuId(inventory.selected));
                } else if (i != -1) {
                    if (SlotUtils.isHotbarSlot(i, player)) {
                        inventory.selected = i;
                    } else {
                        minecraft.gameMode.handlePickItem(i);
                    }
                }

            }
        }
    }
    public static int getRows() {
        return rows;
    }
}
