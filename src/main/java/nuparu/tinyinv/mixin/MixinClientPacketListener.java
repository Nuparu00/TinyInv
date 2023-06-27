package nuparu.tinyinv.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nuparu.tinyinv.client.event.ClientEventHandler;
import nuparu.tinyinv.init.ModAttributes;
import nuparu.tinyinv.init.ModItems;
import nuparu.tinyinv.world.inventory.SlotUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Shadow @Final private Minecraft minecraft;

    @Inject(at = @At("RETURN") ,method = "handleContainerSetSlot(Lnet/minecraft/network/protocol/game/ClientboundContainerSetSlotPacket;)V", cancellable = true)
    private void handleContainerSetSlot(ClientboundContainerSetSlotPacket p_105000_, CallbackInfo ci) {
        Player player = minecraft.player;
        ItemStack itemstack = p_105000_.getItem();
        int i = p_105000_.getSlot();
        if(i <= 0) return;
        if(itemstack.is(ModItems.FAKE_ITEM.get()) || (player.getInventory().getItem(i).is(ModItems.FAKE_ITEM.get()) && !itemstack.is(ModItems.FAKE_ITEM.get()))){
            SlotUtils.fixContainer(player.inventoryMenu, player);
            SlotUtils.fixContainer(player.containerMenu, player);
        }

        /*
        ClientEventHandler.scheduleAction(() -> {
            SlotUtils.fixContainer(player.inventoryMenu, player);
            SlotUtils.fixContainer(player.containerMenu, player);
            return 0;
        });*/
    }
}
