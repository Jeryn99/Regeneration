package me.swirtzly.regeneration.handlers;

import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.network.MessageRegenStateEvent;
import me.swirtzly.regeneration.network.NetworkHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class ActingForwarder {
	
	private static List<IActingHandler> SERVER_HANDLERS = new ArrayList<>(), CLIENT_HANDLERS = new ArrayList<>();
	
	public static void init() {
		register(ActingServerHandler.INSTANCE, Dist.DEDICATED_SERVER);
		
		if (FMLCommonHandler.instance().getSide() == Dist.CLIENT) {
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
		(side == Dist.CLIENT ? CLIENT_HANDLERS : SERVER_HANDLERS).add(handler);
	}
	
	public static void onRegenTick(IRegeneration cap) {
		// Never forwarded, as per the documentation
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' tick `event` from client (this is VERY wrong)");
		
		for (IActingHandler handler : SERVER_HANDLERS) {
			handler.onRegenTick(cap);
		}
	}
	
	public static void onEnterGrace(IRegeneration cap) {
		checkAndForward(cap, RegenEvent.ENTER_GRACE);
		for (IActingHandler handler : SERVER_HANDLERS) {
			handler.onEnterGrace(cap);
		}
	}
	
	public static void onRegenFinish(IRegeneration cap) {
		checkAndForward(cap, RegenEvent.REGEN_FINISH);
		for (IActingHandler handler : SERVER_HANDLERS) {
			handler.onRegenFinish(cap);
		}
	}
	
	public static void onRegenTrigger(IRegeneration cap) {
		checkAndForward(cap, RegenEvent.REGEN_TRIGGER);
		for (IActingHandler handler : SERVER_HANDLERS) {
			handler.onRegenTrigger(cap);
		}
	}
	
	public static void onGoCritical(IRegeneration cap) {
		checkAndForward(cap, RegenEvent.CRITICAL_START);
		for (IActingHandler handler : SERVER_HANDLERS) {
			handler.onGoCritical(cap);
		}
	}
	
	public static void onHandsStartGlowing(IRegeneration cap) {
		checkAndForward(cap, RegenEvent.HAND_GLOW_START);
		for (IActingHandler handler : SERVER_HANDLERS) {
			handler.onHandsStartGlowing(cap);
		}
	}

    public static void onPerformingPost(IRegeneration cap) {
		checkAndForward(cap, RegenEvent.PERFORM_POST);
		for (IActingHandler handler : SERVER_HANDLERS) {
			handler.onPerformingPost(cap);
		}
	}
	
	public static void onClient(RegenEvent event, IRegeneration cap) {
		for (IActingHandler handler : CLIENT_HANDLERS) {
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
				case PERFORM_POST:
					handler.onPerformingPost(cap);
				default:
					break;
			}
		}
	}
	
	/**
	 * Knows what to forward by reflection magic
	 */
	private static void checkAndForward(IRegeneration cap, RegenEvent event) {
		if (cap.getPlayer().world.isRemote)
			throw new IllegalStateException("'Posting' \"acting\" `event` from client");
		NetworkHandler.INSTANCE.sendTo(new MessageRegenStateEvent(cap.getPlayer(), event.name()), (ServerPlayerEntity) cap.getPlayer());
	}
	
	public enum RegenEvent {
		ENTER_GRACE, REGEN_FINISH, REGEN_TRIGGER, CRITICAL_START, PERFORM_POST, HAND_GLOW_START
	}
	
}