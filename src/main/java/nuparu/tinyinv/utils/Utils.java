package nuparu.tinyinv.utils;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.inventory.FakeSlot;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class Utils {
    /*
      Replaces Vanilla player slots by "Fake slots"
       */
    public static void fixContainer(ScreenHandler container, PlayerEntity player) {
        for (int i = 0; i < container.slots.size(); i++) {
            Slot slot = container.slots.get(i);
            if (shouldBeRemoved(slot, player,container)) {
                container.slots.set(i, new FakeSlot(slot.inventory, slot.getIndex(), slot.x, slot.y,player));
            }
        }

    }

    public static boolean shouldBeRemoved(Slot slot, PlayerEntity player, ScreenHandler container) {
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayerEntitys) return false;
        if (slot.inventory != player.getInventory()) return false;
        return shouldBeRemoved(slot.getIndex(),player,container);
    }

    public static boolean shouldBeHidden(Slot slot, PlayerEntity player, ScreenHandler container) {
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayerEntitys) return false;
        if (slot.inventory != player.getInventory()) return false;
        int id = slot.getIndex();

        if(!player.isCreative() && ClientConfig.hideOffhand && isOffhandSlot(slot,player)) return true;

        if (player.world.getGameRules().getBoolean(TinyInv.DISABLE_OFFHAND) && isOffhandSlot(id,player,container)) return true;

        if (player.world.getGameRules().getBoolean(TinyInv.COUNT_FROM_START)) {
            return (id >= player.world.getGameRules().getInt(TinyInv.INVENTORY_SIZE) && id < player.world.getGameRules().getInt(TinyInv.ARMOR_START_ID));
        }
        return (id < player.world.getGameRules().getInt(TinyInv.ARMOR_START_ID)) && ((id < 9 && id >= player.world.getGameRules().getInt(TinyInv.INVENTORY_SIZE)) ||
                (id >= 9 && id <= player.world.getGameRules().getInt(TinyInv.ARMOR_START_ID) - 1 - (Math.max(player.world.getGameRules().getInt(TinyInv.INVENTORY_SIZE) - 9, 0))));
    }

    public static boolean shouldBeRemoved(int id, PlayerEntity player, Object container){
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayerEntitys) return false;
        if (player.world.getGameRules().getBoolean(TinyInv.DISABLE_OFFHAND) && isOffhandSlot(id,player,container)) return true;

        if (player.world.getGameRules().getBoolean(TinyInv.COUNT_FROM_START)) {
            return (id >= player.world.getGameRules().getInt(TinyInv.INVENTORY_SIZE) && id < player.world.getGameRules().getInt(TinyInv.ARMOR_START_ID));
        }
        return (id < player.world.getGameRules().getInt(TinyInv.ARMOR_START_ID)) && ((id < 9 && id >= player.world.getGameRules().getInt(TinyInv.INVENTORY_SIZE)) ||
                (id >= 9 && id <= player.world.getGameRules().getInt(TinyInv.ARMOR_START_ID) - 1 - (Math.max(player.world.getGameRules().getInt(TinyInv.INVENTORY_SIZE) - 9, 0))));
    }


    public static boolean isOffhandSlot(Slot slot, PlayerEntity player) {
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayerEntitys) return false;

        return slot.getIndex() == PlayerInventory.OFF_HAND_SLOT && (slot.inventory instanceof PlayerScreenHandler || slot.inventory instanceof Inventory);
    }

    public static boolean isOffhandSlot(int id, PlayerEntity player, Object container) {
        //if(player.isCreative() && ServerConfig.excludeCreativeModePlayerEntitys) return false;
        return id == PlayerInventory.OFF_HAND_SLOT && (container instanceof PlayerScreenHandler || container instanceof Inventory);
    }

    public static int getHotbarSlots(@NotNull PlayerEntity player) {
        //if(player != null && player.isCreative() && ServerConfig.excludeCreativeModePlayerEntitys) return 9;
        return Math.min(player.world.getGameRules().getInt(TinyInv.INVENTORY_SIZE), TinyInv.server.getOverworld().getGameRules().getInt(TinyInv.HOTBAR_SIZE));
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    public static void syncGameruleInt(int id, GameRules.Key key, ServerPlayerEntity player){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeInt(player.world.getGameRules().getInt(key));
        ServerPlayNetworking.send(player, TinyInv.SYNC_GAMERULE_INT_PACKET_ID, buf);
    }

    public static void syncGameruleBoolean(int id, GameRules.Key key, ServerPlayerEntity player){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(id);
        buf.writeBoolean(player.world.getGameRules().getBoolean(key));
        ServerPlayNetworking.send(player, TinyInv.SYNC_GAMERULE_BOOLEAN_PACKET_ID, buf);
    }
}
