package mc.craig.software.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mc.craig.software.regen.common.commands.arguments.TraitArgumentType;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.util.RTextHelper;
import mc.craig.software.regen.util.constants.RMessages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

public class TraitsCommand implements Command<CommandSourceStack> {
    private static final TraitsCommand CMD = new TraitsCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("set-trait")
                .then(Commands.argument("players", EntityArgument.players())
                        .then(Commands.argument("trait", TraitArgumentType.traitArgumentType())
                                .executes(CMD)));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        // Get the command source, player, and trait from the command context.
        CommandSourceStack source = context.getSource();
        TraitBase trait = context.getArgument("trait", TraitBase.class);
        ServerPlayer player = EntityArgument.getPlayer(context, "player");

        // Get the player and trait text components for use in the messages.
        MutableComponent playerText = RTextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());
        MutableComponent traitText = RTextHelper.getTraitTextObject(trait);

        // Check if the player and trait are valid. If not, send an error message and return 0 (error).
        if (player == null || trait == null) {
            source.sendFailure(Component.translatable(RMessages.SET_TRAIT_ERROR, playerText, traitText));
            return 0;
        }

        // If the player has a RegenerationData instance, set the current trait and sync the data to the clients.
        // Send a success message to the command source.
        RegenerationData.get(player).ifPresent((data) -> {
            TraitBase oldTrait = data.getCurrentTrait();
            oldTrait.onRemoved(player, data);
            data.setCurrentTrait(trait);
            trait.onAdded(player, data); // Start from here
            data.syncToClients(null);
            source.sendSuccess(Component.translatable(RMessages.SET_TRAIT_SUCCESS, playerText, traitText), false);
        });
        return Command.SINGLE_SUCCESS;
    }

}