package me.fril.regeneration.client.rendering;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class LayerItemReplace extends LayerHeldItem {

    public LayerItemReplace(RenderLivingBase<?> livingEntityRendererIn) {
        super(livingEntityRendererIn);
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entitylivingbaseIn;
            if (CapabilityRegeneration.getForPlayer(entityPlayer).getState() != RegenState.REGENERATING) {
                super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }

        }
    }
}
