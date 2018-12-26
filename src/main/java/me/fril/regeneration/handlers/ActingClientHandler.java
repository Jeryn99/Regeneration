package me.fril.regeneration.handlers;

import me.fril.regeneration.client.sound.ConditionalSound;
import me.fril.regeneration.client.sound.MovingSoundPlayer;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.ActingForwarder.IActingHandler;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

class ActingClientHandler implements IActingHandler { //XXX feel free to rename this, I couldn't think of anything better
	
	public static final IActingHandler INSTANCE = new ActingClientHandler();
	
	private ActingClientHandler() {}
	
	//FUTURE Toasts: PlayerUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerations_left"), new TextComponentTranslation(getRegenerationsLeft() + ""), RegenState.REGENERATING);
	
	/** SOON test multiplayer sound handling with hydro
	 * 
	 * Is opening watch heard by others?
	 * Is transferring heard by others?
	 * Is critical heard by others?
	 * Is heartbeat heard by others?
	 * Make sure regeneration is heard by others
	 * Make sure hand-glow is heard by others
	 */
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		//never forwarded as per the documentation
	}
	
	@Override
	public void onEnterGrace(IRegeneration cap) {
		//SOON toast notification for entering grace
		
		Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundPlayer(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, true, ()->!cap.getState().isGraceful()));
	}
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		//SOON toast notification for finishing regeneration
	}
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		//SOON toast notification for triggering regeneration
		
		PlayerUtil.sendMessage(cap.getPlayer(), new TextComponentTranslation("regeneration.messages.remaining_regens.notification", cap.getRegenerationsLeft()), true);
		
		//PlayerUtil.playMovingSound(player, RegenObjects.Sounds.REGENERATION, SoundCategory.PLAYERS);
		Minecraft.getMinecraft().getSoundHandler().playSound(new ConditionalSound(PositionedSoundRecord.getRecord(RegenObjects.Sounds.REGENERATION, 1.0F, 0.5F), ()->cap.getState() != RegenState.REGENERATING));
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		//SOON toast notification for entering critical phase
		
		Minecraft.getMinecraft().getSoundHandler().playSound(new ConditionalSound(PositionedSoundRecord.getRecord(RegenObjects.Sounds.CRITICAL_STAGE, 1.0F, 0.5F), ()->cap.getState() != RegenState.GRACE_CRIT));
	}
	
}
