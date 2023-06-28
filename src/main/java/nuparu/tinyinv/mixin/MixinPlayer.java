package nuparu.tinyinv.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nuparu.tinyinv.init.ModAttributes;
import nuparu.tinyinv.init.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {
    @Inject(at = @At("RETURN"), method = "createAttributes()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;", cancellable = true)
    private static void createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.setReturnValue(cir.getReturnValue()
                .add(ModAttributes.SLOTS.get())
                .add(ModAttributes.HOTBAR_SLOTS.get())
                .add(ModAttributes.OFFHAND_SLOT.get())
                .add(ModAttributes.HEAD_SLOT.get())
                .add(ModAttributes.CHEST_SLOT.get())
                .add(ModAttributes.LEGS_SLOT.get())
                .add(ModAttributes.FEET_SLOT.get())
                .add(ModAttributes.CRAFT_TOP_LEFT_SLOT.get())
                .add(ModAttributes.CRAFT_TOP_RIGHT_SLOT.get())
                .add(ModAttributes.CRAFT_BOTTOM_LEFT_SLOT.get())
                .add(ModAttributes.CRAFT_BOTTOM_RIGHT_SLOT.get())
        );
    }

    @Inject(at = @At("RETURN"), method = "getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;", cancellable = true)
    private void getItemBySlot(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot == EquipmentSlot.OFFHAND && cir.getReturnValue().is(ModItems.FAKE_ITEM.get())) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }
}
