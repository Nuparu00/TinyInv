package nuparu.tinyinv.world.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import nuparu.tinyinv.world.entity.player.PlayerSlots;
import org.slf4j.Logger;

public class ServerGamePacketListenerImplMixinHelper {
    public static void handleSetCarriedItem(ServerboundSetCarriedItemPacket p_9909_, ServerGamePacketListenerImpl listener, Logger logger) {
        PacketUtils.ensureRunningOnSameThread(p_9909_, listener, listener.player.serverLevel());
        if (p_9909_.getSlot() >= 0 && SlotUtils.normalizeSlotId(p_9909_.getSlot()) < PlayerSlots.getHotbarSlots(listener.player)) {
            if (listener.player.getInventory().selected != p_9909_.getSlot() && listener.player.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                listener.player.stopUsingItem();
            }

            listener.player.getInventory().selected = p_9909_.getSlot();
            listener.player.resetLastActionTime();
        } else {
            logger.warn("{} tried to set an invalid carried item", (Object)listener.player.getName().getString());
        }
    }

    public static void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket p_9915_, ServerGamePacketListenerImpl listener) {
        PacketUtils.ensureRunningOnSameThread(p_9915_, listener, listener.player.serverLevel());
        if (listener.player.gameMode.isCreative()) {
            boolean flag = p_9915_.getSlotNum() < 0;
            ItemStack itemstack = p_9915_.getItem();
            if (!itemstack.isItemEnabled(listener.player.level().enabledFeatures())) {
                return;
            }

            CompoundTag compoundtag = BlockItem.getBlockEntityData(itemstack);
            if (!itemstack.isEmpty() && compoundtag != null && compoundtag.contains("x") && compoundtag.contains("y") && compoundtag.contains("z")) {
                BlockPos blockpos = BlockEntity.getPosFromTag(compoundtag);
                if (listener.player.level().isLoaded(blockpos)) {
                    BlockEntity blockentity = listener.player.level().getBlockEntity(blockpos);
                    if (blockentity != null) {
                        blockentity.saveToItem(itemstack);
                    }
                }
            }

            boolean flag1 = p_9915_.getSlotNum() >= 1 && p_9915_.getSlotNum() <= 45;
            boolean flag2 = itemstack.isEmpty() || itemstack.getDamageValue() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();
            if (flag1 && flag2) {
                listener.player.inventoryMenu.getSlot(p_9915_.getSlotNum()).setByPlayer(itemstack);
                listener.player.inventoryMenu.broadcastChanges();
            } else if (flag && flag2 && listener.dropSpamTickCount < 200) {
                listener.dropSpamTickCount += 20;
                listener.player.drop(itemstack, true);
            }
        }
    }
}
