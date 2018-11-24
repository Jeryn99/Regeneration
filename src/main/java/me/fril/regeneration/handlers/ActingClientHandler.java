package me.fril.regeneration.handlers;

import me.fril.regeneration.client.sound.ConditionalSound;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.ActingForwarder.IActingHandler;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.text.TextComponentTranslation;

class ActingClientHandler implements IActingHandler { //XXX feel free to rename this, I couldn't think of anything better
	
	//FUTURE Toasts: PlayerUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerations_left"), new TextComponentTranslation(getRegenerationsLeft() + ""), RegenState.REGENERATING);
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		
	}
	
	@Override
	public void onEnterGrace(IRegeneration cap) {
		//SOON toast notification for entering grace
	}
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		//SOON toast notification for finishing regeneration
	}
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		//SOON toast notification for triggering regeneration
		PlayerUtil.sendHotbarMessage(cap.getPlayer(), new TextComponentTranslation("regeneration.messages.remaining_regens.notification", cap.getRegenerationsLeft()), true);
		
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		//SOON toast notification for entering critical phase
		//SOON red vingette in critical phase
		
		//PlayerUtil.playMovingSound(ev.player, RegenObjects.Sounds.CRITICAL_STAGE, SoundCategory.PLAYERS); NOW should be a player-only sound
		Minecraft.getMinecraft().getSoundHandler().playSound(new ConditionalSound(PositionedSoundRecord.getRecord(RegenObjects.Sounds.CRITICAL_STAGE, 1.0F, 2.0F), ()->cap.getState() != RegenState.GRACE_CRIT));
	}
	
}
