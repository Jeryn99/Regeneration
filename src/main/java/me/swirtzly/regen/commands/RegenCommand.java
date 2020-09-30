package me.swirtzly.regen.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.swirtzly.regen.commands.subcommands.FastForwardCommand;
import me.swirtzly.regen.commands.subcommands.GlowCommand;
import me.swirtzly.regen.commands.subcommands.SetRegensCommand;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class RegenCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(RConstants.MODID)
                        .requires(commandSource -> commandSource.hasPermissionLevel(ServerLifecycleHooks.getCurrentServer().getOpPermissionLevel()))
                        .then(SetRegensCommand.register(dispatcher))
                        .then(GlowCommand.register(dispatcher))
                        .then(FastForwardCommand.register(dispatcher))
        );

    }
}
