package me.swirtzly.regeneration.client;

import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.RegenUtil;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MovingSoundBase extends MovingSound {

    private final Object entity;
    private final Supplier<Boolean> stopCondition;
    private boolean donePlaying = false;

    public MovingSoundBase(Object object, SoundEvent soundIn, SoundCategory categoryIn, boolean repeat, Supplier<Boolean> stopCondition, float volumeSfx) {
        super(soundIn, categoryIn);
        this.entity = object;
        this.stopCondition = stopCondition;
        super.repeat = repeat;
        volume = volumeSfx;
    }

    @Override
    public void update() {

        if (entity instanceof Entity) {
            Entity entityObject = (Entity) entity;
            if (stopCondition.get() || entityObject.isDead) {
                setDonePlaying();
            }

            //I promise this is the only case specific thing I am putting in here ~ swirtzly
            if (sound.getSoundLocation().equals(RegenObjects.Sounds.GRACE_HUM.getRegistryName())) {
                volume = RegenUtil.randFloat(1.5F, 6F);
            }

            super.xPosF = (float) entityObject.posX;
            super.yPosF = (float) entityObject.posY;
            super.zPosF = (float) entityObject.posZ;
        }

        if (entity instanceof TileEntity) {
            TileEntity tileObject = (TileEntity) entity;
            BlockPos pos = tileObject.getPos();
            super.xPosF = (float) pos.getX();
            super.yPosF = (float) pos.getY();
            super.zPosF = (float) pos.getZ();
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
