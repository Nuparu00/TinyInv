package nuparu.tinyinv.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.config.ClientConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class TinyInvClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        File clientConfig = new File(FabricLoader.getInstance().getConfigDir().toFile(), "tininv_fabric_client.json");
        boolean flag = false;
        if(clientConfig.exists()){
            try {
                flag = ClientConfig.load(clientConfig);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                flag = false;
            }
        }

        if(!flag){
            try {
                ClientConfig.save(clientConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(TinyInv.SYNC_GAMERULE_INT_PACKET_ID, (client, handler, buf, responseSender) -> {
            int id = buf.readInt();
            int value = buf.readInt();
            client.execute(() -> {
                if(handler.getWorld() != null){
                    switch (id){
                        case 0 : handler.getWorld().getGameRules().get(TinyInv.HOTBAR_SIZE).set(value,null); break;
                        case 1 : handler.getWorld().getGameRules().get(TinyInv.INVENTORY_SIZE).set(value,null); break;
                        case 2 : handler.getWorld().getGameRules().get(TinyInv.ARMOR_START_ID).set(value,null); break;
                    }

                }
            });
        });


        ClientPlayNetworking.registerGlobalReceiver(TinyInv.SYNC_GAMERULE_BOOLEAN_PACKET_ID, (client, handler, buf, responseSender) -> {
            int id = buf.readInt();
            boolean value = buf.readBoolean();
            client.execute(() -> {
                if(handler.getWorld() != null){
                    switch (id){
                        case 3 : handler.getWorld().getGameRules().get(TinyInv.DISABLE_OFFHAND).set(value,null); break;
                        case 4 : handler.getWorld().getGameRules().get(TinyInv.COUNT_FROM_START).set(value,null); break;
                        case 5 : handler.getWorld().getGameRules().get(TinyInv.EXCLUDE_CREATIVE).set(value,null); break;
                    }

                }
            });
        });
    }
}
