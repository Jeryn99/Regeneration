package me.suff.mc.regen.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.suff.mc.regen.common.commands.subcommands.FastForwardCommand;
import me.suff.mc.regen.common.commands.subcommands.GlowCommand;
import me.suff.mc.regen.common.commands.subcommands.SetRegensCommand;
import me.suff.mc.regen.common.commands.subcommands.SetTraitsCommand;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.server.ServerLifecycleHooks;

public class RegenCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(RConstants.MODID)
                        .requires(commandSource -> commandSource.hasPermission(ServerLifecycleHooks.getCurrentServer().getOperatorUserPermissionLevel()))
                        .then(SetRegensCommand.register(dispatcher))
                        .then(GlowCommand.register(dispatcher))
                        .then(FastForwardCommand.register(dispatcher)).then(SetTraitsCommand.register(dispatcher))
        );

    }
}
