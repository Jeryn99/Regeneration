package me.fril.regeneration.testing;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import me.fril.regeneration.handlers.ActingForwarder;

/**
 * Contains a test for the {@link ActionForwarder}. It's slightly pointless but I don't really care.
 * 
 * @author HoldYourWaffle
 */
public class TestActionForwarder {
	
	/**
	 * Tests if {@link ActionForwarder} implements all {@link IActingHandler} methods statically.
	 * A slightly pointless test, because why would there be a method in the interface if you're unable to forward it?
	 * 
	 * @throws ReflectiveOperationException
	 */
	@Test
	public void testForwarderImplementation() throws ReflectiveOperationException {
		Class<?> actingInterface = Class.forName("me.fril.regeneration.handlers.ActingForwarder$IActingHandler");
		
		List<Method> implementedMethods = Arrays.asList(ActingForwarder.class.getDeclaredMethods());
		List<String> implementedMethodNames = implementedMethods.stream().map(m->m.getName()).collect(Collectors.toList());
		
		for (Method m : actingInterface.getDeclaredMethods()) {
			assertTrue("Missing forwarding method for "+m.getName(), implementedMethodNames.contains(m.getName()));
		}
		
		assertTrue("There's a non-static forwarding method", implementedMethods.stream().allMatch(m->Modifier.isStatic(m.getModifiers())));
	}
	
}
