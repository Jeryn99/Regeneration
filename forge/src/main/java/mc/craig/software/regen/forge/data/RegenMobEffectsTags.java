package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RegenMobEffectsTags extends TagsProvider<MobEffect> {

    public RegenMobEffectsTags(PackOutput arg, ResourceKey<? extends Registry<MobEffect>> arg2, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, arg2, completableFuture, Regeneration.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        TagAppender<MobEffect> postRegen = tag(RegenUtil.POST_REGEN_POTIONS);
        postRegen.add(get(MobEffects.MOVEMENT_SLOWDOWN), get(MobEffects.DIG_SLOWDOWN), get(MobEffects.CONFUSION), get(MobEffects.HUNGER), get(MobEffects.WEAKNESS), get(MobEffects.POISON), get(MobEffects.DARKNESS));
    }

    public ResourceKey<MobEffect> get(MobEffect mobEffect) {
        return BuiltInRegistries.MOB_EFFECT.getResourceKey(mobEffect).get();
    }
}
