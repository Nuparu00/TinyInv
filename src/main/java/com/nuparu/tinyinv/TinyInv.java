package com.nuparu.tinyinv;

import com.nuparu.tinyinv.events.TickHandler;
import com.nuparu.tinyinv.item.ItemFake;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = TinyInv.MODID, name = TinyInv.NAME, version = TinyInv.VERSION)
public class TinyInv
{
    public static final String MODID = "tinyinv";
    public static final String NAME = "Tiny Inv";
    public static final String VERSION = "1.3";

    private static Logger logger;

    public static final Item fakeItem = new ItemFake();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new TickHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }


}
