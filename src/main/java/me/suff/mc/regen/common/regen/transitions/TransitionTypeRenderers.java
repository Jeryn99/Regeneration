package me.suff.mc.regen.common.regen.transitions;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.client.rendering.transitions.TransitionRenderer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* Created by Craig on 25/03/2021 */
public class TransitionTypeRenderers {

    public static Map< TransitionType, TransitionRenderer > TRANSITION_RENDERERS = new ConcurrentHashMap<>();

    public static void add(TransitionType transitionType, TransitionRenderer transitionRenderer) {
        TRANSITION_RENDERERS.put(transitionType, transitionRenderer);
    }

    public static TransitionRenderer get(TransitionType transitionType){
        if(TRANSITION_RENDERERS.containsKey(transitionType)){
            return TRANSITION_RENDERERS.get(transitionType);
        }
        Regeneration.LOG.error("No Renderer registered for " + transitionType.getRegistryName());
        return null;
    }

}
