package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RegenDamageTags extends DamageTypeTagsProvider {

    public RegenDamageTags(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, Regeneration.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(RegenDamageTypes.REGEN_DMG_CRITICAL, RegenDamageTypes.REGEN_DMG_KILLED, RegenDamageTypes.REGEN_DMG_FORCED, RegenDamageTypes.REGEN_DMG_RIFLE, RegenDamageTypes.REGEN_DMG_HAND);
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(RegenDamageTypes.REGEN_DMG_CRITICAL, RegenDamageTypes.REGEN_DMG_KILLED, RegenDamageTypes.REGEN_DMG_FORCED, RegenDamageTypes.REGEN_DMG_HAND);
    }
}
