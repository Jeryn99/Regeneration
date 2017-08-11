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
import net.minecraft.util.text.TextComponentString;
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
			Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;

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

		String s = "";
		for (Ability ability : handler.getAbilities())
		{
			if(ability.isUnlocked())
			{
				if (s.equals(""))
					s = ability.getDisplayName().substring(7);
				else
					s = s + ", " + ability.getDisplayName().substring(7);
			}
		}
		s = s + ".";

		handler.player.sendStatusMessage(new TextComponentString("You've gotten a new life, with new traits: " + s), true);
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
					if(handler.regenCount < 12)
					{
						e.setCanceled(true);
						((EntityPlayer) e.getEntity()).setHealth(0.1f);
						if (handler.regenTicks == 0)
							handler.regenTicks = 1;
						LucraftCoreUtil.sendSuperpowerUpdatePacketToAllPlayers(player);

						String time = "" + (handler.regenCount + 1);
						switch (handler.regenCount + 1){
						case 1: time = time + "st";
							break;
						case 2: time = time + "nd";
							break;
						case 3: time = time + "rd";
							break;
						default: time = time + "th";
							break;
						}
						handler.player.sendStatusMessage(new TextComponentString("You're regenerating for the " + time + " time, you have " + (11 - handler.regenCount) + " regenerations left."), true);

					} else {
						handler.player.sendStatusMessage(new TextComponentString("You're out of regenerations. You're dying for real this time."), true);
					}
				}

			}
		}
	}
}
