package nuparu.tinyinv.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;


public class ServerConfig {

    public static IntValue inventorySlots;
    public static IntValue hotbarSlots;
    public static BooleanValue offhandSlot;
    public static BooleanValue headSlot;
    public static BooleanValue chestSlot;
    public static BooleanValue legsSlot;
    public static BooleanValue feetSlot;
    public static BooleanValue craftingTLSlot;
    public static BooleanValue craftingTRSlot;
    public static BooleanValue craftingBLSlot;
    public static BooleanValue craftingBRSlot;
    public static BooleanValue excludeCreativeModePlayers;
    public static ForgeConfigSpec.EnumValue<Indexing> indexing;



    public static void init(ForgeConfigSpec.Builder server) {
        inventorySlots = server.comment("The default number of slots").defineInRange("general.inventory_slots", 36, 0, 36);
        hotbarSlots = server.comment("The default number of hotbar slots").defineInRange("general.hotbar_slots", 9, 0, 36);
        offhandSlot = server.comment("Should offhand slot be enabled?").define("general.offhand_slot", true);
        headSlot = server.comment("Should offhand slot be enabled?").define("general.head_slot", true);
        chestSlot = server.comment("Should offhand slot be enabled?").define("general.chest_slot", true);
        legsSlot = server.comment("Should offhand slot be enabled?").define("general.legs_slot", true);
        feetSlot = server.comment("Should offhand slot be enabled?").define("general.feet_slot", true);
        craftingTLSlot = server.comment("Should offhand slot be enabled?").define("general.crafting_top_left_slot", true);
        craftingTRSlot = server.comment("Should offhand slot be enabled?").define("general.crafting_top_right_slot", true);
        craftingBLSlot = server.comment("Should offhand slot be enabled?").define("general.crafting_bottom_left_slot", true);
        craftingBRSlot = server.comment("Should offhand slot be enabled?").define("general.crafting_bottom_right_slot", true);

        excludeCreativeModePlayers = server.comment("Should exclude the players who are in creative mode?").define("general.exclude_creative_mode_players", true);
        indexing = server.comment("What indexing to use? Mojang counts inventory slots from top to bottom, while TinyInv counts from bottom to top (which lines up better with hotbar indexes)").defineEnum("general.indexing",Indexing.TINYINV,Indexing.values());

    }
}
