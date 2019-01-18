package me.fril.regeneration.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import me.fril.regeneration.util.ScheduledAction;

/**
 * A class containing tests for the {@link ScheduledAction} timer class
 * 
 * @author HoldYourWaffle
 */
@SuppressWarnings("deprecation")
public class TestTimers {
	private boolean executed;
	
	/**
	 * Tests if the timer executes (at right moment) as well as the correctness of {@link ScheduledAction#getTicksLeft() ticksLeft}
	 */
	@Test
	public void testExecution() {
		ScheduledAction timer = new ScheduledAction(this::runnableMethod, 10);
		
		for (int i = 0; i < 10; i++) {
			assertTrue("Ticksleft is not correct (at tick " + i + ", ticksleft=" + timer.getTicksLeft() + ")", timer.getTicksLeft() == 10 - i);
			assertFalse("Executed too soon (" + i + ")", timer.tick());
		}
		
		assertTrue("There's still a tick left", timer.getTicksLeft() == 0);
		assertFalse("Flag set without execution?", executed);
		assertTrue("Timer didn't tick", timer.tick());
		assertTrue("Never executed method", executed);
		assertTrue("Timer didn't reset properly" + timer.getTicksLeft(), timer.getTicksLeft() == -1);
	}
	
	/*
	 * Tests if the timer handles canceling correctly
	 */
	@Test
	public void testCancel() {
		ScheduledAction timer = new ScheduledAction(this::runnableMethod, 10);
		
		for (int i = 0; i < 5; i++) {
			assertFalse("Executed too soon (" + i + ")", timer.tick());
		}
		
		timer.cancel();
		assertTrue("Cancel did not reset scheduledTick to -1", timer.getTicksLeft() == -1);
		assertFalse("Executed flag set without execution?", executed);
		assertFalse("Timer ticked when canceled", timer.tick());
	}
	
	private void runnableMethod() {
		executed = true;
	}
	
}
