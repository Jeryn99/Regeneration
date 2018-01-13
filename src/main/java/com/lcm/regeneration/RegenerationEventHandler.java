package com.lcm.regeneration;

import com.lcm.regeneration.superpower.TimelordSuperpower;
import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;

import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpowers.capabilities.CapabilitySuperpower;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegenerationEventHandler {
	
	@SubscribeEvent
	public static void onWorldLoaded(WorldEvent.Load e) {
		if (!e.getWorld().isRemote && RegenerationMod.getConfig().disableTraits) {
			for (EntityPlayer p : e.getWorld().playerEntities) if (SuperpowerHandler.hasSuperpower(p, TimelordSuperpower.INSTANCE))
				SuperpowerHandler.getSuperpowerPlayerHandler(p).getAbilities().forEach(ability -> ability.setUnlocked(false));
		}
	}
	
	@SubscribeEvent
	public static void onAttacked(LivingAttackEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer) || !SuperpowerHandler.hasSuperpower((EntityPlayer) e.getEntity(), TimelordSuperpower.INSTANCE)) return;
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if(player.getHealth() - e.getAmount() < 0 && SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class).regenerating && player.world.isRemote && Minecraft.getMinecraft().player.getUniqueID() == player.getUniqueID())
			Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
	}
	
	@SubscribeEvent
	public static void onHurt(LivingHurtEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer)) return;
		
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (player.getHealth() - e.getAmount() > 0 || !SuperpowerHandler.hasSuperpower(player, TimelordSuperpower.INSTANCE))
			return;
		
		TimelordSuperpowerHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class);
		
		if (handler.regenerating || player.posY < 0 || handler.regenerationsLeft <= 0) {
			SuperpowerHandler.removeSuperpower(player);
			((CapabilitySuperpower) player.getCapability(CapabilitySuperpower.SUPERPOWER_CAP, null)).superpowerData.removeTag(TimelordSuperpower.INSTANCE.getRegistryName().toString());
		} else if (handler.regenerationsLeft > 0) {
			e.setCanceled(true);
			handler.regenerating = true;
			SuperpowerHandler.syncToAll(player);
			
			player.setHealth(1.5f);
			player.setAir(300);
			player.getFoodStats().setFoodLevel(20);
			player.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 200, 1, false, false));
			player.extinguish();
			
			if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire) player.world.setBlockToAir(player.getPosition());
			
			String time = "" + (handler.timesRegenerated + 1);
			int lastDigit = handler.timesRegenerated;
			if (lastDigit > 20) while (lastDigit > 10)
				lastDigit -= 10;
			
			if (lastDigit < 3) time = time + StringHelper.translateToLocal("lcm-regen.messages.numsuffix."+lastDigit);
			else time = time + StringHelper.translateToLocal("lcm-regen.messages.numsuffix.ext");
			
			player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-regen.messages.regenLeftExt", time, (handler.regenerationsLeft - 1))), true);
			player.world.playSound(null, player.posX, player.posY, player.posZ, RegenerationSounds.SHORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
		}
	}
}
