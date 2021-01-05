package me.swirtzly.regen.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class RegenConfig {
    public static Common COMMON;
    public static ForgeConfigSpec COMMON_SPEC;
    public static Client CLIENT;
    public static ForgeConfigSpec CLIENT_SPEC;

    public static Skin SKIN;
    public static ForgeConfigSpec SKIN_SPEC;


    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();

        Pair<Client, ForgeConfigSpec> specClientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specClientPair.getRight();
        CLIENT = specClientPair.getLeft();

        Pair<Skin, ForgeConfigSpec> specSkinPair = new ForgeConfigSpec.Builder().configure(Skin::new);
        SKIN_SPEC = specSkinPair.getRight();
        SKIN = specSkinPair.getLeft();
    }

    public static class Skin {
        public final ForgeConfigSpec.BooleanValue downloadTrendingSkins;
        public final ForgeConfigSpec.BooleanValue downloadPreviousSkins;
        public final ForgeConfigSpec.BooleanValue downloadInteralSkins;

        Skin(ForgeConfigSpec.Builder builder) {
            builder.comment("Skin Settings").push("skin");
            downloadTrendingSkins = builder.comment("Toggle whether a bunch of trending skins are downloaded from NameMC").translation("config.regeneration.downloadTrendingSkins").define("downloadTrendingSkins", true);
            downloadPreviousSkins = builder.comment("Toggle whether the mod downloads your past skins from NameMC").translation("config.regeneration.downloadPreviousSkins").define("downloadPastSkins", true);
            downloadInteralSkins = builder.comment("Toggle whether the mod downloads it's own pack of Doctor who Skins").translation("config.regeneration.downloadInternalSkins").define("downloadInternalSkins", true);
            builder.pop();
        }
    }

    public static class Client {

        public final ForgeConfigSpec.BooleanValue changeMySkin;
        public final ForgeConfigSpec.BooleanValue changePerspective;
        public final ForgeConfigSpec.BooleanValue renderTimelordHeadwear;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client Regeneration Settings").push("client");
            builder.comment("Skin Settings").push("skin");
            changeMySkin = builder.comment("Disabling this will disable skin changing for you and you will retain your Mojang one").translation("config.regeneration.skins.changemyskin").define("changeMySkin", true);
            changePerspective = builder.comment("Changes the players perspective on regeneration").translation("config.regeneration.perspective").define("changePerspective", true);
            builder.comment("Misc Settings").push("misc");
            renderTimelordHeadwear = builder.comment("Toggle whether Timelords second head layers render, as some look good without and some look good with, I just leave this decision up to you").translation("config.regeneration.timelordRenderSecondLayers").define("timelordRenderSecondLayers", true);
            builder.pop();
        }

    }

    public static class Common {

        public final ForgeConfigSpec.IntValue regenCapacity;
        public final ForgeConfigSpec.BooleanValue loseRegensOnDeath;
        public final ForgeConfigSpec.BooleanValue fieryRegen;
        public final ForgeConfigSpec.BooleanValue regenFireImmune;
        public final ForgeConfigSpec.BooleanValue infiniteRegeneration;
        public final ForgeConfigSpec.BooleanValue sendRegenDeathMessages;
        public final ForgeConfigSpec.IntValue regenerativeKillRange;
        public final ForgeConfigSpec.ConfigValue<Double> regenerativeKnockback;
        public final ForgeConfigSpec.IntValue regenKnockbackRange;
        public final ForgeConfigSpec.BooleanValue regenerationKnocksbackPlayers;
        public final ForgeConfigSpec.ConfigValue<Integer> gracePhaseLength;
        public final ForgeConfigSpec.IntValue criticalDamageChance;
        public final ForgeConfigSpec.ConfigValue<Integer> criticalPhaseLength;
        public final ForgeConfigSpec.IntValue handGlowInterval;
        public final ForgeConfigSpec.IntValue handGlowTriggerDelay;
        public final ForgeConfigSpec.IntValue postRegenerationDuration;
        public final ForgeConfigSpec.IntValue postRegenerationLevel;
        public final ForgeConfigSpec.BooleanValue resetHunger;
        public final ForgeConfigSpec.BooleanValue resetOxygen;
        public final ForgeConfigSpec.IntValue absorbtionLevel;
        public final ForgeConfigSpec.BooleanValue traitsEnabled;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> postRegenEffects;
        public final ForgeConfigSpec.ConfigValue<String> skinDir;
        public final ForgeConfigSpec.BooleanValue allowUpwardsMotion;


        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("General Regeneration Settings").push("common");
            regenCapacity = builder.comment("The maximum regeneration capacity. This affects the durability of a Fob Watch and the amount of regenerations in a full cycle. Use 0 for infinite regenerations.").translation("config.regeneration.max_regens").defineInRange("regenCapacity", 12, 0, Integer.MAX_VALUE);
            loseRegensOnDeath = builder.comment("If this is false you won't lose your regenerations if you get killed during regeneration").translation("config.regeneration.lose_regens_on_death").define("loseRegensOnDeath", false);
            fieryRegen = builder.comment("Spawn fire during regeneration").translation("config.regeneration.fiery_regen").define("fieryRegen", true);
            regenFireImmune = builder.comment("Players are immune to fire damage while regenerating").translation("config.regeneration.regeneration_fire_immunity").define("fireImmunity", false);
            infiniteRegeneration = builder.comment("config.regeneration.infinite_regenerations").translation("Players are always able to regenerate. Effectively makes the Fob Watch obsolete.").define("infiniteRegeneration", false);
            sendRegenDeathMessages = builder.comment("Sends a message to chat to say that a player is regenerating, and the reason for it").translation("config.regeneration.regen_messages").define("sendRegenDeathMessages", true);
            skinDir = builder.comment("This is where the regeneration skin folder will be generated, the default is './', the path MUST NOT end in /").translation("config.regeneration.skins.skindir").define("skinDir", ".");
            builder.pop();

            builder.comment("Post Regen Settings").push("post");
            postRegenerationDuration = builder.comment("Amount of seconds the post-regeneration effect lasts").translation("config.regeneration.post_regen_duration").defineInRange("postRegenDuration", 360, 0, Integer.MAX_VALUE);
            postRegenerationLevel = builder.comment("The level of the regeneration status effect granted after you regenerate").translation("config.regeneration.post_regenerationEffect_level").defineInRange("postRegenLevel", 4, 0, Integer.MAX_VALUE);
            resetHunger = builder.comment("Regenerate hunger bars").translation("config.regeneration.reset_hunger").define("resetHunger", true);
            resetOxygen = builder.comment("Regenerate Oxygen").translation("config.regeneration.reset_oxygen").define("resetOxygen", true);
            absorbtionLevel = builder.comment("The amount of absorption hearts you get when regenerating").translation("config.regeneration.absorbtion_level").defineInRange("absorbtionLevel", 10, 0, Integer.MAX_VALUE);
            postRegenEffects = builder.translation("config.regeneration.post_effects").comment("List of potion effects the player can endure during post regeneration").defineList("postRegenPotions", Lists.newArrayList("minecraft:weakness", "minecraft:mining_fatigue", "minecraft:resistance", "minecraft:health_boost", "minecraft:hunger", "minecraft:water_breathing", "minecraft:haste"), String.class::isInstance);
            builder.pop();

            builder.comment("Grace Settings").push("grace");
            gracePhaseLength = builder.comment("The time in seconds before your grace period enters a critical phase").translation("config.regeneration.grace.gracePeriodLength").define("gracePhaseLength", 15 * 60);
            criticalDamageChance = builder.comment("Chance that a player in critical phase gets damaged at a given tick. Higher number means more damage.").translation("config.regeneration.grace.criticalDamageChance").defineInRange("criticalDamageChance", 1, 0, Integer.MAX_VALUE);
            criticalPhaseLength = builder.comment("The time in seconds you can stay in the critical phase without dying").translation("config.regeneration.grace.criticalPhaseLength").define("criticalPhaseLength", 60);
            handGlowInterval = builder.comment("Interval (in seconds) at which your hands start to glow").translation("config.regeneration.grace.handGlowInterval").defineInRange("criticalPhaseLength", 120, 0, Integer.MAX_VALUE);
            handGlowTriggerDelay = builder.comment("Amount of time (in seconds) you have when your hands start glowing before you start to regenerate").translation("config.regeneration.grace.handGlowTriggerDelay").defineInRange("handTriggerDelay", 10, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.comment("Mid Regen Settings").push("onRegen");
            regenerativeKillRange = builder.comment("Upon regeneration every mob inside of this radius is immediately killed. Keep in mind that you should round up to accommodate for mobs that aren't standing in the center of a block").translation("config.regeneration.regenerative_kill_range").defineInRange("regenKillRange", 4, 0, Integer.MAX_VALUE);
            regenerativeKnockback = builder.comment("The amount of knockback every mob inside of the knock back radius gets").translation("config.regeneration.regenerative_knockback").define("regenerativeKnockback", 2.5D);
            regenKnockbackRange = builder.comment("Range wherein every mob is knocked back upon regeneration").translation("config.regeneration.regenerative_knockback_range").defineInRange("regenerativeKnockbackRange", 7, 0, Integer.MAX_VALUE);
            regenerationKnocksbackPlayers = builder.comment("Players can be knocked back when too close to a regeneration").translation("config.regeneration.regeneration_knocksback_players").define("regenerationKnocksbackPlayers", true);
            traitsEnabled = builder.comment("Toggle whether traits are enabled").translation("config.regeneration.traitsenabled").define("traitsEnabled", true);
            allowUpwardsMotion = builder.comment("Toggle whether the server allows for players to fly upwards during certain Regeneration transitions").translation("config.regeneration.upwards_motion").define("upwardsMotion", true);
            builder.pop();
        }
    }

}
