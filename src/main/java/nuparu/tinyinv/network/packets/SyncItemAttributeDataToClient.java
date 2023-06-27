package nuparu.tinyinv.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.data.attributes.AttributeDataManager;
import nuparu.tinyinv.world.inventory.SlotUtils;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;
import java.util.function.Supplier;

public class SyncItemAttributeDataToClient {

    private HashMap<Item, Map<EquipmentSlot, Map<Attribute, List<AttributeModifier>>>> itemModifiers;

    public SyncItemAttributeDataToClient() {
        this.itemModifiers = AttributeDataManager.INSTANCE.itemModifiers();
    }
    public SyncItemAttributeDataToClient(HashMap<Item, Map<EquipmentSlot, Map<Attribute, List<AttributeModifier>>>> itemModifiers) {
        this.itemModifiers = itemModifiers;
    }

    public static void encode(SyncItemAttributeDataToClient msg, FriendlyByteBuf buf) {
        CompoundTag nbt = new CompoundTag();
        ListTag list = new ListTag();
        for (Map.Entry<Item, Map<EquipmentSlot, Map<Attribute, List<AttributeModifier>>>> entry : msg.itemModifiers.entrySet()) {

            for (Map.Entry<EquipmentSlot, Map<Attribute, List<AttributeModifier>>> slotEntry : entry.getValue().entrySet()) {

                for (Map.Entry<Attribute, List<AttributeModifier>> attributeEntry : slotEntry.getValue().entrySet()) {
                    for (AttributeModifier modifier : attributeEntry.getValue()) {
                        CompoundTag tag = new CompoundTag();
                        tag.putString("name", ForgeRegistries.ITEMS.getKey(entry.getKey()).toString());
                        tag.putString("slot", slotEntry.getKey().getName());
                        tag.putString("attribute", ForgeRegistries.ATTRIBUTES.getKey(attributeEntry.getKey()).toString());
                        tag.putString("operation", modifier.getOperation().name());
                        tag.putDouble("amount", modifier.getAmount());
                        list.add(tag);
                    }
                }
            }
        }
        nbt.put("list",list);
        buf.writeNbt(nbt);
    }

    public static SyncItemAttributeDataToClient decode(FriendlyByteBuf buf) {
        HashMap<Item, Map<EquipmentSlot, Map<Attribute, List<AttributeModifier>>>> itemModifiers = new HashMap<>();

        CompoundTag nbt = buf.readNbt();
        ListTag list = nbt.getList("list", Tag.TAG_COMPOUND);
        for(Tag tag : list){
            CompoundTag compoundTag = (CompoundTag)tag;
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(compoundTag.getString("name")));
            EquipmentSlot slot = EquipmentSlot.byName(compoundTag.getString("slot"));
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(compoundTag.getString("attribute")));
            AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(compoundTag.getString("operation"));
            double amount = compoundTag.getDouble("amount");
            String modifierName = TinyInv.MODID + "#" + item.getDescriptionId() + "#" + slot.name() + "#" + operation.name() + "#" + attribute.getDescriptionId();
            UUID uuid = AttributeDataManager.stringToUUID(modifierName);
            itemModifiers.computeIfAbsent(item, i -> new HashMap<>()).computeIfAbsent(slot, s -> new HashMap<>()).computeIfAbsent(attribute, a -> new ArrayList<>()).add(new AttributeModifier(uuid, modifierName, amount, operation));
        }

        return new SyncItemAttributeDataToClient(itemModifiers);
    }

    public static class Handler {

        public static void handle(SyncItemAttributeDataToClient msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                AttributeDataManager.INSTANCE.setItemModifiers(msg.itemModifiers);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
