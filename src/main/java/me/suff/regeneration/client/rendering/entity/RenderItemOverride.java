package me.suff.regeneration.client.rendering.entity;

import me.suff.regeneration.RegenConfig;
import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.handlers.RegenObjects;
import me.suff.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderItemOverride extends Render<EntityItemOverride> {
	
	private Vec3d primaryColor = new Vec3d(0.93F, 0.61F, 0.0F);
	private Vec3d secondaryColor = new Vec3d(1F, 0.5F, 0.18F);
	
	public RenderItemOverride(RenderManager rm) {
		super(rm);
	}
	
	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityItemOverride entity) {
		return null;
	}
	
	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityItemOverride entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.getItem().isEmpty())
			return;
		Minecraft mc = Minecraft.getInstance();
		float f = 0.2f;
		Random rand = entity.world.rand;
		
		GlStateManager.pushMatrix();
		if (entity.getItem().getItem() == RegenObjects.Items.FOB_WATCH && entity.getItem().getDamage() != RegenConfig.regenCapacity) {
			for (int j = 0; j < 2; j++) {
				RenderUtil.setupRenderLightning();
				GlStateManager.translated(x, y + 0.20, z);
				GlStateManager.scalef(0.7F, 0.7F, 0.7F);
				GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
				
				for (int i = 0; i < 3; i++) {
					GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
					RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
					RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, secondaryColor, 0);
				}
				RenderUtil.finishRenderLightning();
			}
		}
		
		GlStateManager.translated(x, y + 0.17F, z);
		GlStateManager.rotatef(-entity.rotationYaw, 0, 1, 0);
		Minecraft.getInstance().getItemRenderer().renderItem(entity.getItem(), ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.popMatrix();
	}
	
	
}