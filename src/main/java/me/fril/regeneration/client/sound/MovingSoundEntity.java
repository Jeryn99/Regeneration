package me.fril.regeneration.client.sound;

import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.util.RegenUtil;
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
	
	private final Entity player;
	private final Supplier<Boolean> stopCondition;
	protected boolean donePlaying = false;
	
	public MovingSoundEntity(Entity playerIn, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, Supplier<Boolean> stopCondition, float volumeSfx) {
		super(soundIn, categoryIn);
		this.player = playerIn;
		this.stopCondition = stopCondition;
		super.repeat = repeat;
		volume = volumeSfx;
	}
	
	@Override
	public void update() {
		
		if (stopCondition.get() || player.isDead) {
			donePlaying = true;
		}
		
		//I promise this is the only case specific thing I am putting in here ~ Sub
		if (sound.getSoundLocation().equals(RegenObjects.Sounds.G_HUM.getRegistryName())) {
			volume = RegenUtil.randFloat(1.5F, 6F);
		}
		
		super.xPosF = (float) player.posX;
		super.yPosF = (float) player.posY;
		super.zPosF = (float) player.posZ;
		
	}
	
	@Override
	public boolean isDonePlaying() {
		return donePlaying;
	}
	
}
