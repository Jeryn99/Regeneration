package com.afg.regeneration.superpower;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
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
			
			if (!cap.getPlayer().world.isRemote && Minecraft.getMinecraft().player.getName().equals(cap.getPlayer().getName()))
				Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
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
}
