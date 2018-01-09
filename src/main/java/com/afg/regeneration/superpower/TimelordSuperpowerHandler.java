package com.afg.regeneration.superpower;

import com.afg.regeneration.sounds.SoundReg;
import com.afg.regeneration.traits.negative.INegativeTrait;

import lucraft.mods.lucraftcore.LCConfig;
import lucraft.mods.lucraftcore.karma.KarmaHandler;
import lucraft.mods.lucraftcore.karma.KarmaStat;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
@Mod.EventBusSubscriber
public class TimelordSuperpowerHandler extends SuperpowerPlayerHandler {
	public int regenCount = 0;
	public int regenTicks = 0;
	
	public TimelordSuperpowerHandler(ISuperpowerCapability cap, Superpower superpower) {
		super(cap, superpower);
	}
	
	@Override
	public void onUpdate(TickEvent.Phase phase) {
		if (phase.equals(TickEvent.Phase.END)) return;
		
		if (regenTicks > 0 && regenTicks < 200) {
			regenTicks++;
			
			if (cap.getPlayer().world.isRemote && Minecraft.getMinecraft().player.getName().equals(cap.getPlayer().getName())) 
				Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
			
			if (!cap.getPlayer().world.isRemote && regenTicks > 100) {
				cap.getPlayer().extinguish();
				if (cap.getPlayer().world.getBlockState(cap.getPlayer().getPosition()).getBlock() instanceof BlockFire) cap.getPlayer().world.setBlockToAir(cap.getPlayer().getPosition());
				
				double x = cap.getPlayer().posX + cap.getPlayer().getRNG().nextGaussian() * 2;
				double y = cap.getPlayer().posY + 0.5 + cap.getPlayer().getRNG().nextGaussian() * 2;
				double z = cap.getPlayer().posZ + cap.getPlayer().getRNG().nextGaussian() * 2;
				
				cap.getPlayer().world.newExplosion(cap.getPlayer(), x, y, z, 1, true, false);
			}
		}
		
		if (regenTicks == 200) {
			regenTicks = 0;
			cap.getPlayer().setHealth(cap.getPlayer().getMaxHealth());
			cap.getPlayer().addPotionEffect(new PotionEffect(Potion.getPotionById(10), 3600, 3, false, false));
			TimelordSuperpowerHandler.randomizeTraits(this);
			regenCount++;
			
			if (cap.getPlayer().world.isRemote && Minecraft.getMinecraft().player.getName().equals(cap.getPlayer().getName())) Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
		}
	}
	
	@Override
	public void onApplyPower() {
		TimelordSuperpowerHandler.randomizeTraits(this);
	}
	
	private static void randomizeTraits(SuperpowerPlayerHandler handler) {
		handler.getAbilities().forEach(ability -> ability.setUnlocked(false));
		
		// Reset Karma
		if (LCConfig.modules.karma) for (KarmaStat karmaStat : KarmaStat.getKarmaStats())
			KarmaHandler.setKarmaStat(handler.getPlayer(), karmaStat, 0);
		
		for (int i = 0; i < 2; i++) {
			Ability a = null;
			while (a == null || a instanceof INegativeTrait || a.isUnlocked())
				a = handler.getAbilities().get(handler.getPlayer().getRNG().nextInt(handler.getAbilities().size()));
			a.setUnlocked(true);
		}
		
		for (int i = 0; i < 2; i++) {
			Ability a = null;
			while (a == null || a.isUnlocked() || !(a instanceof INegativeTrait) || TimelordSuperpowerHandler.abilityIsUnlocked(handler, ((INegativeTrait) a).getPositiveTrait()))
				a = handler.getAbilities().get(handler.getPlayer().getRNG().nextInt(handler.getAbilities().size()));
			a.setUnlocked(true);
		}
		
		SuperpowerHandler.syncToAll(handler.getPlayer());
		
		String s = "";
		for (Ability ability : handler.getAbilities()) {
			if (ability.isUnlocked()) {
				if (s.equals("")) s = ability.getDisplayName().substring(7);
				else s = s + ", " + ability.getDisplayName().substring(7);
			}
		}
		s = s + ".";
		
		handler.getPlayer().sendStatusMessage(new TextComponentString("You've gotten a new life, with new traits: " + s), true);
	}
	
	private static boolean abilityIsUnlocked(SuperpowerPlayerHandler handler, Class<? extends Ability> ability) {
		for (Ability ability1 : handler.getAbilities()) if (ability.equals(ability1.getClass())) return ability1.isUnlocked();
		return false;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("regenCount", regenCount);
		compound.setInteger("regenTicks", regenTicks);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		regenCount = compound.getInteger("regenCount");
		regenTicks = compound.getInteger("regenTicks");
	}
	
	@SubscribeEvent
	public static void onAttacked(LivingAttackEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (!SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.instance)) return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
		if ((e.getSource().isExplosion() || e.getSource().isFireDamage()) && handler.regenTicks >= 100) e.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onDeath(LivingDeathEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (!SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.instance)) return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
		handler.regenTicks = 0;
	}
	
	@SubscribeEvent
	public static void onHurt(LivingHurtEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer) || ((EntityPlayer) e.getEntity()).getHealth() - e.getAmount() > 0) return;
		
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (!SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.instance)) return;
			
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
			
			
		if (handler.regenCount < 12 && handler.regenTicks == 0) {
			e.setCanceled(true);
			((EntityPlayer) e.getEntity()).setHealth(1.5f);
			((EntityPlayer) e.getEntity()).addPotionEffect(new PotionEffect(Potion.getPotionById(10), 200, 1, false, false));
			if (handler.regenTicks == 0) handler.regenTicks = 1;
			SuperpowerHandler.syncToAll(player);
			
			String time = "" + (handler.regenCount + 1);
			switch (handler.regenCount + 1) {
				case 1:
					time = time + "st";
					break;
				case 2:
					time = time + "nd";
					break;
				case 3:
					time = time + "rd";
					break;
				default:
					time = time + "th";
					break;
			}
			handler.getPlayer().sendStatusMessage(new TextComponentString("You're regenerating for the " + time + " time, you have " + (11 - handler.regenCount) + " regenerations left."), true);
			player.world.playSound(null, player.posX, player.posY, player.posZ, SoundReg.SHORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		} else if (handler.regenCount >= 12) {
			handler.getPlayer().sendStatusMessage(new TextComponentString("You're out of regenerations. You're dying for real this time."), true);
			SuperpowerHandler.removeSuperpower(handler.getPlayer());
		}
	}
}
