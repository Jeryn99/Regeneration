package me.suff.regeneration.util;

import me.suff.regeneration.RegenerationMod;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.client.renderer.entity.model.ModelPlayer;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RegenerationMod.MODID)
public class LimbManipulationUtil {
	
	public static LimbManipulator getLimbManipulator(RenderPlayer renderPlayer, Limb limb) {
		LimbManipulator manipulator = new LimbManipulator();
		if (MinecraftForgeClient.getRenderPass() == -1) {
			// rendering in inventory
			return manipulator;
		}
		
		List<LayerRenderer<AbstractClientPlayer>> layerList = renderPlayer.layerRenderers;
		try {
			for (LayerRenderer<AbstractClientPlayer> layer : layerList) {
				for (Field field : layer.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					
					if (field.getType() == ModelBiped.class) {
						ModelBiped model = (ModelBiped) field.get(layer);
						
						if (model != null) {
							ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);
							manipulator.limbs.add(new CustomModelRenderer(model, modelRenderer.textureOffsetX, modelRenderer.textureOffsetY, modelRenderer, limb.rendererField));
							
							if (limb == Limb.HEAD) {
								modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
								manipulator.limbs.add(new CustomModelRenderer(model, modelRenderer.textureOffsetX, modelRenderer.textureOffsetY, modelRenderer, limb.secondaryRendererField));
							}
						}
						
					} else if (field.getType() == ModelPlayer.class) {
						ModelBiped model = (ModelBiped) field.get(layer);
						if (model != null) {
							ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);
							manipulator.limbs.add(new CustomModelRenderer(model, modelRenderer.textureOffsetX, modelRenderer.textureOffsetY, modelRenderer, limb.rendererField));
							modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
							manipulator.limbs.add(new CustomModelRenderer(model, modelRenderer.textureOffsetX, modelRenderer.textureOffsetY, modelRenderer, limb.secondaryRendererField));
						}
					}
				}
			}
			
			// This here, handles the rotation of PLAYER limbs
			ModelPlayer model = renderPlayer.getMainModel();
			ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);
			manipulator.limbs.add(new CustomModelRenderer(model, modelRenderer.textureOffsetX, modelRenderer.textureOffsetY, modelRenderer, limb.rendererField));
			modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
			manipulator.limbs.add(new CustomModelRenderer(model, modelRenderer.textureOffsetX, modelRenderer.textureOffsetY, modelRenderer, limb.secondaryRendererField));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return manipulator;
	}
	
	@SubscribeEvent
	public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
		if (MinecraftForgeClient.getRenderPass() == -1) // rendering in inventory
			return;
		
		RenderPlayer renderer = event.getRenderer();
		ModelBase playerModel = renderer.getMainModel();
		List<LayerRenderer<AbstractClientPlayer>> layerList = renderer.layerRenderers;
		
		if (playerModel != null && playerModel.boxList != null) {
			for (ModelRenderer modelRenderer : playerModel.boxList) {
				if (modelRenderer instanceof LimbManipulationUtil.CustomModelRenderer) {
					CustomModelRenderer customMr = (CustomModelRenderer) modelRenderer;
					customMr.reset();
				}
			}
		}
		
		try {
			for (LayerRenderer<?> layer : layerList) {
				for (Field field : layer.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					
					// Model Biped
					if (field.getType() == ModelBiped.class) {
						ModelBiped biped = (ModelBiped) field.get(layer);
						if (biped != null && biped.boxList != null) {
							for (ModelRenderer modelRenderer : biped.boxList) {
								if (modelRenderer instanceof LimbManipulationUtil.CustomModelRenderer) {
									CustomModelRenderer customMr = (CustomModelRenderer) modelRenderer;
									customMr.reset();
								}
							}
						}
					}
					
					// Model Player
					if (field.getType() == ModelPlayer.class) {
						ModelPlayer modelPlayer = (ModelPlayer) field.get(layer);
						if (modelPlayer != null && modelPlayer.boxList != null) {
							for (ModelRenderer modelRenderer : modelPlayer.boxList) {
								if (modelRenderer instanceof LimbManipulationUtil.CustomModelRenderer) {
									CustomModelRenderer customMr = (CustomModelRenderer) modelRenderer;
									customMr.reset();
								}
							}
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public enum Limb {
		HEAD(ModelBiped.class.getDeclaredFields()[0], ModelBiped.class.getDeclaredFields()[1]),
		BODY(ModelBiped.class.getDeclaredFields()[2], ModelPlayer.class.getDeclaredFields()[4]),
		LEFT_ARM(ModelBiped.class.getDeclaredFields()[4], ModelPlayer.class.getDeclaredFields()[0]),
		RIGHT_ARM(ModelBiped.class.getDeclaredFields()[3], ModelPlayer.class.getDeclaredFields()[1]),
		LEFT_LEG(ModelBiped.class.getDeclaredFields()[6], ModelPlayer.class.getDeclaredFields()[2]),
		RIGHT_LEG(ModelBiped.class.getDeclaredFields()[5], ModelPlayer.class.getDeclaredFields()[3]);
		
		public Field rendererField, secondaryRendererField;
		
		Limb(Field rendererField, Field secondaryRendererField) {
			this.rendererField = rendererField;
			this.secondaryRendererField = secondaryRendererField;
		}
	}
	
	public static class LimbManipulator {
		
		private ArrayList<CustomModelRenderer> limbs = new ArrayList<>();
		
		public LimbManipulator setAngles(float x, float y, float z) {
			for (CustomModelRenderer limb : limbs) {
				limb.setAngles(x, y, z);
			}
			return this;
		}
		
		public LimbManipulator setOffsets(float x, float y, float z) {
			for (CustomModelRenderer limb : limbs) {
				limb.setOffsets(x, y, z);
			}
			return this;
		}
	}
	
	public static class CustomModelRenderer extends ModelRenderer {
		
		private float actualX, actualY, actualZ;
		private float offX, offY, offZ;
		private boolean changeAngles = false;
		private ModelBiped modelBiped;
		private ModelRenderer old;
		private Field f;
		
		private CustomModelRenderer(ModelBiped model, int texOffX, int texOffY, ModelRenderer old, Field field) throws IllegalAccessException {
			super(model, "");
			modelBiped = model;
			this.old = old;
			setTextureOffset(texOffX, texOffY);
			f = field;
			cubeList = old.cubeList;
			setRotationPoint(old.rotationPointX, old.rotationPointY, old.rotationPointZ);
			field.set(model, this);
		}
		
		@Override
		public void render(float scalef) {
			if (changeAngles) {
				rotateAngleX = actualX;
				rotateAngleY = actualY;
				rotateAngleZ = actualZ;
			}
			GlStateManager.pushMatrix();
			GlStateManager.translatef(rotationPointX * scalef, rotationPointY * scalef, rotationPointZ * scalef);
			GlStateManager.rotatef(rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotatef(rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translatef(offX, offY, offZ);
			GlStateManager.rotatef(rotateAngleX * 57.295776F, -1.0F, 0.0F, 0.0F);
			GlStateManager.rotatef(rotateAngleY * 57.295776F, 0.0F, -1.0F, 0.0F);
			GlStateManager.rotatef(rotateAngleZ * 57.295776F, 0.0F, 0.0F, -1.0F);
			GlStateManager.translatef(-rotationPointX * scalef, -rotationPointY * scalef, -rotationPointZ * scalef);
			super.render(scalef);
			GlStateManager.popMatrix();
		}
		
		public void reset() {
			if (f != null) {
				try {
					f.set(modelBiped, old);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void setAnglesRadians(float x, float y, float z) {
			actualX = x;
			actualY = y;
			actualZ = z;
			changeAngles = true;
		}
		
		private void setAngles(float x, float y, float z) {
			setAnglesRadians((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
		}
		
		private void setOffsets(float x, float y, float z) {
			offX = x;
			offY = y;
			offZ = z;
		}
	}
}
