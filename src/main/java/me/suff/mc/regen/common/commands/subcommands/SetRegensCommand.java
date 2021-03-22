package me.suff.mc.regen.common.commands.subcommands;

import java.util.Collection;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.RTextHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SetRegensCommand implements Command<CommandSource> {

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher< CommandSource > dispatcher) {
        return Commands.literal("set-regens")
                .then(Commands.argument("entities", EntityArgument.entities())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes(context -> setRegenForMultipleEntities(context, EntityArgument.getEntities(context, "entities"), IntegerArgumentType.getInteger(context, "amount")))));
    }
    
    private static int setRegenForMultipleEntities(CommandContext<CommandSource> context, Collection<? extends Entity> collection, int amount) {
    	collection.forEach(target -> {
    		setRegenForSingleEntity(context, target, amount);
    	});
    	return Command.SINGLE_SUCCESS;
    }
    
    private static int setRegenForSingleEntity(CommandContext<CommandSource> context, Entity entity, int amount) {
    	TextComponent entityText = RTextHelper.getEntityTextObject(context.getSource().getLevel(), entity.getUUID());
    	//Need a special case Armor Stands, for some reason they are classified as LivingEntities...
    	if (entity instanceof LivingEntity && entity.getType() != EntityType.ARMOR_STAND && entity != null) {
    		if (RegenConfig.COMMON.mobsHaveRegens.get() || entity instanceof ServerPlayerEntity) {//If the config option allows mobs to have regens, continue
    			LivingEntity ent = (LivingEntity)entity;
                RegenCap.get(ent).ifPresent((cap) -> cap.setRegens(amount));
                context.getSource().sendSuccess(new TranslationTextComponent("command.regen.set_regen.success", entityText, amount), false);
                return Command.SINGLE_SUCCESS;
    		}
    		else {//Send error message if the config option doesn't allow for it
    			String configOptionKey = "config.regeneration.mobsHaveRegens";
    			context.getSource().sendFailure(new TranslationTextComponent("command.regen.set_regen.config_off", entityText, new TranslationTextComponent(configOptionKey).getString(), RegenConfig.COMMON.mobsHaveRegens.get()));
    			return 0;
    		}
        } else { //Don't make this work for non living entities
            context.getSource().sendFailure(new TranslationTextComponent("command.regen.set_regen.invalid_entity", amount, entityText));
            return 0;
        }
    }


    @Override
    public int run(CommandContext< CommandSource > context) throws CommandSyntaxException {
        return 0;
    }
}
