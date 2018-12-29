package me.fril.regeneration.handlers;

import java.util.Random;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.types.TypeFiery;
import me.fril.regeneration.handlers.ActingForwarder.IActingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.tardis.mod.common.dimensions.WorldProviderTardis;
import net.tardis.mod.common.entities.controls.ControlDoor;
import net.tardis.mod.common.systems.TardisSystems;
import net.tardis.mod.common.tileentity.TileEntityTardis;

public class TardisModHandler implements IActingHandler {
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRegenTick(IRegeneration cap) {
		if (cap.getType() instanceof TypeFiery) {
			World world = cap.getPlayer().world;
			TypeFiery fiery = (TypeFiery) cap.getType();
			
			if (world.provider instanceof WorldProviderTardis && fiery.getAnimationTicks() == fiery.getAnimationLength()/2) { //TODO getAnimationTicks() reference is probably not needed
				world.loadedTileEntityList.forEach(tileEntity -> damageTardis(tileEntity, cap.getPlayer()));
			}
		}
	}
	
	private void damageTardis(TileEntity tileEntity, EntityPlayer player) {
		if (!(tileEntity instanceof TileEntityTardis) || player.getDistanceSq(tileEntity.getPos()) <= 10) //SUB are you sure the second condition is correct? I don't know exactly what it's supposed to do, but why does the distance have to be exactly 10?
			return;
		
		TileEntityTardis tileEntityTardis = (TileEntityTardis) tileEntity;
		Random rand = tileEntity.getWorld().rand;
		
		//Lets close the door
		ControlDoor controlDoor = tileEntityTardis.getDoor();
		if (controlDoor.isOpen()) {
			controlDoor.processInitialInteract(player, EnumHand.MAIN_HAND);
		}
		
		for (int i = 0; i < 300; ++i) {
			player.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (double) (tileEntityTardis.getPos().getX() + (rand.nextInt(3) - 1)), (double) (tileEntityTardis.getPos().getY() + (rand.nextInt(3) - 1)), (double) (tileEntityTardis.getPos().getZ() + (rand.nextInt(3) - 1)), 0.0D, 1.0D, 0.0D, 0);
		}
		
		//Lets damage some shit
		TardisSystems.BaseSystem[] systems = tileEntityTardis.systems;
		for (TardisSystems.BaseSystem system : systems) {
			int times = rand.nextInt(3) + 1;
			for (int i = 0; i <= times; i++) {
				system.damage();
			}
		}
	}

	@Override
	public void onEnterGrace(IRegeneration cap) {
		
	}

	@Override
	public void onRegenFinish(IRegeneration cap) {
		
	}

	@Override
	public void onRegenTrigger(IRegeneration cap) {
		
	}

	@Override
	public void onGoCritical(IRegeneration cap) {
		
	}
	
	
}
