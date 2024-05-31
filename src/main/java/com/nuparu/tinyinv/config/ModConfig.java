package com.nuparu.tinyinv.config;

import com.nuparu.tinyinv.TinyInv;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = TinyInv.MODID, category = "")
@Config.LangKey("tinyinv.config.title")
public class ModConfig {
    public static CategoryClient client = new CategoryClient();
    public static CategoryCommon common = new CategoryCommon();

    public static class CategoryClient {
        @Config.Comment("Should draw overlay over the fake slots")
        public boolean fakeSlotOverlay = true;
        @Config.Comment("Color of the fake slot overlays")
        public int fakeSlotOverlayColor = 0xC6C6C6;
    }
    public static class CategoryCommon {
        @Config.Comment("Number of slots")
        @Config.RangeInt(min = 1, max = 36)
        public int inventorySlots = 36;
        @Config.Comment("Number of hotbar slots")
        @Config.RangeInt(min = 1, max = 9)
        public int hotbarSlots = 9;
        @Config.Comment("ID of the first armor (non-container) inventory slot)")
        public int armorStartID = 36;
        @Config.Comment("Should disable the left hand?")
        public boolean disableOffhand = false;
        @Config.Comment("Counts slots by their numerical ID - hotbar has IDs 0-8, the top inventory row 9-17,....")
        public boolean countSlotsFromStart = false;
    }


    @Mod.EventBusSubscriber(modid = TinyInv.MODID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(TinyInv.MODID)) {
                ConfigManager.sync(TinyInv.MODID, Config.Type.INSTANCE);
            }
        }
    }

}
