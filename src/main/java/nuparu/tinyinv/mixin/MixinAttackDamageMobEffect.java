package nuparu.tinyinv.mixin;

import net.minecraft.world.effect.AttackDamageMobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.world.entity.player.PlayerSlots;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mojang was lazy a made the multiplier affect everything
 */
@Mixin(AttackDamageMobEffect.class)
public class MixinAttackDamageMobEffect {
    @Inject(at = @At("HEAD") ,method = "getAttributeModifierValue(ILnet/minecraft/world/entity/ai/attributes/AttributeModifier;)D", cancellable = true)
    private void getAttributeModifierValue(int p_19430_, AttributeModifier p_19431_, CallbackInfoReturnable<Double> cir) {
        if(p_19431_.getName().startsWith(TinyInv.MODID)){
            cir.cancel();
            cir.setReturnValue(p_19431_.getAmount() * (double)(p_19430_ + 1));
        }
    }
}
