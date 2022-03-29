package committee.nova.itemfx.common.command.init;

import com.mojang.brigadier.CommandDispatcher;
import committee.nova.itemfx.common.command.impl.CommandImpls;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandInit {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        final CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("itemfx").then(Commands.literal("identify").requires(e -> e.hasPermission(2)).then(CommandImpls.registerIdentify())));
        dispatcher.register(Commands.literal("itemfx").then(Commands.literal("setEffect").requires(e -> e.hasPermission(2)).then(CommandImpls.registerSetEffect())));
    }
}
