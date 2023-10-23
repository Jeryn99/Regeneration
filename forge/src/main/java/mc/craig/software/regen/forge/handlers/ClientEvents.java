package mc.craig.software.regen.forge.handlers;

import mc.craig.software.regen.client.RKeybinds;
import mc.craig.software.regen.client.screen.overlay.RegenerationOverlay;
import mc.craig.software.regen.client.skin.VisualManipulator;
import mc.craig.software.regen.client.visual.FogTracker;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.regen.transitions.TransitionTypeRenderers;
import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.FoliageColor;
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
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();
        RegenerationData.get(player).ifPresent(iRegen -> {
            TransitionType type = iRegen.transitionType();
            TransitionTypeRenderers.get(type).onPlayerRenderPost(event.getEntity(), event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        });
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        VisualManipulator.tick(player);
        RegenerationData.get(player).ifPresent(iRegen -> TransitionTypeRenderers.get(iRegen.transitionType()).firstPersonHand(event.getHand(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), event.getPartialTick(), event.getSwingProgress(), event.getEquipProgress(), event.getItemStack()));
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
        RegenerationOverlay.renderUi(event.getGuiGraphics());
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
        ClientUtil.handleInput(player, e.getInput());
    }


}
