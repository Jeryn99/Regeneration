package me.swirtzly.regeneration.client.animation;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;

public class AnimationContext {

    private float limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch;
    private BipedModel modelBiped;
    private PlayerEntity entityPlayer;

    public AnimationContext(BipedModel model, PlayerEntity player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.modelBiped = model;
        this.entityPlayer = player;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
    }

    public PlayerEntity getEntityPlayer() {
        return entityPlayer;
    }

    public BipedModel getModelBiped() {
        return modelBiped;
    }

    public float getAgeInTicks() {
        return ageInTicks;
    }

    public float getHeadPitch() {
        return headPitch;
    }

    public float getLimbSwing() {
        return limbSwing;
    }

    public float getLimbSwingAmount() {
        return limbSwingAmount;
    }

    public float getNetHeadYaw() {
        return netHeadYaw;
    }

}
