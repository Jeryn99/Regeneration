package com.afg.regeneration.superpower;

import com.afg.regeneration.Regeneration;
import com.afg.regeneration.client.layers.LayerRegenerationLimbs;
import lucraft.mods.lucraftcore.superpower.ISuperpowerPlayerRenderer;
import lucraft.mods.lucraftcore.superpower.Superpower;
import lucraft.mods.lucraftcore.superpower.SuperpowerHandler;
import lucraft.mods.lucraftcore.superpower.SuperpowerPlayerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by AFlyingGrayson on 8/7/17
 */
@Mod.EventBusSubscriber
public class TimelordRenderhandler implements ISuperpowerPlayerRenderer
{
	private final static float modelSize = 0.5f;

	private static ArrayList<EntityPlayer> layersAddedTo = new ArrayList<>();

	@Override public void onRenderPlayer(RenderLivingBase<?> renderLivingBase, Minecraft minecraft, EntityPlayer entityPlayer, Superpower superpower,
			SuperpowerPlayerHandler superpowerPlayerHandler, float v, float v1, float v2, float v3, float v4, float v5, float v6)
	{
		TimelordHandler handler = (TimelordHandler) superpowerPlayerHandler;
		if(handler.regenTicks > 0)
		{
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder vertexbuffer = tessellator.getBuffer();

			Random random = entityPlayer.getRNG();

			GlStateManager.enableCull();
			GlStateManager.disableCull();
			GlStateManager.shadeModel(7425);
			GlStateManager.disableTexture2D();
			GlStateManager.enableAlpha();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.enableBlend();
			GlStateManager.depthMask(false);

			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

			float scale;
			ModelBase var10000 = renderLivingBase.getMainModel();
			ModelBiped model = (ModelBiped) var10000;

			for (int i = 0; i < 8; i++)
			{
				GlStateManager.pushMatrix();
				model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
				GlStateManager.translate(0f, -0.2f, 0f);

				int green = 120 + random.nextInt(80);

				GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(1.0f, 1.0f, 0.65f);
				Color color = new Color(255, green, 0, 100);
				scale = (float) handler.regenTicks/40;
				float f3 = (float) handler.regenTicks/40;
				vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(0.0D, (double) scale, (double) (1.0F * f3)).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
						.endVertex();
				vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				tessellator.draw();

				GlStateManager.popMatrix();
			}

			for (int i = 0; i < 8; i++)
			{
				GlStateManager.pushMatrix();
				model.postRenderArm(0.0625F, EnumHandSide.LEFT);
				GlStateManager.translate(0f, -0.2f, 0f);

				int green = 120 + random.nextInt(80);

				GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(1.0f, 1.0f, 0.65f);
				Color color = new Color(255, green, 0, 100);
				scale = (float) handler.regenTicks/40;
				float f3 = (float) handler.regenTicks/40;
				vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(0.0D, (double) scale, (double) (1.0F * f3)).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
						.endVertex();
				vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				tessellator.draw();

				GlStateManager.popMatrix();
			}

			for (int i = 0; i < 8; i++)
			{
				GlStateManager.pushMatrix();
				GlStateManager.translate(0f, 0.3f, 0f);
				GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);

				int green = 120 + random.nextInt(80);

				GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
				GlStateManager.scale(1.0f, 1.0f, 0.65f);
				Color color = new Color(255, green, 0, 100);
				scale = (float) handler.regenTicks/40;
				float f3 = (float) handler.regenTicks/40;
				vertexbuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				vertexbuffer.pos(0.0D, (double) scale, (double) (1.0F * f3)).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
						.endVertex();
				vertexbuffer.pos(-0.266D * (double) f3, (double) scale, (double) (-0.5F * f3))
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
				tessellator.draw();

				GlStateManager.popMatrix();
			}

			GlStateManager.disableCull();
			GlStateManager.shadeModel(7424);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			RenderHelper.enableStandardItemLighting();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
		}
	}

	@Override public void applyColor()
	{

	}

	@Override public void onRenderHandEvent(RenderHandEvent e)
	{

	}

	@Override public void onRenderSpecificHandEvent(RenderSpecificHandEvent e)
	{

	}

	@Override public void onRenderFirstPersonArmRPAPI(EnumHandSide side)
	{

	}

//	@SubscribeEvent
//	public static void onRotateArms(RenderModelEvent.SetRotationAngels e){
//		if(e.getEntity() instanceof EntityPlayer)
//		{
//			e.model.bipedLeftArm.rotateAngleZ = 90;
//			e.model.bipedRightArm.rotateAngleZ = 90;
//		}
//	}

	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre e){

		if(SuperpowerHandler.hasSuperpower(e.getEntityPlayer(), Regeneration.timelord))
		{
			TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordHandler.class);
			if (handler.regenTicks > 0)
			{
				boolean smallArms = ((AbstractClientPlayer) e.getEntityPlayer()).getSkinType().equals("slim");
				createLeftArm(e.getRenderer().getMainModel(), 0, 0, -75, smallArms);
				createRightArm(e.getRenderer().getMainModel(), 0, 0, 75, smallArms);
				createHead(e.getRenderer().getMainModel(), -20, 0, 0);
			}
		}
	}

	@SubscribeEvent
	public static void onRenderPlayerPost(RenderPlayerEvent.Post e){
		if(!layersAddedTo.contains(e.getEntityPlayer())){
			layersAddedTo.add(e.getEntityPlayer());
			e.getRenderer().addLayer(new LayerRegenerationLimbs(e.getRenderer()));
		}

		if(SuperpowerHandler.hasSuperpower(e.getEntityPlayer(), Regeneration.timelord))
		{
			TimelordHandler handler = SuperpowerHandler.getSpecificSuperpowerPlayerHandler(e.getEntityPlayer(), TimelordHandler.class);
			if (handler.regenTicks > 0)
			{
				boolean smallArms = ((AbstractClientPlayer) e.getEntityPlayer()).getSkinType().equals("slim");
				resetLeftArm(e.getRenderer().getMainModel(), smallArms);
				resetRightArm(e.getRenderer().getMainModel(), smallArms);
				resetHead(e.getRenderer().getMainModel());
			}
		}
	}

	public static void resetLeftArm(ModelPlayer model, boolean smallArms){
		if (smallArms)
		{
			model.bipedLeftArm = new ModelRenderer(model, 32, 48);
			model.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
			model.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
			model.bipedLeftArmwear = new ModelRenderer(model, 48, 48);
			model.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
			model.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

		}
		else
		{
			model.bipedLeftArm = new ModelRenderer(model, 32, 48);
			model.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
			model.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
			model.bipedLeftArmwear = new ModelRenderer(model, 48, 48);
			model.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
			model.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
		}
	}

	public static void resetRightArm(ModelPlayer model, boolean smallArms){
		model.bipedRightArm = new ModelRenderer(model, 40, 16);
		model.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		model.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

		if (smallArms)
		{
			model.bipedRightArm = new ModelRenderer(model, 40, 16);
			model.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
			model.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
			model.bipedRightArmwear = new ModelRenderer(model, 40, 32);
			model.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
			model.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
		}
		else
		{
			model.bipedRightArmwear = new ModelRenderer(model, 40, 32);
			model.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
			model.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
		}
	}

	public static void resetHead(ModelPlayer model){
		model.bipedHead = new ModelRenderer(model, 0, 0);
		model.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
		model.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		model.bipedHeadwear = new ModelRenderer(model, 32, 0);
		model.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
		model.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public static void createRightArm(ModelPlayer model, float x, float y, float z, boolean smallArms){
		createRightArm(model, smallArms);
		((CustomModelRenderer)model.bipedRightArm).setAngles((float)Math.toRadians(x),(float)Math.toRadians( y),(float)Math.toRadians( z));
		((CustomModelRenderer)model.bipedRightArmwear).setAngles((float)Math.toRadians(x),(float)Math.toRadians( y),(float)Math.toRadians( z));
	}

	public static void createLeftArm(ModelPlayer model, float x, float y, float z, boolean smallArms){
		createLeftArm(model, smallArms);
		((CustomModelRenderer)model.bipedLeftArm).setAngles((float)Math.toRadians(x),(float)Math.toRadians( y),(float)Math.toRadians( z));
		((CustomModelRenderer)model.bipedLeftArmwear).setAngles((float)Math.toRadians(x),(float)Math.toRadians( y),(float)Math.toRadians( z));
	}

	public static void createHead(ModelPlayer model, float x, float y, float z){
		createHead(model);
		((CustomModelRenderer)model.bipedHead).setAngles((float)Math.toRadians(x),(float)Math.toRadians(y),(float)Math.toRadians(z));
		((CustomModelRenderer)model.bipedHeadwear).setAngles((float)Math.toRadians(x),(float)Math.toRadians(y),(float)Math.toRadians(z));
	}

	public static void createRightArm(ModelPlayer model, boolean smallArms){
		model.bipedRightArm = new CustomModelRenderer(model, 40, 16);
		model.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		model.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		if(smallArms){
			model.bipedRightArm = new CustomModelRenderer(model, 40, 16);
			model.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
			model.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

			model.bipedRightArmwear = new CustomModelRenderer(model, 40, 32);
			model.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
			model.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
		} else {
			model.bipedRightArmwear = new CustomModelRenderer(model, 40, 32);
			model.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
			model.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
		}
	}

	public static void createLeftArm(ModelPlayer model, boolean smallArms){
		if (smallArms)
		{
			model.bipedLeftArm = new CustomModelRenderer(model, 32, 48);
			model.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
			model.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
			model.bipedLeftArmwear = new CustomModelRenderer(model, 48, 48);
			model.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
			model.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

		}
		else
		{
			model.bipedLeftArm = new CustomModelRenderer(model, 32, 48);
			model.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
			model.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
			model.bipedLeftArmwear = new CustomModelRenderer(model, 48, 48);
			model.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
			model.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
		}
	}

	public static void createHead(ModelPlayer model){
		model.bipedHead = new CustomModelRenderer(model, 0, 0);
		model.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
		model.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		model.bipedHeadwear = new CustomModelRenderer(model, 32, 0);
		model.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
		model.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public static class CustomModelRenderer extends ModelRenderer{
		private float actualX, actualY, actualZ;
		private boolean changeAngles = false;

		public CustomModelRenderer(ModelBase model, String boxNameIn)
		{
			super(model, boxNameIn);
		}

		public CustomModelRenderer(ModelBase model)
		{
			this(model, null);
		}

		public CustomModelRenderer(ModelBase model, int texOffX, int texOffY){
			this(model);
			this.setTextureOffset(texOffX, texOffY);
		}

		@Override
		public void render(float scale)
		{
			if(this.changeAngles){
				this.rotateAngleX = this.actualX;
				this.rotateAngleY = this.actualY;
				this.rotateAngleZ = this.actualZ;
			}
			super.render(scale);


		}

		public void setAngles(float x, float y, float z){
			this.actualX = x;
			this.actualY = y;
			this.actualZ = z;
			this.changeAngles = true;
		}

		public void setAnglesDegrees(float x, float y, float z){
			this.setAngles((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
		}
	}
}
