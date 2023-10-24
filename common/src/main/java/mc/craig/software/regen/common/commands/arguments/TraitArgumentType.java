package mc.craig.software.regen.common.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class TraitArgumentType implements ArgumentType<TraitBase> {

    public static final DynamicCommandExceptionType INVALID_TRAIT_EXCEPTION = new DynamicCommandExceptionType((trait) -> Component.translatable("argument.regeneration.trait.invalid", trait));

    public static TraitArgumentType traitArgumentType() {
        return new TraitArgumentType();
    }

    @Override
    public TraitBase parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation location = ResourceLocation.read(reader);
        TraitBase trait = TraitRegistry.TRAITS_REGISTRY.get(location);
        if (trait != null) {
            return trait;
        }
        throw INVALID_TRAIT_EXCEPTION.create(location);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(TraitRegistry.TRAITS_REGISTRY.getKeys(), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ArgumentType.super.getExamples();
    }
}
