package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.util.RegenDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class RegenDamageTags extends DamageTypeTagsProvider {

    public RegenDamageTags(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, Regeneration.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(RegenDamageTypes.REGEN_DMG_CRITICAL.location());
        this.tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(RegenDamageTypes.REGEN_DMG_KILLED.location());
        this.tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(RegenDamageTypes.REGEN_DMG_FORCED.location());
        this.tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(RegenDamageTypes.REGEN_DMG_RIFLE.location());
        this.tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(RegenDamageTypes.REGEN_DMG_HAND.location());

        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).addOptional(RegenDamageTypes.REGEN_DMG_CRITICAL.location());
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).addOptional(RegenDamageTypes.REGEN_DMG_KILLED.location());
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).addOptional(RegenDamageTypes.REGEN_DMG_FORCED.location());
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).addOptional(RegenDamageTypes.REGEN_DMG_HAND.location());
    }
}
