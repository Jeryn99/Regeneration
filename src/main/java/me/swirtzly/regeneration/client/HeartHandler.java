package me.swirtzly.regeneration.client;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@Mod.EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public class HeartHandler {
    protected static final Random rand = new Random();
    public static int playerHealth;
    protected static long healthUpdateCounter;
    protected static int lastPlayerHealth;
    protected static long lastSystemTime;
    protected static Minecraft minecraft = Minecraft.getMinecraft();
    protected static int updateCounter;


    public static void renderPlayerStats(ScaledResolution scaledRes) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RegenerationMod.MODID, "textures/gui/hearts.png"));
        if (minecraft.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) minecraft.getRenderViewEntity();
            int i = MathHelper.ceil(entityplayer.getHealth());
            boolean flag = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

            if (i < playerHealth && entityplayer.hurtResistantTime > 0) {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = (long) (updateCounter + 20);
            } else if (i > playerHealth && entityplayer.hurtResistantTime > 0) {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = (long) (updateCounter + 10);
            }

            if (Minecraft.getSystemTime() - lastSystemTime > 1000L) {
                playerHealth = i;
                lastPlayerHealth = i;
                lastSystemTime = Minecraft.getSystemTime();
            }

            playerHealth = i;
            int j = lastPlayerHealth;
            rand.setSeed((long) (updateCounter * 312871));
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
            int l = scaledRes.getScaledWidth() / 2 - 91;
            int i1 = scaledRes.getScaledWidth() / 2 + 91;
            int j1 = scaledRes.getScaledHeight() - 39;
            float f = (float) iattributeinstance.getAttributeValue();
            int k1 = MathHelper.ceil(entityplayer.getAbsorptionAmount());
            int l1 = MathHelper.ceil((f + (float) k1) / 2.0F / 10.0F);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = j1 - (l1 - 1) * i2 - 10;
            int k2 = j1 - 10;
            int l2 = k1;
            int i3 = entityplayer.getTotalArmorValue();
            int j3 = -1;

            if (entityplayer.isPotionActive(MobEffects.REGENERATION)) {
                j3 = updateCounter % MathHelper.ceil(f + 5.0F);
            }


            minecraft.profiler.endStartSection("health");

            for (int j5 = MathHelper.ceil((f + (float) k1) / 2.0F) - 1; j5 >= 0; --j5) {
                int k5 = 16;

                if (entityplayer.isPotionActive(MobEffects.POISON)) {
                    k5 += 36;
                } else if (entityplayer.isPotionActive(MobEffects.WITHER)) {
                    k5 += 72;
                }

                int i4 = 0;

                if (flag) {
                    i4 = 1;
                }

                int j4 = MathHelper.ceil((float) (j5 + 1) / 10.0F) - 1;
                int k4 = l + j5 % 10 * 8;
                int l4 = j1 - j4 * i2;

                if (i <= 4) {
                    l4 += rand.nextInt(2);
                }

                if (l2 <= 0 && j5 == j3) {
                    l4 -= 2;
                }

                int i5 = 0;

                if (entityplayer.world.getWorldInfo().isHardcoreModeEnabled()) {
                    i5 = 5;
                }

                minecraft.ingameGUI.drawTexturedModalRect(k4, l4, 16 + i4 * 9, 9 * i5, 9, 9);

                if (flag) {
                    if (j5 * 2 + 1 < j) {
                        minecraft.ingameGUI.drawTexturedModalRect(k4, l4, k5 + 54, 9 * i5, 9, 9);
                    }

                    if (j5 * 2 + 1 == j) {
                        minecraft.ingameGUI.drawTexturedModalRect(k4, l4, k5 + 63, 9 * i5, 9, 9);
                    }
                }

                if (l2 > 0) {
                    if (l2 == k1 && k1 % 2 == 1) {
                        minecraft.ingameGUI.drawTexturedModalRect(k4, l4, k5 + 153, 9 * i5, 9, 9);
                        --l2;
                    } else {
                        minecraft.ingameGUI.drawTexturedModalRect(k4, l4, k5 + 144, 9 * i5, 9, 9);
                        l2 -= 2;
                    }
                } else {
                    if (j5 * 2 + 1 < i) {
                        minecraft.ingameGUI.drawTexturedModalRect(k4, l4, k5 + 36, 9 * i5, 9, 9);
                    }

                    if (j5 * 2 + 1 == i) {
                        minecraft.ingameGUI.drawTexturedModalRect(k4, l4, k5 + 45, 9 * i5, 9, 9);
                    }
                }
            }
            minecraft.profiler.endSection();
        }
    }


    @SubscribeEvent
    public static void renderPlayerHealth(RenderGameOverlayEvent.Pre event) {
        if (CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getRegenerationsLeft() == 0) return;
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
            ScaledResolution scaledRes = event.getResolution();
            if (!event.isCanceled()) {
                event.setCanceled(true);
            }
            renderPlayerStats(scaledRes);
        }
    }
}