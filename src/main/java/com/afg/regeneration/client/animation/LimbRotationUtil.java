package com.afg.regeneration.client.animation;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;

/**
 * Created by AFlyingGrayson on 8/15/17
 */
public class LimbRotationUtil
{
	private final static float modelSize = 0.5f;
	private final static int LEFTARM = 1, RIGHTARM = 2, HEAD = 3, LEFTARMWEAR = 4, RIGHTARMWEAR = 5, HEADWEAR = 6;

	public static void createRightArm(ModelPlayer model, float x, float y, float z, boolean smallArms){
		createRightArm(model, smallArms);
		((CustomModelRenderer)model.bipedRightArm).setAngles(x,y,z);
		((CustomModelRenderer)model.bipedRightArmwear).setAngles(x,y,z);
	}

	public static void createLeftArm(ModelPlayer model, float x, float y, float z, boolean smallArms){
		createLeftArm(model, smallArms);
		((CustomModelRenderer)model.bipedLeftArm).setAngles(x,y,z);
		((CustomModelRenderer)model.bipedLeftArmwear).setAngles(x,y,z);
	}

	public static void createHead(ModelPlayer model, float x, float y, float z){
		createHead(model);
		((CustomModelRenderer)model.bipedHead).setAngles(x,y,z);
		((CustomModelRenderer)model.bipedHeadwear).setAngles(x,y,z);
	}

	public static void createRightArm(ModelPlayer model, boolean smallArms){
		model.bipedRightArm = new CustomModelRenderer(model, 40, 16, RIGHTARM, model.bipedRightArm);
		model.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
		model.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		if(smallArms){
			model.bipedRightArm = new CustomModelRenderer(model, 40, 16, RIGHTARM, model.bipedRightArm);
			model.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
			model.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

			model.bipedRightArmwear = new CustomModelRenderer(model, 40, 32, RIGHTARMWEAR, model.bipedRightArmwear);
			model.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
			model.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
		} else {
			model.bipedRightArmwear = new CustomModelRenderer(model, 40, 32, RIGHTARMWEAR, model.bipedRightArmwear);
			model.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
			model.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
		}
	}

	public static void createLeftArm(ModelPlayer model, boolean smallArms){
		if (smallArms)
		{
			model.bipedLeftArm = new CustomModelRenderer(model, 32, 48, LEFTARM, model.bipedLeftArm);
			model.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
			model.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
			model.bipedLeftArmwear = new CustomModelRenderer(model, 48, 48, LEFTARMWEAR, model.bipedLeftArmwear);
			model.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
			model.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

		}
		else
		{
			model.bipedLeftArm = new CustomModelRenderer(model, 32, 48, LEFTARM, model.bipedLeftArm);
			model.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
			model.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
			model.bipedLeftArmwear = new CustomModelRenderer(model, 48, 48, LEFTARMWEAR, model.bipedLeftArmwear);
			model.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
			model.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
		}
	}

	public static void createHead(ModelPlayer model){
		model.bipedHead = new CustomModelRenderer(model, 0, 0, HEAD, model.bipedHead);
		model.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
		model.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		model.bipedHeadwear = new CustomModelRenderer(model, 32, 0, HEADWEAR, model.bipedHeadwear);
		model.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
		model.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public static class CustomModelRenderer extends ModelRenderer{
		private float actualX, actualY, actualZ;
		private boolean changeAngles = false;
		private ModelPlayer modelPlayer;
		private int boxNum;
		private ModelRenderer old;

		public CustomModelRenderer(ModelBase model, int boxNum, ModelRenderer old)
		{
			super(model, "");
			if(model instanceof ModelPlayer)
				modelPlayer = (ModelPlayer) model;
			this.boxNum = boxNum;
			this.old = old;
		}

		public CustomModelRenderer(ModelBase model, int texOffX, int texOffY, int boxNum, ModelRenderer old){
			this(model, boxNum, old);
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

		public void reset(){
			switch (boxNum){
			case LEFTARM:
				modelPlayer.bipedLeftArm = this.old;
				break;
			case RIGHTARM:
				modelPlayer.bipedRightArm = this.old;
				break;
			case HEAD:
				modelPlayer.bipedHead = this.old;
				break;
			case LEFTARMWEAR:
				modelPlayer.bipedLeftArmwear = this.old;
				break;
			case RIGHTARMWEAR:
				modelPlayer.bipedRightArmwear = this.old;
				break;
			case HEADWEAR:
				modelPlayer.bipedHeadwear = this.old;
				break;
			}
		}

		void setAngle(float x, float y, float z){
			this.actualX = x;
			this.actualY = y;
			this.actualZ = z;
			this.changeAngles = true;
		}

		public void setAngles(float x, float y, float z){
			this.setAngle((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
		}
	}
}
