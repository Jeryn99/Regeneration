package craig.software.mc.regen.handlers;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Transformation;
import craig.software.mc.regen.Regeneration;
import craig.software.mc.regen.client.RKeybinds;
import craig.software.mc.regen.client.rendering.JarTileRender;
import craig.software.mc.regen.client.rendering.entity.TimelordRenderer;
import craig.software.mc.regen.client.skin.SkinHandler;
import craig.software.mc.regen.common.item.GunItem;
import craig.software.mc.regen.common.objects.RSounds;
import craig.software.mc.regen.common.regen.IRegen;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.state.RegenStates;
import craig.software.mc.regen.common.regen.transitions.TransitionType;
import craig.software.mc.regen.common.regen.transitions.TransitionTypeRenderers;
import craig.software.mc.regen.common.regen.transitions.TransitionTypes;
import craig.software.mc.regen.config.RegenConfig;
import craig.software.mc.regen.util.PlayerUtil;
import craig.software.mc.regen.util.RConstants;
import craig.software.mc.regen.util.RenderHelp;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    public static ResourceLocation OLD = GuiComponent.GUI_ICONS_LOCATION;
    public static ResourceLocation NEW = new ResourceLocation(RConstants.MODID, "textures/gui/crosshair.png");
    public static ResourceLocation HEARTS = new ResourceLocation(RConstants.MODID, "textures/gui/hearts.png");
    private static SoundInstance iSound = null;

    @SubscribeEvent
    public static void onName(RenderNameTagEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        RegenCap.get(player).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.POST || iRegen.regenState() == RegenStates.GRACE_CRIT) {
                event.setContent(Component.literal(ChatFormatting.OBFUSCATED + event.getContent().getString()));
            }
        });
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre playerEvent) {
        Player player = playerEvent.getEntity();
        SkinHandler.tick((AbstractClientPlayer) playerEvent.getEntity());
        RegenCap.get(player).ifPresent(iRegen -> {
            TransitionType type = iRegen.transitionType();
            TransitionTypeRenderers.get(type).onPlayerRenderPre(playerEvent);
        });
    }

    @SubscribeEvent
    public static void keyMapping(RegisterKeyMappingsEvent event){
        event.register(RKeybinds.FORCE_REGEN);
        event.register(RKeybinds.REGEN_GUI);
        event.register(RKeybinds.TOGGLE_TRAIT);
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post playerEvent) {
        Player player = playerEvent.getEntity();
        RegenCap.get(player).ifPresent(iRegen -> {
            TransitionType type = iRegen.transitionType();
            TransitionTypeRenderers.get(type).onPlayerRenderPost(playerEvent);
        });
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        RegenCap.get(Minecraft.getInstance().player).ifPresent(iRegen -> TransitionTypeRenderers.get(iRegen.transitionType()).firstPersonHand(event));
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) return;

        RKeybinds.tickKeybinds();

        if (Minecraft.getInstance().player != null) {
            LocalPlayer ep = Minecraft.getInstance().player;
            SoundManager sound = Minecraft.getInstance().getSoundManager();
            RegenCap.get(ep).ifPresent(iRegen -> {
                if (iRegen.regenState() == RegenStates.POST && PlayerUtil.isPlayerAboveZeroGrid(ep)) {

                    if (iSound == null) {
                        iSound = SimpleSoundInstance.forLocalAmbience(RSounds.GRACE_HUM.get(), 1, 1);
                    }

                    if (!sound.isActive(iSound)) {
                        sound.play(iSound);
                    }
                } else {
                    if (sound.isActive(iSound)) {
                        sound.stop(iSound);
                    }
                }
            });
        }


        destroyTextures();
    }

    private static void destroyTextures() {
        //Clean up our mess we might have made!
        if (Minecraft.getInstance().level == null) {
            if (SkinHandler.PLAYER_SKINS.size() > 0) {
                SkinHandler.PLAYER_SKINS.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().release(texture)));
                SkinHandler.PLAYER_SKINS.clear();
                TimelordRenderer.TIMELORDS.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().release(texture)));
                TimelordRenderer.TIMELORDS.clear();
                JarTileRender.TEXTURES.forEach(((uuid, texture) -> Minecraft.getInstance().getTextureManager().release(texture)));
                JarTileRender.TEXTURES.clear();
                Regeneration.LOG.warn("Cleared Regeneration texture cache");
            }
        }
    }

    @SubscribeEvent
    public static void onColorFog(ViewportEvent.ComputeFogColor e) {
        Entity renderView = Minecraft.getInstance().getCameraEntity();
        if (!(Minecraft.getInstance().getCameraEntity() instanceof LivingEntity)) {
            return;
        }

        RegenCap.get((LivingEntity) renderView).ifPresent((data) -> {

            if (data.regenState() == RegenStates.GRACE_CRIT) {
                e.setRed(0.5F);
                e.setBlue(0.5F);
                e.setGreen(0.5F);
            }

            if (data.transitionType() == TransitionTypes.TROUGHTON.get() && data.regenState() == RegenStates.REGENERATING) {
                e.setRed(0);
                e.setGreen(0);
                e.setBlue(0);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        LocalPlayer player = Minecraft.getInstance().player;
        RegenCap.get(player).ifPresent((cap) -> {
            String warning = null;

            handleGunCrosshair(event, player, cap);

            // if (event.getType() != RenderGameOverlayEvent.ElementType.HELMET) return;

            if (cap.regenState() == RegenStates.REGENERATING && event.getType() == ElementType.ALL) {
                event.setCanceled(true);
            }

            Component forceKeybind = RKeybinds.FORCE_REGEN.getKey().getDisplayName();
            RenderHelp.renderVig(cap.getPrimaryColors(), 0.3F);

            switch (cap.regenState()) {
                case GRACE:
                    RenderHelp.renderVig(cap.getPrimaryColors(), 0.3F);
                    warning = Component.translatable("regen.messages.warning.grace", forceKeybind.getString()).getString();
                    break;
                case GRACE_CRIT:
                    RenderHelp.renderVig(new Vec3(1, 0, 0), 0.5F);
                    warning = Component.translatable("regen.messages.warning.grace_critical", forceKeybind.getString()).getString();
                    break;

                case REGENERATING:
                    RenderHelp.renderVig(cap.getSecondaryColors(), 0.5F);
                    break;

                case POST:
                    if (player.hurtTime > 0 || player.getEffect(MobEffects.CONFUSION) != null) {
                        RenderHelp.renderVig(cap.getSecondaryColors(), 0.5F);
                    }
                    break;
            }

            if (cap.glowing()) {
                RenderHelp.renderVig(TransitionTypes.FIERY.get().getDefaultPrimaryColor(), 0.5F);
            }

            MultiBufferSource.BufferSource renderImpl = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            if (warning != null)
                Minecraft.getInstance().font.drawInBatch(warning, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - Minecraft.getInstance().font.width(warning) / 2, 4, ChatFormatting.WHITE.getColor(), false, Transformation.identity().getMatrix(), renderImpl, false, 0, 15728880);
            renderImpl.endBatch();
        });
    }

    private static void handleGunCrosshair(RenderGuiOverlayEvent.Pre event, LocalPlayer player, IRegen cap) {
        boolean gunSight = player.getMainHandItem().getItem() instanceof GunItem && player.getUseItemRemainingTicks() > 0;
        boolean healthCheck = event.getType().name().toLowerCase().contains("health");
        if (gunSight && healthCheck) {
            event.setCanceled(true);
        }
        GuiComponent.GUI_ICONS_LOCATION = gunSight ? HEARTS : OLD;
    }

    @SubscribeEvent
    public static void onSetupFogDensity(ViewportEvent.RenderFog event) {
        Entity viewer = Minecraft.getInstance().getCameraEntity();
        if (viewer instanceof LivingEntity livingEntity) {
            RegenCap.get(livingEntity).ifPresent((data) -> {


                if (data.regenState() == RegenStates.GRACE_CRIT) {
                    event.setCanceled(true);
                    event.setNearPlaneDistance(-8);
                    event.setFarPlaneDistance(22 * 0.5F);
                    event.setFogShape(FogShape.SPHERE);
                    return;
                }
                if (data.transitionType() == TransitionTypes.TROUGHTON.get() && data.updateTicks() > 0) {
                    event.setCanceled(true);
                    event.setNearPlaneDistance(-8);
                    event.setFarPlaneDistance(17 * 0.5F);
                    event.setFogShape(FogShape.SPHERE);
                    return;
                }
            });
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        if (e.getEntity() instanceof Player player) {
            SkinHandler.PLAYER_SKINS.remove(player.getUUID());

            if (player.getUUID().equals(Minecraft.getInstance().player.getUUID())) {
                SkinHandler.sendResetMessage();
            }
        }
    }

    @SubscribeEvent
    public static void keyInput(MovementInputUpdateEvent e) {
        if (Minecraft.getInstance().player == null) return;
        LocalPlayer player = Minecraft.getInstance().player;
        RegenCap.get(Minecraft.getInstance().player).ifPresent((data -> {
            if (data.regenState() == RegenStates.REGENERATING) { // locking user
                Input moveType = e.getInput();
                blockMovement(moveType);
                upwardsMovement(player, data);
            }
        }));
    }

    private static void upwardsMovement(LocalPlayer player, IRegen data) {
        if (data.transitionType() == TransitionTypes.ENDER_DRAGON.get() && RegenConfig.COMMON.allowUpwardsMotion.get()) {
            if (player.blockPosition().getY() <= 100) {
                BlockPos upwards = player.blockPosition().above(2);
                BlockPos pos = upwards.subtract(player.blockPosition());
                Vec3 vec = new Vec3(pos.getX(), pos.getY(), pos.getZ()).normalize();
                player.setDeltaMovement(player.getDeltaMovement().add(vec.scale(0.10D)));
            }
        }
    }

    private static void blockMovement(Input moveType) {
        moveType.right = false;
        moveType.left = false;
        moveType.down = false;
        moveType.jumping = false;
        moveType.forwardImpulse = 0.0F;
        moveType.shiftKeyDown = false;
        moveType.leftImpulse = 0.0F;
    }

}
