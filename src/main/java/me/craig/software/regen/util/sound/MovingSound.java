package me.craig.software.regen.util.sound;

import me.craig.software.regen.util.RegenUtil;
import me.craig.software.regen.common.objects.RSounds;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class MovingSound extends TickableSound {

    private final Object entity;
    private final Supplier<Boolean> stopCondition;
    private boolean donePlaying = false;

    public MovingSound(Object object, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, Supplier<Boolean> stopCondition, float volumeSfx) {
        super(soundIn, categoryIn);
        this.entity = object;
        this.stopCondition = stopCondition;
        super.looping = repeat;
        volume = volumeSfx;
    }

    @Override
    public void tick() {

        if (entity instanceof Entity) {
            Entity entityObject = (Entity) entity;
            if (stopCondition.get() || !entityObject.isAlive()) {
                setDonePlaying();
            }

            // I promise this is the only case specific thing I am putting in here ~ Craig
            if (sound.getLocation().equals(RSounds.GRACE_HUM.get().getRegistryName())) {
                volume = RegenUtil.randFloat(1.5F, 6F);
            }

            super.x = (float) entityObject.getX();
            super.y = (float) entityObject.getY();
            super.z = (float) entityObject.getZ();
        }

        if (entity instanceof TileEntity) {
            TileEntity tileObject = (TileEntity) entity;
            BlockPos pos = tileObject.getBlockPos();
            super.x = (float) pos.getX();
            super.y = (float) pos.getY();
            super.z = (float) pos.getZ();
        }

    }

    public void setDonePlaying() {
        this.looping = false;
        this.donePlaying = true;
        this.delay = 0;
    }

    @Override
    public boolean isLooping() {
        return this.looping;
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
    public boolean isStopped() {
        return donePlaying;
    }

    @Override
    public int getDelay() {
        return this.delay;
    }

    @Override
    public AttenuationType getAttenuation() {
        return AttenuationType.LINEAR;
    }
}

