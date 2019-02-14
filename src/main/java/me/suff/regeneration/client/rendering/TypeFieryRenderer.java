package me.suff.regeneration.client.rendering;

import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.types.TypeFiery;
import me.suff.regeneration.util.LimbHelper;
import me.suff.regeneration.util.RenderUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.client.renderer.entity.model.ModelPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.monster.EntityPhantom;
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
		if (MinecraftForgeClient.getRenderPass() == -1) // rendering in inventory
			return;
		
		RenderPlayer renderPlayer = ev.getRenderer();
		double animationProgress = type.getAnimationProgress();
		int arm_shake = ev.getEntityPlayer().getRNG().nextInt(7);
		
		int headRot = 50;
		if (animationProgress < 0.05) {
			headRot = (int) ((animationProgress / 0.05F) * 50F); // %headRotatingPhase * maxHeadRot
		}
		
		float armRot = 85;
		if (animationProgress < 0.075) {
			arm_shake = 0;
			armRot = (int) ((animationProgress / 0.075F) * 85F); // %armRotatingPhase * maxArmRot
		}
		
		LimbHelper.rotateHead(renderPlayer, -headRot, 0, 0);
		LimbHelper.rotateLeftArm(renderPlayer, 0, 0, -armRot + arm_shake);
		LimbHelper.rotateRightArm(renderPlayer, 0, 0, armRot + arm_shake);
		LimbHelper.rotateRightLeg(renderPlayer, 0, 0, 10);
		LimbHelper.rotateLeftLeg(renderPlayer, 0, 0, -10);
		LimbHelper.rotateBody(renderPlayer, 0, 0, 0);
	}
	
	@Override
	protected void renderRegeneratingPlayerPost(TypeFiery type, RenderPlayerEvent.Post event, IRegeneration capability) {
		
	}
	
	@Override
	public void renderRegenerationLayer(TypeFiery type, RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scalef) {
		if (MinecraftForgeClient.getRenderPass() == -1) // rendering in inventory
			return;
		
		ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();
		
		// State manager changes
		GlStateManager.disableTexture2D();
		GlStateManager.enableAlphaTest();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.depthMask(true);
		RenderUtil.setLightmapTextureCoords(65, 65);
		
		NBTTagCompound style = capability.getStyle();
		Vec3d primaryColor = new Vec3d(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
		Vec3d secondaryColor = new Vec3d(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));
		
		double x = type.getAnimationProgress();
		double p = 109.89010989010987; // see the wiki for the explanation of these "magic" numbers
		double r = 0.09890109890109888;
		double f = p * Math.pow(x, 2) - r;
		
		float cf = MathHelper.clamp((float) f, 0F, 1F);
		float primaryscalef = cf * 4F;
		float secondaryscalef = cf * 6.4F;
		
		// Render right cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
		GlStateManager.translatef(0f, -0.2f, 0f);
		renderCone(entityPlayer, primaryscalef, primaryscalef, primaryColor);
		renderCone(entityPlayer, secondaryscalef, secondaryscalef * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Render left cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.LEFT);
		GlStateManager.translatef(0f, -0.2f, 0f);
		renderCone(entityPlayer, primaryscalef, primaryscalef, primaryColor);
		renderCone(entityPlayer, secondaryscalef, secondaryscalef * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Render head cone
		GlStateManager.pushMatrix();
		GlStateManager.translatef(0f, 0.3f, 0f);
		GlStateManager.rotatef(180, 1.0f, 0.0f, 0.0f);
		renderCone(entityPlayer, primaryscalef / 1.6F, primaryscalef * .75F, primaryColor);
		renderCone(entityPlayer, secondaryscalef / 1.6F, secondaryscalef / 1.5F, secondaryColor);
		GlStateManager.popMatrix();
		
		// Check which slightly larger model to use
		ModelPlayer playerModel = LayerRegeneration.playerModelSteve;
		
		// Define which parts are glowing
		playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = false;
		
		// Copy model attributes from the real player model
		playerModel.setModelAttributes(model);
		
		// Render glowing overlay
		GlStateManager.color4f((float) primaryColor.x, (float) primaryColor.y, (float) primaryColor.z, 1);
		playerModel.render(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scalef);
		
		// Undo state manager changes
		RenderUtil.restoreLightMap();
		GlStateManager.depthMask(false);
		GlStateManager.disableBlend();
		GlStateManager.disableAlphaTest();
		GlStateManager.color4f(255, 255, 255, 255);
		GlStateManager.enableTexture2D();
		GlStateManager.popAttrib();
	}
	
	private void renderCone(EntityPlayer entityPlayer, float scalef, float scalef2, Vec3d color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		
		for (int i = 0; i < 8; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.rotatef(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
			GlStateManager.scalef(1.0f, 1.0f, 0.65f);
			vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexBuffer.pos(0.0D, 0.0D, 0.0D).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			vertexBuffer.pos(-0.266D * scalef, scalef, -0.5F * scalef).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			vertexBuffer.pos(0.266D * scalef, scalef, -0.5F * scalef).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			vertexBuffer.pos(0.0D, scalef2, 1.0F * scalef).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			vertexBuffer.pos(-0.266D * scalef, scalef, -0.5F * scalef).color((float) color.x, (float) color.y, (float) color.z, 100).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}
	
}
