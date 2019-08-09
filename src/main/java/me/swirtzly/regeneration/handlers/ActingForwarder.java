package me.swirtzly.regeneration.handlers;

import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.network.MessageRegenStateEvent;
import me.swirtzly.regeneration.network.NetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class ActingForwarder {

    private static List<IActingHandler> SERVER_HANDLERS = new ArrayList<>(), CLIENT_HANDLERS = new ArrayList<>();

    public static void init() {
        register(ActingServerHandler.INSTANCE, Side.SERVER);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            register(ActingClientHandler.INSTANCE, Side.CLIENT);
        }
    }

    public static void register(Class<? extends IActingHandler> handlerClass, Side side) {
        try {
            register(handlerClass.newInstance(), side);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error while instantiating acting handler", e);
        }
    }

    public static void register(IActingHandler handler, Side side) {
        (side == Side.CLIENT ? CLIENT_HANDLERS : SERVER_HANDLERS).add(handler);
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

    public static void onStartPost(IRegeneration cap) {
        checkAndForward(cap, RegenEvent.PERFORM_POST);
        for (IActingHandler handler : SERVER_HANDLERS) {
            handler.onStartPost(cap);
        }
    }

    public static void onProcessDone(IRegeneration cap) {
        checkAndForward(cap, RegenEvent.PROCESS_DONE);
        for (IActingHandler handler : SERVER_HANDLERS) {
            handler.onProcessDone(cap);
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
                    handler.onStartPost(cap);
                case PROCESS_DONE:
                    handler.onProcessDone(cap);
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
        NetworkHandler.INSTANCE.sendTo(new MessageRegenStateEvent(cap.getPlayer(), event.name()), (EntityPlayerMP) cap.getPlayer());
    }

    public enum RegenEvent {
        ENTER_GRACE, REGEN_FINISH, REGEN_TRIGGER, CRITICAL_START, PERFORM_POST, HAND_GLOW_START, PROCESS_DONE
    }

}