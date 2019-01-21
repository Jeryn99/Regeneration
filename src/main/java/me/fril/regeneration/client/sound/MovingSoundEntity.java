package me.fril.regeneration.client.sound;

import java.util.function.Supplier;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

/**
 * Created by Sub
 * on 20/09/2018.
 */
//FIXME sound doesn't stop when dying mid-regen
//FIXME I'm not sure, but it's possible that the hand-glow sound continues when the regen is triggered
//FIXME citical & hand glow sound conflict
//FIXME sometimes the hand glow sound never starts at all for a second interval
//TODO add heartbeat sound in grace (or just critical?)
public class MovingSoundEntity extends MovingSound {
	
	private final Entity player;
	private final Supplier<Boolean> stopCondition;
	
	public MovingSoundEntity(Entity playerIn, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, Supplier<Boolean> stopCondition) {
		super(soundIn, categoryIn);
		
		this.player = playerIn;
		this.stopCondition = stopCondition;
		super.repeat = repeat;
	}
	
	// FIXME Sometimes ConcurrentModificationException's in subtitle renderer, probably due to a race condition because we're modifying it here and in ConditionalSound
	@Override
	public void update() {
		super.xPosF = (float) player.posX;
		super.yPosF = (float) player.posY;
		super.zPosF = (float) player.posZ;
	}
	
	@Override
	public boolean isDonePlaying() {
		return player.isDead || stopCondition.get();
	}
	
}
