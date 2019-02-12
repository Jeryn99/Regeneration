package me.suff.regeneration.testing;

import static org.junit.Assert.*;

import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.common.commands.RegenDebugCommand;
import org.junit.Test;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.command.CommandException;

public class TestDebugCommand {
	
	@Test
	public void testSetRegens() throws ReflectiveOperationException, CommandException {
		CapabilityRegeneration cap = RegenTestUtil.setupFullMockSuite(false);
		RegenDebugCommand cmd = new RegenDebugCommand();
		
		assertEquals(0, cap.getRegenerationsLeft());
		commandSetRegens(cmd, cap, 5);
		commandSetRegens(cmd, cap, 10);
		commandSetRegens(cmd, cap, 2);
		commandSetRegens(cmd, cap, 0);
		commandSetRegens(cmd, cap, RegenConfig.regenCapacity);
		commandSetRegens(cmd, cap, 0);
	}
	
	private static void commandSetRegens(RegenDebugCommand cmd, CapabilityRegeneration cap, int i) throws CommandException {
		cmd.execute(null, cap.getPlayer(), new String[] { "setregens", String.valueOf(i) });
		assertEquals(i, cap.getRegenerationsLeft());
	}
	
}
