package me.suff.regeneration.client.sound;

import me.suff.regeneration.handlers.RegenObjects;
import me.suff.regeneration.util.RegenUtil;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
//FIXME sound doesn't stop when dying mid-regen
public class MovingSoundEntity extends MovingSound {
	
	private final Entity entity;
	private final Supplier<Boolean> stopCondition;
	private boolean donePlaying = false;
	
	public MovingSoundEntity(Entity playerIn, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, Supplier<Boolean> stopCondition, float volumeSfx) {
		super(soundIn, categoryIn);
		this.entity = playerIn;
		this.stopCondition = stopCondition;
		super.repeat = repeat;
		volume = volumeSfx;
	}
	
	@Override
	public void tick() {
		
		if (stopCondition.get() || !entity.isAlive()) {
			setDonePlaying();
		}
		
		//I promise this is the only case specific thing I am putting in here ~ Sub
		if (sound.getSoundLocation().equals(RegenObjects.Sounds.GRACE_HUM.getRegistryName())) {
			volume = RegenUtil.randFloat(1.5F, 6F);
		}
		
		super.x = (float) entity.posX;
		super.y = (float) entity.posY;
		super.z = (float) entity.posZ;
		
	}
	
	//Explanation: http://www.minecraftforge.net/forum/topic/26645-solvedmovingsound-itickablesound-and-soundmanager-starting-stopping-sounds/
	public void setDonePlaying() {
		this.repeat = false;
		this.donePlaying = true;
		this.repeatDelay = 0;
	}
	
	
	@Override
	public boolean canRepeat() {
		return this.repeat;
	}
	
	@Override
	public float getVolume() {
		return this.volume;
	}
	
	@Override
	public float getPitch() {
		return this.pitch;
	}
	
	@Override
	public boolean isDonePlaying() {
		return donePlaying;
	}
	
	
	@Override
	public int getRepeatDelay() {
		return this.repeatDelay;
	}
	
	@Override
	public AttenuationType getAttenuationType() {
		return AttenuationType.LINEAR;
	}
	
}
