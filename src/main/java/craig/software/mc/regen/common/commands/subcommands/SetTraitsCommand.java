package craig.software.mc.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import craig.software.mc.regen.common.commands.arguments.TraitsArgumentType;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.traits.AbstractTrait;
import craig.software.mc.regen.common.traits.RegenTraitRegistry;
import craig.software.mc.regen.util.RTextHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
        MutableComponent playerText = RTextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());
        MutableComponent traitText = RTextHelper.getTraitTextObject(trait);

        if (player == null || trait == null) {
            source.sendFailure(Component.translatable("command.regen.set_trait.error", playerText, traitText));
            return 0; //Zero is error
        }
        RegenCap.get(player).ifPresent((data) -> {
            ResourceLocation oldDna = RegenTraitRegistry.getTraitLocation(data.trait());
            AbstractTrait oldTrait = RegenTraitRegistry.fromID(oldDna);
            oldTrait.remove(data);
            data.setTrait(trait);
            trait.apply(data);
            source.sendSuccess(Component.translatable("command.regen.set_trait.success", playerText, traitText), false);
        });
        return Command.SINGLE_SUCCESS;
    }


}