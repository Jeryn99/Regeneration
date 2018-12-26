package me.fril.regeneration.handlers;

import java.lang.reflect.InvocationTargetException;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.network.MessageRegenStateEvent;
import me.fril.regeneration.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ActingForwarder {
	
	public static void onRegenTick(IRegeneration cap) {
		//Never forwarded, as per the documentation
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' tick `event` from client (this is VERY wrong)");
		
		ActingServerHandler.INSTANCE.onRegenTick(cap);
	}
	
	public static void onEnterGrace(IRegeneration cap) {
		checkAndForward(cap);
		ActingServerHandler.INSTANCE.onEnterGrace(cap);
	}
	
	public static void onRegenFinish(IRegeneration cap) {
		checkAndForward(cap);
		ActingServerHandler.INSTANCE.onRegenFinish(cap);
	}
	
	public static void onRegenTrigger(IRegeneration cap) {
		checkAndForward(cap);
		ActingServerHandler.INSTANCE.onRegenTrigger(cap);
	}
	
	public static void onGoCritical(IRegeneration cap) {
		checkAndForward(cap);
		ActingServerHandler.INSTANCE.onGoCritical(cap);
	}
	
	
	
	public static void onClient(String event, IRegeneration cap) {
		try {
			ActingClientHandler.class.getMethod(event, IRegeneration.class).invoke(ActingClientHandler.INSTANCE, cap);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new IllegalStateException("Illegal handler method on client: "+event, e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Error while forwarding event to client ("+event+")", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Unknown method was forwarded '"+event+"'", e);
		}
	}
	
	
	
	/** Knows what to forward by reflection magic */
	private static void checkAndForward(IRegeneration cap) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' \"acting\" `event` from client");
		
		String event = Thread.currentThread().getStackTrace()[2].getMethodName();
		NetworkHandler.INSTANCE.sendTo(new MessageRegenStateEvent(cap.getPlayer(), event), (EntityPlayerMP)cap.getPlayer());
	}
	
	
	
	interface IActingHandler {
		/** NOT FORWARDED TO THE CLIENT! Having a packet sent every tick probably is not something we want, and it creates issues with half-loaded players */
		void onRegenTick(IRegeneration cap);
		void onEnterGrace(IRegeneration cap);
		void onRegenFinish(IRegeneration cap);
		void onRegenTrigger(IRegeneration cap);
		void onGoCritical(IRegeneration cap);
	}
	
}
