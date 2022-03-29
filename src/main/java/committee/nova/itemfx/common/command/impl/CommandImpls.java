package committee.nova.itemfx.common.command.impl;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import committee.nova.itemfx.common.util.TagReference;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.text.MessageFormat;

import static committee.nova.itemfx.common.tool.GenericHandler.*;

public class CommandImpls {
    public static ArgumentBuilder<CommandSourceStack, ?> registerIdentify() {
        return Commands.argument("isMainHand", BoolArgumentType.bool()).executes(CommandImpls::identifyExecution);
    }

    public static ArgumentBuilder<CommandSourceStack, ?> registerSetEffect() {
        return Commands.argument("effectId", IntegerArgumentType.integer()).executes(CommandImpls::setEffectExecution);
    }

    public static int identifyExecution(CommandContext<CommandSourceStack> context) throws CommandRuntimeException, CommandSyntaxException {
        final Player player = context.getSource().getPlayerOrException();
        final ItemStack stack = (context.getArgument("isMainHand", Boolean.class)) ? player.getMainHandItem() : player.getOffhandItem();
        if (stackNoEffect(stack)) return 0;
        if (stack.getOrCreateTag().getBoolean(TagReference.IDENTIFIED)) return 0;
        identify(stack, player, true);
        return 0;
    }

    public static int setEffectExecution(CommandContext<CommandSourceStack> context) throws CommandRuntimeException, CommandSyntaxException {
        final Player player = context.getSource().getPlayerOrException();
        final ItemStack stack = player.getMainHandItem();
        if (stackNoEffect(stack)) return 0;
        stack.getOrCreateTag().putInt(TagReference.EFFECT_ID, context.getArgument("effectId", Integer.class));
        notifyServerPlayer(player, new TextComponent(MessageFormat.format(new TranslatableComponent("msg.itemfx.effect_set").getString(), stack.getDisplayName().getString(), getEffectStr(stack.getOrCreateTag().getInt(TagReference.EFFECT_ID)).getString())));
        return 0;
    }


}
