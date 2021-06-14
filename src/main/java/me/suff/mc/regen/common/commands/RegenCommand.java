package me.suff.mc.regen.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.suff.mc.regen.common.commands.subcommands.*;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class RegenCommand {

    public static void register(CommandDispatcher< CommandSource > dispatcher) {
        dispatcher.register(
                Commands.literal(RConstants.MODID)
                        .requires(commandSource -> commandSource.hasPermission(ServerLifecycleHooks.getCurrentServer().getOperatorUserPermissionLevel()))
                        .then(SetRegensCommand.register(dispatcher))
                        .then(GlowCommand.register(dispatcher))
                        .then(FastForwardCommand.register(dispatcher)).then(SetTraitsCommand.register(dispatcher))
        );

    }
}
