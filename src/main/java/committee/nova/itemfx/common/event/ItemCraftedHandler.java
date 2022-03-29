package committee.nova.itemfx.common.event;

import committee.nova.itemfx.common.tool.GenericHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemCraftedHandler {
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        final Player player = event.getPlayer();
        final ItemStack stack = event.getCrafting();
        GenericHandler.decideItemEffect(player, stack);
        GenericHandler.identify(stack, player, true);
    }
}
