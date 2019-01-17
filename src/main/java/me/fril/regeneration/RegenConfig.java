package me.fril.regeneration;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Sub
 * on 17/09/2018.
 */
@Config(modid = RegenerationMod.MODID, name = "Regeneration Mod")
public class RegenConfig {

	@Config.LangKey("Loot")
	public static final Loot loot = new Loot();

	@Config.Name("Grace Period")
	public static final GracePeriod grace = new GracePeriod();

	@Config.Name("Lucraft Core")
	public static final LucraftCore lucraftcore = new LucraftCore();

	@Config.Name("Tardis Mod")
	public static final TardisMod tardisMod = new TardisMod();

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
	
	@Config.LangKey("config.regeneration.fiery_regen")
	@Config.Comment("Spawn fire during regeneration")
	public static boolean fieryRegen = true;
	
	@Config.LangKey("config.regeneration.regenerative_kill_range")
	@Config.Comment("Upon regeneration every mob inside of this radius is immediately killed. Keep in mind that you should round up to accommodate for mobs that aren't standing in the center of a block")
	@Config.RangeInt(min = 0)
	public static int regenerativeKillRange = 4;
	
	@Config.LangKey("config.regeneration.regenerative_knockback")
	@Config.Comment("The amount of knockback every mob inside of the knock back radius gets")
	public static float regenerativeKnockback = 2.5F;
	
	@Config.LangKey("config.regeneration.regenerative_knockback_range")
	@Config.Comment("Range wherein every mob is knocked back upon regeneration")
	@Config.RangeInt(min = 0)
	public static int regenerativeKnockbackRange = 7;
	
	@Config.LangKey("config.regeneration.post_regen_duration")
	@Config.Comment("Amount of seconds the post-regeneration effect lasts")
	@Config.RangeInt(min = 0)
	public static int postRegenerationDuration = 180;
	
	@Config.LangKey("config.regeneration.post_regenerationEffect_level")
	@Config.Comment("The level of the regeneration status effect granted after you regenerate")
	@Config.RangeInt(min = 0)
	public static int postRegenerationLevel = 4;
	
	@Config.LangKey("config.regeneration.reset_hunger")
	@Config.Comment("Regenerate hunger bars")
	public static boolean resetHunger = true;
	
	@Config.LangKey("config.regeneration.reset_oxygen")
	@Config.Comment("Regenerate Oxygen")
	public static boolean resetOxygen = true;
	
	@Config.LangKey("config.regeneration.absorbtion_level")
	@Config.Comment("The amount of absorption hearts you get when regenerating")
	@Config.RangeInt(min = 0)
	public static int absorbtionLevel = 10;
	
	@Config.LangKey("config.regeneration.lose_regens_on_death")
	@Config.Comment("If this is false you won't lose your regenerations if you get killed during regeneration")
	public static boolean loseRegensOnDeath = true; //SUB unimplemented
	
	@Config.LangKey("config.regeneration.regeneration_knocksback_players")
	@Config.Comment("Players can be knocked back when too close to a regeneration")
	public static boolean regenerationKnocksbackPlayers = true;
	
	@Config.LangKey("config.regeneration.regeneration_kills_players")
	@Config.Comment("Players can be killed when too close to a regeneration")
	public static boolean regenerationKillsPlayers = false;
	
	@Config.LangKey("config.regeneration.regeneration_fire_immunity")
	@Config.Comment("Players are immune to fire damage while regenerating")
	public static boolean regenFireImmune = false;
	
	@Config.LangKey("config.regeneration.infinite_regenerations")
	@Config.Comment("Players are always able to regenerate. Effectively makes the Fob Watch obsolete.")
	public static boolean infiniteRegeneration = false;

	@Config.LangKey("config.regeneration.regen_messages")
	@Config.Comment("Sends a message to chat to say that a player is regenerating, and the reason for it")
	public static boolean sendRegenDeathMessages = true;

	@Config.Comment("The URL skins are downloaded from when you reset back to Mojangs skin")
	//TODO Actually use mojang instead
    public static String downloadUrl = "https://crafatar.com/skins/%s";

	@Config.LangKey("config,regeneration.changemyskin")
	@Config.Comment("Disabling this will disable skin changing for you and you will retain your Mojang one")
	public static boolean changeMySkin = true;

	public static class Loot {
		
		@Config.LangKey("config.regeneration.loot_regex")
		@Config.Comment("The loot pool for chameleon arch's will only be added to loot tables whose name matches this regular expression")
		@Config.RequiresWorldRestart
		public String lootRegex = "minecraft:chests\\/.*";
		
		@Config.LangKey("config.regeneration.disable_loot")
		@Config.Comment("If this is true there won't be any Fob Watches spawned naturally")
		@Config.RequiresWorldRestart
		public boolean disableLoot = false;
		
	}
	
	public static class GracePeriod {
		
		@Config.LangKey("config.regeneration.grace.gracePeriodLength")
		@Config.Comment("The time in seconds before your grace period enters a critical phase")
		public int gracePhaseLength = 15 * 60;
		
		@Config.LangKey("config.regeneration.grace.criticalPhaseLength")
		@Config.Comment("The time in seconds you can stay in the critical phase without dying")
		public int criticalPhaseLength = 60;
		
		@Config.LangKey("config.regeneration.grace.criticalDamageChance")
		@Config.Comment("Chance that a player in critical phase gets damaged at a given tick. Higher number means more damage.")
		public float criticalDamageChance = 1;
		
	}

	public static class LucraftCore {

		@Config.LangKey("config.regeneration.size_changing")
		@Config.Comment("If this is true and LCCore is installed, you will change size on regeneration")
		public boolean lucraftcoreSizeChanging = true;

		@Config.LangKey("config.regeneration.size_changing_min")
		@Config.Comment("Minimum Size Change value")
		public float sizeChangingMin = 0.796544F;

		@Config.LangKey("config.regeneration.size_changing_max")
		@Config.Comment("Maximum Size Change value")
        public float sizeChangingMax = 1.1F;

		@Config.LangKey("config.regeneration.radiation_immunity")
		@Config.Comment("If this is true and LCCore is installed, timelords are immune to radiation")
		public boolean immuneToRadiation = true;
	}

	public static class TardisMod {
		@Config.LangKey("config.regeneration.tardis_damage")
		@Config.Comment("If this is true and The Tardis mod is installed, it's systems will be slightly damaged")
		public boolean damageTardis = true;
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
