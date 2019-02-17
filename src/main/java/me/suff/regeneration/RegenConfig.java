package me.suff.regeneration;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class RegenConfig {
	public static final RegenConfig CONFIG;
	public static final ForgeConfigSpec CONFIG_SPEC;

	public final ForgeConfigSpec.IntValue regenCapacity;
	public final ForgeConfigSpec.IntValue freeRegenerations;
	public final ForgeConfigSpec.BooleanValue firstStartGiftOnly;
	public final ForgeConfigSpec.BooleanValue loseRegensOnDeath;
	public final ForgeConfigSpec.BooleanValue fieryRegen;
	public final ForgeConfigSpec.BooleanValue regenFireImmune;
	public final ForgeConfigSpec.BooleanValue infiniteRegeneration;
	public final ForgeConfigSpec.BooleanValue sendRegenDeathMessages;
	public final ForgeConfigSpec.BooleanValue changePerspective;
	public final ForgeConfigSpec.BooleanValue changeHand;
	public final ForgeConfigSpec.IntValue regenerativeKillRange;
	public final ForgeConfigSpec.BooleanValue regenKillsPlayers;
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
	public final ForgeConfigSpec.BooleanValue changeMySkin;
	public final ForgeConfigSpec.ConfigValue<String> lootRegex;
	public final ForgeConfigSpec.BooleanValue disableLoot;

	static {
		Pair<RegenConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(RegenConfig::new);
		CONFIG_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	Common(ForgeConfigSpec.Builder builder) {
		builder.comment("General Regeneration Settings").push("common");

		regenCapacity = builder.comment("The maximum regeneration capacity. This affects the durability of a Fob Watch and the amount of regenerations in a full cycle. Use 0 for infinite regenerations.").translation("config.regeneration.max_regens").defineInRange("regenCapacity", 12, 0, Integer.MAX_VALUE);

		freeRegenerations = builder.comment("Every player will start with this number of regenerations. Will cause undefined behavior if bigger than the amount of regenerations per cycle.").translation("config.regeneration.free_regens").defineInRange("freeRegenerations", 0, 0, Integer.MAX_VALUE);

		firstStartGiftOnly = builder.comment("Only give new players free regenerations").translation("config.regeneration.first_start_gift_only").define("firstStartGiftOnly", true);

		loseRegensOnDeath = builder.comment("If this is false you won't lose your regenerations if you get killed during regeneration").translation("config.regeneration.lose_regens_on_death").define("loseRegensOnDeath", false);

		fieryRegen = builder.comment("Spawn fire during regeneration").translation("config.regeneration.fiery_regen").define("fieryRegen", true);

		regenFireImmune = builder.comment("Players are immune to fire damage while regenerating").translation("config.regeneration.regeneration_fire_immunity").define("fireImmunity", false);

		infiniteRegeneration = builder.comment("config.regeneration.infinite_regenerations").translation("Players are always able to regenerate. Effectively makes the Fob Watch obsolete.").define("infiniteRegeneration", false);

		sendRegenDeathMessages = builder.comment("Sends a message to chat to say that a player is regenerating, and the reason for it").translation("config.regeneration.regen_messages").define("sendRegenDeathMessages", true);

		changePerspective = builder.comment("Changes the players perspective on regeneration").translation("config.regeneration.perspective").define("changePerspective", true);

		changeHand = builder.comment("Toggle whether your hand has the chance of inverting after a regen").translation("config.regeneration.hand_change").define("changeHand", true);
		builder.pop();


		builder.comment("Post Regen Settings").push("post");

		postRegenerationDuration = builder.comment("Amount of seconds the post-regeneration effect lasts").translation("config.regeneration.post_regen_duration").defineInRange("postRegenDuration", 180, 0, Integer.MAX_VALUE);
		postRegenerationLevel = builder.comment("The level of the regeneration status effect granted after you regenerate").translation("config.regeneration.post_regenerationEffect_level").defineInRange("postRegenLevel", 4, 0, Integer.MAX_VALUE);
		resetHunger = builder.comment("Regenerate hunger bars").translation("config.regeneration.reset_hunger").define("resetHunger", true);
		resetOxygen = builder.comment("Regenerate Oxygen").translation("config.regeneration.reset_oxygen").define("resetOxygen", true);
		absorbtionLevel = builder.comment("The amount of absorption hearts you get when regenerating").translation("config.regeneration.absorbtion_level").defineInRange("absorbtionLevel", 10, 0, Integer.MAX_VALUE);
		builder.pop();

		builder.comment("Loot Settings").push("loot");
		lootRegex = builder.worldRestart().comment("The loot pool for chameleon arch's will only be added to loot tables whose name matches this regular expression").translation("config.regeneration.loot_regex").define("lootRegex", "minecraft:chests\\/.*");
		disableLoot = builder.comment("If this is true there won't be any Fob Watches spawned naturally").translation("config.regeneration.disable_loot").define("disableLoot", false);
		builder.pop();

		builder.comment("Skin Settings").push("skin");
		changeMySkin = builder.comment("Disabling this will disable skin changing for you and you will retain your Mojang one").translation("config.regeneration.skins.changemyskin").define("changeMySkin", true);
		builder.pop();

		builder.comment("Grace Settings").push("grace");
		gracePhaseLength = builder.comment("The time in seconds before your grace period enters a critical phase").translation("config.regeneration.grace.gracePeriodLength").define("gracePhaseLength",15 * 60);
		criticalDamageChance = builder.comment("Chance that a player in critical phase gets damaged at a given tick. Higher number means more damage.").translation("config.regeneration.grace.criticalDamageChance").defineInRange("criticalDamageChance", 1, 0, Integer.MAX_VALUE);
		criticalPhaseLength = builder.comment("The time in seconds you can stay in the critical phase without dying").translation("config.regeneration.grace.criticalPhaseLength").define("criticalPhaseLength", 60);
		handGlowInterval = builder.comment("Interval (in seconds) at which your hands start to glow").translation("config.regeneration.grace.handGlowInterval").defineInRange("criticalPhaseLength", 120, 0, Integer.MAX_VALUE);
		handGlowTriggerDelay = builder.comment("Amount of time (in seconds) you have when your hands start glowing before you start to regenerate").translation("config.regeneration.grace.handGlowTriggerDelay").defineInRange("handTriggerDelay", 10, 0, Integer.MAX_VALUE);
		builder.pop();

		builder.comment("Mid Regen Settings").push("onRegen");
		regenerativeKillRange = builder.comment("Upon regeneration every mob inside of this radius is immediately killed. Keep in mind that you should round up to accommodate for mobs that aren't standing in the center of a block").translation("config.regeneration.regenerative_kill_range").defineInRange("regenKillRange", 4, 0, Integer.MAX_VALUE);
		regenKillsPlayers = builder.comment("Players can be killed when too close to a regeneration").translation("config.regeneration.regeneration_kills_players").define("regenKillsPlayers", false);
		regenerativeKnockback = builder.comment("The amount of knockback every mob inside of the knock back radius gets").translation("config.regeneration.regenerative_knockback").define("regenerativeKnockback", 2.5D);
		regenKnockbackRange = builder.comment("Range wherein every mob is knocked back upon regeneration").translation("config.regeneration.regenerative_knockback_range").defineInRange("regenerativeKnockbackRange", 7, 0, Integer.MAX_VALUE);
		regenerationKnocksbackPlayers = builder.comment("Players can be knocked back when too close to a regeneration").translation("config.regeneration.regeneration_knocksback_players").define("regenerationKnocksbackPlayers", true);
		builder.pop();
	}

	//For example, more example here : https://github.com/LotuxPunk/OpenBackup/blob/26fff2d7e5934b282d52ea005e0bef601db8539d/src/main/java/com/vandendaelen/openbackup/configs/OBConfig.java#L78
	public static int getRegenCapacity() {
		return CONFIG.regenCapacity.get();
	}
}
