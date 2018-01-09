package com.afg.regeneration.client.animation;

import java.lang.reflect.Field;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;

/**
 * Created by AFlyingGrayson on 8/15/17
 */
public class LimbRotationUtil {
	public static void createCustomModelRenderer(ModelPlayer model, float x, float y, float z, Field modelRendererField) {
		try {
			ModelRenderer modelRenderer = (ModelRenderer) modelRendererField.get(model);
			Field textureOffsetXField = ModelRenderer.class.getDeclaredFields()[2];
			Field textureOffsetYField = ModelRenderer.class.getDeclaredFields()[3];
			
			textureOffsetXField.setAccessible(true);
			textureOffsetYField.setAccessible(true);
			
			int textureOffsetX = textureOffsetXField.getInt(modelRenderer);
			int textureOffsetY = textureOffsetYField.getInt(modelRenderer);
			
			CustomModelRenderer newModelRenderer = new CustomModelRenderer(model, textureOffsetX, textureOffsetY, modelRenderer, modelRendererField);
			newModelRenderer.cubeList = modelRenderer.cubeList;
			newModelRenderer.setRotationPoint(modelRenderer.rotationPointX, modelRenderer.rotationPointY, modelRenderer.rotationPointZ);
			newModelRenderer.setAngles(x, y, z);
			
			modelRendererField.set(model, newModelRenderer);
			
			textureOffsetXField.setAccessible(false);
			textureOffsetYField.setAccessible(false);
		} catch (IllegalAccessException ignored) {
		}
	}
	
	public static class CustomModelRenderer extends ModelRenderer {
		private float actualX, actualY, actualZ;
		private boolean changeAngles = false;
		private ModelPlayer modelPlayer;
		public ModelRenderer old;
		private Field f;
		
		public CustomModelRenderer(ModelBase model, int texOffX, int texOffY, ModelRenderer old, Field field) {
			super(model, "");
			if (model instanceof ModelPlayer) modelPlayer = (ModelPlayer) model;
			this.old = old;
			setTextureOffset(texOffX, texOffY);
			f = field;
		}
		
		@Override
		public void render(float scale) {
			if (changeAngles) {
				rotateAngleX = actualX;
				rotateAngleY = actualY;
				rotateAngleZ = actualZ;
			}
			super.render(scale);
		}
		
		public void reset() {
			if (f != null) try {
				f.set(modelPlayer, old);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		void setAnglesRadians(float x, float y, float z) {
			actualX = x;
			actualY = y;
			actualZ = z;
			changeAngles = true;
		}
		
		public void setAngles(float x, float y, float z) {
			setAnglesRadians((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
		}
	}
}
