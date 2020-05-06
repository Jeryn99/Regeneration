package me.swirtzly.regeneration.client.rendering.layers;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.RegenType;
import me.swirtzly.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.util.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import static me.swirtzly.regeneration.client.rendering.types.FieryRenderer.renderOverlay;
import static me.swirtzly.regeneration.util.client.RenderUtil.drawGlowingLine;

/**
 * Created by Sub on 16/09/2018.
 */
public class RegenerationLayer extends LayerRenderer {

    public static final PlayerModel playerModelSteve = new PlayerModel(0.1F, false);

    private final IEntityRenderer livingEntityRenderer;

    public RegenerationLayer(IEntityRenderer livingEntityRendererIn) {
        super(livingEntityRendererIn);
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public static void renderGlowingHands(LivingEntity player, IRegen handler, float scale, HandSide side) {
		Vec3d primaryColor = handler.getPrimaryColor();
		Vec3d secondaryColor = handler.getSecondaryColor();
		
		Minecraft mc = Minecraft.getInstance();
		Random rand = player.world.rand;
		float factor = 0.2F;
		
		RenderUtil.setupRenderLightning();
        GlStateManager.scalef(scale, scale, scale);
        GlStateManager.translatef(0, 0.3F, 0);
        GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
		for (int i = 0; i < 7; i++) {
            GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
			drawGlowingLine(new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, primaryColor, 0);
			drawGlowingLine(new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, secondaryColor, 0);
		}
		RenderUtil.finishRenderLightning();
	}

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        LivingEntity player = (LivingEntity) entity;
        RegenCap.get(player).ifPresent((data) -> {
            RegenType type = data.getType().create();
            if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                type.getRenderer().onRenderRegenerationLayer(type, (LivingRenderer) livingEntityRenderer, data, player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }

            if (data.getState() == PlayerUtil.RegenState.POST && player.hurtTime > 0) {
                renderOverlay(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }

        });

    }

    @Override
	public boolean shouldCombineTextures() {
		return false;
	}
	
}
