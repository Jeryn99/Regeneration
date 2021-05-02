package me.suff.mc.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.handlers.CommonEvents;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class RegenCitadel implements Command< CommandSource > {
    private static final RegenCitadel CMD = new RegenCitadel();

    public static ArgumentBuilder< CommandSource, ? > register(CommandDispatcher< CommandSource > dispatcher) {
        return Commands.literal("regen-citadel")
                .executes(CMD);
    }

    @Override
    public int run(CommandContext< CommandSource > context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        CommonEvents.generateCitadel(source.getLevel());
        return Command.SINGLE_SUCCESS;
    }
}
