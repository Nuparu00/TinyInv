package nuparu.tinyinv.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import nuparu.tinyinv.TinyInv;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, TinyInv.MODID);
    public static final RegistryObject<Attribute> SLOTS = ATTRIBUTES.register("slots", () -> new RangedAttribute("tinyinv.slots",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> HOTBAR_SLOTS = ATTRIBUTES.register("hotbar_slots", () -> new RangedAttribute("tinyinv.hotbar_slots",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> OFFHAND_SLOT = ATTRIBUTES.register("offhand_slot", () -> new RangedAttribute("tinyinv.offhand_slot",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> HEAD_SLOT = ATTRIBUTES.register("head_slot", () -> new RangedAttribute("tinyinv.head_slot",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> CHEST_SLOT = ATTRIBUTES.register("chest_slot", () -> new RangedAttribute("tinyinv.chest_slot",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> LEGS_SLOT = ATTRIBUTES.register("legs_slot", () -> new RangedAttribute("tinyinv.legs_slot",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> FEET_SLOT = ATTRIBUTES.register("feet_slot", () -> new RangedAttribute("tinyinv.feet_slot",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> CRAFT_TOP_LEFT_SLOT = ATTRIBUTES.register("crafting_top_left_slot", () -> new RangedAttribute("tinyinv.crafting_top_left_slot",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> CRAFT_TOP_RIGHT_SLOT = ATTRIBUTES.register("crafting_top_right_slot", () -> new RangedAttribute("tinyinv.crafting_top_right_slot",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> CRAFT_BOTTOM_LEFT_SLOT = ATTRIBUTES.register("crafting_bottom_left_slot", () -> new RangedAttribute("tinyinv.crafting_bottom_left_slot",0,-64,64).setSyncable(true));
    public static final RegistryObject<Attribute> CRAFT_BOTTOM_RIGHT_SLOT = ATTRIBUTES.register("crafting_bottom_right_slot", () -> new RangedAttribute("tinyinv.crafting_bottom_right_slot",0,-64,64).setSyncable(true));
}
