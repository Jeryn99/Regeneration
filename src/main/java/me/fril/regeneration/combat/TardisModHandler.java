package me.fril.regeneration.combat;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.types.TypeFiery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tardis.mod.common.dimensions.WorldProviderTardis;
import net.tardis.mod.common.entities.controls.ControlDoor;
import net.tardis.mod.common.systems.TardisSystems;
import net.tardis.mod.common.tileentity.TileEntityTardis;

import java.util.Random;

public class TardisModHandler {
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new TardisModHandler());
	}
	
	@SubscribeEvent
	public void onRegen(LivingEvent.LivingUpdateEvent e) {
		
		if (e.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntity();
			IRegeneration playercap = CapabilityRegeneration.getForPlayer(player);
			World world = player.world;
			
			if (playercap.getType() instanceof TypeFiery) {
				TypeFiery fiery = (TypeFiery) playercap.getType();
				long aniProg = fiery.getAnimationTicks();
				if (world.provider instanceof WorldProviderTardis && aniProg > 10 && aniProg < 12) {
					world.loadedTileEntityList.forEach(tileEntity -> damageTardis(tileEntity, player));
				}
			}
			
		}
	}
	
	private void damageTardis(TileEntity tileEntity, EntityPlayer player) {
		if (tileEntity instanceof TileEntityTardis && player.getDistanceSq(tileEntity.getPos()) == 10) {
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
	}
	
	
}
