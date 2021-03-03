package me.swirtzly.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class SetRegensCommand implements Command< CommandSource > {
    private static final SetRegensCommand CMD = new SetRegensCommand();

    public static ArgumentBuilder< CommandSource, ? > register(CommandDispatcher< CommandSource > dispatcher) {
        return Commands.literal("set-regens")
                .then(Commands.argument("username", StringArgumentType.string())
                        .suggests((context, builder) -> ISuggestionProvider.suggest(ServerLifecycleHooks.getCurrentServer().getOnlinePlayerNames(), builder))
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes(CMD)));
    }


    @Override
    public int run(CommandContext< CommandSource > context) {
        CommandSource source = context.getSource();
        Integer amount = context.getArgument("amount", Integer.class);
        String username = context.getArgument("username", String.class);
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(username);

        if (player != null) {
            RegenCap.get(player).ifPresent((cap) -> cap.setRegens(amount));
        } else {
            source.sendErrorMessage(new StringTextComponent("No player found for this username."));
        }
        return Command.SINGLE_SUCCESS;
    }
}
