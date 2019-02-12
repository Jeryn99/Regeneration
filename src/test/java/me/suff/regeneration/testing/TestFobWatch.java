package me.suff.regeneration.testing;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import me.suff.regeneration.RegenConfig;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.item.ItemFobWatch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;

public class TestFobWatch {
	
	@SuppressWarnings("unused")
	private int currentStackDamage; // easier mocking
	
	private CapabilityRegeneration setup(boolean sneaking, boolean creative) throws ReflectiveOperationException {
		CapabilityRegeneration cap = RegenTestUtil.setupFullMockSuite(false);
		EntityPlayer player = cap.getPlayer();
		
		when(player.isSneaking()).thenReturn(sneaking);
		when(player.isCreative()).thenReturn(creative);
		
		ItemFobWatch watchItem = new ItemFobWatch();
		ItemStack watchStack = mock(ItemStack.class);
		
		/*
		 * when(watchStack.getItemDamage()).thenReturn(currentStackDamage);
		 * doAnswer(a->currentStackDamage = a.getArgument(0)).when(watchStack).setItemDamage(anyInt());
		 */
		
		when(watchStack.getItem()).thenReturn(watchItem);
		doCallRealMethod().when(watchStack).useItemRightClick(any(World.class), any(EntityPlayer.class), isNull());
		
		when(player.getHeldItem(isNull())).thenAnswer(a->watchStack);
		
		/*
		 * doAnswer(a->{ //record any status messages sent
		 * ITextComponent in = a.getArgument(0); //can't combine lines because generics
		 * lastStatusKey = in.getUnformattedText();
		 * return null;
		 * }).when(player).sendStatusMessage(any(ITextComponent.class), any(Boolean.class));
		 */
		
		return cap;
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	private void test(CapabilityRegeneration cap, EnumActionResult result, /* String statusKey, */ int regenIn, int damageIn, int expRegenOut, int expDamageOut) throws AssertionError {
		ItemStack stack = cap.getPlayer().getHeldItem(null);
		stack.setItemDamage(damageIn);
		cap.setRegenerationsLeft(regenIn);
		
		assertEquals("Incorrect ActionResult", result, stack.useItemRightClick(cap.getPlayer().world, cap.getPlayer(), null).getType());
		// assertEquals("Incorrect status message key", "regeneration.messages.transfer."+statusKey, lastStatusKey);
		
		assertEquals("Incorrect regenerations-left value", expRegenOut, cap.getRegenerationsLeft());
		assertEquals("Incorrect stack damage value", expDamageOut, stack.getItemDamage());
	}
	
	@Test
	@Ignore
	public void testReceive() throws ReflectiveOperationException {
		@SuppressWarnings("unused")
		CapabilityRegeneration cap = setup(false, false);
		
		// TESTING do the actual watch testing
		
		/*
		 * for (int M = 6; M < 15; M += 3) {
		 * RegenConfig.regenCapacity = M;
		 * int x = M/2;
		 * 
		 * test(cap, FAIL, M, M, M, M);
		 * test(cap, SUCCESS, 0, 0, M, M);
		 * 
		 * test(cap, FAIL, M, x, M, x);
		 * test(cap, FAIL, x, M, x, M);
		 * 
		 * test(cap, FAIL, M, 0, M, 0);
		 * test(cap, FAIL, 0, M, 0, M);
		 * 
		 * test(cap, SUCCESS, x, x, x + (M-x), x + (M-x) );
		 * test(cap, SUCCESS, 0, x, M - x, x + (M-x) );
		 * test(cap, SUCCESS, x, 0, M, M - x );
		 * }
		 */
	}
	
	@Test
	@Ignore
	public void testTransfer() throws ReflectiveOperationException {
		@SuppressWarnings("unused")
		CapabilityRegeneration cap = setup(true, false);
		
		// TESTING do the actual watch testing
	}
	
	@Test
	@Ignore
	public void testCreative() throws ReflectiveOperationException {
		@SuppressWarnings("unused")
		CapabilityRegeneration cap = setup(false, true);
		
		// TESTING do the actual watch testing
		
		cap = setup(true, true);
		
		// TESTING do the actual watch testing
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testCapacityIsDurability() {
		RegenConfig.regenCapacity = 0;
		assertEquals(RegenConfig.regenCapacity, new ItemFobWatch().getMaxDamage());
		
		RegenConfig.regenCapacity = 5;
		assertEquals(RegenConfig.regenCapacity, new ItemFobWatch().getMaxDamage());
		
		RegenConfig.regenCapacity = 999999999;
		assertEquals(RegenConfig.regenCapacity, new ItemFobWatch().getMaxDamage());
	}
	
	@After
	public void cleanup() throws IOException {
		FileUtils.forceDeleteOnExit(new File("logs"));
	}
}
