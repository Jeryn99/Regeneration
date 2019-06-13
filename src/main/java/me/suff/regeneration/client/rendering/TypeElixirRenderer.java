package me.suff.regeneration.client.rendering;

import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.types.TypeElixir;
import me.suff.regeneration.util.ClientUtil;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class TypeElixirRenderer extends ATypeRenderer<TypeElixir> {

    public static final TypeElixirRenderer INSTANCE = new TypeElixirRenderer();

    private TypeElixirRenderer() {
    }

    @Override
    protected void renderRegeneratingPlayerPre(TypeElixir type, RenderPlayerEvent.Pre event, IRegeneration capability) {

    }

    @Override
    protected void renderRegeneratingPlayerPost(TypeElixir type, RenderPlayerEvent.Post event, IRegeneration capability) {

    }

    @Override
    protected void renderRegenerationLayer(TypeElixir type, RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        // renderGlowingHands(entityPlayer, capability, scale);
    }

    @Override
    public boolean onAnimateRegen(AnimationContext animationContext) {
        ModelBiped modelBiped = animationContext.getModelBiped();
        EntityPlayer entityIn = animationContext.getEntityPlayer();
        modelBiped.bipedRightArm.rotateAngleX = (float) Math.toRadians(-90);
        modelBiped.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-90);

        //EXTERNAL WEAR
        if (modelBiped instanceof ModelPlayer) {
            ClientUtil.copyAnglesToWear((ModelPlayer) modelBiped);
        }
        return true;
    }

}
