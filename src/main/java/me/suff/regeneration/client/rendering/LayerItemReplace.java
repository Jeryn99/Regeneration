package me.suff.regeneration.client.rendering;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.util.RegenState;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class LayerItemReplace extends LayerHeldItem {
	
	public LayerItemReplace(RenderLivingBase<?> livingEntityRendererIn) {
		super(livingEntityRendererIn);
	}
	
	@Override
	public void render(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scalef) {
		if (entitylivingbaseIn instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer) entitylivingbaseIn;
			if (CapabilityRegeneration.getForPlayer(entityPlayer).getState() != RegenState.REGENERATING) {
				super.render(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scalef);
			}
			
		}
	}
}
