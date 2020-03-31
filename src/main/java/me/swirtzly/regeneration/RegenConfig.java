package me.swirtzly.regeneration;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Sub on 17/09/2018.
 */
@Config(modid = RegenerationMod.MODID, name = "Regeneration")
public class RegenConfig {

    @Config.LangKey("config.regeneration.category.grace")
    public static final GracePeriod grace = new GracePeriod();

    @Config.LangKey("config.regeneration.category.compat")
    public static final ModIntegrations modIntegrations = new ModIntegrations();

    @Config.LangKey("config.regeneration.category.on_regen")
    public static final OnRegen onRegen = new OnRegen();

    @Config.LangKey("config.regeneration.category.post_regen")
    public static final PostRegen postRegen = new PostRegen();

    @Config.LangKey("config.regeneration.category.skins")
    public static final Skins skins = new Skins();

    @Config.LangKey("config.regeneration.max_regens")
    @Config.Comment("The maximum regeneration capacity. This affects the durability of a Fob Watch and the amount of regenerations in a full cycle. Use 0 for infinite regenerations.")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 0)
    public static int regenCapacity = 12;

    @Config.LangKey("config.regeneration.free_regens")
    @Config.Comment("Every player will start with this number of regenerations. Will cause undefined behavior if bigger than the amount of regenerations per cycle.")
    @Config.RangeInt(min = 0)
    public static int freeRegenerations = 0;

    @Config.LangKey("config.regeneration.first_start_gift_only")
    @Config.Comment("Only give new players free regenerations")
    public static boolean firstStartGiftOnly = true;

    @Config.LangKey("config.regeneration.lose_regens_on_death")
    @Config.Comment("If this is false you won't lose your regenerations if you get killed during regeneration")
    public static boolean loseRegensOnDeath = false;

    @Config.LangKey("config.regeneration.fiery_regen")
    @Config.Comment("Spawn fire during regeneration")
    public static boolean fieryRegen = true;

    @Config.LangKey("config.regeneration.regeneration_fire_immunity")
    @Config.Comment("Players are immune to fire damage while regenerating")
    public static boolean regenFireImmune = false;

    @Config.LangKey("config.regeneration.infinite_regenerations")
    @Config.Comment("Players are always able to regenerate. Effectively makes the Fob Watch obsolete.")
    public static boolean infiniteRegeneration = false;

    @Config.LangKey("config.regeneration.regen_messages")
    @Config.Comment("Sends a message to chat to say that a player is regenerating, and the reason for it")
    public static boolean sendRegenDeathMessages = true;

    @Config.LangKey("config.regeneration.perspective")
    @Config.Comment("Changes the players perspective on regeneration")
    public static boolean changePerspective = true;

    @Config.LangKey("config.regeneration.update_checker")
    @Config.Comment("Display a notification in chat when there's an update to the mod")
    public static boolean enableUpdateChecker = true;

    @Config.LangKey("config.regeneration.hand_change")
    @Config.Comment("Toggle whether your hand has the chance of inverting after a regen")
    public static boolean changeHand = true;

    @Config.LangKey("config.regeneration.craft")
    @Config.Comment("Toggle whether Fob watches are full when crafted")
    public static boolean craftWithRegens = true;

    @Config.LangKey("config.regeneration.shaders")
    @Config.Comment("Toggle whether cool shaders become applied when Regenerating")
    public static boolean regenerationShaders = true;

    @Config.Comment("Toggle whether cool bar things display when Regenerating")
    public static boolean coolCustomBarThings = true;

    public static class OnRegen {

        @Config.LangKey("config.regeneration.regenerative_kill_range")
        @Config.Comment("Upon regeneration every mob inside of this radius is immediately killed. Keep in mind that you should round up to accommodate for mobs that aren't standing in the center of a block")
        @Config.RangeInt(min = 0)
        public int regenerativeKillRange = 4;

        @Config.LangKey("config.regeneration.regenerative_knockback")
        @Config.Comment("The amount of knockback every mob inside of the knock back radius gets")
        public float regenerativeKnockback = 2.5F;

        @Config.LangKey("config.regeneration.regenerative_knockback_range")
        @Config.Comment("Range wherein every mob is knocked back upon regeneration")
        @Config.RangeInt(min = 0)
        public int regenerativeKnockbackRange = 7;

        @Config.LangKey("config.regeneration.regeneration_knocksback_players")
        @Config.Comment("Players can be knocked back when too close to a regeneration")
        public boolean regenerationKnocksbackPlayers = true;

        @Config.LangKey("config.regeneration.regeneration_kills_players")
        @Config.Comment("Players can be killed when too close to a regeneration")
        public boolean regenerationKillsPlayers = false;

        @Config.LangKey("config.regeneration.traits")
        @Config.Comment("Toggle whether traits are enabled")
        public boolean traitsEnabled = true;

    }

    public static class PostRegen {

        @Config.LangKey("config.regeneration.post_regen_duration")
        @Config.Comment("Amount of seconds the post-regeneration effect lasts")
        @Config.RangeInt(min = 0)
        public int postRegenerationDuration = 180;

        @Config.LangKey("config.regeneration.post_regenerationEffect_level")
        @Config.Comment("The level of the regeneration status effect granted after you regenerate")
        @Config.RangeInt(min = 0)
        public int postRegenerationLevel = 4;

        @Config.LangKey("config.regeneration.reset_hunger")
        @Config.Comment("Regenerate hunger bars")
        public boolean resetHunger = true;

        @Config.LangKey("config.regeneration.reset_oxygen")
        @Config.Comment("Regenerate Oxygen")
        public boolean resetOxygen = true;

        @Config.LangKey("config.regeneration.absorbtion_level")
        @Config.Comment("The amount of absorption hearts you get when regenerating")
        @Config.RangeInt(min = 0)
        public int absorbtionLevel = 10;

        @Config.LangKey("config.regeneration.post_grater")
        @Config.Comment("Create a creator when falling from a great height in POST")
        public boolean genGreator = false;
    }

    public static class Skins {
        @Config.LangKey("config.regeneration.skins.changemyskin")
        @Config.Comment("Disabling this will disable skin changing for you and you will retain your Mojang one")
        public boolean changeMySkin = true;

        @Config.LangKey("config.regeneration.skins.folder")
        @Config.Comment("This is where the regeneration skin folder will be generated, the default is './mods/', the path MUST NOT end in /")
        @Config.RequiresMcRestart
        public String skinDir = "./mods";

        @Config.LangKey("config.regeneration.skins.trending")
        @Config.Comment("Toggle whether trending skins are downloaded once per day from namemc")
        public boolean downloadTrendingSkins = true;

        @Config.LangKey("config.regeneration.skins.past_skins")
        @Config.Comment("Toggle whether the users past skins are downloaded once per day from namemc")
        public boolean downloadPastSkins = true;

        @Config.LangKey("config.regeneration.skins.internal_skins")
        @Config.Comment("Toggle whether the mod downloads its own batch of skins")
        public boolean downloadInternalSkins = true;
    }

    public static class GracePeriod {

        @Config.LangKey("config.regeneration.grace.gracePeriodLength")
        @Config.Comment("The time in seconds before your grace period enters a critical phase")
        @Config.RangeInt(min = 0)
        public int gracePhaseLength = 15 * 60;

        @Config.LangKey("config.regeneration.grace.criticalPhaseLength")
        @Config.Comment("The time in seconds you can stay in the critical phase without dying")
        @Config.RangeInt(min = 0)
        public int criticalPhaseLength = 60;

        @Config.LangKey("config.regeneration.grace.criticalDamageChance")
        @Config.Comment("Chance that a player in critical phase gets damaged at a given tick. Higher number means more damage.")
        @Config.RangeInt(min = 0)
        public float criticalDamageChance = 1;

        @Config.LangKey("config.regeneration.grace.handGlowInterval")
        @Config.Comment("Interval (in seconds) at which your hands start to glow")
        @Config.RangeInt(min = 0)
        public int handGlowInterval = 120;

        @Config.LangKey("config.regeneration.grace.handGlowTriggerDelay")
        @Config.Comment("Amount of time (in seconds) you have when your hands start glowing before you start to regenerate")
        @Config.RangeInt(min = 0)
        public int handGlowTriggerDelay = 10;

    }

    public static class ModIntegrations {

        @Config.LangKey("config.regeneration.category.compat.lccore")
        public final LucraftCore lucraftcore = new LucraftCore();

        @Config.LangKey("config.regeneration.category.compat.tardis")
        public final TardisMod tardisMod = new TardisMod();

        public static class LucraftCore {

            @Config.LangKey("config.regeneration.compat.lccore.radiation_immunity")
            @Config.Comment("If this is true and LCCore is installed, timelords are immune to radiation")
            public boolean immuneToRadiation = true;

            @Config.LangKey("config.regeneration.compat.lccore.superpower_disable")
            @Config.Comment("If this is true and LCCore is installed, you cannot regenerate while you have a superpower")
            public boolean superpowerDisable = true;
        }

        public static class TardisMod {
            @Config.LangKey("config.regeneration.compat.tardis.tardis_damage")
            @Config.Comment("If this is true and The Tardis mod is installed, it's systems will be slightly damaged")
            public boolean damageTardis = true;
        }

    }

    @EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(RegenerationMod.MODID)) {
                ConfigManager.sync(RegenerationMod.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
