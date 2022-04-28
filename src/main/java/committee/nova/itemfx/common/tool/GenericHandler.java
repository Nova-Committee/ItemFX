package committee.nova.itemfx.common.tool;

import committee.nova.itemfx.common.ItemFx;
import committee.nova.itemfx.common.config.Configuration;
import committee.nova.itemfx.common.sound.init.SoundInit;
import committee.nova.itemfx.common.util.TagReference;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.text.MessageFormat;
import java.util.Random;

import static committee.nova.itemfx.common.util.TagReference.*;

public class GenericHandler {
    public static final DamageSource UNSEEN_HAND = new DamageSource("unSeenHand").bypassArmor().bypassInvul();
    public static final DamageSource BLOOD_SUCKING = new DamageSource("bloodSucking").bypassArmor().bypassInvul();

    public static final TagKey<Item> BLACKLIST = ItemTags.create(new ResourceLocation(ItemFx.MODID, "blacklist"));
    public static final TagKey<Item> WHITELIST = ItemTags.create(new ResourceLocation(ItemFx.MODID, "whitelist"));

    public static void killPlayerByUnseenHand(Player player) {
        player.hurt(UNSEEN_HAND, Float.MAX_VALUE);
    }

    public static void scare(Player player) {
        player.level.playSound(null, player.getOnPos().above(), SoundEvents.AMBIENT_CAVE, SoundSource.PLAYERS, 1F, 1F);
    }

    public static void bloodsucking(Player player) {
        player.hurt(BLOOD_SUCKING, new Random().nextInt(9));
    }

    public static int rarityToInt(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> 2;
            case UNCOMMON -> 3;
            case RARE -> 4;
            case EPIC -> 5;
        };
    }

    public static boolean stackNoEffect(ItemStack stack) {
        final Item item = stack.getItem();
        return !(item instanceof Vanishable) && !stack.isEdible();
    }


    public static void decideItemEffect(Player player, ItemStack stack) {
        if (stackNoEffect(stack)) return;
        if (Configuration.blackListItem.get() && stack.is(BLACKLIST)) return;
        if (Configuration.whiteListItem.get() && !stack.is(WHITELIST)) return;
        final CompoundTag tag = stack.getOrCreateTag();
        if (tag.getBoolean(EFFECT_DECIDED)) return;
        final double rarityInfluenced = Math.log10(rarityToInt(stack.getRarity()));
        final double e = player.getRandom().nextDouble();
        final int effectType = (e <= (rarityInfluenced / 8)) ? 3 : (e <= (rarityInfluenced / 2)) ? 1 : (e <= rarityInfluenced) ? 2 : 0;
        tag.putInt(EFFECT_ID, effectType);
        tag.putBoolean(EFFECT_DECIDED, true);
        tag.putBoolean(IDENTIFIED, false);
    }

    public static void notifyPlayerIdentificationResult(Player player, ItemStack stack) {
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1F, 1F);
        final int effectId = stack.getOrCreateTag().getInt(TagReference.EFFECT_ID);
        final String toNotify = MessageFormat.format(new TranslatableComponent("msg.itemfx.identified").getString(),
                stack.getDisplayName().getString(), getEffectStr(effectId).getString());
        notifyServerPlayer(player, new TextComponent(toNotify));
    }

    public static Component getEffectStr(int effectId) {
        return new TranslatableComponent("tooltip.itemfx.effect." + effectId);
    }

    public static void notifyServerPlayer(Player player, Component component) {
        if (!player.level.isClientSide) player.sendMessage(component, Util.NIL_UUID);
    }

    public static void identify(ItemStack stack, Player player, boolean notify) {
        final CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(IDENTIFIED, true);
        if (notify) GenericHandler.notifyPlayerIdentificationResult(player, stack);
    }

    public static void triggerEvent(int i, Player player) {
        final double possibility = i == 0 ? 0 : Math.max(0.1, Math.log10(i) - 0.5) / 20;
        final double e = player.getRandom().nextDouble();
        //4 -> unseen hand; 3 -> scare; 2 -> blood sucking; 1 -> randomTp; 0 -> nothing
        if (e > possibility) return;
        final int triggeredEvent = (e <= (possibility / 8)) ? 4 : (e <= (possibility / 4)) ? 3 : (e <= (possibility / 2)) ? 2 : 1;
        switch (triggeredEvent) {
            case 4 -> killPlayerByUnseenHand(player);
            case 3 -> scare(player);
            case 2 -> bloodsucking(player);
            case 1 -> randomTp(player);
            default -> {
            }
        }
    }

    public static void randomTp(Player player) {
        final Level level = player.level;
        final double x = player.getX() + player.getRandom().nextInt(51) - 25;
        final double z = player.getZ() + player.getRandom().nextInt(51) - 25;
        //todo:msg
        player.teleportToWithTicket(x, level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, player.getY(), z)).getY() + 1, z);
        level.playSound(null, player.getOnPos().above(), SoundInit.sounds.get(SoundInit.soundNames[0]).get(), SoundSource.PLAYERS, 1F, 1F);
    }
}
