package me.fril.regeneration.testing;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.items.ItemFobWatch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Tests the correct handling of receiving/extracting regenerations using the {@link ItemFobWatch fob watch}
 * 
 * @author HoldYourWaffle
 */
public class TestFobWatch {
	
	//Mocks initialised by setup(...)
	private World world;
	private EntityPlayer player;
	private ItemStack watchStack;
	private ItemFobWatch watchItem;
	private CapabilityRegeneration capability;
	
	//Actual state holders, used in the mocks
	private int stackDamage, regenerationsLeft;
	private String lastStatusKey;
	
	private void setup(boolean sneaking, boolean creative) {
		Objenesis objenesis = new ObjenesisStd();
		world = objenesis.newInstance(WorldServer.class);
		
		
		player = mock(EntityPlayer.class);
		player.world = world;
		when(player.isSneaking()).thenReturn(sneaking);
		when(player.isCreative()).thenReturn(creative);
		
		when(player.getHeldItem(any(EnumHand.class))).thenAnswer(a->watchStack);
		when(player.getCapability(isNull(), isNull())).thenAnswer(a->capability);
		when(player.hasCapability(isNull(), isNull())).thenAnswer(a->true);
		
		doAnswer(a->{
			ITextComponent in = a.getArgument(0);
			lastStatusKey = in.getUnformattedText();
			return null;
		}).when(player).sendStatusMessage(any(ITextComponent.class), any(Boolean.class));
		
		
		capability = mock(CapabilityRegeneration.class);
		when(capability.getPlayer()).thenReturn(player);
		when(capability.getRegenerationsLeft()).thenAnswer(a->regenerationsLeft);
		
		doAnswer(a->regenerationsLeft += (int) a.getArgument(0)).when(capability).receiveRegenerations(any(Integer.class));
		doAnswer(a->regenerationsLeft -= (int) a.getArgument(0)).when(capability).extractRegeneration(any(Integer.class));
		
		
		watchStack = mock(ItemStack.class);
		when(watchStack.getItemDamage()).thenAnswer(a->stackDamage);
		doAnswer(a->stackDamage = a.getArgument(0)).when(watchStack).setItemDamage(any(Integer.class));
	}
	
	
	
	@Test
	public void testReceive() {
		setup(false, false);
		
		for (int M = 6; M < 15; M += 3) {
			RegenConfig.regenCapacity = M;
			int x  = M/2;
			
			/*test(FAIL, M, M, M, M);
			test(SUCCESS, 0, 0, M, M);
			
			test(FAIL, M, x, M, x);
			test(FAIL, x, M, x, M);
			
			test(FAIL, M, 0, M, 0);
			test(FAIL, 0, M, 0, M);
			
			test(SUCCESS, x, x,   x + (M-x),  x + (M-x)  );
			test(SUCCESS, 0, x,   M - x,      x + (M-x)  );
			test(SUCCESS, x, 0,   M,          M - x      );*/
			
			//NOW x,y test with supply < needed (-> r + supply)
			//NOW x,y test with supply > needed (-> r + needed)
		}
		
		//NOW add status key parameters
	}
	
	/*@Test
	public void testTransfer() {
		setup(true, false);
		
		int x = 5, y = 7;
		for (int M = 6; M < 15; M += 3) {
			RegenConfig.regenCapacity = M;
			
			test(M, M, , );
			test(0, 0, , );
			
			test(M, x, , );
			test(x, M, , );
			
			test(M, 0, , );
			test(0, M, , );
			
			test(x, x, , );
			test(0, x, , );
			test(x, 0, , );
			test(x, y, , );
		}
		
		//NOW add tests expectations
	}
	
	@Test
	public void testCreative() {
		setup(false, true);
		int x = 5, y = 7;
		
		for (int M = 6; M < 15; M += 3) {
			RegenConfig.regenCapacity = M;
			
			test(M, M, , 0);
			test(0, 0, , 0);
			
			test(M, x, , 0);
			test(x, M, , 0);
			
			test(M, 0, , 0);
			test(0, M, , 0);
			
			test(x, x, , 0);
			test(0, x, , 0);
			test(x, 0, , 0);
			test(x, y, , 0);
		}
		
		setup(true, true);
		for (int M = 6; M < 15; M += 3) {
			RegenConfig.regenCapacity = M;
			
			test(M, M, , 0);
			test(0, 0, , 0);
			
			test(M, x, , 0);
			test(x, M, , 0);
			
			test(M, 0, , 0);
			test(0, M, , 0);
			
			test(x, x, , 0);
			test(0, x, , 0);
			test(x, 0, , 0);
			test(x, y, , 0);
		}
	}*/
	
	
	
	private void test(EnumActionResult result, String statusKey, int regenIn, int damageIn, int expRegenOut, int expDamageOut) throws AssertionError {
		regenerationsLeft = regenIn;
		stackDamage = damageIn;
		
		watchItem = new ItemFobWatch();
		assertEquals("Incorrect ActionResult", result, watchItem.onItemRightClick(world, player, EnumHand.MAIN_HAND).getType());
		assertEquals("Incorrect status message key", "regeneration.messages.transfer."+statusKey, lastStatusKey);
		
		assertEquals("Incorrect regenerations-left value", expRegenOut, regenerationsLeft);
		assertEquals("Incorrect stack damage value", expDamageOut, stackDamage);
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
