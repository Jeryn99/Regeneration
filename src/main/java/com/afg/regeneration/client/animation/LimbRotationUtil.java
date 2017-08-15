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
