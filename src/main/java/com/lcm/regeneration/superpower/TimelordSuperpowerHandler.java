package com.lcm.regeneration.superpower;

import com.lcm.regeneration.RegenerationMod;
import com.lcm.regeneration.traits.negative.INegativeTrait;
import lucraft.mods.lucraftcore.LCConfig;
import lucraft.mods.lucraftcore.karma.KarmaHandler;
import lucraft.mods.lucraftcore.karma.KarmaStat;
import lucraft.mods.lucraftcore.superpowers.Superpower;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.SuperpowerPlayerHandler;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.capabilities.CapabilitySuperpower;
import lucraft.mods.lucraftcore.superpowers.capabilities.ISuperpowerCapability;
import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.stream.Collectors;

/** Created by AFlyingGrayson on 8/7/17 */
@Mod.EventBusSubscriber
public class TimelordSuperpowerHandler extends SuperpowerPlayerHandler {
	public int regenerationsLeft, timesRegenerated, regenTicks;
	public boolean regenerating = false;
	
	public TimelordSuperpowerHandler(ISuperpowerCapability cap, Superpower superpower) {
		super(cap, superpower);
	}
	
	@Override
	public void onUpdate(TickEvent.Phase phase) {
		if (phase.equals(TickEvent.Phase.END)) return;
		
		EntityPlayer player = cap.getPlayer();
		if (player.world.isRemote) {
			// Client Behavior
			if (regenTicks == 0 && regenerating) regenTicks = 1;
			if (regenTicks > 0) {
				if (Minecraft.getMinecraft().player.getUniqueID() == player.getUniqueID()) Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
				regenTicks++;
			}
			if (regenTicks >= 200 && !regenerating) {
				regenTicks = 0;
				if (Minecraft.getMinecraft().player.getUniqueID() == player.getUniqueID()) Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
			}
		} else {
			// Server Behavior
			if (regenTicks == 0 && regenerating)
				regenTicks = 1;
			else if (regenTicks > 0 && regenTicks < 200) {
				regenTicks++;
				if (!player.world.isRemote && regenTicks > 100) {
					player.extinguish();
					if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire) player.world.setBlockToAir(player.getPosition());
					
					double x = player.posX + player.getRNG().nextGaussian() * 2;
					double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
					double z = player.posZ + player.getRNG().nextGaussian() * 2;
					
					player.world.newExplosion(player, x, y, z, 1, true, false);
					for (BlockPos bs : BlockPos.getAllInBox(player.getPosition().north().west(), player.getPosition().south().east())) {
						if (player.world.getBlockState(bs).getBlock() instanceof BlockFire) player.world.setBlockToAir(bs);
					}
				}
			} else if (regenTicks >= 200) {
				regenerating = false;
				regenTicks = 0;
				player.setHealth(player.getMaxHealth());
				player.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 3600, 3, false, false));
				TimelordSuperpowerHandler.randomizeTraits(this);
				regenerationsLeft--;
				timesRegenerated++;
				cap.syncToAll();
			}
		}
	}
	
	@Override
	public void onApplyPower() {
		this.getAbilities().clear();
		TimelordSuperpower.INSTANCE.addDefaultAbilities(this.getPlayer(), this.getAbilities());
		TimelordSuperpowerHandler.randomizeTraits(this);
		this.regenerationsLeft = 12;
		SuperpowerHandler.syncToAll(this.getPlayer());
	}
	
	protected static void randomizeTraits(SuperpowerPlayerHandler handler) {
		// Reset Karma
		if (LCConfig.modules.karma) for (KarmaStat karmaStat : KarmaStat.getKarmaStats())
			KarmaHandler.setKarmaStat(handler.getPlayer(), karmaStat, 0);
		if (RegenerationMod.getConfig().disableTraits) return;
		
		handler.getAbilities().forEach(ability -> ability.setUnlocked(false));
		
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
		
		String s = "";
		for (Ability ability : handler.getAbilities()) {
			if (ability.isUnlocked()) {
				if (s.equals(""))
					s = ability.getDisplayName().substring(7);
				else
					s = s + ", " + ability.getDisplayName().substring(7);
			}
		}
		
		handler.getPlayer().sendStatusMessage(new TextComponentString("You've gotten a new life, with new traits: " + s + "."), true);
	}
	
	protected static boolean abilityIsUnlocked(SuperpowerPlayerHandler handler, Class<? extends Ability> ability) {
		for (Ability ability1 : handler.getAbilities())
			if (ability.equals(ability1.getClass())) return ability1.isUnlocked();
		return false;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setInteger("regenerationsLeft", regenerationsLeft);
		compound.setInteger("timesRegenerated", timesRegenerated);
		compound.setBoolean("regenerating", regenerating);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		regenerationsLeft = compound.getInteger("regenerationsLeft");
		timesRegenerated = compound.getInteger("timesRegenerated");
		regenerating = compound.getBoolean("regenerating");
		
		ArrayList<Ability> abilities = new ArrayList<>();
		abilities.addAll(getAbilities());
		abilities.removeAll(getAbilities().stream().filter(ability -> !ability.isUnlocked()).collect(Collectors.toCollection(ArrayList::new)));
		
		this.getAbilities().clear();
		((CapabilitySuperpower) getPlayer().getCapability(CapabilitySuperpower.SUPERPOWER_CAP, null)).superpowerData.getCompoundTag(TimelordSuperpower.INSTANCE.getRegistryName().toString()).removeTag("Abilities");
		
		this.getAbilities().addAll(abilities);
	}
}
