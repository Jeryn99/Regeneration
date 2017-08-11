package com.afg.regeneration.superpower;

import com.afg.regeneration.Regeneration;
import lucraft.mods.lucraftcore.abilities.Ability;
import lucraft.mods.lucraftcore.superpower.Superpower;
import lucraft.mods.lucraftcore.superpower.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpower.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.util.LucraftCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
@Mod.EventBusSubscriber
public class TimelordHandler extends SuperpowerPlayerHandler
{
	public int regenCount = 0;
	public int regenTicks = 0;

	public TimelordHandler(EntityPlayer player, Superpower superpower) { super(player, superpower); }

	@Override public void onUpdate(TickEvent.Phase phase) {
		if(phase.equals(TickEvent.Phase.END))
			return;

		if(regenTicks > 0)
		{
			regenTicks++;
			if(player.getHealth() > 0.1f)
			player.setHealth(0.1f);

			if(player.world.isRemote)
				Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
		}

		if(regenTicks > 200){
			regenTicks = 0;
			player.setHealth(player.getMaxHealth());
			randomizeTraits(this);
			this.regenCount++;
		}
	}

	@Override public void onApplyPower() {
		randomizeTraits(this);
	}


	private static void randomizeTraits(SuperpowerPlayerHandler handler){
		handler.getAbilities().forEach(ability -> ability.setUnlocked(false));

		for(int i = 0; i < 3; i++){
			Ability a = null;
			while(a == null || a.isUnlocked())
				a = handler.getAbilities().get(handler.player.getRNG().nextInt(handler.getAbilities().size()));
			a.setUnlocked(true);
		}

		LucraftCoreUtil.sendSuperpowerUpdatePacketToAllPlayers(handler.player);

	}

	@Override public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound = super.writeToNBT(compound);
		compound.setInteger("regenCount", regenCount);
		compound.setInteger("regenTicks", regenTicks);
		return compound;
	}

	@Override public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		regenCount = compound.getInteger("regenCount");
		regenTicks = compound.getInteger("regenTicks");
	}


	@SubscribeEvent
	public static void onDeath(LivingDeathEvent e){
		if(e.getEntity() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) e.getEntity();
			if(SuperpowerHandler.hasSuperpower(player, Regeneration.timelord)){
				TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordHandler.class);
				e.setCanceled(true);
				if(handler.regenTicks == 0)
					handler.regenTicks = 1;
			}
		}
	}

	@SubscribeEvent
	public static void onHurt(LivingHurtEvent e){
		if(e.getEntity() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) e.getEntity();
			if(SuperpowerHandler.hasSuperpower(player, Regeneration.timelord)){
				TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordHandler.class);

				if(handler.regenTicks > 0)
				{
					e.setCanceled(true);
					return;
				}

				if(((EntityPlayer) e.getEntity()).getHealth() - e.getAmount() <= 0)
				{
					e.setCanceled(true);
					((EntityPlayer) e.getEntity()).setHealth(0.1f);
					if(handler.regenTicks == 0)
						handler.regenTicks = 1;
					LucraftCoreUtil.sendSuperpowerUpdatePacketToAllPlayers(player);
				}

			}
		}
	}
}
