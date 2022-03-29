package committee.nova.itemfx.common.event;

import committee.nova.itemfx.common.tool.GenericHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

import static committee.nova.itemfx.common.util.TagReference.*;

public class ToolTipHandler {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onItemToolTip(ItemTooltipEvent event) {
        final ItemStack stack = event.getItemStack();
        if (GenericHandler.stackNoEffect(stack)) return;
        final CompoundTag tag = stack.getOrCreateTag();
        if (!tag.getBoolean(EFFECT_DECIDED)) return;
        final List<Component> tooltips = event.getToolTip();
        final Component effect = !tag.getBoolean(IDENTIFIED) ? new TranslatableComponent("tooltip.itemfx.unidentified") : GenericHandler.getEffectStr(tag.getInt(EFFECT_ID));
        tooltips.add(effect);
    }
}
