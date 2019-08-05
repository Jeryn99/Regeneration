package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.client.rendering.types.TypeLayFadeRenderer;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundEvent;

public class TypeElixir implements IRegenType<TypeLayFadeRenderer> {

    @Override
    public int getAnimationLength() {
        return 350;
    }

    @Override
    public TypeLayFadeRenderer getRenderer() {
        return TypeLayFadeRenderer.INSTANCE;
    }

    @Override
    public void onStartRegeneration(EntityPlayer player, IRegeneration capability) {

    }

    @Override
    public void onUpdateMidRegen(EntityPlayer player, IRegeneration capability) {
        if (!player.world.isRemote) {
            PlayerUtil.setPerspective((EntityPlayerMP) player, true, false);
        }
    }

    @Override
    public void onFinishRegeneration(EntityPlayer player, IRegeneration capability) {
        PlayerUtil.setPerspective((EntityPlayerMP) player, false, true);
    }

    @Override
    public double getAnimationProgress(IRegeneration cap) {
        return Math.min(1, cap.getAnimationTicks() / (double) getAnimationLength());
    }

    @Override
    public TypeHandler.RegenType getTypeID() {
        return TypeHandler.RegenType.LAY_FADE;
    }

    private SoundEvent[] soundEvents = new SoundEvent[]{RegenObjects.Sounds.HAND_GLOW};

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return soundEvents;
    }

}
