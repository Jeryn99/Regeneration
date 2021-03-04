package me.suff.mc.regen.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.suff.mc.regen.common.commands.subcommands.FastForwardCommand;
import me.suff.mc.regen.common.commands.subcommands.GlowCommand;
import me.suff.mc.regen.common.commands.subcommands.SetRegensCommand;
import me.suff.mc.regen.common.commands.subcommands.SetTraitsCommand;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class RegenCommand {

    public static void register(CommandDispatcher< CommandSource > dispatcher) {
        dispatcher.register(
                Commands.literal(RConstants.MODID)
                        .requires(commandSource -> commandSource.hasPermissionLevel(ServerLifecycleHooks.getCurrentServer().getOpPermissionLevel()))
                        .then(SetRegensCommand.register(dispatcher))
                        .then(GlowCommand.register(dispatcher))
                        .then(FastForwardCommand.register(dispatcher)).then(SetTraitsCommand.register(dispatcher))
        );

    }
}
