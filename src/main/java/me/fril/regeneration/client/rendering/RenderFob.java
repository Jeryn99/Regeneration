package me.fril.regeneration.client.rendering;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.common.EntityFobWatch;
import me.fril.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Random;

public class RenderFob extends Render<EntityFobWatch> {
	
	private Vec3d primaryColor = new Vec3d(0.93F, 0.61F, 0.0F);
	private Vec3d secondaryColor = new Vec3d(1F, 0.5F, 0.18F);
	
	public RenderFob(RenderManager rm) {
		super(rm);
	}
	
	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityFobWatch entity) {
		return null;
	}
	
	/**
	 * Renders the desired {@code T} type Entity.
	 */
	@Override
	public void doRender(EntityFobWatch entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.getItem().isEmpty())
			return;
		Minecraft mc = Minecraft.getMinecraft();
		float f = 0.2f;
		Random rand = entity.world.rand;
		
		
		GlStateManager.pushMatrix();
		
		if (entity.getItem().getItemDamage() != RegenConfig.regenCapacity) {
			for (int j = 0; j < 2; j++) {
				RenderUtil.setupRenderLightning();
				GlStateManager.translate(x, y + 0.20, z);
				GlStateManager.scale(0.7F, 0.7F, 0.7F);
				GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
				
				for (int i = 0; i < 3; i++) {
					GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
					RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
					RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, secondaryColor, 0);
				}
				
				RenderUtil.finishRenderLightning();
			}
		}
		
		GlStateManager.translate(x, y + 0.17F, z);
		GlStateManager.rotate(-entity.rotationYaw, 0, 1, 0);
		Minecraft.getMinecraft().getRenderItem().renderItem(entity.getItem(), ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.popMatrix();
	}
	
	
}