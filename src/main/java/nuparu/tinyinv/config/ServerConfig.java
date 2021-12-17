package nuparu.tinyinv.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;


public class ServerConfig {

    public static IntValue inventorySlots;
    public static IntValue hotbarSlots;
    public static IntValue armorStartID;
    public static BooleanValue disableOffhand;
    public static BooleanValue countSlotsFromStart;
    public static BooleanValue excludeCreativeModePlayers;


    public static void init(ForgeConfigSpec.Builder server) {
        inventorySlots = server.comment("Number of slots").defineInRange("general.inventory_slots", 36, 1, 36);
        hotbarSlots = server.comment("Number of hotbar slots").defineInRange("general.hotbar_slots", 9, 1, 36);
        armorStartID = server.comment("ID of the first armor (non-container) inventory slot)").defineInRange("general.armor_start_id", 36, 1, Integer.MAX_VALUE);
        disableOffhand = server.comment("Should disable the left hand?").define("general.disable_offhand", false);
        countSlotsFromStart = server.comment("Counts slots by their numerical ID - hotbar has IDs 0-8, the top inventory row 9-17,....").define("general.count_slots_from_start", false);
        excludeCreativeModePlayers = server.comment("Should exclude the players who are in creative mode?").define("general.exclude_creative_mode_players", true);
    }
}
