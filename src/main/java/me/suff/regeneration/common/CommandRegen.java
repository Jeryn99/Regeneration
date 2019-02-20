package me.suff.regeneration.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.suff.regeneration.RegenerationMod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.text.MessageFormat;

public class CommandRegen {
    public static void register(CommandDispatcher<CommandSource> dispatcher){
        dispatcher.register(Commands.literal("regendebug")
        .requires(s -> s.hasPermissionLevel(ServerLifecycleHooks.getCurrentServer().getOpPermissionLevel()))
            .then(Commands.literal("glow")
                .executes(ctx -> glow(ctx.getSource())))
            .then(Commands.literal("fastforward")
                .executes(ctx -> fastForward(ctx.getSource())))
            .then(Commands.literal("open")
                .executes(ctx -> open(ctx.getSource())))
            .then(Commands.literal("setregens")
                .then(Commands.argument("amount", IntegerArgumentType.integer(1)) //minimal regen to set is 1
                    .executes(ctx -> setRegens(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "amount"))))));
    }

    private static int glow(CommandSource source){
        RegenerationMod.LOG.info(MessageFormat.format("YO DAWG, I DID {0}", "glow"));
        //TODO : Glow action on Command
        return Command.SINGLE_SUCCESS;
    }

    private static int fastForward(CommandSource source){
        RegenerationMod.LOG.info(MessageFormat.format("YO DAWG, I DID {0}", "fastforward"));
        //TODO : FastForward action on Command
        return Command.SINGLE_SUCCESS;
    }

    private static int open(CommandSource source){
        RegenerationMod.LOG.info(MessageFormat.format("YO DAWG, I DID {0}", "open"));
        RegenerationMod.DEBUGGER.open();
        return Command.SINGLE_SUCCESS;
    }

    private static int setRegens(CommandSource source, int amount){
        RegenerationMod.LOG.info(MessageFormat.format("YO DAWG, I DID {0}", "set" + amount + "regens"));
        //TODO : SetRegens action on Command
        return Command.SINGLE_SUCCESS;
    }
}
