package me.suff.regeneration.handlers;

import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.util.ClientUtil;
import me.suff.regeneration.util.RegenState;
import me.suff.regeneration.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.IOException;

class ActingClientHandler implements IActingHandler {
	
	public static final IActingHandler INSTANCE = new ActingClientHandler();
	
	// TODO 'now a timelord' into toast
	
	private ActingClientHandler() {
	}
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		// never forwarded as per the documentation
	}
	
	@Override
	public void onEnterGrace(IRegeneration cap) {
		ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HEART_BEAT.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().isGraceful(), 0.2F);
		ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.GRACE_HUM.getRegistryName(), SoundCategory.AMBIENT, true, () -> cap.getState() != RegenState.GRACE, 1.5F);
	}
	
	@Override
	public void onHandsStartGlowing(IRegeneration cap) {
		ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 1.0F);
	}
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerated"), new TextComponentTranslation("regeneration.toast.regenerations_left", cap.getRegenerationsLeft()));
		
		if (RegenConfig.CONFIG.changeHand.get() && cap.getPlayer().getUniqueID() == Minecraft.getInstance().player.getUniqueID()) {
			Minecraft.getInstance().gameSettings.mainHand = RegenUtil.randomEnum(EnumHandSide.class);
			Minecraft.getInstance().gameSettings.sendSettingsToServer();
		}
		
	}
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		if (Minecraft.getInstance().player.getUniqueID().equals(cap.getPlayer().getUniqueID())) {
			try {
				SkinChangingHandler.sendSkinUpdate(cap.getPlayer().world.rand, cap.getPlayer());
			} catch (IOException e) {
				RegenerationMod.LOG.error(e.getMessage());
			}
		}
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.enter_critical"), new TextComponentTranslation("regeneration.toast.enter_critical.sub", RegenConfig.CONFIG.criticalPhaseLength.get() / 60));
		ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.CRITICAL_STAGE.getRegistryName(), SoundCategory.PLAYERS, true, () -> cap.getState() != RegenState.GRACE_CRIT, 1.0F);
	}
	
}
