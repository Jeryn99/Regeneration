package me.fril.regeneration.handlers;

import org.lwjgl.input.Keyboard;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.client.RegenKeyBinds;
import me.fril.regeneration.client.sound.ConditionalSound;
import me.fril.regeneration.client.sound.MovingSoundPlayer;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.ClientUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

class ActingClientHandler implements IActingHandler {
	
	public static final IActingHandler INSTANCE = new ActingClientHandler();
	
	// TODO 'now a timelord' into toast
	
	private ActingClientHandler() {
	}
	
	/**
	 * SOON test multiplayer sound handling with hydro
	 * Is opening watch heard by others?
	 * Is transferring heard by others?
	 * Is critical heard by others?
	 * Is heartbeat heard by others? < DEAD
	 * Make sure hand-glow is heard by others < It is
	 */
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		// never forwarded as per the documentation
	}
	
	@Override
	public void onEnterGrace(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.enter_grace"), new TextComponentTranslation("regeneration.toast.enter_grace.sub", Keyboard.getKeyName(RegenKeyBinds.REGEN_NOW.getKeyCode()), (RegenConfig.grace.criticalPhaseLength + RegenConfig.grace.gracePhaseLength) / 60), cap.getState());
	}
	
	@Override
	public void onHandsStartGlowing(IRegeneration cap) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundPlayer(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing()));
	}
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerated"), new TextComponentTranslation("regeneration.toast.regenerations_left", cap.getRegenerationsLeft()), cap.getState());
	}
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundPlayer(cap.getPlayer(), RegenObjects.Sounds.REGENERATION_2, SoundCategory.PLAYERS, true, () -> cap.getState().equals(RegenState.REGENERATING)));
		cap.getSkinHandler().randomizeSkin(cap.getPlayer().world.rand);
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.enter_critical"), new TextComponentTranslation("regeneration.toast.enter_critical.sub", RegenConfig.grace.criticalPhaseLength / 60), cap.getState());
		Minecraft.getMinecraft().getSoundHandler().playSound(new ConditionalSound(PositionedSoundRecord.getRecord(RegenObjects.Sounds.CRITICAL_STAGE, 1.0F, 0.5F), () -> cap.getState() != RegenState.GRACE_CRIT));
	}

}
