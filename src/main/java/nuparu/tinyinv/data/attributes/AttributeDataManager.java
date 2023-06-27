package nuparu.tinyinv.data.attributes;

import com.google.gson.*;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.tinyinv.TinyInv;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AttributeDataManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static final AttributeDataManager INSTANCE = new AttributeDataManager();

    private HashMap<Item, Map<EquipmentSlot, Map<Attribute, List<AttributeModifier>>>> itemModifiers = new HashMap<>();

    private HashMap<MobEffect, Map<Attribute, List<AttributeModifier>>> effectModifiers = new HashMap<>();

    public AttributeDataManager() {
        super(GSON, "default_attributes");
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> objectIn, @NotNull ResourceManager resourceManagerIn, @NotNull ProfilerFiller profilerIn) {
        itemModifiers.clear();
        effectModifiers.clear();
        clearEffectModifiers();
        HashMap<Item, List<AttributeValue>> itemAttributeData = new HashMap<>();
        HashMap<MobEffect, List<AttributeValue>> effectAttributeData = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            JsonObject jo = je.getAsJsonObject();
            String type = JsonUtils.getStringOr("type", jo, "item");
            if (type.equals("item")) {
                try {
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jo.get("item").getAsString()));
                    JsonArray attributesArray = jo.get("attributes").getAsJsonArray();
                    List<AttributeValue> list = itemAttributeData.computeIfAbsent(item, item1 -> new ArrayList<>());
                    for (int i = 0; i < attributesArray.size(); i++) {
                        JsonElement attributeElement = attributesArray.get(i);
                        JsonObject attributeObject = attributeElement.getAsJsonObject();
                        ResourceLocation attributeName = new ResourceLocation(attributeObject.get("attribute").getAsString());
                        double amount = attributeObject.get("amount").getAsDouble();
                        AttributeModifier.Operation operation = attributeObject.has("operation") ? AttributeModifier.Operation.valueOf(attributeObject.get("operation").getAsString()) : AttributeModifier.Operation.ADDITION;
                        list.add(new AttributeValue(ForgeRegistries.ATTRIBUTES.getValue(attributeName), amount, operation, EquipmentSlot.byName(attributeObject.get("slot").getAsString())));
                    }
                } catch (Exception e) {
                    TinyInv.LOGGER.error("An error occurred while trying to load default attributes (" + key.toString() + ") :" + e.getMessage());
                }
            } else if (type.equals("effect")) {
                try {
                    MobEffect item = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(jo.get("effect").getAsString()));
                    JsonArray attributesArray = jo.get("attributes").getAsJsonArray();
                    List<AttributeValue> list = effectAttributeData.computeIfAbsent(item, item1 -> new ArrayList<>());
                    for (int i = 0; i < attributesArray.size(); i++) {
                        JsonElement attributeElement = attributesArray.get(i);
                        JsonObject attributeObject = attributeElement.getAsJsonObject();
                        ResourceLocation attributeName = new ResourceLocation(attributeObject.get("attribute").getAsString());
                        double amount = attributeObject.get("amount").getAsDouble();
                        AttributeModifier.Operation operation = attributeObject.has("operation") ? AttributeModifier.Operation.valueOf(attributeObject.get("operation").getAsString()) : AttributeModifier.Operation.ADDITION;
                        list.add(new AttributeValue(ForgeRegistries.ATTRIBUTES.getValue(attributeName), amount, operation, null));
                    }
                } catch (Exception e) {
                    TinyInv.LOGGER.error("An error occurred while trying to load default attributes (" + key.toString() + ") :" + e.getMessage());
                }
            }
        }
        this.calculateItemModifiers(itemAttributeData);
        this.calculateEffectModifiers(effectAttributeData);
        applyEffectModifiers();
    }

    private void clearEffectModifiers() {
        for(Map.Entry<MobEffect, Map<Attribute, List<AttributeModifier>>> entry : effectModifiers.entrySet()){
            for(Map.Entry<Attribute, List<AttributeModifier>> attributeListEntry : entry.getValue().entrySet()){
                for(AttributeModifier modifier : attributeListEntry.getValue()){
                    entry.getKey().getAttributeModifiers().remove(attributeListEntry.getKey(), modifier);
                }
            }
        }
    }

    private void applyEffectModifiers() {
        for(Map.Entry<MobEffect, Map<Attribute, List<AttributeModifier>>> entry : effectModifiers.entrySet()){
            for(Map.Entry<Attribute, List<AttributeModifier>> attributeListEntry : entry.getValue().entrySet()){
                for(AttributeModifier modifier : attributeListEntry.getValue()){
                    entry.getKey().getAttributeModifiers().put(attributeListEntry.getKey(), modifier);
                }
            }
        }
    }

    private void calculateItemModifiers(HashMap<Item, List<AttributeValue>> attributeData) {
        for (Map.Entry<Item, List<AttributeValue>> entry : attributeData.entrySet()) {
            Item item = entry.getKey();
            Map<EquipmentSlot, Map<Attribute, Map<AttributeModifier.Operation, List<AttributeValue>>>> groups = entry.getValue().stream().collect(Collectors.groupingBy(AttributeValue::slot, Collectors.groupingBy(AttributeValue::attribute, Collectors.groupingBy(AttributeValue::operation))));
            Map<EquipmentSlot, Map<Attribute, List<AttributeModifier>>> result = new HashMap<>();
            groups.forEach((slot, equipmentSlotMapMap) ->
                    equipmentSlotMapMap.forEach((attribute, operationListMap) ->
                            operationListMap.entrySet().stream().map(operationListEntry -> createModifier(operationListEntry, operation -> TinyInv.MODID + "#" + item.getDescriptionId() + "#" + slot.name() + "#" + operation.name() + "#" + attribute.getDescriptionId()))
                                    .forEach(optional -> optional
                                            .ifPresent(attributeModifier -> result.computeIfAbsent(slot, slot1 -> new HashMap<>())
                                                    .computeIfAbsent(attribute, attribute1 -> new ArrayList<>()).add(attributeModifier))))
            );
            itemModifiers.put(item, result);
        }
    }

    private void calculateEffectModifiers(HashMap<MobEffect, List<AttributeValue>> attributeData) {
        for (Map.Entry<MobEffect, List<AttributeValue>> entry : attributeData.entrySet()) {
            MobEffect effect = entry.getKey();
            Map<Attribute, Map<AttributeModifier.Operation, List<AttributeValue>>> groups = entry.getValue().stream().collect(Collectors.groupingBy(AttributeValue::attribute, Collectors.groupingBy(AttributeValue::operation)));
            Map<Attribute, List<AttributeModifier>> result = new HashMap<>();
            groups.forEach((attribute, operationListMap) ->
                    operationListMap.entrySet().stream().map(operationListEntry -> createModifier(operationListEntry, operation -> TinyInv.MODID + "#" + effect.getDescriptionId() + "#" + operation.name() + "#" + attribute.getDescriptionId()))
                            .forEach(optional -> optional
                                    .ifPresent(attributeModifier -> result.computeIfAbsent(attribute, attribute1 -> new ArrayList<>()).add(attributeModifier)))
            );
            effectModifiers.put(effect, result);
        }
    }

    private static Optional<AttributeModifier> createModifier(Map.Entry<AttributeModifier.Operation, List<AttributeValue>> entry, Function<AttributeModifier.Operation, String> nameFunction) {
        double amount = compressOperations(entry);
        if (entry.getKey() == AttributeModifier.Operation.ADDITION) {
            if (amount == 0) {
                return Optional.empty();
            }
        } else if (amount == 1) {
            return Optional.empty();
        }
        String name = nameFunction.apply(entry.getKey());
        UUID uuid = stringToUUID(name);
        if(uuid == null){
            return Optional.empty();
        }
        return Optional.of(new AttributeModifier(uuid, name, amount, entry.getKey()));
    }

    private static double compressOperations(Map.Entry<AttributeModifier.Operation, List<AttributeValue>> entry) {
        return compressOperations(entry.getKey(), entry.getValue());
    }

    private static double compressOperations(AttributeModifier.Operation operation, List<AttributeValue> values) {
        return switch (operation) {
            case ADDITION -> values.stream().map(AttributeValue::amount).reduce(0d, Double::sum);
            case MULTIPLY_BASE, MULTIPLY_TOTAL -> values.stream().map(AttributeValue::amount).reduce(1d, (a, b) -> a * b);
        };
    }

    public static UUID stringToUUID(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return UUID.nameUUIDFromBytes(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }



    public Map<Attribute, List<AttributeModifier>> itemModifiers(Item item, EquipmentSlot slot) {
        return itemModifiers.getOrDefault(item, new HashMap<>()).getOrDefault(slot, new HashMap<>());
    }
    public Map<Attribute, List<AttributeModifier>> effectModifiers(MobEffect effect) {
        return effectModifiers.getOrDefault(effect, new HashMap<>());
    }

    public HashMap<Item, Map<EquipmentSlot, Map<Attribute, List<AttributeModifier>>>> itemModifiers() {
        return itemModifiers;
    }
    public HashMap<MobEffect, Map<Attribute, List<AttributeModifier>>> effectModifiers() {
        return effectModifiers;
    }

    public void setItemModifiers(HashMap<Item, Map<EquipmentSlot, Map<Attribute, List<AttributeModifier>>>> itemModifiers){
        this.itemModifiers = itemModifiers;
    }
}
