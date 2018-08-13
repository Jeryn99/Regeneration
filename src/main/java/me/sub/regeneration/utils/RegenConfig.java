package me.sub.regeneration.utils;

import me.sub.regeneration.Regeneration;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Regeneration.MODID)
public class RegenConfig {

	public static final Loot LOOT = new Loot();
	public static final Regen REGENERATION = new Regen();

	@Config.LangKey("category.loot")
	public static class Loot {
		@Config.LangKey("config.loot_regex")
		@Config.Comment("The loot pool for chameleon arch's will only be added to loot tables whose name matches this regular expression")
		public String lootRegex = "minecraft:chests\\/.*";
	}

	@Config.LangKey("category.regeneration")
	public static class Regen {

		@Config.LangKey("config.max_regens")
		@Config.Comment("The maximum regeneration capacity. This affects the durability of a Chameleon Arch and the amount of regenerations in a full cycle. Use 0 for infinite regenerations, the chameleon arch will grant the timelord ability and give you infinite regenerations. If you die while regenerating you'll lose your ability (unless dontLosePower is set to true)")
		@Config.RangeInt(max = Integer.MAX_VALUE, min = 0)
		public int regenCapacity = 12;

		@Config.LangKey("config.start_as_timelord")
		@Config.Comment("The maximum regeneration capacity. This affects the durability of a Chameleon Arch and the amount of regenerations in a full cycle. Use 0 for infinite regenerations, the chameleon arch will grant the timelord ability and give you infinite regenerations. If you die while regenerating you'll lose your ability (unless dontLosePower is set to true)")
		public boolean startAsTimelord = false;

		@Config.LangKey("config.fiery_regen")
		@Config.Comment("Spawn fire when regenerating")
		public boolean fieryRegen = true;

		@Config.LangKey("config.regenerative_kill_range")
		@Config.Comment("Upon regeneration every mob inside of this radius is immediately killed. Keep in mind that you should round up to accomodate for mobs that aren't standing in the center of a block")
		@Config.RangeInt(max = Integer.MAX_VALUE, min = 0)
		public int regenerativeKillRange = 4;

		@Config.LangKey("config.disable_arch")
		@Config.Comment("If this is true you won't lose your timelord power if you get killed during regeneration")
		public boolean disableArch = false;

		@Config.LangKey("config.regenerative_knockback")
		@Config.Comment("The amount of knockback every mob inside of the knock back radius gets")
		public float regenerativeKnockback = 2.5F;

		@Config.LangKey("config.regenerative_knockback_range")
		@Config.Comment("Range wherein every mob is knocked back upon regeneration")
		@Config.RangeInt(max = 30000000, min = 0)
		public int regenerativeKnockbackRange = 7;

		@Config.LangKey("config.post_regen_duration")
		@Config.Comment("Amount of seconds the post-regeneration effect lasts")
		public int postRegenerationDuration = 180;

		@Config.LangKey("config.post_regenerationEffect_level")
		@Config.Comment("The level of the regeneration status effect granted upon regeneration finish")
		public int postRegenerationLevel = 4; 

		@Config.LangKey("config.regenerationEffect_level")
		@Config.Comment("The level of the regeneration status effect granted upon regeneration finish")
		@Config.RangeInt(max = 127, min = 0)
		public int regenerationLevel = 2;

		@Config.LangKey("config.reset_hunger")
		@Config.Comment("Regenerate hunger bars")
		public boolean resetHunger = true;

		@Config.LangKey("config.reset_oxygen")
		@Config.Comment("Regenerate Oxygen")
		public boolean resetOxygen = true;

		@Config.LangKey("config.absorbtion_level")
		@Config.Comment("The amount of absorbtion hearts you get when regenerating")
		public int absorbtionLevel = 10; 

		@Config.LangKey("config.loose_regens_on_death")
		@Config.Comment("If this is true you won't lose your timelord power if you get killed during regeneration")
		public boolean dontLoseUponDeath = false;
	}

	@Mod.EventBusSubscriber
	public static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(me.sub.regeneration.Regeneration.MODID)) {
				ConfigManager.sync(me.sub.regeneration.Regeneration.MODID, Config.Type.INSTANCE);
			}
		}
	}

}
