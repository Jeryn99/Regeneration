package me.fril.regeneration.handlers;

import me.fril.regeneration.common.capability.IRegeneration;

public class ActingForwarder { //XXX feel free to rename this, I couldn't think of anything better
	
	private final static IActingHandler server, client;
	
	static {
		server = new ActingServerHandler();
		client = new ActingClientHandler();
	}
	
	public static void onRegenTick(IRegeneration cap) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' \"acting\" `event` from client");
		
		server.onRegenTick(cap);
	}
	
	public static void onEnterGrace(IRegeneration cap) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' \"acting\" `event` from client");
		
		server.onEnterGrace(cap);
	}
	
	public static void onRegenFinish(IRegeneration cap) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' \"acting\" `event` from client");
		
		server.onRegenFinish(cap);
	}
	
	public static void onRegenTrigger(IRegeneration cap) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' \"acting\" `event` from client");
		
		server.onRegenTrigger(cap);
	}
	
	public static void onGoCritical(IRegeneration cap) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' \"acting\" `event` from client");
		
		server.onGoCritical(cap);
	}
	
	
	
	interface IActingHandler {
		void onRegenTick(IRegeneration cap);
		void onEnterGrace(IRegeneration cap);
		void onRegenFinish(IRegeneration cap);
		void onRegenTrigger(IRegeneration cap);
		void onGoCritical(IRegeneration cap);
	}
	
}
