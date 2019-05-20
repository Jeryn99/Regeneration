package me.suff.regeneration.client.rendering;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.types.TypeFiery;
import me.suff.regeneration.util.RenderUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class TypeFieryRenderer extends ATypeRenderer<TypeFiery> {
	
	public static final TypeFieryRenderer INSTANCE = new TypeFieryRenderer();
	
	private TypeFieryRenderer() {
	}
	
	@Override
	public void renderRegeneratingPlayerPre(TypeFiery type, RenderPlayerEvent.Pre ev, IRegeneration cap) {
	}
	
	@Override
	protected void renderRegeneratingPlayerPost(TypeFiery type, RenderPlayerEvent.Post event, IRegeneration capability) {
		
	}
	
	@Override
	public void renderRegenerationLayer(TypeFiery type, RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (MinecraftForgeClient.getRenderPass() == -1) // rendering in inventory
			return;
		
		ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();
		
		// State manager changes
		GlStateManager.pushAttrib();
		GlStateManager.disableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.depthMask(true);
		RenderUtil.setLightmapTextureCoords(65, 65);
		
		NBTTagCompound style = capability.getStyle();
		Vec3d primaryColor = new Vec3d(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
		Vec3d secondaryColor = new Vec3d(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));
		
		double x = type.getAnimationProgress(capability);
		double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
		double r = 0.09890109890109888;
		double f = p * Math.pow(x, 2) - r;
		
		float cf = MathHelper.clamp((float) f, 0F, 1F);
		float primaryScale = cf * 4F;
		float secondaryScale = cf * 6.4F;
		
		// Render right cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
		GlStateManager.translate(0f, -0.2f, 0f);
		renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
		renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Render left cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.LEFT);
		GlStateManager.translate(0f, -0.2f, 0f);
		renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
		renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Render head cone
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, 0.3f, 0f);
		GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);
		renderCone(entityPlayer, primaryScale / 1.6F, primaryScale * .75F, primaryColor);
		renderCone(entityPlayer, secondaryScale / 1.6F, secondaryScale / 1.5F, secondaryColor);
		GlStateManager.popMatrix();
		
		// Check which slightly larger model to use
		ModelPlayer playerModel = LayerRegeneration.playerModelSteve;
		
		// Define which parts are glowing
		playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = false;
		
		// Copy model attributes from the real player model
		playerModel.setModelAttributes(model);
		
		// Render glowing overlay
		GlStateManager.color((float) primaryColor.x, (float) primaryColor.y, (float) primaryColor.z, 1);
		playerModel.render(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		// Undo state manager changes
		RenderUtil.restoreLightMap();
		GlStateManager.depthMask(false);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.color(255, 255, 255, 255);
		GlStateManager.enableTexture2D();
		GlStateManager.popAttrib();
	}

	@Override
	public boolean onAnimateRegen(ModelBiped playerModel, EntityPlayer player) {
		IRegeneration data = CapabilityRegeneration.getForPlayer(player);
		double animationProgress = data.getAnimationTicks();
		double arm_shake = player.getRNG().nextDouble();

		float armRot = (float) animationProgress * 3.5F;
		if (armRot > 90) {
			armRot = 90;
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


		//EXTERNAL WEAR
		if (playerModel instanceof ModelPlayer) {
			ModelPlayer model = (ModelPlayer) playerModel;
			ModelBase.copyModelAngles(playerModel.bipedRightArm, model.bipedRightArmwear);
			ModelBase.copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
			ModelBase.copyModelAngles(model.bipedRightLeg, model.bipedRightLegwear);
			ModelBase.copyModelAngles(model.bipedLeftLeg, model.bipedLeftLegwear);
			ModelBase.copyModelAngles(model.bipedBody, model.bipedBodyWear);
		}
		return true;
	}
	
	private void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Vec3d color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		
		for (int i = 0; i < 8; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(1.0f, 1.0f, 0.65f);
			vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexBuffer.pos(0.0D, 0.0D, 0.0D).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}
	
}
