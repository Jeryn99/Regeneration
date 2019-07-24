package me.swirtzly.regeneration.asm;

import me.swirtzly.regeneration.client.animation.ModelRotationEvent;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;

public class RegenClientHooks {

	public static void handleRotations(BipedModel model, float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		if (entity == null)
			return;
		ModelRotationEvent rotationEvent = new ModelRotationEvent(entity, model, f, f1, f2, f3, f4, f5);
		MinecraftForge.EVENT_BUS.post(rotationEvent);
	}

	public static void renderBipedPre(BipedModel model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if(entity instanceof PlayerEntity && CapabilityRegeneration.getForPlayer((PlayerEntity) entity).getState() == PlayerUtil.RegenState.GRACE_CRIT){
				GlStateManager.translated(0, 1.5, 0);
				GlStateManager.rotated(90, 1.0F, 0F, 0.0F);
		}
	}

	static int colorModeCache;
	static float savedRed;
	static float savedGreen;
	static float savedBlue;

	public static float modRed(float red) {
		if (!enabled()) {
			return red;
		}

		if (colorModeCache == 1) {
			red = savedRed = Math.min(1, red + 0.1f);
		}
		savedRed = red;
		return red;
	}

	public static float modGreen(float green) {
		if (!enabled()) {
			return green;
		}

		savedGreen = green;

		if (savedRed > green) {
			return Math.max(savedRed, green);
		} else if (savedRed < green) {
			return Math.min(savedRed, green);
		} else {
			return green;
		}
	}

	public static float modBlue(float blue) {
		if (!enabled()) {
			return blue;
		}

		savedBlue = blue;

		if (savedRed > blue) {
			return Math.max(savedRed, blue);
		} else if (savedRed < blue) {
			return Math.min(savedRed, blue);
		} else {
			return blue;
		}
	}

	public static int[] modifyLightmap(int[] original) {
		if (!enabled()) {
			return original;
		}

		colorModeCache = 1;
		if (Minecraft.getInstance().player.isPotionActive(Effects.NIGHT_VISION) && colorModeCache == 0) {
			for (int i = 0; i < original.length; i++) {
				int height = i / 16;

				if (height != 0 && height < 16) {
					Color color = new Color(original[i]);

					Color newColor = new Color(modRed(1F / 255F * color.getRed()), modGreen(1F / 255F * color.getGreen()), modBlue(1F / 255F * color.getBlue()));

					original[i] = newColor.getRGB();
				}
			}
		}

		return original;
	}

	public static float up(float f) {
		if (enabled()) {
			return 1;
		} else {
			return f;
		}
	}

	public static float down(float f) {
		if (enabled()) {
			return 0;
		} else {
			return f;
		}
	}

	private static boolean enabled() {
		return Minecraft.getInstance().player != null && CapabilityRegeneration.getForPlayer(Minecraft.getInstance().player).getState() == PlayerUtil.RegenState.GRACE_CRIT;

	}


	public static float overrideGamma(float original) {
		if (enabled()) {
			return -4F;
		}
		return original;
	}

}
