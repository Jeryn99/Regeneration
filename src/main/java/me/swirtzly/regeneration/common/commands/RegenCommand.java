package me.swirtzly.regeneration.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.common.commands.subcommands.FastForwardCommand;
import me.swirtzly.regeneration.common.commands.subcommands.GlowCommand;
import me.swirtzly.regeneration.common.commands.subcommands.SetRegensCommand;
import me.swirtzly.regeneration.common.commands.subcommands.SetTraitsCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class RegenCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(Regeneration.MODID)
                        .requires(commandSource -> commandSource.hasPermission(ServerLifecycleHooks.getCurrentServer().getOperatorUserPermissionLevel()))
                        .then(SetRegensCommand.register(dispatcher))
                        .then(GlowCommand.register(dispatcher))
                        .then(FastForwardCommand.register(dispatcher))
                        .then(SetTraitsCommand.register(dispatcher))
        );

    }
}
