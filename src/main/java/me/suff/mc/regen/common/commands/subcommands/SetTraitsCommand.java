package me.suff.mc.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.suff.mc.regen.common.commands.arguments.TraitsArgumentType;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.traits.AbstractTrait;
import me.suff.mc.regen.common.traits.RegenTraitRegistry;
import me.suff.mc.regen.util.RTextHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SetTraitsCommand implements Command<CommandSourceStack> {
    private static final SetTraitsCommand CMD = new SetTraitsCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("set-trait")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("trait", TraitsArgumentType.createArgument())
                                .executes(CMD)));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        AbstractTrait trait = context.getArgument("trait", AbstractTrait.class);
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        BaseComponent playerText = RTextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());
        BaseComponent traitText = RTextHelper.getTraitTextObject(trait);

        if (player == null || trait == null) {
            source.sendFailure(new TranslatableComponent("command.regen.set_trait.error", playerText, traitText));
            return 0; //Zero is error
        }
        RegenCap.get(player).ifPresent((data) -> {
            ResourceLocation oldDna = data.trait().getRegistryName();
            AbstractTrait oldTrait = RegenTraitRegistry.fromID(oldDna);
            oldTrait.remove(data);
            data.setTrait(trait);
            trait.apply(data);
            source.sendSuccess(new TranslatableComponent("command.regen.set_trait.success", playerText, traitText), false);
        });
        return Command.SINGLE_SUCCESS;
    }


}