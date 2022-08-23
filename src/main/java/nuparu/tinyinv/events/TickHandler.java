package nuparu.tinyinv.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.init.ModItems;
import nuparu.tinyinv.utils.Utils;

@Mod.EventBusSubscriber
public class TickHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if(player == null) return;
        if (player.getInventory().selected >= Utils.getHotbarSlots(player)) {
            player.getInventory().selected = 0;
        }
        //player.getInventory().selected = 11;
        if (event.phase == TickEvent.Phase.START) {

            if (ServerConfig.disableOffhand.get() && !player.getOffhandItem().isEmpty()) {
                //player.captureDrops = true;
                ItemEntity entity = player.drop(player.getOffhandItem(), false, false);
                //player.capturedDrops.clear();
                //player.captureDrops = false;

                if (!player.level.isClientSide()) {
                    player.level.addFreshEntity(entity);
                }
                player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            }
            Inventory inv = player.getInventory();
            for (int i = 0; i < ServerConfig.armorStartID.get(); i++) {
                if(!Utils.shouldBeRemoved(i,player, player.inventoryMenu)) continue;
                ItemStack stack = inv.getItem(i);
                if (!stack.isEmpty()) {
                    if(stack.getItem() == ModItems.FAKE_ITEM.get()) continue;
                    //player.captureDrops = true;
                    ItemEntity entity = player.drop(stack, false, false);
                    if(entity == null) continue;
                    //player.capturedDrops.clear();
                    //player.captureDrops = false;

                    if (player.level != null && !player.level.isClientSide()) {
                        player.level.addFreshEntity(entity);
                        inv.setItem(i, new ItemStack(ModItems.FAKE_ITEM.get()));
                    }
                }else{
                    inv.setItem(i, new ItemStack(ModItems.FAKE_ITEM.get()));
                }
            }
        }
    }

}
