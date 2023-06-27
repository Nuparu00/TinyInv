package nuparu.tinyinv.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nuparu.tinyinv.TinyInv;
import nuparu.tinyinv.world.item.BackpackItem;
import nuparu.tinyinv.world.item.FakeItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TinyInv.MODID);
    public static final RegistryObject<FakeItem> FAKE_ITEM = ITEMS.register("fake_item", FakeItem::new);
    public static final RegistryObject<BackpackItem> BACKPACK = ITEMS.register("backpack", () -> new BackpackItem(new Item.Properties().stacksTo(1),9));

}
