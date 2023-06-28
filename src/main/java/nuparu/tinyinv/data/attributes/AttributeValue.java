package nuparu.tinyinv.data.attributes;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeValue(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot){

}