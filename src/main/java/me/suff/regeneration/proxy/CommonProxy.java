package me.suff.regeneration.proxy;

import me.suff.regeneration.common.dna.DnaHandler;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class CommonProxy implements IProxy {
	
	
	
	@Override
	public void postInit() {
		DnaHandler.init();
	}
}
