package me.swirtzly.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class GlowCommand implements Command<CommandSource> {
    private static final GlowCommand CMD = new GlowCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("glow")
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        RegenCap.get(source.asPlayer()).ifPresent((cap) -> cap.getStateManager().fastForwardHandGlow());
        return Command.SINGLE_SUCCESS;
    }
}
