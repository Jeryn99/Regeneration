package mc.craig.software.regen.util;

import mc.craig.software.regen.Regeneration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RegenDamageTypes {
    public static ResourceKey<DamageType> REGEN_DMG_ENERGY_EXPLOSION = createKey("regeneration_blast"),
            REGEN_DMG_CRITICAL = createKey("critical_period"),
            REGEN_DMG_KILLED = createKey("mid_regeneration"),
            REGEN_DMG_FORCED = createKey("forced_regeneration"),
            REGEN_DMG_RIFLE = createKey("rifle_shot"),
            REGEN_DMG_HAND = createKey("severed_arm"),
            REGEN_DMG_STASER = createKey("staser_shot");

    public static ResourceKey<DamageType> createKey(String key) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Regeneration.MOD_ID, key));
    }

    public static Holder<DamageType> getHolder(LivingEntity livingEntity, ResourceKey<DamageType> damageTypeResourceKey) {
        return getHolder((Player) livingEntity, damageTypeResourceKey);
    }

    public static Holder<DamageType> getHolder(Player player, ResourceKey<DamageType> damageTypeResourceKey) {
        return getHolder(player.level(), damageTypeResourceKey);
    }

    public static Holder<DamageType> getHolder(Level level, ResourceKey<DamageType> damageTypeResourceKey) {
        return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeResourceKey);
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(REGEN_DMG_ENERGY_EXPLOSION, new DamageType("regeneration_blast", DamageScaling.ALWAYS, 1.0F));
        context.register(REGEN_DMG_CRITICAL, new DamageType("critical_period", DamageScaling.ALWAYS, 1.0F));
        context.register(REGEN_DMG_KILLED, new DamageType("mid_regeneration", DamageScaling.ALWAYS, 1.0F));
        context.register(REGEN_DMG_FORCED, new DamageType("forced_regeneration", DamageScaling.ALWAYS, 1.0F));
        context.register(REGEN_DMG_RIFLE, new DamageType("rifle_shot", DamageScaling.ALWAYS, 1.0F));
        context.register(REGEN_DMG_HAND, new DamageType("severed_arm", DamageScaling.ALWAYS, 1.0F));
        context.register(REGEN_DMG_STASER, new DamageType("staser_shot", DamageScaling.ALWAYS, 1.0F));
    }

}
