package committee.nova.itemfx.common.item.impl;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

import static committee.nova.itemfx.common.tool.GenericHandler.identify;
import static committee.nova.itemfx.common.tool.GenericHandler.stackNoEffect;
import static committee.nova.itemfx.common.util.TagReference.EFFECT_ID;
import static committee.nova.itemfx.common.util.TagReference.IDENTIFIED;

public class IdentifyScrollItem extends Item {
    public IdentifyScrollItem() {
        super(new Properties().tab(CreativeModeTab.TAB_TOOLS));
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        final ItemStack scroll = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(scroll);
        final ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);
        final int effect = scroll.getOrCreateTag().getInt(EFFECT_ID);
        switch (effect) {
            case 0 -> identifyOrSkip(player, scroll, off);
            case 1 -> tryIdentifyInventoryItem(player, scroll, off);
            case 2, 3 -> {
                player.level.explode(player, player.getX(), player.getY(), player.getZ(), 2.5F * effect, Explosion.BlockInteraction.NONE);
                scroll.shrink(1);
                if (scroll.isEmpty()) player.getInventory().removeItem(scroll);
            }
        }
        return InteractionResultHolder.success(scroll);
    }


    private void tryIdentifyInventoryItem(Player player, ItemStack scroll, ItemStack off) {
        identifyOrSkip(player, scroll, off);
        final NonNullList<ItemStack> inv = player.getInventory().items;
        int i = 0;
        for (final ItemStack stack : inv) {
            if (isIdentifiable(player, stack)) {
                i++;
                identifyAndConsume(player, scroll, stack);
            }
            if (i >= 2) return;
        }
    }

    private void identifyOrSkip(Player player, ItemStack scroll, ItemStack target) {
        if (!isIdentifiable(player, target)) return;
        identifyAndConsume(player, scroll, target);
    }

    private void identifyAndConsume(Player player, ItemStack scroll, ItemStack target) {
        identify(target, player, true);
        scroll.shrink(1);
        if (scroll.isEmpty()) player.getInventory().removeItem(scroll);
    }

    private boolean isIdentifiable(Player player, ItemStack target) {
        final CompoundTag tag = target.getOrCreateTag();
        if (stackNoEffect(target)) {
            player.displayClientMessage(new TranslatableComponent("msg.itemfx.invalid_stack"), true);
            return false;
        }
        if (tag.getBoolean(IDENTIFIED)) {
            player.displayClientMessage(new TranslatableComponent("msg.itemfx.already_identified"), true);
            return false;
        }
        return true;
    }

}
