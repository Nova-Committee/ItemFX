package committee.nova.itemfx.common.event;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

import static committee.nova.itemfx.common.tool.GenericHandler.*;
import static committee.nova.itemfx.common.util.TagReference.EFFECT_ID;
import static committee.nova.itemfx.common.util.TagReference.IDENTIFIED;

public class LivingUpdateHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof final Player player)) return;
        tryAddItemEffect(player);
        tickPlayerArmor(player);
        tickItem(player.getMainHandItem(), player);
        tickItem(player.getOffhandItem(), player);
        randomIdentifyItem(player);
    }

    private void tryAddItemEffect(Player player) {
        final NonNullList<ItemStack> inventory = player.getInventory().items;
        for (final ItemStack stack : inventory) {
            decideItemEffect(player, stack);
        }
    }

    private void randomIdentifyItem(Player player) {
        if (new Random().nextDouble() > 0.00001) return;
        final NonNullList<ItemStack> inv = player.getInventory().items;
        for (final ItemStack stack : inv) {
            final CompoundTag tag = stack.getOrCreateTag();
            if (tag.getBoolean(IDENTIFIED)) {
                identify(stack, player, true);
                return;
            }
        }
    }

    private void tickPlayerArmor(Player player) {
        final NonNullList<ItemStack> armors = player.getInventory().armor;
        int i = 0;
        for (final ItemStack armor : armors) {
            final int effect = armor.getOrCreateTag().getInt(EFFECT_ID);
            final int v = switch (effect) {
                case 1, 0 -> 0;
                default -> effect;
            };
            i += v;
        }
        triggerEvent(i, player);
    }

    private void tickItem(ItemStack stack, Player player) {
        if (!(stack.getItem() instanceof Vanishable)) return;
        final int effect = stack.getOrCreateTag().getInt(EFFECT_ID);
        final int v = switch (effect) {
            case 1, 0 -> 0;
            default -> effect;
        };
        triggerEvent(v, player);
    }


}
