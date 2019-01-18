package me.fril.regeneration.testing;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Test;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.CapabilityRegeneration.RegenerationStateManager;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.RegenState;
import me.fril.regeneration.util.RegenState.Transition;

/**
 * A class containing tests for various assumptions made throughout the state-based system
 * 
 * @author HoldYourWaffle
 */
public class TestStateBasedSystem {
	
	/**
	 * Tests if all {@link Transition Transitions} have a corresponding {@link RegenerationStateManager#transitionRunnables callback}
	 * 
	 * @throws ReflectiveOperationException There is some reflection involved to circumvent encapsulation, which may (but shouldn't) fail
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void allTransitionsHaveCallback() throws ReflectiveOperationException {
		IRegeneration cap = new CapabilityRegeneration();
		
		// Reflection stuff to circumvent encapsulation
		Constructor<RegenerationStateManager> con = CapabilityRegeneration.RegenerationStateManager.class.getDeclaredConstructor(CapabilityRegeneration.class);
		con.setAccessible(true);
		RegenerationStateManager stateManager = con.newInstance(cap);
		
		Field field = stateManager.getClass().getDeclaredField("transitionCallbacks");
		field.setAccessible(true);
		
		Map<Transition, Runnable> transitionRunnables = (Map<Transition, Runnable>) field.get(stateManager);
		assertTrue("Missing or duplicate transition callback", transitionRunnables.size() == Transition.values().length);
	}
	
	/**
	 * All states should have a corresponding transition, except for <b>ALIVE</b>, hence the -1.
	 * <b>HAND_GLOW_START</b> and <b>HAND_GLOW_TRIGGER</b> don't have corresponding states, hence the -2.
	 * This <i>may</i> change in the future, but that's probably a red flag, because the system assumes this paradigm in multiple places.
	 */
	@Test
	public void transitionStateCountMatch() {
		assertTrue("Not all states have a transition, or vice versa", RegenState.values().length - 1 == Transition.values().length - 2);
	}
	
	// TESTING could probably add some tests that actually verify the correctness of transition callbacks
	// TESTING add actual state-flow test?
	
}
