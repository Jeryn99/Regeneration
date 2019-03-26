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
			
			for (IActingHandler handler : serverHandlers) {
				handler.onRegenTick(data);
			}
		});
	}
	
	public static void onEnterGrace(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap, RegenEvent.ENTER_GRACE);
		for (IActingHandler handler : serverHandlers) {
			handler.onEnterGrace(cap);
		}
	}
	
	public static void onRegenFinish(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap, RegenEvent.REGEN_FINISH);
		for (IActingHandler handler : serverHandlers) {
			handler.onRegenFinish(cap);
		}
	}
	
	public static void onRegenTrigger(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap, RegenEvent.REGEN_TRIGGER);
		for (IActingHandler handler : serverHandlers) {
			handler.onRegenTrigger(cap);
		}
	}
	
	public static void onGoCritical(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap, RegenEvent.CRITICAL_START);
		for (IActingHandler handler : serverHandlers) {
			handler.onGoCritical(cap);
		}
	}
	
	public static void onHandsStartGlowing(LazyOptional<IRegeneration> cap) {
		checkAndForward(cap, RegenEvent.HAND_GLOW_START);
		for (IActingHandler handler : serverHandlers) {
			handler.onHandsStartGlowing(cap);
		}
	}
	
	public static void onClient(RegenEvent event, LazyOptional<IRegeneration> cap) {
		for (IActingHandler handler : clientHandlers) {
			switch (event) {
				case ENTER_GRACE:
					handler.onEnterGrace(cap);
					break;
				case REGEN_FINISH:
					handler.onRegenFinish(cap);
					break;
				case REGEN_TRIGGER:
					handler.onRegenTrigger(cap);
					break;
				case CRITICAL_START:
					handler.onGoCritical(cap);
					break;
				case HAND_GLOW_START:
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
	private static void checkAndForward(LazyOptional<IRegeneration> data, RegenEvent event) {
		data.ifPresent((cap) -> {
			if (cap.getPlayer().world.isRemote)
				throw new IllegalStateException("'Posting' \"acting\" `event` from client");
			NetworkHandler.sendTo(new MessageRegenStateEvent(cap.getPlayer(), event.name()), (EntityPlayerMP) cap.getPlayer());
		});
	}
	
	public enum RegenEvent {
		ENTER_GRACE, REGEN_FINISH, REGEN_TRIGGER, CRITICAL_START, HAND_GLOW_START
	}
	
}
