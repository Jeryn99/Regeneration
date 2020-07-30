package me.swirtzly.regeneration.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.commands.arguments.TraitsArgumentType;
import me.swirtzly.regeneration.common.traits.TraitManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class SetTraitsCommand implements Command<CommandSource> {
    private static final SetTraitsCommand CMD = new SetTraitsCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("set-traits")
                .then(Commands.argument("username", StringArgumentType.string())
                        .suggests((context, builder) -> ISuggestionProvider.suggest(ServerLifecycleHooks.getCurrentServer().getOnlinePlayerNames(), builder))
                            .then(Commands.argument("trait", TraitsArgumentType.createArgument())
                                .executes(CMD)));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        TraitManager.IDna trait = context.getArgument("trait", TraitManager.IDna.class);
        String username = context.getArgument("username", String.class);
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUsername(username);

        if (player == null || trait == null) {
            return Command.SINGLE_SUCCESS;
        }

        RegenCap.get(player).ifPresent((data) -> {
            ResourceLocation oldDna = data.getTrait();
            TraitManager.IDna oldTrait = TraitManager.getDnaEntry(oldDna);
            oldTrait.onRemoved(data);
            data.setTrait(trait.getRegistryName());
            trait.onAdded(data);
        });
        return Command.SINGLE_SUCCESS;
    }


}
