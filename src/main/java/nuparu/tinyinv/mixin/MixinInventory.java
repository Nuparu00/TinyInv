package nuparu.tinyinv.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.tinyinv.event.PlayerEventHandler;
import nuparu.tinyinv.init.ModItems;
import nuparu.tinyinv.world.entity.player.PlayerSlots;
import nuparu.tinyinv.world.inventory.InventoryMixinHelper;
import nuparu.tinyinv.world.inventory.SlotUtils;
import nuparu.tinyinv.world.item.FakeItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class MixinInventory {
    @Shadow @Final public NonNullList<ItemStack> items;

    @Shadow @Final public Player player;

    @Shadow @Final public NonNullList<ItemStack> armor;

    @Shadow @Final public NonNullList<ItemStack> offhand;

    @Shadow public int selected;

    @Inject(at = @At("RETURN") ,method = "tick()V")
    private void tick(CallbackInfo ci) {
        SlotUtils.fixSelectedSlot(player);

        boolean updatePlayer = false;

        int slotId = 0;
        for(ItemStack stack : items){
            if(stack.is(ModItems.FAKE_ITEM.get())){
                updatePlayer = updatePlayer || FakeItem.checkValidity(stack,player,slotId);
            }
            slotId++;
        }
        for(ItemStack stack : armor){
            if(stack.is(ModItems.FAKE_ITEM.get())){
                updatePlayer = updatePlayer || FakeItem.checkValidity(stack,player,slotId);
            }
            slotId++;
        }
        for(ItemStack stack : offhand){
            if(stack.is(ModItems.FAKE_ITEM.get())){
                updatePlayer = updatePlayer || FakeItem.checkValidity(stack,player,slotId);
            }
            slotId++;
        }
        if(updatePlayer){
            if(player instanceof ServerPlayer serverPlayer) {
                PlayerEventHandler.schedulePlayerForUpdate(serverPlayer);
            }
            else{
                //SlotUtils.fixContainer(player.inventoryMenu, player);
                //SlotUtils.fixContainer(player.containerMenu, player);
            }
        }
    }


    @Inject(at = @At("HEAD") ,method = "getSuitableHotbarSlot()I", cancellable = true)
    private void getSuitableHotbarSlot(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(InventoryMixinHelper.getSuitableHotbarSlot((Inventory)(Object)this));
        cir.cancel();
    }
    @Inject(at = @At("HEAD") ,method = "swapPaint(D)V", cancellable = true)
    private void swapPaint(double p_35989_, CallbackInfo ci) {
        Inventory self = (Inventory)(Object)this;
        InventoryMixinHelper.swapPaint(p_35989_, player, self);
        ci.cancel();
    }
    @Inject(at = @At("HEAD") ,method = "getSelected()Lnet/minecraft/world/item/ItemStack;", cancellable = true)
    private void getSelected(CallbackInfoReturnable<ItemStack> cir) {
        Inventory self = (Inventory)(Object)this;
        cir.cancel();
        cir.setReturnValue(InventoryMixinHelper.getSelected(self));
    }
    @Inject(at = @At("HEAD") ,method = "setPickedItem(Lnet/minecraft/world/item/ItemStack;)V", cancellable = true)
    private void setPickedItem(ItemStack p_36013_, CallbackInfo ci) {
        Inventory self = (Inventory)(Object)this;
        ci.cancel();
        InventoryMixinHelper.setPickedItem(p_36013_, self);
    }

    @Inject(at = @At("HEAD") ,method = "getDestroySpeed(Lnet/minecraft/world/level/block/state/BlockState;)F", cancellable = true)
    private void getDestroySpeed(BlockState p_36021_, CallbackInfoReturnable<Float> cir) {
        cir.cancel();
        cir.setReturnValue((PlayerSlots.getSlots(player) == 0 ? ItemStack.EMPTY : this.items.get(this.selected)).getDestroySpeed(p_36021_));
    }
}
