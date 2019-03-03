package me.suff.regeneration.handlers;

import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.network.MessageRegenStateEvent;
import me.suff.regeneration.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

public class ActingForwarder {
	
	private static List<IActingHandler> serverHandlers = new ArrayList<>(), clientHandlers = new ArrayList<>();
	
	public static void init() {
		register(ActingServerHandler.INSTANCE, Dist.DEDICATED_SERVER);
		
		if (FMLEnvironment.dist != Dist.DEDICATED_SERVER) {
			register(ActingClientHandler.INSTANCE, Dist.CLIENT);
		}
	}
	
	public static void register(Class<? extends IActingHandler> handlerClass, Dist side) {
		try {
			register(handlerClass.newInstance(), side);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Error while instantiating acting handler", e);
		}
	}
	
	public static void register(IActingHandler handler, Dist side) {
		(side == Dist.CLIENT ? clientHandlers : serverHandlers).add(handler);
	}
	
	public static void onRegenTick(LazyOptional<IRegeneration> data) {
		data.ifPresent((cap) -> {
			// Never forwarded, as per the documentation
			if (cap.getPlayer().world.isRemote)
				throw new IllegalStateException("'Posting' tick `event` from client (this is VERY wrong)");
			
			serverHandlers.forEach(handler -> handler.onRegenTick(data));
		});
	}
	
	public static void onEnterGrace(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onEnterGrace(cap));
	}
	
	public static void onRegenFinish(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onRegenFinish(cap));
	}
	
	public static void onRegenTrigger(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onRegenTrigger(cap));
	}
	
	public static void onGoCritical(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onGoCritical(cap));
	}
	
	public static void onHandsStartGlowing(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onHandsStartGlowing(cap));
	}
	
	public static void onClient(String event, LazyOptional<IRegeneration> cap) {
		for (IActingHandler handler : clientHandlers) {
			switch (event) {
				case "onEnterGrace":
					handler.onEnterGrace(cap);
					break;
				case "onRegenFinish":
					handler.onRegenFinish(cap);
					break;
				case "onRegenTrigger":
					handler.onRegenTrigger(cap);
					break;
				case "onGoCritical":
					handler.onGoCritical(cap);
					break;
				case "onHandsStartGlowing":
					handler.onHandsStartGlowing(cap);
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * Knows what to forward by reflection magic
	 */
	private static void checkAndForward(LazyOptional<IRegeneration> data) {
		String event = Thread.currentThread().getStackTrace()[2].getMethodName();
		data.ifPresent((cap) -> {
			if (cap.getPlayer().world.isRemote)
				throw new IllegalStateException("'Posting' \"acting\" `event` from client");
			NetworkHandler.sendTo(new MessageRegenStateEvent(cap.getPlayer(), event), (EntityPlayerMP) cap.getPlayer());
		});
	}
	
}
