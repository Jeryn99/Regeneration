package me.fril.regeneration.proxy;

import me.fril.regeneration.util.PlayerUtil;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class CommonProxy implements IProxy {
	
	@Override
	public void postInit() {
		PlayerUtil.createPostList();
	}
}
