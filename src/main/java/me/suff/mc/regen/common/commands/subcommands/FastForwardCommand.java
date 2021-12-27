package me.suff.mc.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.RemoveSkinPlayerMessage;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.network.PacketDistributor;

public class FastForwardCommand implements Command<CommandSourceStack> {
    private static final FastForwardCommand CMD = new FastForwardCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("fast-forward")
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        RegenCap.get(source.getPlayerOrException()).ifPresent((cap) -> {
            if (cap.regenState() != RegenStates.ALIVE) {
                cap.stateManager().skip();
                try {
                    NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), new RemoveSkinPlayerMessage(source.getPlayerOrException().getUUID()));
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                throw new CommandRuntimeException(new TranslatableComponent("regen.messages.fast_forward_cmd_fail"));
            }
        });
        return Command.SINGLE_SUCCESS;
    }
}
