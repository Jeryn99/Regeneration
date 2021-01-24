package me.swirtzly.regen.util.sound;

import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.util.RegenUtil;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class MovingSound extends TickableSound {

    private final Object entity;
    private final Supplier< Boolean > stopCondition;
    private boolean donePlaying = false;

    public MovingSound(Object object, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, Supplier< Boolean > stopCondition, float volumeSfx) {
        super(soundIn, categoryIn);
        this.entity = object;
        this.stopCondition = stopCondition;
        super.repeat = repeat;
        volume = volumeSfx;
    }

    @Override
    public void tick() {

        if (entity instanceof Entity) {
            Entity entityObject = (Entity) entity;
            if (stopCondition.get() || !entityObject.isAlive()) {
                setDonePlaying();
            }

            // I promise this is the only case specific thing I am putting in here ~ swirtzly
            if (sound.getSoundLocation().equals(RSounds.GRACE_HUM.get().getRegistryName())) {
                volume = RegenUtil.randFloat(1.5F, 6F);
            }

            super.x = (float) entityObject.getPosX();
            super.y = (float) entityObject.getPosY();
            super.z = (float) entityObject.getPosZ();
        }

        if (entity instanceof TileEntity) {
            TileEntity tileObject = (TileEntity) entity;
            BlockPos pos = tileObject.getPos();
            super.x = (float) pos.getX();
            super.y = (float) pos.getY();
            super.z = (float) pos.getZ();
        }

    }

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

