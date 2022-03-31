package nuparu.tinyinv;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import nuparu.tinyinv.event.ServerTickListener;
import nuparu.tinyinv.item.FakeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TinyInv implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    //public static ServerConfig CONFIG = new ServerConfig();
    public static final GameRules.Key<GameRules.IntRule> HOTBAR_SIZE =
            GameRuleRegistry.register("hotbarSize", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(9));
    public static final GameRules.Key<GameRules.IntRule> INVENTORY_SIZE =
            GameRuleRegistry.register("inventorySize", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(36));
    public static final GameRules.Key<GameRules.IntRule> ARMOR_START_ID =
            GameRuleRegistry.register("armorStartID", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(36));
    public static final GameRules.Key<GameRules.BooleanRule> DISABLE_OFFHAND =
            GameRuleRegistry.register("disableOffhand", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> COUNT_FROM_START =
            GameRuleRegistry.register("countSlotsFromStart", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));
    public static final GameRules.Key<GameRules.BooleanRule> EXCLUDE_CREATIVE =
            GameRuleRegistry.register("excludeCreativeModePlayers", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));

    public static Identifier SYNC_GAMERULE_INT_PACKET_ID = new Identifier("tinyinv","sync_int_gamerule");
    public static Identifier SYNC_GAMERULE_BOOLEAN_PACKET_ID = new Identifier("tinyinv","sync_boolean_gamerule");

    public static final Item FAKE_ITEM = new FakeItem();
    public static MinecraftServer server;

    public static void onServerTick(MinecraftServer minecraftServer) {
        server = minecraftServer;
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("tinyinv", "fake_item"), FAKE_ITEM);
        ServerTickEvents.END_SERVER_TICK.register(new ServerTickListener());
        /*CONFIG.readConfigFromFile();
        CONFIG.saveConfigToFile();
        ServerLifecycleEvents.SERVER_STOPPED.register(instance -> CONFIG.saveConfigToFile());
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> new ConfigCommand<ServerCommandSource>(CONFIG).register(dispatcher, p -> p.hasPermissionLevel(2)));
    */}
}
