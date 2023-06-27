package nuparu.tinyinv.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.TinyInv;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEventHandler {
    public static final ResourceLocation EXTENDED_PLAYER_KEY = new ResourceLocation(TinyInv.MODID,
            "extended_player");

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player){
            ExtendedPlayerProvider provider = new ExtendedPlayerProvider().setOwner(player);
            event.addCapability(EXTENDED_PLAYER_KEY, provider);
            event.addListener(provider.lazyInstance::invalidate);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getOriginal();

        IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
        IExtendedPlayer oldExtendedPlayer = CapabilityHelper.getExtendedPlayer(event.getOriginal());

        if (!event.isWasDeath()) {
            extendedPlayer.copy(oldExtendedPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if(!player.level().isClientSide()) {
            IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
            extendedPlayer.onDataChanged();
        }
    }

    @SubscribeEvent
    public static void playerStartedTracking(PlayerEvent.StartTracking event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (target instanceof Player targetPlayer) {

            IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(targetPlayer);
            if(extendedPlayer == null) return;

            extendedPlayer.onStartedTracking(targetPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
        extendedPlayer.onDataChanged();
    }
}
