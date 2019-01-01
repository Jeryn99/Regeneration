package me.fril.regeneration.testing;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class RegenTestUtil {
	
	@SuppressWarnings("deprecation")
	public static CapabilityRegeneration setupFullMockSuite(boolean isRemote) throws ReflectiveOperationException {
		Objenesis objenesis = new ObjenesisStd();
		World world = objenesis.newInstance(WorldClient.class);
		
		Field fieldIsRemote = world.getClass().getField("isRemote");
		fieldIsRemote.setAccessible(true);
		fieldIsRemote.set(world, isRemote);
		
		Class<? extends EntityPlayer> clazz = isRemote ? EntityPlayerSP.class : EntityPlayerMP.class; //can't combine these lines for some reason
		EntityPlayer player = mock(clazz);
		player.world = world;
		
		CapabilityRegeneration capability = mock(CapabilityRegeneration.class);
		doCallRealMethod().when(capability).extractRegeneration(anyInt());
		doCallRealMethod().when(capability).receiveRegenerations(anyInt());
		doCallRealMethod().when(capability).setRegenerationsLeft(anyInt());
		doCallRealMethod().when(capability).getRegenerationsLeft();
		
		when(player.getCapability(isNull(), isNull())).thenAnswer(a->capability);
		when(player.hasCapability(isNull(), isNull())).thenAnswer(a->true);
		
		when(capability.getPlayer()).thenReturn(player);
		
		return capability;
	}
	
}
