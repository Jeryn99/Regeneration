package me.fril.regeneration.compat.lucraft;

import java.util.Random;

import lucraft.mods.lucraftcore.materials.potions.PotionRadiation;
import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.lucraftcore.sizechanging.capabilities.ISizeChanging;
import lucraft.mods.lucraftcore.util.abilitybar.AbilityBarHandler;
import me.fril.regeneration.RegenConfig.ModIntegrations;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.IActingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LucraftCoreHandler implements IActingHandler {
	
	public static void registerEntry() {
		AbilityBarHandler.registerProvider(new LCCoreBarEntry());
	}
	
	public static void registerEventBus() {
		MinecraftForge.EVENT_BUS.register(new LucraftCoreHandler());
	}
	
	public static float randFloat(float min, float max) {
		Random rand = new Random();
		float result = rand.nextFloat() * (max - min) + min;
		return result;
	}
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		
	}
	
	@Override
	public void onEnterGrace(IRegeneration cap) {
		
	}
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		
	}
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		if (ModIntegrations.lucraftcore.lucraftcoreSizeChanging) {
			EntityPlayer player = cap.getPlayer();
			ISizeChanging sizeCap = player.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null);
			sizeCap.setSize(randFloat(ModIntegrations.lucraftcore.sizeChangingMin, ModIntegrations.lucraftcore.sizeChangingMax));
		}
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		
	}
	
	@SubscribeEvent
	public void onHurt(LivingHurtEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			IRegeneration data = CapabilityRegeneration.getForPlayer(player);
			boolean flag = data.canRegenerate() && e.getSource() == PotionRadiation.RADIATION && ModIntegrations.lucraftcore.immuneToRadiation;
			e.setCanceled(flag);
		}
	}
}
