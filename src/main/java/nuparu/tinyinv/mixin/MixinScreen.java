package nuparu.tinyinv.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface MixinScreen {
    @Accessor("itemRenderer")
    ItemRenderer tinyinv_getItermRenderer();
}
