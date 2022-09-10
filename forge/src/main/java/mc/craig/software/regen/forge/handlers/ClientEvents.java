package mc.craig.software.regen.forge.handlers;

import mc.craig.software.regen.client.RKeybinds;
import mc.craig.software.regen.client.screen.overlay.RegenerationOverlay;
import mc.craig.software.regen.client.skin.VisualManipulator;
import mc.craig.software.regen.client.visual.FogTracker;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.regen.transitions.TransitionTypeRenderers;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

    @SubscribeEvent
    public static void onName(RenderNameTagEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        RegenerationData.get(player).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.POST || iRegen.regenState() == RegenStates.GRACE_CRIT) {
                event.setContent(Component.literal(ChatFormatting.OBFUSCATED + event.getContent().getString()));
            }
        });
    }


    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        VisualManipulator.tick((AbstractClientPlayer) event.getEntity());
        RegenerationData.get(player).ifPresent(iRegen -> {
            TransitionType type = iRegen.transitionType();
            TransitionTypeRenderers.get(type).onPlayerRenderPre(event.getEntity(), event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        });
    }

    @SubscribeEvent
    public static void keyMapping(RegisterKeyMappingsEvent event){
        event.register(RKeybinds.FORCE_REGEN);
        event.register(RKeybinds.REGEN_GUI);
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        RegenerationData.get(player).ifPresent(iRegen -> {
            TransitionType type = iRegen.transitionType();
            TransitionTypeRenderers.get(type).onPlayerRenderPost(event.getEntity(), event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        });
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        RegenerationData.get(Minecraft.getInstance().player).ifPresent(iRegen -> TransitionTypeRenderers.get(iRegen.transitionType()).firstPersonHand(event.getHand(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getPartialTick(), event.getSwingProgress(), event.getEquipProgress(), event.getItemStack()));
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) return;
        ClientUtil.tickClient();
    }

    @SubscribeEvent
    public static void onColorFog(ViewportEvent.ComputeFogColor e) {
        Vec3 color = FogTracker.getSitutionalFogColor();
        if (color != null) {
            e.setRed((float) color.x);
            e.setBlue((float) color.y);
            e.setGreen((float) color.z);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        RegenerationOverlay.renderUi(event.getPoseStack());
    }

    @SubscribeEvent
    public static void onSetupFogDensity(ViewportEvent.RenderFog event) {
        Entity viewer = Minecraft.getInstance().getCameraEntity();
        if (viewer instanceof LivingEntity livingEntity) {
            event.setCanceled(FogTracker.setUpFog(livingEntity));
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        if (e.getEntity() instanceof Player player) {
            VisualManipulator.PLAYER_SKINS.remove(player.getUUID());

            if (player.getUUID().equals(Minecraft.getInstance().player.getUUID())) {
                VisualManipulator.sendResetMessage();
            }
        }
    }

    @SubscribeEvent
    public static void keyInput(MovementInputUpdateEvent e) {
        if (Minecraft.getInstance().player == null) return;
        LocalPlayer player = Minecraft.getInstance().player;
        RegenerationData.get(Minecraft.getInstance().player).ifPresent((data -> {
            if (data.regenState() == RegenStates.REGENERATING) { // locking user
                Input moveType = e.getInput();
                blockMovement(moveType);
                upwardsMovement(player, data);
            }
        }));
    }

    private static void upwardsMovement(LocalPlayer player, IRegen data) {
        if (data.transitionType() == TransitionTypes.ENDER_DRAGON && RegenConfig.COMMON.allowUpwardsMotion.get()) {
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
