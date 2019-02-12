package me.suff.regeneration.client.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.util.function.Supplier;

public class ConditionalSound implements ITickableSound {
	
	private final ISound sound;
	private final Supplier<Boolean> test;
	
	public ConditionalSound(ISound sound, Supplier<Boolean> test) {
		this.sound = sound;
		this.test = test;
	}
	
	@Override
	public boolean isDonePlaying() {
		return test.get();
	}
	
	
	@Override
	public ResourceLocation getSoundLocation() {
		return sound.getSoundLocation();
	}
	
	@Override
	public SoundEventAccessor createAccessor(SoundHandler handler) {
		return sound.createAccessor(handler);
	}
	
	@Override
	public Sound getSound() {
		return sound.getSound();
	}
	
	@Override
	public SoundCategory getCategory() {
		return sound.getCategory();
	}
	
	@Override
	public boolean canRepeat() {
		return sound.canRepeat();
	}
	
	@Override
	public boolean isPriority() {
		return false;
	}
	
	@Override
	public int getRepeatDelay() {
		return sound.getRepeatDelay();
	}
	
	@Override
	public float getVolume() {
		return sound.getVolume();
	}
	
	@Override
	public float getPitch() {
		return sound.getPitch();
	}
	
	@Override
	public float getX() {
		return 0;
	}
	
	@Override
	public float getY() {
		return 0;
	}
	
	@Override
	public float getZ() {
		return 0;
	}
	
	@Override
	public AttenuationType getAttenuationType() {
		return sound.getAttenuationType();
	}
	
	@Override
	public void tick() {
	
	}
}
