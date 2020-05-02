package me.swirtzly.regeneration.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.swirtzly.regeneration.common.traits.TraitManager;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.concurrent.CompletableFuture;

public class TraitsArgumentType implements ArgumentType<TraitManager.IDna> {
    public static final DynamicCommandExceptionType INVALID_TRAIT_EXCEPTION = new DynamicCommandExceptionType((trait) -> new TranslationTextComponent("argument.regeneration.trait.invalid", new Object[]{trait}));

    public static TraitsArgumentType createArgument()
    {
        return new TraitsArgumentType();
    }

    @Override
    public TraitManager.IDna parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation location = ResourceLocation.read(reader);
        TraitManager.IDna trait = TraitManager.DNA_ENTRIES.getOrDefault(location, null);
        if (trait != null){
            return trait;
        }
        throw INVALID_TRAIT_EXCEPTION.create(location);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggestIterable(TraitManager.DNA_ENTRIES.keySet(), builder);
    }
}
