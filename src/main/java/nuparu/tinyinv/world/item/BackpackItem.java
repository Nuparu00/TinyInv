package nuparu.tinyinv.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import nuparu.tinyinv.init.ModAttributes;

import java.util.UUID;

public class BackpackItem extends Item implements Equipable {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private final UUID uuid = UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E");;
    private final int slots;
    public BackpackItem(Properties p_41383_, int slots) {
        super(p_41383_);
        this.slots = slots;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ModAttributes.SLOTS.get(), new AttributeModifier(uuid, "Armor modifier", slots, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40390_) {
        return p_40390_ == getEquipmentSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_40390_);
    }
}
