package mc.craig.software.regen.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class RegenConfig {
    public static Common COMMON;
    public static ForgeConfigSpec COMMON_SPEC;
    public static Client CLIENT;
    public static ForgeConfigSpec CLIENT_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();


        Pair<Client, ForgeConfigSpec> specClientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specClientPair.getRight();
        CLIENT = specClientPair.getLeft();
    }


    public static class Client {

        public final ForgeConfigSpec.BooleanValue changeMySkin;
        public final ForgeConfigSpec.BooleanValue changePerspective;
        public final ForgeConfigSpec.BooleanValue renderTimelordHeadwear;
        public final ForgeConfigSpec.BooleanValue downloadTrendingSkins;
        public final ForgeConfigSpec.BooleanValue downloadInteralSkins;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client").push("client");
            changeMySkin = builder.comment("Disabling this will disable skin changing for you and you will retain your Mojang one").translation("config.regen.changeMySkin").define("changeMySkin", true);
            changePerspective = builder.comment("Changes the players perspective on regeneration").translation("config.regen.changePerspective").define("changePerspective", true);
            renderTimelordHeadwear = builder.comment("Toggle whether Timelords second head layers render, as some look good without and some look good with, I just leave this decision up to you").translation("config.regen.timelordRenderSecondLayers").define("timelordRenderSecondLayers", true);
            downloadTrendingSkins = builder.comment("Toggle whether a bunch of trending skins are downloaded from mineskin").translation("config.regen.downloadTrendingSkins").define("downloadTrendingSkins", true);
            downloadInteralSkins = builder.comment("Toggle whether the mod downloads it's own pack of Doctor who Skins").translation("config.regen.downloadInternalSkins").define("downloadInternalSkins", true);
            builder.pop();
        }

    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue loseRegensOnDeath;
        public final ForgeConfigSpec.BooleanValue fieryRegen;
        public final ForgeConfigSpec.BooleanValue regenFireImmune;
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

        public final ForgeConfigSpec.BooleanValue allowUpwardsMotion;
        public final ForgeConfigSpec.BooleanValue mobsHaveRegens;


        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("General Regeneration Settings").push("common");
            loseRegensOnDeath = builder.comment("If this is false you won't lose your regenerations if you get killed during regeneration").translation("config.regen.loseRegensOnDeath").define("loseRegensOnDeath", false);
            fieryRegen = builder.comment("Spawn fire during fiery regeneration").translation("config.regen.fieryRegen").define("fieryRegen", true);
            regenFireImmune = builder.comment("Players are immune to fire damage while regenerating").translation("config.regen.fireImmunity").define("fireImmunity", true);
            mobsHaveRegens = builder.comment("Toggle whether mobs have regenerations. In most cases, requires a game restart.").translation("config.regen.mobsHaveRegens").define("mobsHaveRegens", true);
            sendRegenDeathMessages = builder.comment("Sends a message to chat to say that a player is regenerating, and the reason for it").translation("config.regen.sendRegenDeathMessages").define("sendRegenDeathMessages", true);

            builder.comment("Post Regen Settings").push("Post Regeneration");
            postRegenerationDuration = builder.comment("Amount of seconds the post-regeneration effect lasts").translation("config.regen.postRegenDuration").defineInRange("postRegenDuration", 360, 0, Integer.MAX_VALUE);
            postRegenerationLevel = builder.comment("The level of the regeneration status effect granted after you regenerate").translation("config.regen.postRegenLevel").defineInRange("postRegenLevel", 4, 0, Integer.MAX_VALUE);
            resetHunger = builder.comment("Regenerate hunger bars").translation("config.regen.reset_hunger").define("resetHunger", true);
            resetOxygen = builder.comment("Regenerate Oxygen").translation("config.regen.reset_oxygen").define("resetOxygen", true);
            absorbtionLevel = builder.comment("The amount of absorption hearts you get when regenerating").translation("config.regen.absorbtionLevel").defineInRange("absorbtionLevel", 10, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.comment("Grace Settings").push("Grace Stage");
            gracePhaseLength = builder.comment("The time in seconds before your grace period enters a critical phase").translation("config.regen.gracePhaseLength").define("gracePhaseLength", 15 * 60);
            criticalDamageChance = builder.comment("Chance that a player in critical phase gets damaged at a given tick. Higher number means more damage.").translation("config.regen.criticalDamageChance").defineInRange("criticalDamageChance", 1, 0, Integer.MAX_VALUE);
            criticalPhaseLength = builder.comment("The time in seconds you can stay in the critical phase without dying").translation("config.regen.criticalPhaseLength").define("criticalPhaseLength", 60);
            handGlowInterval = builder.comment("Interval (in seconds) at which your hands start to glow").translation("config.regen.handGlowInterval").defineInRange("handGlowInterval", 120, 0, Integer.MAX_VALUE);
            handGlowTriggerDelay = builder.comment("Amount of time (in seconds) you have when your hands start glowing before you start to regenerate").translation("config.regen.handTriggerDelay").defineInRange("handTriggerDelay", 10, 0, Integer.MAX_VALUE);
            builder.pop();

            builder.comment("Mid Regen Settings").push("While Regenerating");
            regenerativeKillRange = builder.comment("Upon regeneration every mob inside of this radius is immediately killed. Keep in mind that you should round up to accommodate for mobs that aren't standing in the center of a block").translation("config.regen.regenKillRange").defineInRange("regenKillRange", 4, 0, Integer.MAX_VALUE);
            regenerativeKnockback = builder.comment("The amount of knockback every mob inside of the knock back radius gets").translation("config.regen.regenerativeKnockback").define("regenerativeKnockback", 2.5D);
            regenKnockbackRange = builder.comment("Range wherein every mob is knocked back upon regeneration").translation("config.regen.regenerativeKnockbackRange").defineInRange("regenerativeKnockbackRange", 7, 0, Integer.MAX_VALUE);
            regenerationKnocksbackPlayers = builder.comment("Players can be knocked back when too close to a regeneration").translation("config.regen.regenerationKnocksbackPlayers").define("regenerationKnocksbackPlayers", true);
            allowUpwardsMotion = builder.comment("Toggle whether the server allows for players to fly upwards during certain Regeneration transitions").translation("config.regen.upwardsMotion").define("upwardsMotion", true);
            builder.pop();

            builder.pop();

        }
    }

}
