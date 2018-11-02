package me.fril.regeneration.client.models;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;

public class ModelArmorOverride extends ModelBiped {
    public ModelArmorOverride() {
        super(1.0F);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        RenderPlayer render = (RenderPlayer) Minecraft.getMinecraft().getRenderManager().<AbstractClientPlayer>getEntityRenderObject(entityIn);
        ModelPlayer modelPlayer = render.getMainModel();

        copyModelAngles(modelPlayer.bipedRightArm, this.bipedRightArm);
        copyModelAngles(modelPlayer.bipedLeftArm, this.bipedLeftArm);
        copyModelAngles(modelPlayer.bipedHead, this.bipedHead);
        copyModelAngles(modelPlayer.bipedHeadwear, this.bipedHeadwear);
        copyModelAngles(modelPlayer.bipedLeftLegwear, this.bipedLeftLeg);
        copyModelAngles(modelPlayer.bipedLeftLeg, this.bipedLeftLeg);
        copyModelAngles(modelPlayer.bipedRightLegwear, this.bipedRightLeg);
        copyModelAngles(modelPlayer.bipedRightLeg, this.bipedRightLeg);
    }
}
