package me.fril.regeneration.handlers;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.types.TypeFiery;
import me.fril.regeneration.handlers.ActingForwarder.IActingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tardis.mod.common.entities.controls.ControlDoor;
import net.tardis.mod.common.sounds.TSounds;
import net.tardis.mod.common.systems.SystemDimension;
import net.tardis.mod.common.systems.TardisSystems;
import net.tardis.mod.common.tileentity.TileEntityTardis;

import java.util.Random;

public class TardisModHandler implements IActingHandler {

	@Override
	public void onRegenTick(IRegeneration cap) {
		if (cap.getPlayer().ticksExisted % 60 == 0) {
			playBells(cap);
		}
	}
	
	private void damageTardis(TileEntity tileEntity, EntityPlayer player) {


		if (tileEntity instanceof TileEntityTardis && player.getDistanceSq(tileEntity.getPos()) < 10) {

			TileEntityTardis tileEntityTardis = (TileEntityTardis) tileEntity;
			tileEntityTardis.getWorld().playSound(null, tileEntityTardis.getPos(), TSounds.cloister_bell, SoundCategory.BLOCKS, 1.0F, 1.0F);

			Random rand = tileEntity.getWorld().rand;

			//Lets close the door
			ControlDoor controlDoor = tileEntityTardis.getDoor();
			if (controlDoor != null && controlDoor.isOpen()) {
				controlDoor.processInitialInteract(player, EnumHand.MAIN_HAND);
			}

			//Lets damage some shit
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

		} else {
			World world = tileEntity.getWorld();
			BlockPos tilePos = tileEntity.getPos();
			if (world.rand.nextBoolean()) {
				world.playSound(null, tilePos.getX(), tilePos.getY(), tilePos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
				if (world.isRemote) {
					for (int i = 0; i <= 12; i++) {
						world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, tilePos.getX(), tilePos.getY(), tilePos.getZ(), 1.0D, 0.0D, 0.0D);
					}
				}
			}
		}
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
			World world = cap.getPlayer().world;
			world.loadedTileEntityList.forEach(tileEntity -> damageTardis(tileEntity, cap.getPlayer()));
		}
	}

	@Override
	public void onGoCritical(IRegeneration cap) {
		playBells(cap);
	}

	public void playBells(IRegeneration cap) {
		World world = cap.getPlayer().world;
		world.playSound(null, cap.getPlayer().getPosition(), TSounds.cloister_bell, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}
	
}
