package nuparu.tinyinv.client;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import nuparu.tinyinv.config.ClientConfig;
import nuparu.tinyinv.world.entity.player.PlayerSlots;

public class ClientSlotUtils {

    public static int getHotbarRows(Window window){
        int slots = getHotbarSlots();
        if(slots <= 0){
            return 1;
        }
        int maxRowLength = ClientConfig.maxSlotsInHotbarRow.get() == 0 ? window.getScreenWidth() / 21 : ClientConfig.maxSlotsInHotbarRow.get();
        return (int) Math.ceil((float) slots / maxRowLength);
    }

    public static int getHotbarSlots(){
        Entity camera = Minecraft.getInstance().getCameraEntity();
        if(camera == Minecraft.getInstance().player){
            Player player = Minecraft.getInstance().player;
            if(!player.isSpectator()){
                return PlayerSlots.getHotbarSlots(player);
            }
        }
        else{
            if(camera instanceof Player player){
                return PlayerSlots.getHotbarSlots(player);
            }
        }
        return 9;
    }
}
