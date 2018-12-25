package me.fril.regeneration.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import me.fril.regeneration.RegenerationMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RegenerationMod.MODID)
public class LimbManipulationUtil {
	
	private static Field textureOffsetXField = ModelRenderer.class.getDeclaredFields()[2];
	private static Field textureOffsetYField = ModelRenderer.class.getDeclaredFields()[3];
	
	public static LimbManipulator getLimbManipulator(RenderPlayer renderPlayer, Limb limb) {
		LimbManipulator manipulator = new LimbManipulator();
		try {
			textureOffsetXField.setAccessible(true);
			textureOffsetYField.setAccessible(true);
			
			List<LayerRenderer<AbstractClientPlayer>> layerList = ReflectionHelper.getPrivateValue(RenderLivingBase.class, renderPlayer, 4);
			
			for (LayerRenderer<AbstractClientPlayer> layer : layerList) {
				for (Field field : layer.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					
					if (field.getType() == ModelBiped.class) {
						ModelBiped model = (ModelBiped) field.get(layer);
						ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);
						manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.rendererField));
						
						if (limb == Limb.HEAD) {
							modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
							manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.secondaryRendererField));
						}
						
					} else if (field.getType() == ModelPlayer.class) {
						ModelBiped model = (ModelBiped) field.get(layer);
						ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);
						manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.rendererField));
						modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
						manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.secondaryRendererField));
					}
				}
			}
			
			ModelPlayer model = renderPlayer.getMainModel();
			ModelRenderer modelRenderer = (ModelRenderer) limb.rendererField.get(model);
			
			manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.rendererField));
			modelRenderer = (ModelRenderer) limb.secondaryRendererField.get(model);
			manipulator.limbs.add(new CustomModelRenderer(model, textureOffsetXField.getInt(modelRenderer), textureOffsetYField.getInt(modelRenderer), modelRenderer, limb.secondaryRendererField));
			
			textureOffsetXField.setAccessible(false);
			textureOffsetYField.setAccessible(false);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return manipulator;
	}
	
	@SubscribeEvent
	public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
		@SuppressWarnings("rawtypes")
		RenderLivingBase renderer = (RenderLivingBase) Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(event.getEntityPlayer());
		List<LayerRenderer<AbstractClientPlayer>> layerList = ReflectionHelper.getPrivateValue(RenderLivingBase.class, renderer, 4);
		
		
		if (event.getRenderer().getMainModel().boxList != null) {
			for (ModelRenderer modelRenderer : event.getRenderer().getMainModel().boxList) {
				if (modelRenderer != null && modelRenderer instanceof LimbManipulationUtil.CustomModelRenderer) {
					((LimbManipulationUtil.CustomModelRenderer) modelRenderer).reset();
				}
			}
		}
		
		
		//TODO This entire try, could actually be ditchable? As I patch biped armor in the ClientProxy
		try {
			if (layerList == null) return;
			for (LayerRenderer<AbstractClientPlayer> layer : layerList) {
				
				for (Field field : layer.getClass().getDeclaredFields()) {
					field.setAccessible(true);
					
					//Model Biped
					if (field.getType() == ModelBiped.class) {
						ModelBiped biped = (ModelBiped) field.get(layer);
						if (biped.boxList != null) {
							for (ModelRenderer modelRenderer : biped.boxList) {
								if (modelRenderer != null && modelRenderer instanceof LimbManipulationUtil.CustomModelRenderer) {
									((LimbManipulationUtil.CustomModelRenderer) modelRenderer).reset();
								}
							}
						}
					}
					
					//Model Player
					if (field.getType() == ModelPlayer.class) {
						ModelPlayer modelPlayer = (ModelPlayer) field.get(layer);
						if (modelPlayer.boxList != null) {
							for (ModelRenderer modelRenderer : modelPlayer.boxList) {
								if (modelRenderer instanceof LimbManipulationUtil.CustomModelRenderer) {
									((LimbManipulationUtil.CustomModelRenderer) modelRenderer).reset();
								}
							}
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace(); //SUB why are we ignoring this?
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
		public void render(float scale) {
			if (changeAngles) {
				rotateAngleX = actualX;
				rotateAngleY = actualY;
				rotateAngleZ = actualZ;
			}
			GlStateManager.pushMatrix();
			GlStateManager.translate(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale);
			GlStateManager.rotate(rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
			GlStateManager.translate(offX, offY, offZ);
			GlStateManager.rotate(rotateAngleX * 57.295776F, -1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(rotateAngleY * 57.295776F, 0.0F, -1.0F, 0.0F);
			GlStateManager.rotate(rotateAngleZ * 57.295776F, 0.0F, 0.0F, -1.0F);
			GlStateManager.translate(-rotationPointX * scale, -rotationPointY * scale, -rotationPointZ * scale);
			super.render(scale);
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
			setAnglesRadians((float) Math.toRadians(x), (float) Math.toRadians(y),
					(float) Math.toRadians(z));
		}
		
		private void setOffsets(float x, float y, float z) {
			offX = x;
			offY = y;
			offZ = z;
		}
	}
}
