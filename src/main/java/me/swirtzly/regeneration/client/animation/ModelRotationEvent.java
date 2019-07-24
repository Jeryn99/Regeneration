package me.swirtzly.regeneration.client.animation;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Event is fired via RegenClientHooks (me.swirtzly.regeneration.asm.RegenClientHooks)
 * It is fired after setRotationAngles in ModelBiped via ASM
 * Allows the manipulation of the players limbs without breaking compatibility with other mods (So far as I know)
 */
@Cancelable
public class ModelRotationEvent extends EntityEvent {

    public BipedModel model;
    public float limbSwing;
    public float limbSwingAmount;
    public float partialTicks;
    public float ageInTicks;
    public float netHeadYaw;
    public float headPitch;

    public ModelRotationEvent(Entity entity, BipedModel model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        super(entity);
        this.model = model;
        this.limbSwing = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        this.partialTicks = partialTicks;
        this.ageInTicks = ageInTicks;
        this.netHeadYaw = netHeadYaw;
        this.headPitch = headPitch;
    }
}