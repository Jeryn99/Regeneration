package me.fril.regeneration.handlers;

import java.util.Random;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.types.TypeFiery;
import me.fril.regeneration.handlers.ActingForwarder.IActingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.tardis.mod.common.entities.controls.ControlDoor;
import net.tardis.mod.common.sounds.TSounds;
import net.tardis.mod.common.systems.SystemDimension;
import net.tardis.mod.common.systems.TardisSystems;
import net.tardis.mod.common.tileentity.TileEntityTardis;

public class TardisModIntegrationHandler implements IActingHandler {
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		if (cap.getPlayer().ticksExisted % 60 == 0)
			playBells(cap);
	}
	
	@Override
	public void onEnterGrace(IRegeneration cap) {
		playBells(cap);
	}

	@Override
	public void onRegenFinish(IRegeneration cap) {
		playBells(cap);
	}

	@Override
	public void onRegenTrigger(IRegeneration cap) {
		if (cap.getType() instanceof TypeFiery) {
			damageTardisInRange(cap.getPlayer());
		}
		
		playBells(cap);
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		playBells(cap);
	}
	
	
	
	private void playBells(IRegeneration cap) {
		cap.getPlayer().world.playSound(null, cap.getPlayer().getPosition(), TSounds.cloister_bell, SoundCategory.BLOCKS, 1, 1);
	}
	
	private void damageTardisInRange(EntityPlayer player) {
		for (TileEntity te : player.world.loadedTileEntityList) {
			if (!(te instanceof TileEntityTardis) || player.getDistanceSq(te.getPos()) > 10)
				continue;
			
			TileEntityTardis tileEntityTardis = (TileEntityTardis) te;
			Random rand = tileEntityTardis.getWorld().rand;
			tileEntityTardis.getWorld().playSound(null, tileEntityTardis.getPos(), TSounds.cloister_bell, SoundCategory.BLOCKS, 1.0F, 1.0F);
			
			// Lets close the door
			ControlDoor controlDoor = tileEntityTardis.getDoor();
			if (controlDoor != null && controlDoor.isOpen()) {
				controlDoor.processInitialInteract(player, EnumHand.MAIN_HAND);
			}
			
			// Lets damage some shit
			TardisSystems.BaseSystem[] systems = tileEntityTardis.systems;
			for (TardisSystems.BaseSystem system : systems) {
				if (rand.nextInt(5) < 2 && !(system instanceof SystemDimension)) {
					int times = rand.nextInt(3) + 1;
					for (int i = 0; i <= times; i++) {
						system.damage();
					}
				}
			}
			
			if (tileEntityTardis.isInFlight()) {
				tileEntityTardis.crash(true);
			}
				
			/*
			SUB this was firing once for every tile entity in the world which was either:
				- Not a tardis
				- Outside 10 blocks of the regenerating player
			Which means that when somebody regenerates, every tile entity in the world has a 50% chance of doing a fake explosion.
			Are you sure that that's what you want?
			
			else {
				World world = tileEntityTardis.getWorld();
				BlockPos pos = tileEntityTardis.getPos();
				
				if (world.rand.nextBoolean()) {
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
					if (world.isRemote) { //SUB keep in mind that this will *never* be true in the regen tick event (although we're using it on regen trigger now)
						for (int i = 0; i <= 12; i++) {
							world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, pos.getX(), pos.getY(), pos.getZ(), 1.0D, 0.0D, 0.0D);
						}
					}
				}
			}*/
		}
	}
	
	
}
