package me.swirtzly.regeneration.client;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.animation.AnimationContext;
import me.swirtzly.regeneration.client.animation.AnimationHandler;
import me.swirtzly.regeneration.client.animation.ModelRotationEvent;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.network.MessageRepairArms;
import me.swirtzly.regeneration.network.MessageTriggerForcedRegen;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.EnumCompatModids;
import me.swirtzly.regeneration.util.RenderUtil;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.HandSide;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;
import java.util.UUID;

import static me.swirtzly.regeneration.util.PlayerUtil.RegenState.*;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = RegenerationMod.MODID)
public class ClientEventHandler {

    private static boolean initialJoin = false;

    @SubscribeEvent
    public static void onGui(InputUpdateEvent tickEvent) {

        if (RegenKeyBinds.REGEN_FORCEFULLY.isPressed()) {
            NetworkHandler.INSTANCE.sendToServer(new MessageTriggerForcedRegen());
        }

        if (EnumCompatModids.LCCORE.isLoaded()) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.currentScreen == null && minecraft.player != null) {
            ClientUtil.keyBind = RegenKeyBinds.getRegenerateNowDisplayName();
        }
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity && !initialJoin) {
            if (Minecraft.getInstance().player == event.getEntity()) {
                ClientPlayerEntity localPlayer = Minecraft.getInstance().player;
                NetworkHandler.INSTANCE.sendToServer(new MessageRepairArms(localPlayer.getSkinType().equals("slim") ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE));
                initialJoin = true;
            }
        }
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) return;
        if (Minecraft.getInstance().world == null) {
            if (SkinChangingHandler.PLAYER_SKINS.size() > 0 || SkinChangingHandler.TYPE_BACKUPS.size() > 0) {
                SkinChangingHandler.TYPE_BACKUPS.clear();
                SkinChangingHandler.PLAYER_SKINS.clear();
                RegenerationMod.LOG.warn("CLEARED CACHE OF PLAYER_SKINS AND TYPE_BACKUPS");
            }
            initialJoin = false;
        }
    }

    @SubscribeEvent
    public static void onClientUpdate(LivingEvent.LivingUpdateEvent e) {
        if (!(e.getEntity() instanceof PlayerEntity) || Minecraft.getInstance().player == null)
            return;

        PlayerEntity player = (PlayerEntity) e.getEntity();
        Minecraft.getInstance().runAsync(() -> {

            if (player.ticksExisted == 50) {

                UUID clientUUID = Minecraft.getInstance().player.getUniqueID();
                IRegeneration cap = CapabilityRegeneration.getForPlayer(player);

                if (cap.areHandsGlowing()) {
                    ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 0.5F);
                }

                if (cap.getState() == REGENERATING) {
                    ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.REGENERATION.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().equals(REGENERATING), 1.0F);
                }

                if (cap.getState().isGraceful() && clientUUID == player.getUniqueID()) {
                    ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.CRITICAL_STAGE.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().equals(GRACE_CRIT), 1F);
                    ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HEART_BEAT.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().isGraceful(), 0.2F);
                    ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.GRACE_HUM.getRegistryName(), SoundCategory.AMBIENT, true, () -> cap.getState() != GRACE, 1.5F);
                }
            }

        });

    }

    @SubscribeEvent(receiveCanceled = true)
    public static void onAnimate(ModelRotationEvent ev) {
        if (EnumCompatModids.LCCORE.isLoaded()) return;
        if (ev.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) ev.getEntity();
            IRegeneration data = CapabilityRegeneration.getForPlayer(player).orElse(null);
            AnimationContext context = new AnimationContext(ev.model, player, ev.limbSwing, ev.limbSwingAmount, ev.ageInTicks, ev.netHeadYaw, ev.headPitch);
            if (data.getState() == REGENERATING) {
                ev.setCanceled(TypeHandler.getTypeInstance(data.getType()).getRenderer().onAnimateRegen(context));
            } else {
                AnimationHandler.animate(context);
            }
        }
    }

    @SuppressWarnings("incomplete-switch")
    @SubscribeEvent
    public static void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;

        SkinInfo skin = SkinChangingHandler.PLAYER_SKINS.get(player.getUniqueID());
        if (skin != null) {
            SkinChangingHandler.setPlayerSkin(player, skin.getSkinTextureLocation());
        }

        CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
            String warning = null;

            switch (cap.getState()) {
                case GRACE:
                    RenderUtil.renderVignette(cap.getPrimaryColor(), 0.3F, cap.getState());
                    warning = new TranslationTextComponent("regeneration.messages.warning.grace", ClientUtil.keyBind).getUnformattedComponentText();
                    break;

                case GRACE_CRIT:
                    RenderUtil.renderVignette(new Vec3d(1, 0, 0), 0.5F, cap.getState());
                    warning = new TranslationTextComponent("regeneration.messages.warning.grace_critical", ClientUtil.keyBind).getUnformattedComponentText();
                    break;

                case REGENERATING:
                    RenderUtil.renderVignette(cap.getSecondaryColor(), 0.5F, cap.getState());
                    break;

                case POST:
                    if (player.hurtTime > 0 || player.getActivePotionEffect(Effects.NAUSEA) != null) {
                        RenderUtil.renderVignette(cap.getSecondaryColor(), 0.5F, cap.getState());
                    }
                    break;
            }

            if (warning != null)
                Minecraft.getInstance().fontRenderer.drawString(warning, new MainWindow(Minecraft.getInstance()).getScaledWidth() / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(warning) / 2, 4, 0xffffffff);

        });


    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null)
            return;

        CapabilityRegeneration.getForPlayer(mc.player).ifPresent((cap) -> {

            if (e.getName().equals("entity.generic.explode")) {
                ISound sound = PositionedSoundRecord.getRecord(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 0.2F);

                for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
                    if (mc.player != player && mc.player.getDistance(player) < 40) {
                        if (cap.getState().equals(REGENERATING)) {
                            e.setResultSound(sound);
                        }
                    }
                }

                if (cap.getState() == REGENERATING) {
                    e.setResultSound(sound);
                }
            }
        });

    }


    @SubscribeEvent
    public static void onSetupFogDensity(EntityViewRenderEvent.RenderFogEvent.FogDensity event) {
        IRegeneration data = CapabilityRegeneration.getForPlayer(Minecraft.getInstance().player).orElse(null);
        if (data.getState() == GRACE_CRIT) {
            GlStateManager.fogMode(GlStateManager.FogMode.EXP);
            event.setCanceled(true);
            float amount = MathHelper.cos(data.getPlayer().ticksExisted * 0.06F) * -0.09F;
            event.setDensity(amount);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientChatRecieved(ClientChatReceivedEvent e) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (e.getType() != ChatType.CHAT) return;

        IRegeneration data = CapabilityRegeneration.getForPlayer(player).orElse(null);
        if (data.getState() != POST) return;

        if (player.world.rand.nextBoolean()) {
            String message = e.getMessage().getUnformattedComponentText();
            StringTextComponent newMessage = new StringTextComponent("");
            String[] words = message.split(" ");
            for (String word : words) {
                if (word.equals(words[0])) {
                    StringTextComponent name = new StringTextComponent(word + " ");
                    newMessage.appendSibling(name);
                    continue;
                }
                if (player.world.rand.nextBoolean()) {
                    StringTextComponent txtComp = new StringTextComponent(getColoredText("&k" + word + "&r "));
                    txtComp.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(word)));
                    newMessage.appendSibling(txtComp);
                } else {
                    StringTextComponent txtComp = new StringTextComponent(word + " ");
                    newMessage.appendSibling(txtComp);
                }
            }
            e.setMessage(newMessage);
        }
    }

    public static String getColoredText(String msg) {
        return msg.replaceAll("&", String.valueOf('\u00a7'));
    }

    @SubscribeEvent
    public static void keyInput(InputUpdateEvent e) {
        if (Minecraft.getInstance().player == null)
            return;

        CapabilityRegeneration.getForPlayer(Minecraft.getInstance().player).ifPresent((data -> {
            if (data.getState() == REGENERATING || data.isSyncingToJar()) { // locking user
                MovementInput moveType = e.getMovementInput();
                moveType.rightKeyDown = false;
                moveType.leftKeyDown = false;
                moveType.backKeyDown = false;
                moveType.jump = false;
                moveType.moveForward = 0.0F;
                moveType.sneak = false;
                moveType.moveStrafe = 0.0F;
            }
        }));
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntityLiving();
            SkinChangingHandler.PLAYER_SKINS.remove(player.getUniqueID());

            if (player.getUniqueID().equals(Minecraft.getInstance().player.getUniqueID())) {
                ClientUtil.sendSkinResetPacket();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent e) {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = Minecraft.getInstance().player;

        float factor = 0.2F;
        if (player.getHeldItemMainhand().getItem() != Items.AIR || mc.gameSettings.thirdPersonView > 0)
            return;

        CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> {

            boolean flag = data.getType() == TypeHandler.RegenType.CONFUSED && data.getState() == REGENERATING;
            e.setCanceled(flag);

            if (!data.areHandsGlowing() || !flag)
                return;


            GlStateManager.pushMatrix();

            float leftHandedFactor = mc.gameSettings.mainHand.equals(HandSide.RIGHT) ? 1 : -1;
            GlStateManager.translatef(0.33F * leftHandedFactor, -0.23F, -0.5F); // move in place
            GlStateManager.translatef(-.8F * player.swingProgress * leftHandedFactor, -.8F * player.swingProgress, -.4F * player.swingProgress); // compensate for 'punching' motion
            GlStateManager.translatef(-(player.renderArmYaw - player.prevRenderArmYaw) / 400F, (player.renderArmPitch - player.prevRenderArmPitch) / 500F, 0); // compensate for 'swinging' motion

            RenderUtil.setupRenderLightning();
            GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
            for (int i = 0; i < 15; i++) {
                GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
                Vec3d primaryColor = data.getPrimaryColor();

                Random rand = player.world.rand;
                RenderUtil.drawGlowingLine(new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, primaryColor, 0);
            }
            RenderUtil.finishRenderLightning();

            GlStateManager.popMatrix();
        });
    }

}
