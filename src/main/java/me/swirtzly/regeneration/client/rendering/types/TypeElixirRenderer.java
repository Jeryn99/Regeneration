package me.swirtzly.regeneration.client.rendering.types;

import me.swirtzly.regeneration.client.animation.AnimationContext;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.TypeElixir;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderPlayerEvent;

import static me.swirtzly.regeneration.client.animation.AnimationHandler.copyAndReturn;

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
        return copyAndReturn(modelBiped, true);
    }
    
    @Override
    public void renderHand(EntityPlayer player, EnumHandSide handSide, RenderLivingBase<?> render) {
    
    }
    
}
