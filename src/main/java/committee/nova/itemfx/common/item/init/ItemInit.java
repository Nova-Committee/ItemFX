package committee.nova.itemfx.common.item.init;

import committee.nova.itemfx.common.item.impl.IdentifyScrollItem;
import committee.nova.itemfx.common.util.RegistryHandler;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final RegistryObject<Item> identifyScroll = RegistryHandler.Items.register("identify_scroll", IdentifyScrollItem::new);

    public static void register() {
    }
}
