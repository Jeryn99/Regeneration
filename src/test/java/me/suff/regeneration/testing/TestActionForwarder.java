package me.suff.regeneration.testing;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.handlers.IActingHandler;
import org.junit.Ignore;
import org.junit.Test;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.handlers.ActingForwarder;

/**
 * Contains a test for the {@link ActionForwarder}. It's slightly pointless but I don't really care.
 * 
 * @author HoldYourWaffle
 */
public class TestActionForwarder {
	
	/**
	 * Tests if {@link ActingForwarder} implements all {@link IActingHandler} methods statically.
	 * A slightly pointless test, because why would there be a method in the interface if you're unable to forward it?
	 * 
	 * @throws ReflectiveOperationException
	 */
	@Test
	public void testForwarderImplementation() {
		List<Method> implementedMethods = Arrays.asList(ActingForwarder.class.getDeclaredMethods());
		List<String> implementedMethodNames = implementedMethods.stream().map(m->m.getName()).collect(Collectors.toList());
		
		for (Method m : IActingHandler.class.getDeclaredMethods()) {
			assertTrue("Missing forwarding method for " + m.getName(), implementedMethodNames.contains(m.getName()));
		}
		
		assertTrue("There's a non-static forwarding method", implementedMethods.stream().allMatch(m->Modifier.isStatic(m.getModifiers())));
	}
	
	/**
	 * Tests if the {@link ActingForwarder} correctly <b>refuses</b> to forward <code>tick</code>-events to the client, as per the documentation.
	 * Forwarding something every tick would mean a packet every tick, which is horrible practice as well as buggy with half-loaded players.
	 * 
	 * @throws ReflectiveOperationException
	 */
	@Test
	@Ignore
	public void testTickNotForwarded() throws ReflectiveOperationException {
		// FUTURE should probably add a test for this, no idea how though since I can't mock static methods without the possiblity of ruining other stuff
	}
	
	/**
	 * Tests if the {@link ActingForwarder} throws an {@link IllegalStateException} when posting from the client.
	 * 
	 * @throws ReflectiveOperationException
	 */
	@Test
	public void testClientPost() throws ReflectiveOperationException {
		CapabilityRegeneration capability = RegenTestUtil.setupFullMockSuite(true);
		
		List<Method> interfaceMethods = Arrays.asList(IActingHandler.class.getDeclaredMethods());
		List<Method> forwarderMethods = interfaceMethods.stream().map(m-> {
			try {
				return ActingForwarder.class.getMethod(m.getName(), IRegeneration.class);
			} catch (NoSuchMethodException e) {
				throw new Error("Could not get implementation method for " + m.getName(), e);
			}
		}).collect(Collectors.toList());
		
		for (Method m : forwarderMethods) {
			boolean thrown = false;
			
			try {
				m.invoke(null, capability);
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof IllegalStateException)
					thrown = true;
				else
					throw new AssertionError("Forwarding throwed an exception other than IllegalStateException", e);
			}
			
			assertTrue("Client action posting did not throw IllegalStateException " + m.getName(), thrown);
		}
	}
	
}
