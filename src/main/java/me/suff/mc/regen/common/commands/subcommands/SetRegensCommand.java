package me.suff.mc.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.util.RTextHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SetRegensCommand implements Command< CommandSource > {
    private static final SetRegensCommand CMD = new SetRegensCommand();

    public static ArgumentBuilder< CommandSource, ? > register(CommandDispatcher< CommandSource > dispatcher) {
        return Commands.literal("set-regens")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes(CMD)));
    }


    @Override
    public int run(CommandContext< CommandSource > context) throws CommandSyntaxException {
        CommandSource source = context.getSource();
        Integer amount = context.getArgument("amount", Integer.class);
        ServerPlayerEntity player = EntityArgument.getPlayer(context, "player");
        
        if (player != null) {
        	TextComponent playerText = RTextHelper.getPlayerTextObject(source.getLevel(), player.getUUID());
            RegenCap.get(player).ifPresent((cap) -> cap.setRegens(amount));
            source.sendSuccess(new TranslationTextComponent("command.regen.set_regen.success", playerText, amount), false);
            return Command.SINGLE_SUCCESS;
        } else {
            source.sendFailure(new StringTextComponent("No player found."));
            return 0;
        }
    }
}
