package committee.nova.itemfx.common;

import committee.nova.itemfx.common.config.Configuration;
import committee.nova.itemfx.common.event.ItemCraftedHandler;
import committee.nova.itemfx.common.event.LivingUpdateHandler;
import committee.nova.itemfx.common.event.ToolTipHandler;
import committee.nova.itemfx.common.util.RegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(ItemFx.MODID)
public class ItemFx {
    public static final String MODID = "itemfx";

    public ItemFx() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);
        RegistryHandler.register();
        MinecraftForge.EVENT_BUS.register(new LivingUpdateHandler());
        MinecraftForge.EVENT_BUS.register(new ItemCraftedHandler());
        MinecraftForge.EVENT_BUS.register(new ToolTipHandler());
        MinecraftForge.EVENT_BUS.register(this);
    }
}
