package mc.craig.software.regen.common.regen.acting;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.network.messages.StateMessage;

import java.util.ArrayList;
import java.util.List;

public class ActingForwarder {

    private static final List<Acting> SERVER_HANDLERS = new ArrayList<>();
    private static final List<Acting> CLIENT_HANDLERS = new ArrayList<>();

    public static void init(boolean isClient) {
        register(CommonActing.INSTANCE, Side.COMMON);

        if (isClient) {
            register(ClientActing.INSTANCE, Side.CLIENT);
        }
    }

    public static void register(Class<? extends Acting> handlerClass, Side side) {
        try {
            register(handlerClass.newInstance(), side);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error while instantiating acting handler", e);
        }
    }

    public static void register(Acting handler, Side side) {
        (side == Side.CLIENT ? CLIENT_HANDLERS : SERVER_HANDLERS).add(handler);
    }

    public static void onRegenTick(RegenerationData cap) {
        // Never forwarded, as per the documentation
        if (cap.getLiving().level().isClientSide)
            throw new IllegalStateException("'Posting' tick `event` from client (this is VERY wrong)");

        for (Acting handler : SERVER_HANDLERS) {
            handler.onRegenTick(cap);
        }
    }

    public static void onEnterGrace(RegenerationData cap) {
        checkAndForward(cap, RegenEvent.ENTER_GRACE);
        for (Acting handler : SERVER_HANDLERS) {
            handler.onEnterGrace(cap);
        }
    }

    public static void onRegenFinish(RegenerationData cap) {
        checkAndForward(cap, RegenEvent.REGEN_FINISH);
        for (Acting handler : SERVER_HANDLERS) {
            handler.onRegenFinish(cap);
        }
    }

    public static void onRegenTrigger(RegenerationData cap) {
        checkAndForward(cap, RegenEvent.REGEN_TRIGGER);
        for (Acting handler : SERVER_HANDLERS) {
            handler.onRegenTrigger(cap);
        }
    }

    public static void onGoCritical(RegenerationData cap) {
        checkAndForward(cap, RegenEvent.CRITICAL_START);
        for (Acting handler : SERVER_HANDLERS) {
            handler.onGoCritical(cap);
        }
    }

    public static void onHandsStartGlowing(RegenerationData cap) {
        checkAndForward(cap, RegenEvent.HAND_GLOW_START);
        for (Acting handler : SERVER_HANDLERS) {
            handler.onHandsStartGlowing(cap);
        }
    }

    public static void onPerformingPost(RegenerationData cap) {
        checkAndForward(cap, RegenEvent.PERFORM_POST);
        for (Acting handler : SERVER_HANDLERS) {
            handler.onPerformingPost(cap);
        }
    }

    public static void onClient(RegenEvent event, RegenerationData cap) {
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
    private static void checkAndForward(RegenerationData cap, RegenEvent event) {
        if (cap.getLiving().level().isClientSide) {
            throw new IllegalStateException("'Posting' \"acting\" `event` from client");
        }
        new StateMessage(cap.getLiving(), event).sendToAll();
    }

    public enum Side {
        COMMON, CLIENT
    }

    public enum RegenEvent {
        ENTER_GRACE, REGEN_FINISH, REGEN_TRIGGER, CRITICAL_START, PERFORM_POST, HAND_GLOW_START
    }

}
