package me.suff.mc.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.suff.mc.regen.common.commands.arguments.TraitsArgumentType;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.traits.Traits;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class SetTraitsCommand implements Command< CommandSource > {
    private static final SetTraitsCommand CMD = new SetTraitsCommand();

    public static ArgumentBuilder< CommandSource, ? > register(CommandDispatcher< CommandSource > dispatcher) {
        return Commands.literal("set-trait")
                .then(Commands.argument("username", StringArgumentType.string())
                        .suggests((context, builder) -> ISuggestionProvider.suggest(ServerLifecycleHooks.getCurrentServer().getPlayerNames(), builder))
                        .then(Commands.argument("trait", TraitsArgumentType.createArgument())
                                .executes(CMD)));
    }

    @Override
    public int run(CommandContext< CommandSource > context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        Traits.ITrait trait = context.getArgument("trait", Traits.ITrait.class);
        String username = context.getArgument("username", String.class);
        ServerPlayerEntity player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(username);

        if (player == null || trait == null) {
            return Command.SINGLE_SUCCESS;
        }

        RegenCap.get(player).ifPresent((data) -> {
            ResourceLocation oldDna = data.trait().getRegistryName();
            Traits.ITrait oldTrait = Traits.fromID(oldDna.toString());
            oldTrait.remove(data);
            data.setTrait(trait);
            trait.apply(data);
        });
        return Command.SINGLE_SUCCESS;
    }


}