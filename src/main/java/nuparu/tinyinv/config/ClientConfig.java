package nuparu.tinyinv.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.*;

public class ClientConfig {
    public static boolean fakeSlotOverlay = true;
    public static int fakeSlotOverlayColor = 0xC6C6C6;
    public static int maxSlotsInHotbarRow = 0;
    public static boolean hideOffhand = false;

    public static boolean load(File clientConfig) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(clientConfig));
        Gson gson = new Gson();
        JsonElement je = gson.fromJson(reader, JsonElement.class);
        JsonObject json = je.getAsJsonObject();

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (!json.has("fakeSlotOverlay")) {
            return false;
        }

        if (!json.has("fakeSlotOverlayColor")) {
            return false;
        }

        if (!json.has("maxSlotsInHotbarRow")) {
            return false;
        }

        if (!json.has("hideOffhand")) {
            return false;
        }

        fakeSlotOverlay = json.get("fakeSlotOverlay").getAsBoolean();
        fakeSlotOverlayColor = json.get("fakeSlotOverlayColor").getAsInt();
        maxSlotsInHotbarRow = json.get("maxSlotsInHotbarRow").getAsInt();
        hideOffhand = json.get("hideOffhand").getAsBoolean();

        return true;
    }


    public static void save(File clientConfig) throws IOException {
        fakeSlotOverlay = true;
        fakeSlotOverlayColor = 0xC6C6C6;
        maxSlotsInHotbarRow = 0;
        hideOffhand = false;

        clientConfig.delete();
        clientConfig.getParentFile().mkdirs();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonWriter writer = new JsonWriter(new FileWriter(clientConfig));
        writer.beginObject();
        writer.name("fakeSlotOverlay").value(fakeSlotOverlay);
        writer.name("fakeSlotOverlayColor").value(fakeSlotOverlayColor);
        writer.name("maxSlotsInHotbarRow").value(maxSlotsInHotbarRow);
        writer.name("hideOffhand").value(hideOffhand);
        writer.endObject();
        writer.close();

    }
}