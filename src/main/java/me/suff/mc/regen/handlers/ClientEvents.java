package me.suff.mc.regen.handlers;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.client.RKeybinds;
import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.client.skin.SkinHandler;
import me.suff.mc.regen.common.item.GunItem;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RenderHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    public static ResourceLocation OLD = new ResourceLocation("textures/gui/icons.png");
    public static ResourceLocation NEW = new ResourceLocation(RConstants.MODID, "textures/gui/icons.png");
    public static ResourceLocation HEARTS = new ResourceLocation(RConstants.MODID, "textures/gui/regen_hearts.png");

    @SubscribeEvent
    public static void onName(RenderNameplateEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.getCurrentState() == RegenStates.POST || iRegen.getCurrentState() == RegenStates.GRACE_CRIT) {
                event.setContent(new StringTextComponent(TextFormatting.OBFUSCATED + event.getContent().getString()));
            }
        });
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre playerEvent) {
        PlayerEntity player = playerEvent.getPlayer();
        SkinHandler.tick((AbstractClientPlayerEntity) playerEvent.getPlayer());
        RegenCap.get(player).ifPresent(iRegen -> {
            TransitionType< ? > type = iRegen.getTransitionType().get();
            type.getRenderer().onPlayerRenderPre(playerEvent);
        });
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post playerEvent) {
        PlayerEntity player = playerEvent.getPlayer();
        RegenCap.get(player).ifPresent(iRegen -> {
            TransitionType< ? > type = iRegen.getTransitionType().get();
            type.getRenderer().onPlayerRenderPost(playerEvent);
        });
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        RegenCap.get(Minecraft.getInstance().player).ifPresent(iRegen -> iRegen.getTransitionType().get().getRenderer().firstPersonHand(event));
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) return;

        RKeybinds.tickKeybinds();

        //Clean up our mess we might have made!
        if (Minecraft.getInstance().world == null) {
            if (SkinHandler.PLAYER_SKINS.size() > 0) {
                SkinHandler.PLAYER_SKINS.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().deleteTexture(texture)));
                SkinHandler.PLAYER_SKINS.clear();
                TimelordRenderer.TIMELORDS.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().deleteTexture(texture)));
                TimelordRenderer.TIMELORDS.clear();
                Regeneration.LOG.warn("Cleared Regeneration texture cache.");
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

            if (data.getCurrentState() == RegenStates.GRACE_CRIT) {
                e.setRed(0.5F);
                e.setBlue(0.5F);
                e.setGreen(0.5F);
            }

            if (data.getTransitionType() == TransitionTypes.TROUGHTON && data.getCurrentState() == RegenStates.REGENERATING) {
                e.setRed(0);
                e.setGreen(0);
                e.setBlue(0);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        RegenCap.get(player).ifPresent((cap) -> {
            String warning = null;

            if (player.getHeldItemMainhand().getItem() instanceof GunItem && player.getItemInUseCount() > 0) {
                AbstractGui.GUI_ICONS_LOCATION = NEW;
                if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS && event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
                    event.setCanceled(true);
                }
            } else {
                AbstractGui.GUI_ICONS_LOCATION = cap.getRegens() > 0 && RegenConfig.CLIENT.heartIcons.get() ? HEARTS : OLD;
            }


            if (event.getType() != RenderGameOverlayEvent.ElementType.HELMET) return;
            if (cap.getCurrentState() == RegenStates.REGENERATING && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                event.setCanceled(true);
            }


            ITextComponent forceKeybind = new TranslationTextComponent(RKeybinds.FORCE_REGEN.getTranslationKey().replace("key.keyboard.", "").toUpperCase());

            switch (cap.getCurrentState()) {
                case ALIVE:
                    break;
                case GRACE:
                    RenderHelp.renderVig(cap.getPrimaryColors(), 0.3F);
                    warning = new TranslationTextComponent("regen.messages.warning.grace", forceKeybind.getString()).getString();
                    break;
                case GRACE_CRIT:
                    RenderHelp.renderVig(new Vector3d(1, 0, 0), 0.5F);
                    warning = new TranslationTextComponent("regen.messages.warning.grace_critical", forceKeybind.getString()).getString();
                    break;

                case REGENERATING:
                    RenderHelp.renderVig(cap.getSecondaryColors(), 0.5F);
                    break;

                case POST:
                    if (player.hurtTime > 0 || player.getActivePotionEffect(Effects.NAUSEA) != null) {
                        RenderHelp.renderVig(cap.getSecondaryColors(), 0.5F);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + cap.getCurrentState());
            }

            if (cap.areHandsGlowing()) {
                RenderHelp.renderVig(TransitionTypes.FIERY.get().getDefaultPrimaryColor(), 0.5F);
            }

            IRenderTypeBuffer.Impl renderImpl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            if (warning != null)
                Minecraft.getInstance().fontRenderer.renderString(warning, Minecraft.getInstance().getMainWindow().getScaledWidth() / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(warning) / 2, 4, TextFormatting.WHITE.getColor(), false, TransformationMatrix.identity().getMatrix(), renderImpl, false, 0, 15728880);
            renderImpl.finish();
        });
    }

    @SubscribeEvent
    public static void onSetupFogDensity(EntityViewRenderEvent.RenderFogEvent.FogDensity event) {
        Entity viewer = Minecraft.getInstance().getRenderViewEntity();
        if (viewer != null) {
            RegenCap.get((LivingEntity) viewer).ifPresent((data) -> {
                if (data.getCurrentState() == RegenStates.GRACE_CRIT) {
                    event.setCanceled(true);
                    float amount = MathHelper.cos(data.getLiving().ticksExisted * 0.02F) * -0.10F;
                    event.setDensity(amount);
                }
                if (data.getTransitionType() == TransitionTypes.TROUGHTON && data.getTicksAnimating() > 0) {
                    event.setCanceled(true);
                    event.setDensity(0.3F);
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
        ClientPlayerEntity player = Minecraft.getInstance().player;
        RegenCap.get(Minecraft.getInstance().player).ifPresent((data -> {
            if (data.getCurrentState() == RegenStates.REGENERATING) { // locking user
                MovementInput moveType = e.getMovementInput();
                moveType.rightKeyDown = false;
                moveType.leftKeyDown = false;
                moveType.backKeyDown = false;
                moveType.jump = false;
                moveType.moveForward = 0.0F;
                moveType.sneaking = false;
                moveType.moveStrafe = 0.0F;

                if (data.getTransitionType() == TransitionTypes.ENDER_DRAGON && RegenConfig.COMMON.allowUpwardsMotion.get()) {
                    if (player.getPosition().getY() <= 100) {
                        BlockPos upwards = player.getPosition().up(2);
                        BlockPos pos = upwards.subtract(player.getPosition());
                        Vector3d vec = new Vector3d(pos.getX(), pos.getY(), pos.getZ()).normalize();
                        player.setMotion(player.getMotion().add(vec.scale(0.10D)));
                    }
                }
            }
        }));
    }

}
