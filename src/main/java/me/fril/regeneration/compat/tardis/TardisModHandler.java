package me.fril.regeneration.compat.tardis;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.types.TypeFiery;
import me.fril.regeneration.handlers.IActingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.tardis.mod.common.dimensions.WorldProviderTardis;
import net.tardis.mod.common.entities.controls.ControlDoor;
import net.tardis.mod.common.sounds.TSounds;
import net.tardis.mod.common.systems.SystemDimension;
import net.tardis.mod.common.systems.TardisSystems;
import net.tardis.mod.common.tileentity.TileEntityTardis;

import java.util.Random;

public class TardisModHandler implements IActingHandler {
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		playBells(cap, false);
	}
	
	@Override
	public void onEnterGrace(IRegeneration cap) {
		if (cap.getPlayer().world.provider instanceof WorldProviderTardis) {
			playBells(cap, true);
		}
	}
	
	@Override
	public void onHandsStartGlowing(IRegeneration cap) {
		if (cap.getPlayer().world.provider instanceof WorldProviderTardis) {
			playBells(cap, true);
		}
	}
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		if (cap.getPlayer().world.provider instanceof WorldProviderTardis) {
			playBells(cap, true);
		}
	}
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		if (cap.getType() instanceof TypeFiery) {
			damageTardisInRange(cap.getPlayer());
		}
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		if (cap.getPlayer().world.provider instanceof WorldProviderTardis) {
			playBells(cap, true);
		}
	}
	
	private void playBells(IRegeneration cap, boolean force) {
		if (cap.getPlayer().ticksExisted % 1200 == 0 && cap.getPlayer().world.provider instanceof WorldProviderTardis || force) {
			cap.getPlayer().world.playSound(null, cap.getPlayer().getPosition(), TSounds.cloister_bell, SoundCategory.BLOCKS, 1, 1);
		}
	}
	
	private void damageTardisInRange(EntityPlayer player) {
		if (!RegenConfig.modIntegrations.tardisMod.damageTardis)
			return;
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
						tileEntityTardis.getWorld().getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().expand(45, 45, 45)).forEach(player1 -> {
							if (player1 instanceof EntityPlayerMP) {
								EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player1;
								BlockPos tilePos = tileEntityTardis.getPos();
								entityPlayerMP.connection.sendPacket(new SPacketParticles(EnumParticleTypes.FLAME, true, tilePos.getX(), tilePos.getY(), tilePos.getZ(), 3, 3, 3, 2, 10));
							}
						});
					}
				}
			}
			
			if (tileEntityTardis.isInFlight()) {
				tileEntityTardis.crash(true);
			}
		}
	}
}
