package me.craig.software.regen.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.craig.software.regen.common.commands.subcommands.FastForwardCommand;
import me.craig.software.regen.common.commands.subcommands.GlowCommand;
import me.craig.software.regen.common.commands.subcommands.SetRegensCommand;
import me.craig.software.regen.common.commands.subcommands.SetTraitsCommand;
import me.craig.software.regen.util.RConstants;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class RegenCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(RConstants.MODID)
                        .requires(commandSource -> commandSource.hasPermission(ServerLifecycleHooks.getCurrentServer().getOperatorUserPermissionLevel()))
                        .then(SetRegensCommand.register(dispatcher))
                        .then(GlowCommand.register(dispatcher))
                        .then(FastForwardCommand.register(dispatcher)).then(SetTraitsCommand.register(dispatcher))
        );

    }
}
