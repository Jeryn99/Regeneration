package me.suff.regeneration.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class RegenConfigNew {
	
	public static class Common {
		public final ForgeConfigSpec.IntValue regenCapacity;
		public final ForgeConfigSpec.IntValue freeRegenerations;
		public final ForgeConfigSpec.BooleanValue firstStartGiftOnly;
		
		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("General Regeneration Settings").translation("Common Settings").push("common");
			
			regenCapacity =
			builder.comment("The maximum regeneration capacity. This affects the durability of a Fob Watch and the amount of regenerations in a full cycle. Use 0 for infinite regenerations.")
			.translation("config.regeneration.max_regens")
			.defineInRange("max_regens", 12, 0, Integer.MAX_VALUE);
			
			freeRegenerations =
			builder.comment("Every player will start with this number of regenerations. Will cause undefined behavior if bigger than the amount of regenerations per cycle.")
			.translation("config.regeneration.free_regens")
			.defineInRange("free_regens", 0, 0, Integer.MAX_VALUE);
			
			firstStartGiftOnly = builder.comment("Only give new players free regenerations").translation("config.regeneration.first_start_gift_only").define("firstStartGiftOnly", true);
			
			builder.pop();
		}
	}
	
	private static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;
	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
