package me.fril.regeneration.client.sound;

import java.util.function.Supplier;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

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
	public void update() {
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
	public float getXPosF() {
		return sound.getXPosF();
	}
	
	@Override
	public float getYPosF() {
		return sound.getYPosF();
	}
	
	@Override
	public float getZPosF() {
		return sound.getZPosF();
	}
	
	@Override
	public AttenuationType getAttenuationType() {
		return sound.getAttenuationType();
	}
	
}
