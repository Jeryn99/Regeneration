package me.swirtzly.regeneration.client.rendering.types;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.FieryType;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.util.RenderUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderPlayerEvent;

import static me.swirtzly.regeneration.client.rendering.layers.RegenerationLayer.playerModelSteve;

public class FieryRenderer extends ATypeRenderer<FieryType> {
	
	public static final FieryRenderer INSTANCE = new FieryRenderer();

	public static void renderOverlay(PlayerEntity entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		GlStateManager.pushMatrix();
		RenderUtil.setLightmapTextureCoords(240, 240);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		GlStateManager.blendFunc(770, 1);
		Vec3d color = RegenCap.get(entityPlayer).orElse(null).getPrimaryColor();
		float opacity = MathHelper.clamp(MathHelper.sin((entityPlayer.ticksExisted + partialTicks) / 10F) * 0.1F + 0.1F, 0.11F, 1F);
		GlStateManager.color4f((float) color.x, (float) color.y, (float) color.z, opacity);
		playerModelSteve.isChild = false;
		playerModelSteve.render(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		RenderUtil.restoreLightMap();
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}

	public static void renderCone(PlayerEntity entityPlayer, float scale, float scale2, Vec3d color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();

		for (int i = 0; i < 8; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.rotatef(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
			GlStateManager.scalef(1.0f, 1.0f, 0.65f);
			vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexBuffer.pos(0.0D, 0.0D, 0.0D).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
			vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
			vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
			vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
			vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 55).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}
	
    public static void renderConeAtArms(PlayerEntity player, LivingRenderer renderLivingBase, HandSide side) {
		RegenCap.get(player).ifPresent((data) -> {
            double x = TypeManager.getTypeInstance(data.getType()).getAnimationProgress(data);
            double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
            double r = 0.09890109890109888;
            double f = p * Math.pow(x, 2) - r;
            float cf = MathHelper.clamp((float) f, 0F, 1F);
            float primaryScale = data.isSyncingToJar() ? 100 : cf * 4F;
            float secondaryScale = data.isSyncingToJar() ? 100 : cf * 6.4F;

            CompoundNBT style = data.getStyle();
            Vec3d primaryColor = new Vec3d(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
            Vec3d secondaryColor = new Vec3d(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));


            // State manager changes
            GlStateManager.pushTextureAttributes();
            GlStateManager.disableTexture();
            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
           // GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.SourceFactor.CONSTANT_ALPHA.value);
            GlStateManager.depthMask(true);
            RenderUtil.setLightmapTextureCoords(65, 65);

            if (data.isSyncingToJar()) {
                GlStateManager.rotatef(-20, 1, 0, 0);
            }

            renderCone(player, primaryScale, primaryScale, primaryColor);
            renderCone(player, secondaryScale, secondaryScale * 1.5f, secondaryColor);

            // Undo state manager changes
            RenderUtil.restoreLightMap();
            GlStateManager.depthMask(false);
            GlStateManager.disableBlend();
            GlStateManager.disableAlphaTest();
            GlStateManager.color4f(255, 255, 255, 255);
            GlStateManager.enableTexture();
            GlStateManager.popAttributes();
        });
    }

	@Override
	public void renderRegeneratingPlayerPre(FieryType type, RenderPlayerEvent.Pre ev, IRegen cap) {
	}

	@Override
	protected void renderRegeneratingPlayerPost(FieryType type, RenderPlayerEvent.Post event, IRegen capability) {

	}

    @Override
    public void renderHand(PlayerEntity player, HandSide handSide, LivingRenderer render) {
        renderConeAtArms(player, render, handSide);
    }

    @Override
	public void renderRegenerationLayer(FieryType type, LivingRenderer renderLivingBase, IRegen capability, PlayerEntity entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		
		// State manager changes
        GlStateManager.pushTextureAttributes();
        GlStateManager.disableTexture();
		GlStateManager.enableAlphaTest();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.depthMask(true);
		RenderUtil.setLightmapTextureCoords(65, 65);
		
		CompoundNBT style = capability.getStyle();
		Vec3d primaryColor = new Vec3d(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
		Vec3d secondaryColor = new Vec3d(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));
		
		double x = type.getAnimationProgress(capability);
		double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
		double r = 0.09890109890109888;
		double f = p * Math.pow(x, 2) - r;
		
		float cf = MathHelper.clamp((float) f, 0F, 1F);
		float primaryScale = cf * 4F;
		float secondaryScale = cf * 6.4F;
		
		// Render head cone
		GlStateManager.pushMatrix();

        if (renderLivingBase.getEntityModel() instanceof PlayerModel) {
            PlayerModel player = (PlayerModel) renderLivingBase.getEntityModel();
			player.bipedHead.postRender(0.0625F);
		}

        GlStateManager.translatef(0f, 0.09f, 0f);
        GlStateManager.rotatef(180, 1.0f, 0.0f, 0.0f);
		
		renderCone(entityPlayer, primaryScale / 1.6F, primaryScale * .75F, primaryColor);
		renderCone(entityPlayer, secondaryScale / 1.6F, secondaryScale / 1.5F, secondaryColor);
		GlStateManager.popMatrix();
		
		if (!capability.isSyncingToJar()) {
			// Render glowing overlay
			renderOverlay(entityPlayer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
		}
		// Undo state manager changes
		RenderUtil.restoreLightMap();
		GlStateManager.depthMask(false);
		GlStateManager.disableBlend();
		GlStateManager.disableAlphaTest();
		GlStateManager.color4f(255, 255, 255, 255);
        GlStateManager.enableTexture();
        GlStateManager.popAttributes();
	}

	@Override
	public void preAnimation(BipedModel model, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void postAnimation(BipedModel playerModel, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		RegenCap.get(entity).ifPresent((data) -> {
			if (data.getState() == PlayerUtil.RegenState.REGENERATING && data.getType() == TypeManager.Type.FIERY) {

				double animationProgress = data.getAnimationTicks();
				double arm_shake = entity.getRNG().nextDouble();
				float armRot = (float) animationProgress * 1.5F;
				float headRot = (float) animationProgress * 0.5F;

				if (armRot > 90) {
					armRot = 90;
				}

				if (headRot > 45) {
					headRot = 45;
				}

				//ARMS
				playerModel.bipedLeftArm.rotateAngleY = 0;
				playerModel.bipedRightArm.rotateAngleY = 0;

				playerModel.bipedLeftArm.rotateAngleX = 0;
				playerModel.bipedRightArm.rotateAngleX = 0;

				playerModel.bipedLeftArm.rotateAngleZ = (float) -Math.toRadians(armRot + arm_shake);
				playerModel.bipedRightArm.rotateAngleZ = (float) Math.toRadians(armRot + arm_shake);

				//BODY
				playerModel.bipedBody.rotateAngleX = 0;
				playerModel.bipedBody.rotateAngleY = 0;
				playerModel.bipedBody.rotateAngleZ = 0;


				//LEGS
				playerModel.bipedLeftLeg.rotateAngleY = 0;
				playerModel.bipedRightLeg.rotateAngleY = 0;

				playerModel.bipedLeftLeg.rotateAngleX = 0;
				playerModel.bipedRightLeg.rotateAngleX = 0;

				playerModel.bipedLeftLeg.rotateAngleZ = (float) -Math.toRadians(5);
				playerModel.bipedRightLeg.rotateAngleZ = (float) Math.toRadians(5);

				playerModel.bipedHead.rotateAngleX = (float) Math.toRadians(-headRot);
			}
		});
	}

	@Override
	public boolean useVanilla() {
		return true;
	}
}
