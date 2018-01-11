package com.lcm.regeneration;

import net.minecraftforge.common.config.Configuration;

public class RegenerationConfiguration {
	public final boolean disableTraits;
	
	public RegenerationConfiguration(Configuration cfg) {
		cfg.load();
		disableTraits = cfg.getBoolean("traits", "enableTraits", true, "Enable the trait system. If this is false all trait effects are disabled");
		cfg.save();
	}
}
