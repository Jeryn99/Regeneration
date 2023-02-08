package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class RegenMobEffectsTags extends TagsProvider<MobEffect> {

    public RegenMobEffectsTags(DataGenerator arg, Registry<MobEffect> arg2, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, arg2, RConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        TagAppender<MobEffect> postRegen = tag(RegenUtil.POST_REGEN_POTIONS);
        postRegen.add(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.DIG_SLOWDOWN, MobEffects.CONFUSION, MobEffects.HUNGER, MobEffects.WEAKNESS, MobEffects.POISON, MobEffects.DARKNESS);
    }
}
