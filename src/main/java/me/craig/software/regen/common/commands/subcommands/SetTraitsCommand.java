package me.craig.software.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.craig.software.regen.common.commands.arguments.TraitsArgumentType;
import me.craig.software.regen.util.RTextHelper;
import me.craig.software.regen.common.regen.RegenCap;
import me.craig.software.regen.common.traits.AbstractTrait;
import me.craig.software.regen.common.traits.RegenTraitRegistry;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SetTraitsCommand implements Command<CommandSource> {
    private static final SetTraitsCommand CMD = new SetTraitsCommand();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("set-trait")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("trait", TraitsArgumentType.createArgument())
                                .executes(CMD)));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        AbstractTrait trait = context.getArgument("trait", AbstractTrait.class);
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
        TextComponent playerText = RTextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());
        TextComponent traitText = RTextHelper.getTraitTextObject(trait);

        if (player == null || trait == null) {
            source.sendFailure(new TranslationTextComponent("command.regen.set_trait.error", playerText, traitText));
            return 0; //Zero is error
        }
        RegenCap.get(player).ifPresent((data) -> {
            ResourceLocation oldDna = data.trait().getRegistryName();
            AbstractTrait oldTrait = RegenTraitRegistry.fromID(oldDna);
            oldTrait.remove(data);
            data.setTrait(trait);
            trait.apply(data);
            source.sendSuccess(new TranslationTextComponent("command.regen.set_trait.success", playerText, traitText), false);
        });
        return Command.SINGLE_SUCCESS;
    }


}