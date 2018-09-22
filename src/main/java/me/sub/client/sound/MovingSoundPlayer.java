package me.sub.client.sound;

import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import me.sub.common.init.RObjects;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class MovingSoundPlayer extends MovingSound {

    private final EntityPlayer player;
    private float distance = 0.0F;
    private SoundEvent soundCheck;

    public MovingSoundPlayer(EntityPlayer player, SoundEvent soundIn, SoundCategory categoryIn) {
        super(soundIn, categoryIn);
        this.player = player;
        repeat = true;
        repeatDelay = 0;
        soundCheck = soundIn;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    @Override
    public void update() {
        IRegeneration regenInfo = CapabilityRegeneration.get(player);

        if (soundCheck.getSoundName().equals(RObjects.Sounds.HAND_GLOW.getSoundName())) {
            volume = 0.3F;
            if (!regenInfo.isGlowing() || regenInfo.getTicksRegenerating() == 2) {
                donePlaying = true;
            }
        }

        if (soundCheck.getSoundName().equals(RObjects.Sounds.HEART_BEAT.getSoundName())) {
            if (!regenInfo.isInGracePeriod()) {
                donePlaying = true;
            }
        }

        if (soundCheck.getSoundName().equals(RObjects.Sounds.REGEN_1.getSoundName())) {
            if (regenInfo.getTicksRegenerating() == 199) {
                donePlaying = true;
            }
        }

        if (player.isDead) {
            donePlaying = true;
        } else {
            xPosF = (float) player.posX;
            yPosF = (float) player.posY;
            zPosF = (float) player.posZ;

            distance = MathHelper.clamp(distance + 0.0025F, 0.0F, 1.0F);
            volume = 1.0F;
        }
    }
}
