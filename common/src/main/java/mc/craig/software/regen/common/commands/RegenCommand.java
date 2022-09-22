package mc.craig.software.regen.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import mc.craig.software.regen.common.commands.subcommands.FastForwardCommand;
import mc.craig.software.regen.common.commands.subcommands.GlowCommand;
import mc.craig.software.regen.common.commands.subcommands.SetRegensCommand;
import mc.craig.software.regen.common.commands.subcommands.TraitsCommand;
import mc.craig.software.regen.util.Platform;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RegenCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(RConstants.MODID)
                        .requires(commandSource -> commandSource.hasPermission(Platform.getServer().getOperatorUserPermissionLevel()))
                        .then(SetRegensCommand.register(dispatcher))
                        .then(GlowCommand.register(dispatcher))
                        .then(FastForwardCommand.register(dispatcher))
                        .then(TraitsCommand.register(dispatcher))
        );

    }
}
