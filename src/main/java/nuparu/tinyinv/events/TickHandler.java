package nuparu.tinyinv.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.config.CommonConfig;
import nuparu.tinyinv.utils.Utils;
import nuparu.tinyinv.utils.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TickHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = event.player;
            if(player == null) return;
            if (player.inventory.selected >= Utils.getHotbarSlots()) {
                player.inventory.selected = 0;
            }
            if (CommonConfig.disableOffhand.get() && !player.getOffhandItem().isEmpty()) {
                //player.captureDrops = true;
                ItemEntity entity = player.drop(player.getOffhandItem(), false, false);
                //player.capturedDrops.clear();
                //player.captureDrops = false;

                if (!player.level.isClientSide()) {
                    player.level.addFreshEntity(entity);
                }
                player.setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
            }
            PlayerInventory inv = player.inventory;
            for (int i = 0; i < CommonConfig.armorStartID.get(); i++) {
                if(!Utils.shouldBeRemoved(i,player, player.inventoryMenu)) continue;
                ItemStack stack = inv.getItem(i);
                if (!stack.isEmpty()) {
                    if(stack.getItem() == TinyInv.FAKE_ITEM.get()) continue;
                    //player.captureDrops = true;
                    ItemEntity entity = player.drop(stack, false, false);
                    if(entity == null) continue;
                    //player.capturedDrops.clear();
                    //player.captureDrops = false;

                    if (player.level != null && !player.level.isClientSide()) {
                        player.level.addFreshEntity(entity);
                        inv.setItem(i, new ItemStack(TinyInv.FAKE_ITEM.get()));
                    }
                }else{
                    inv.setItem(i, new ItemStack(TinyInv.FAKE_ITEM.get()));
                }
            }
        }
    }

}
