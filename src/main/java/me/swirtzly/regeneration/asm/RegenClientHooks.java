package me.swirtzly.regeneration.asm;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.client.animation.ModelRotationEvent;
import me.swirtzly.regeneration.client.animation.RenderCallbackEvent;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;

import static me.swirtzly.regeneration.client.ClientEventHandler.SHADERS_TEXTURES;

public class RegenClientHooks {

    public static int colorModeCache;
    public static float savedGreen, savedRed, savedBlue;

    public static void handleRotations(ModelBiped model, float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        if (entity == null)
            return;
        ModelRotationEvent rotationEvent = new ModelRotationEvent(entity, model, f, f1, f2, f3, f4, f5);
        MinecraftForge.EVENT_BUS.post(rotationEvent);
    }

    public static void preRenderCallBack(RenderLivingBase renderer, EntityLivingBase entity) {
        if (entity == null)
            return;
        RenderCallbackEvent ev = new RenderCallbackEvent(entity, renderer);
        MinecraftForge.EVENT_BUS.post(ev);
    }

    //Needs optimised greatly, as code had to be lifted and repeated, so when loadEntityShader is called
    //It does the original stuff first, then this method, which winds up performing it again
    public static void handleShader() {
        if (Minecraft.getMinecraft().player == null || !RegenConfig.regenerationShaders) return;

        EntityRenderer entityRender = Minecraft.getMinecraft().entityRenderer;

        if (OpenGlHelper.shadersSupported) {
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                IRegeneration data = CapabilityRegeneration.getForPlayer(player);
                switch (data.getState()) {
                    case POST:
                        entityRender.loadShader(SHADERS_TEXTURES[data.getPlayer().world.rand.nextInt(SHADERS_TEXTURES.length)]);
                        break;
                    case GRACE:
                    case GRACE_CRIT:
                        entityRender.loadShader(SHADERS_TEXTURES[16]);
                        break;
                    case ALIVE:
                    case REGENERATING:
                        resetShader(entityRender);
                    default:
                        break;
                }
            } else {
                resetShader(entityRender);
            }

        }
    }

    public static void resetShader(EntityRenderer entityRender) {
        //This part is the unoptimised part, it's needed to reset the players shader
        //It's just my bad ASM, but I'll look into it at a later point
        Entity entityIn = Minecraft.getMinecraft().getRenderViewEntity();
        entityRender.stopUseShader();

        if (entityIn instanceof EntityCreeper) {
            entityRender.loadShader(new ResourceLocation("shaders/post/creeper.json"));
        } else if (entityIn instanceof EntitySpider) {
            entityRender.loadShader(new ResourceLocation("shaders/post/spider.json"));
        } else if (entityIn instanceof EntityEnderman) {
            entityRender.loadShader(new ResourceLocation("shaders/post/invert.json"));
        } else net.minecraftforge.client.ForgeHooksClient.loadEntityShader(entityIn, entityRender);
    }

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
        if (Minecraft.getMinecraft().player.isPotionActive(MobEffects.NIGHT_VISION) && colorModeCache == 0) {
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
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer) {
            return CapabilityRegeneration.getForPlayer((EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity()).getState() == PlayerUtil.RegenState.GRACE_CRIT;
        }
        return false;
    }


    public static float overrideGamma(float original) {
        if (enabled()) {
            return -4F;
        }
        return original;
    }

}
