package committee.nova.itemfx.common.util;

import committee.nova.itemfx.common.ItemFx;
import committee.nova.itemfx.common.item.init.ItemInit;
import committee.nova.itemfx.common.sound.init.SoundInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<SoundEvent> Sounds = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ItemFx.MODID);
    public static final DeferredRegister<Item> Items = DeferredRegister.create(ForgeRegistries.ITEMS, ItemFx.MODID);

    public static void register() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SoundInit.register();
        ItemInit.register();
        Sounds.register(eventBus);
        Items.register(eventBus);
    }
}
