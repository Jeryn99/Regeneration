package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.rendering.types.TypeLayFadeRenderer;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;

public class TypeLayFade implements RegenType {

    private SoundEvent[] soundEvents = new SoundEvent[]{RegenObjects.Sounds.HAND_GLOW.get()};

    @Override
    public int getAnimationLength() {
        return 350;
    }

    @Override
    public TypeLayFadeRenderer getRenderer() {
        return TypeLayFadeRenderer.INSTANCE;
    }

    @Override
    public void onStartRegeneration(IRegen capability) {

    }

    @Override
    public void onUpdateMidRegen(IRegen capability) {
        if (capability.getLivingEntity() instanceof ServerPlayerEntity) {
            LivingEntity player = capability.getLivingEntity();
            if (!player.world.isRemote) {
                PlayerUtil.setPerspective((ServerPlayerEntity) player, true, false);
            }
        }
    }

    @Override
    public void onFinishRegeneration(IRegen capability) {
        if (capability.getLivingEntity() instanceof ServerPlayerEntity) {
            LivingEntity player = capability.getLivingEntity();
            if (!player.world.isRemote) {
                PlayerUtil.setPerspective((ServerPlayerEntity) player, false, true);
            }
        }
    }

    @Override
    public double getAnimationProgress(IRegen cap) {
        return Math.min(1, cap.getAnimationTicks() / (double) getAnimationLength());
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return soundEvents;
    }

    @Override
    public Vec3d getDefaultPrimaryColor() {
        return new Vec3d(1, 1, 1);
    }

        @Override
    public Vec3d getDefaultSecondaryColor() {
        return new Vec3d(1, 1, 1);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(Regeneration.MODID, "hartnell");
    }

}
