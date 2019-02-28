package me.suff.regeneration.client.rendering.model;

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
		
		RenderPlayer render = (RenderPlayer) Minecraft.getInstance().getRenderManager().<AbstractClientPlayer>getEntityRenderObject(entityIn);
		ModelPlayer modelPlayer = render.getMainModel();
		
		copyModelAngles(modelPlayer.bipedRightArm, bipedRightArm);
		copyModelAngles(modelPlayer.bipedLeftArm, bipedLeftArm);
		copyModelAngles(modelPlayer.bipedHead, bipedHead);
		copyModelAngles(modelPlayer.bipedHeadwear, bipedHeadwear);
		copyModelAngles(modelPlayer.bipedLeftLegwear, bipedLeftLeg);
		copyModelAngles(modelPlayer.bipedLeftLeg, bipedLeftLeg);
		copyModelAngles(modelPlayer.bipedRightLegwear, bipedRightLeg);
		copyModelAngles(modelPlayer.bipedRightLeg, bipedRightLeg);
	}
	
}
