package mc.craig.software.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.network.messages.RemoveSkinPlayerMessage;
import mc.craig.software.regen.util.constants.RMessages;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class FastForwardCommand implements Command<CommandSourceStack> {
    private static final FastForwardCommand CMD = new FastForwardCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("fast-forward")
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        RegenerationData.get(source.getPlayerOrException()).ifPresent((cap) -> {
            if (cap.regenState() != RegenStates.ALIVE) {
                cap.stateManager().skip();
                try {
                    new RemoveSkinPlayerMessage(source.getPlayerOrException().getUUID()).sendToAll();
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                throw new CommandRuntimeException(Component.translatable(RMessages.FAST_FORWARD_CMD_FAIL));
            }
        });
        return Command.SINGLE_SUCCESS;
    }
}
