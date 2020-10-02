package me.swirtzly.regen.handlers;

import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.client.skin.SkinHandler;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import me.swirtzly.regen.util.RenderHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.swirtzly.regen.common.regen.state.RegenStates.GRACE_CRIT;
import static me.swirtzly.regen.common.regen.state.RegenStates.REGENERATING;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre playerEvent) {
        SkinHandler.tick((AbstractClientPlayerEntity) playerEvent.getPlayer());
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) return;

        //Clean up our mess we might have made!
        if (Minecraft.getInstance().world == null) {
            if (SkinHandler.PLAYER_SKINS.size() > 0) {
                SkinHandler.PLAYER_SKINS.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().deleteTexture(texture)));
                SkinHandler.PLAYER_SKINS.clear();
                Regeneration.LOG.warn("CLEARED CACHE OF PLAYER_SKINS");
            }
        }
    }

    @SubscribeEvent
    public static void onColorFog(EntityViewRenderEvent.RenderFogEvent.FogColors e) {
        Entity renderView = Minecraft.getInstance().getRenderViewEntity();
        if (!(Minecraft.getInstance().getRenderViewEntity() instanceof LivingEntity)) {
            return;
        }

        RegenCap.get((LivingEntity) renderView).ifPresent((data) -> {

            if (data.getCurrentState() == GRACE_CRIT) {
                e.setRed(0);
                e.setBlue(0);
                e.setGreen(0);
            }

            if (data.getTransitionType() == TransitionTypes.HARTNELL && data.getCurrentState() == REGENERATING) {
                e.setRed((float) data.getPrimaryColors().x);
                e.setGreen((float) data.getPrimaryColors().y);
                e.setBlue((float) data.getPrimaryColors().z);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderOverlay(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            ClientPlayerEntity player = Minecraft.getInstance().player;

            RegenCap.get(player).ifPresent((cap) -> {
                String warning = null;

                switch (cap.getCurrentState()) {
                    case GRACE:
                        RenderHelp.renderVig(cap.getPrimaryColors(), 0.3F);
                        //TODO FIX NULL TRANSLATIONS
                        warning = new TranslationTextComponent("regeneration.messages.warning.grace", new TranslationTextComponent("ClientUtil.keyBind")).getUnformattedComponentText();
                        break;

                    case GRACE_CRIT:
                        RenderHelp.renderVig(new Vector3d(1, 0, 0), 0.5F);
                        warning = new TranslationTextComponent("regeneration.messages.warning.grace_critical", "ClientUtil.keyBind").getUnformattedComponentText();
                        break;

                    case REGENERATING:
                        RenderHelp.renderVig(cap.getSecondaryColors(), 0.5F);
                        break;

                    case POST:
                        if (player.hurtTime > 0 || player.getActivePotionEffect(Effects.NAUSEA) != null) {
                            RenderHelp.renderVig(cap.getSecondaryColors(), 0.5F);
                        }
                        break;
                }

                IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
                if (warning != null)
                Minecraft.getInstance().fontRenderer.renderString(warning, Minecraft.getInstance().getMainWindow().getScaledWidth() / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(warning) / 2, 4, TextFormatting.WHITE.getColor(), false, TransformationMatrix.identity().getMatrix(), irendertypebuffer$impl, false, 0, 15728880);
                irendertypebuffer$impl.finish();
            });
        }
    }


    @SubscribeEvent
    public static void onSetupFogDensity(EntityViewRenderEvent.RenderFogEvent.FogDensity event) {
        Entity viewer = Minecraft.getInstance().getRenderViewEntity();
        if (viewer != null) {
            RegenCap.get((LivingEntity) viewer).ifPresent((data) -> {
                if (data.getCurrentState() == GRACE_CRIT) {
                    event.setCanceled(true);
                    float amount = MathHelper.cos(data.getLiving().ticksExisted * 0.02F) * -0.10F;
                    event.setDensity(amount);
                }

                if (data.getTransitionType() == TransitionTypes.HARTNELL && data.getTicksAnimating() > 0) {
                    event.setCanceled(true);
                    float opacity = MathHelper.clamp(MathHelper.sin((viewer.ticksExisted + Minecraft.getInstance().getRenderPartialTicks()) / 10F) * 0.1F + 0.1F, 0.11F, 1F);
                    event.setDensity(opacity);
                }

            });
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntityLiving();
            SkinHandler.PLAYER_SKINS.remove(player.getUniqueID());

            if (player.getUniqueID().equals(Minecraft.getInstance().player.getUniqueID())) {
                SkinHandler.sendResetMessage();
            }
        }
    }


    @SubscribeEvent
    public static void keyInput(InputUpdateEvent e) {
        if (Minecraft.getInstance().player == null) return;

        RegenCap.get(Minecraft.getInstance().player).ifPresent((data -> {
            if (data.getCurrentState() == REGENERATING) { // locking user
                MovementInput moveType = e.getMovementInput();
                moveType.rightKeyDown = false;
                moveType.leftKeyDown = false;
                moveType.backKeyDown = false;
                moveType.jump = false;
                moveType.moveForward = 0.0F;
                moveType.sneaking = false;
                moveType.moveStrafe = 0.0F;
            }
        }));
    }

}
