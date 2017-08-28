package com.afg.regeneration.client.animation;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;

import java.lang.reflect.Field;

/**
 * Created by AFlyingGrayson on 8/15/17
 */
public class LimbRotationUtil
{
	private final static float modelSize = 0.5f;
	private final static int LEFTARM = 1, RIGHTARM = 2, HEAD = 3, LEFTARMWEAR = 4, RIGHTARMWEAR = 5, HEADWEAR = 6;

	public static void createRightArm(ModelPlayer model, float x, float y, float z)
	{
		try
		{
			createRightArm(model);
			((CustomModelRenderer) model.bipedRightArm).setAngles(x, y, z);
			((CustomModelRenderer) model.bipedRightArmwear).setAngles(x, y, z);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}

	public static void createLeftArm(ModelPlayer model, float x, float y, float z)
	{
		try
		{
			createLeftArm(model);
			((CustomModelRenderer) model.bipedLeftArm).setAngles(x, y, z);
			((CustomModelRenderer) model.bipedLeftArmwear).setAngles(x, y, z);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

	}

	public static void createHead(ModelPlayer model, float x, float y, float z)
	{
		createHead(model);
		((CustomModelRenderer) model.bipedHead).setAngles(x, y, z);
		((CustomModelRenderer) model.bipedHeadwear).setAngles(x, y, z);
	}

	public static void createRightArm(ModelPlayer model) throws IllegalAccessException
	{
		Field textureOffsetXField = ModelRenderer.class.getDeclaredFields()[2];
		textureOffsetXField.setAccessible(true);
		Field textureOffsetYField = ModelRenderer.class.getDeclaredFields()[3];
		textureOffsetYField.setAccessible(true);
		int textureOffsetX = textureOffsetXField.getInt(model.bipedRightArm);
		int textureOffsetY = textureOffsetYField.getInt(model.bipedRightArm);
		CustomModelRenderer newRightArm = new CustomModelRenderer(model, textureOffsetX, textureOffsetY, RIGHTARM, model.bipedRightArm);
		newRightArm.cubeList = model.bipedRightArm.cubeList;
		newRightArm.setRotationPoint(model.bipedRightArm.rotationPointX, model.bipedRightArm.rotationPointY, model.bipedRightArm.rotationPointZ);
		model.bipedRightArm = newRightArm;

		textureOffsetX = textureOffsetXField.getInt(model.bipedRightArmwear);
		textureOffsetY = textureOffsetYField.getInt(model.bipedRightArmwear);
		CustomModelRenderer newRightArmwear = new CustomModelRenderer(model, textureOffsetX, textureOffsetY, RIGHTARMWEAR, model.bipedRightArmwear);
		newRightArmwear.cubeList = model.bipedRightArmwear.cubeList;
		newRightArmwear.setRotationPoint(model.bipedRightArmwear.rotationPointX, model.bipedRightArmwear.rotationPointY, model.bipedRightArmwear.rotationPointZ);
		model.bipedRightArmwear = newRightArmwear;


		textureOffsetXField.setAccessible(false);
		textureOffsetYField.setAccessible(false);
	}

	public static void createLeftArm(ModelPlayer model) throws IllegalAccessException
	{
		Field textureOffsetXField = ModelRenderer.class.getDeclaredFields()[2];
		textureOffsetXField.setAccessible(true);
		Field textureOffsetYField = ModelRenderer.class.getDeclaredFields()[3];
		textureOffsetYField.setAccessible(true);
		int textureOffsetX = textureOffsetXField.getInt(model.bipedLeftArm);
		int textureOffsetY = textureOffsetYField.getInt(model.bipedLeftArm);
		CustomModelRenderer newLeftArm = new CustomModelRenderer(model, textureOffsetX, textureOffsetY, LEFTARM, model.bipedLeftArm);
		newLeftArm.cubeList = model.bipedLeftArm.cubeList;
		newLeftArm.setRotationPoint(model.bipedLeftArm.rotationPointX, model.bipedLeftArm.rotationPointY, model.bipedLeftArm.rotationPointZ);
		model.bipedLeftArm = newLeftArm;


		textureOffsetX = textureOffsetXField.getInt(model.bipedLeftArmwear);
		textureOffsetY = textureOffsetYField.getInt(model.bipedLeftArmwear);
		CustomModelRenderer newLeftArmwear = new CustomModelRenderer(model, textureOffsetX, textureOffsetY, LEFTARMWEAR, model.bipedLeftArmwear);
		newLeftArmwear.cubeList = model.bipedLeftArmwear.cubeList;
		newLeftArmwear.setRotationPoint(model.bipedLeftArmwear.rotationPointX, model.bipedLeftArmwear.rotationPointY, model.bipedLeftArmwear.rotationPointZ);
		model.bipedLeftArmwear = newLeftArmwear;


		textureOffsetXField.setAccessible(false);
		textureOffsetYField.setAccessible(false);
	}

	public static void createHead(ModelPlayer model)
	{
		model.bipedHead = new CustomModelRenderer(model, 0, 0, HEAD, model.bipedHead);
		model.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
		model.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		model.bipedHeadwear = new CustomModelRenderer(model, 32, 0, HEADWEAR, model.bipedHeadwear);
		model.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
		model.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public static class CustomModelRenderer extends ModelRenderer
	{
		private float actualX, actualY, actualZ;
		private boolean changeAngles = false;
		private ModelPlayer modelPlayer;
		private int boxNum;
		private ModelRenderer old;

		public CustomModelRenderer(ModelBase model, int boxNum, ModelRenderer old)
		{
			super(model, "");
			if (model instanceof ModelPlayer)
				modelPlayer = (ModelPlayer) model;
			this.boxNum = boxNum;
			this.old = old;
		}

		public CustomModelRenderer(ModelBase model, int texOffX, int texOffY, int boxNum, ModelRenderer old)
		{
			this(model, boxNum, old);
			this.setTextureOffset(texOffX, texOffY);
		}

		@Override
		public void render(float scale)
		{
			if (this.changeAngles)
			{
				this.rotateAngleX = this.actualX;
				this.rotateAngleY = this.actualY;
				this.rotateAngleZ = this.actualZ;
			}
			super.render(scale);
		}

		public void reset()
		{
			switch (boxNum)
			{
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

		void setAngle(float x, float y, float z)
		{
			this.actualX = x;
			this.actualY = y;
			this.actualZ = z;
			this.changeAngles = true;
		}

		public void setAngles(float x, float y, float z)
		{
			this.setAngle((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
		}
	}
}
