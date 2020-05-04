package me.swirtzly.regeneration.handlers.acting;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.network.messages.UpdateStateMessage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.ArrayList;
import java.util.List;

public class ActingForwarder {
	
	private static List<Acting> SERVER_HANDLERS = new ArrayList<>(), CLIENT_HANDLERS = new ArrayList<>();
	
	public static void init() {
		register(CommonActing.INSTANCE, Dist.DEDICATED_SERVER);

        if (FMLEnvironment.dist == Dist.CLIENT) {
			register(ClientActing.INSTANCE, Dist.CLIENT);
		}
	}
	
	public static void register(Class<? extends Acting> handlerClass, Dist side) {
		try {
			register(handlerClass.newInstance(), side);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Error while instantiating acting handler", e);
		}
	}
	
	public static void register(Acting handler, Dist side) {
		(side == Dist.CLIENT ? CLIENT_HANDLERS : SERVER_HANDLERS).add(handler);
	}

    public static void onRegenTick(IRegen cap) {
		// Never forwarded, as per the documentation
        if (cap.getPlayer().world.isRemote)
            throw new IllegalStateException("'Posting' tick `event` from client (this is VERY wrong)");
		
		for (Acting handler : SERVER_HANDLERS) {
			handler.onRegenTick(cap);
		}
	}

    public static void onEnterGrace(IRegen cap) {
		checkAndForward(cap, RegenEvent.ENTER_GRACE);
		for (Acting handler : SERVER_HANDLERS) {
			handler.onEnterGrace(cap);
		}
	}

    public static void onRegenFinish(IRegen cap) {
		checkAndForward(cap, RegenEvent.REGEN_FINISH);
		for (Acting handler : SERVER_HANDLERS) {
			handler.onRegenFinish(cap);
		}
	}

    public static void onRegenTrigger(IRegen cap) {
		checkAndForward(cap, RegenEvent.REGEN_TRIGGER);
		for (Acting handler : SERVER_HANDLERS) {
			handler.onRegenTrigger(cap);
		}
	}

    public static void onGoCritical(IRegen cap) {
		checkAndForward(cap, RegenEvent.CRITICAL_START);
		for (Acting handler : SERVER_HANDLERS) {
			handler.onGoCritical(cap);
		}
	}

    public static void onHandsStartGlowing(IRegen cap) {
		checkAndForward(cap, RegenEvent.HAND_GLOW_START);
		for (Acting handler : SERVER_HANDLERS) {
			handler.onHandsStartGlowing(cap);
		}
	}

    public static void onPerformingPost(IRegen cap) {
		checkAndForward(cap, RegenEvent.PERFORM_POST);
		for (Acting handler : SERVER_HANDLERS) {
			handler.onPerformingPost(cap);
		}
	}

    public static void onClient(RegenEvent event, IRegen cap) {
		for (Acting handler : CLIENT_HANDLERS) {
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
    private static void checkAndForward(IRegen cap, RegenEvent event) {
        if (cap.getPlayer().world.isRemote) throw new IllegalStateException("'Posting' \"acting\" `event` from client");
        NetworkDispatcher.sendPacketToAll(new UpdateStateMessage(cap.getPlayer(), event.name()));
	}
	
	public enum RegenEvent {
		ENTER_GRACE, REGEN_FINISH, REGEN_TRIGGER, CRITICAL_START, PERFORM_POST, HAND_GLOW_START
	}
	
}
