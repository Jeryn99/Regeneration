package me.suff.regeneration.handlers;

import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.network.MessageRegenStateEvent;
import me.suff.regeneration.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.lang.reflect.InvocationTargetException;
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
	
	public static void onRegenTick(IRegeneration cap) {
		// Never forwarded, as per the documentation
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' tick `event` from client (this is VERY wrong)");
		
		serverHandlers.forEach(handler -> handler.onRegenTick(cap));
	}
	
	public static void onEnterGrace(IRegeneration cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onEnterGrace(cap));
	}
	
	public static void onRegenFinish(IRegeneration cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onRegenFinish(cap));
	}
	
	public static void onRegenTrigger(IRegeneration cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onRegenTrigger(cap));
	}
	
	public static void onGoCritical(IRegeneration cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onGoCritical(cap));
	}
	
	public static void onHandsStartGlowing(IRegeneration cap) {
		checkAndForward(cap);
		serverHandlers.forEach(handler -> handler.onHandsStartGlowing(cap));
	}
	
	public static void onClient(String event, IRegeneration cap) {
		try {
			for (IActingHandler handler : clientHandlers) {
				handler.getClass().getMethod(event, IRegeneration.class).invoke(handler, cap); // TODO this can definitely be optimized
			}
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new IllegalStateException("Illegal handler method on client: " + event, e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Error while forwarding event to client (" + event + ")", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Unknown method was forwarded '" + event + "'", e);
		}
	}
	
	/**
	 * Knows what to forward by reflection magic
	 */
	private static void checkAndForward(IRegeneration cap) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' \"acting\" `event` from client");
		
		String event = Thread.currentThread().getStackTrace()[2].getMethodName();
		NetworkHandler.sendTo(new MessageRegenStateEvent(cap.getPlayer(), event), (EntityPlayerMP) cap.getPlayer());
	}
	
}
