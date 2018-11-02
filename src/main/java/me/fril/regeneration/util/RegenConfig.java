package me.fril.regeneration.util;

import me.fril.regeneration.RegenerationMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Sub
 * on 17/09/2018.
 */
@Config(modid = RegenerationMod.MODID)
public class RegenConfig { // TODO externalize comment strings?
	
	public static final Loot Loot = new Loot();
	
	@Config.LangKey("config.regeneration.max_regens")
	@Config.Comment("The maximum regeneration capacity. This affects the durability of a Fob Watch and the amount of regenerations in a full cycle. Use 0 for infinite regenerations, the Fob Watch will grant the timelord ability and give you infinite regenerations. If you die while regenerating you'll lose your ability (unless dontLosePower is set to true)")
	@Config.RangeInt(min = 0)
	public static int regenCapacity = 12;
	
	@Config.LangKey("config.regeneration.start_as_timelord")
	@Config.Comment("If true you'll start a world with a Fob Watch in your inventory")
	public static boolean startAsTimelord = false;
	
	@Config.LangKey("config.regeneration.fiery_regen")
	@Config.Comment("Spawn fire when regenerating")
	public static boolean fieryRegen = true;
	
	@Config.LangKey("config.regeneration.regenerative_kill_range")
	@Config.Comment("Upon regeneration every mob inside of this radius is immediately killed. Keep in mind that you should round up to accommodate for mobs that aren't standing in the center of a block")
	@Config.RangeInt(max = 200, min = 0)
	public static int regenerativeKillRange = 4;
	
	@Config.LangKey("config.regeneration.regenerative_knockback")
	@Config.Comment("The amount of knockback every mob inside of the knock back radius gets")
	public static float regenerativeKnockback = 2.5F;
	
	@Config.LangKey("config.regeneration.regenerative_knockback_range")
	@Config.Comment("Range wherein every mob is knocked back upon regeneration")
	@Config.RangeInt(max = 300, min = 0)
	public static int regenerativeKnockbackRange = 7;
	
	@Config.LangKey("config.regeneration.post_regen_duration")
	@Config.Comment("Amount of seconds the post-regeneration effect lasts")
	public static int postRegenerationDuration = 180;
	
	@Config.LangKey("config.regeneration.post_regenerationEffect_level")
	@Config.Comment("The level of the regeneration status effect granted after you regenerate")
	public static int postRegenerationLevel = 4;
	
	@Config.LangKey("config.regeneration.reset_hunger")
	@Config.Comment("Regenerate hunger bars")
	public static boolean resetHunger = true;
	
	@Config.LangKey("config.regeneration.reset_oxygen")
	@Config.Comment("Regenerate Oxygen")
	public static boolean resetOxygen = true;
	
	@Config.LangKey("config.regeneration.absorbtion_level")
	@Config.Comment("The amount of absorption hearts you get when regenerating")
	public static int absorbtionLevel = 10;
	
	@Config.LangKey("config.regeneration.lose_power_on_mid_regen_death")
	@Config.Comment("If this is false you won't lose your regenerations if you get killed during regeneration")
	public static boolean losePowerOnMidRegenDeath = true;
	
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
	
	@Mod.EventBusSubscriber
	public static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(RegenerationMod.MODID)) {
				ConfigManager.sync(RegenerationMod.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
