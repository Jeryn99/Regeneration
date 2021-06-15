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
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

public class FastForwardCommand implements Command<CommandSource> {
    private static final FastForwardCommand CMD = new FastForwardCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("fast-forward")
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();

        RegenCap.get(source.getPlayerOrException()).ifPresent((cap) -> {
            if (cap.regenState() != RegenStates.ALIVE) {
                cap.stateManager().skip();
                try {
                    NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), new RemoveSkinPlayerMessage(source.getPlayerOrException().getUUID()));
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                throw new CommandException(new TranslationTextComponent("regen.messages.fast_forward_cmd_fail"));
            }
        });
        return Command.SINGLE_SUCCESS;
    }
}
