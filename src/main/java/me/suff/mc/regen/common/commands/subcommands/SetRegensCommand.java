package me.suff.mc.regen.common.commands.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.RTextHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class SetRegensCommand implements Command<CommandSourceStack> {

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("set-regens")
                .then(Commands.argument("entities", EntityArgument.entities())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                .executes(context -> setRegenForMultipleEntities(context, EntityArgument.getEntities(context, "entities"), IntegerArgumentType.getInteger(context, "amount")))));
    }

    private static int setRegenForMultipleEntities(CommandContext<CommandSourceStack> context, Collection<? extends Entity> collection, int amount) {
        collection.forEach(target -> setRegenForSingleEntity(context, target, amount));
        return Command.SINGLE_SUCCESS;
    }

    private static int setRegenForSingleEntity(CommandContext<CommandSourceStack> context, Entity entity, int amount) {
        MutableComponent entityText = RTextHelper.getEntityTextObject(context.getSource().getLevel(), entity.getUUID());
        //Need a special case Armor Stands, for some reason they are classified as LivingEntities...
        if (entity instanceof LivingEntity && entity.getType() != EntityType.ARMOR_STAND && entity != null) {
            if (RegenConfig.COMMON.mobsHaveRegens.get() || entity instanceof ServerPlayer) {//If the config option allows mobs to have regens, continue
                LivingEntity ent = (LivingEntity) entity;
                RegenCap.get(ent).ifPresent((cap) -> cap.setRegens(amount));
                context.getSource().sendSuccess(Component.translatable("command.regen.set_regen.success", entityText, amount), false);
                return Command.SINGLE_SUCCESS;
            } else {//Send error message if the config option doesn't allow for it
                String configOptionKey = "config.regen.mobsHaveRegens";
                context.getSource().sendFailure(Component.translatable("command.regen.set_regen.config_off", entityText, Component.translatable(configOptionKey).getString(), RegenConfig.COMMON.mobsHaveRegens.get()));
                return 0;
            }
        } else { //Don't make this work for non living entities
            context.getSource().sendFailure(Component.translatable("command.regen.set_regen.invalid_entity", amount, entityText));
            return 0;
        }
    }


    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return 0;
    }
}
