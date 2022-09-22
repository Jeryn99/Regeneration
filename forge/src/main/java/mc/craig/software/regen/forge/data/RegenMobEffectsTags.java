package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.util.constants.RConstants;
import mc.craig.software.regen.util.RegenUtil;
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
        for (MobEffect mobEffect : Registry.MOB_EFFECT) {

            if (!mobEffect.isBeneficial() && mobEffect != MobEffects.LEVITATION) {
                tag(RegenUtil.POST_REGEN_POTIONS).add(mobEffect);
            }


            tag(RegenUtil.POST_REGEN_POTIONS).remove(MobEffects.LEVITATION);
            tag(RegenUtil.POST_REGEN_POTIONS).remove(MobEffects.HARM);
            tag(RegenUtil.POST_REGEN_POTIONS).remove(MobEffects.WITHER);


        }
    }
}
