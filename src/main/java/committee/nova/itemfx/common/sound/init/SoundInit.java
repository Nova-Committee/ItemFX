package committee.nova.itemfx.common.sound.init;

import committee.nova.itemfx.common.ItemFx;
import committee.nova.itemfx.common.util.RegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class SoundInit {
    public static final Map<String, RegistryObject<SoundEvent>> sounds = new HashMap<>();
    public static final String[] soundNames = new String[]{"teleport"};

    public static void register() {
        for (final String name : soundNames) {
            sounds.put(name, addSound(name));
        }
    }

    private static RegistryObject<SoundEvent> addSound(String name) {
        return RegistryHandler.Sounds.register(name, () -> new SoundEvent(new ResourceLocation(ItemFx.MODID, name)));
    }
}
