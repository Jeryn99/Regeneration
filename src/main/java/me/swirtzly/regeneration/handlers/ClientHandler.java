package me.swirtzly.regeneration.handlers;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.RegenKeyBinds;
import me.swirtzly.regeneration.client.gui.CustomizerScreen;
import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.network.AdjustArmsMessage;
import me.swirtzly.regeneration.network.ForceRegenerationMessage;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.EnumCompatModids;
import me.swirtzly.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
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
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;
import java.util.UUID;

import static me.swirtzly.regeneration.util.PlayerUtil.RegenState.*;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class ClientHandler {

    private static boolean initialJoin = false;

    private static final ResourceLocation ACCESSIBILITY_TEXTURES = new ResourceLocation("textures/gui/accessibility.png");


    @SubscribeEvent
    public void onGui(GuiScreenEvent.InitGuiEvent event){
        if(event.getGui() instanceof InventoryScreen){
            int j = event.getGui().height / 4 + 48;
            event.addWidget(new ImageButton(((InventoryScreen) event.getGui()).getGuiLeft() + 134, event.getGui().height / 2 - 22, 20, 20, 0, 0, 20, ACCESSIBILITY_TEXTURES, 32, 64, (p_213088_1_) -> {
                Minecraft.getInstance().displayGuiScreen(new CustomizerScreen());
            }, I18n.format("narrator.button.accessibility")));
        }
    }

    @SubscribeEvent
    public void onKeybindPress(InputUpdateEvent tickEvent) {

        if (RegenKeyBinds.REGEN_FORCEFULLY.isPressed()) {
            NetworkDispatcher.sendToServer(new ForceRegenerationMessage());
        }

        if (EnumCompatModids.LCCORE.isLoaded()) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.currentScreen == null && minecraft.player != null) {
            ClientUtil.keyBind = RegenKeyBinds.getRegenerateNowDisplayName();
        }
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity && !initialJoin) {
            if (Minecraft.getInstance().player == event.getEntity()) {
                ClientPlayerEntity localPlayer = Minecraft.getInstance().player;
                NetworkDispatcher.INSTANCE.sendToServer(new AdjustArmsMessage(localPlayer.getSkinType().equals("slim") ? SkinInfo.SkinType.ALEX : SkinInfo.SkinType.STEVE));
                initialJoin = true;
            }
        }
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) return;
        if (Minecraft.getInstance().world == null) {
            if (SkinManipulation.PLAYER_SKINS.size() > 0 || SkinManipulation.TYPE_BACKUPS.size() > 0) {
                SkinManipulation.TYPE_BACKUPS.clear();
                SkinManipulation.PLAYER_SKINS.clear();
                RegenerationMod.LOG.warn("CLEARED CACHE OF PLAYER_SKINS AND TYPE_BACKUPS");
            }
            initialJoin = false;
        }
    }

    @SubscribeEvent
    public void onClientUpdate(LivingEvent.LivingUpdateEvent e) {
        if (!(e.getEntity() instanceof PlayerEntity) || Minecraft.getInstance().player == null)
            return;

        PlayerEntity player = (PlayerEntity) e.getEntity();
        Minecraft.getInstance().runAsync(() -> {

            if (player.ticksExisted == 50) {

                UUID clientUUID = Minecraft.getInstance().player.getUniqueID();
                RegenCap.get(player).ifPresent((data) -> {
                    if (data.areHandsGlowing()) {
                        ClientUtil.playSound(data.getPlayer(), RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.PLAYERS, true, () -> !data.areHandsGlowing(), 0.5F);
                    }

                    if (data.getState() == REGENERATING) {
                        ClientUtil.playSound(data.getPlayer(), RegenObjects.Sounds.REGENERATION.getRegistryName(), SoundCategory.PLAYERS, true, () -> !data.getState().equals(REGENERATING), 1.0F);
                    }

                    if (data.getState().isGraceful() && clientUUID == player.getUniqueID()) {
                        ClientUtil.playSound(data.getPlayer(), RegenObjects.Sounds.CRITICAL_STAGE.getRegistryName(), SoundCategory.PLAYERS, true, () -> !data.getState().equals(GRACE_CRIT), 1F);
                        ClientUtil.playSound(data.getPlayer(), RegenObjects.Sounds.HEART_BEAT.getRegistryName(), SoundCategory.PLAYERS, true, () -> !data.getState().isGraceful(), 0.2F);
                        ClientUtil.playSound(data.getPlayer(), RegenObjects.Sounds.GRACE_HUM.getRegistryName(), SoundCategory.AMBIENT, true, () -> data.getState() != GRACE, 1.5F);
                    }
                });
            }

        });

    }

    @SuppressWarnings("incomplete-switch")
    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;

        SkinInfo skin = SkinManipulation.PLAYER_SKINS.get(player.getUniqueID());
        if (skin != null) {
            SkinManipulation.setPlayerSkin(player, skin.getSkinTextureLocation());
        }

        RegenCap.get(player).ifPresent((cap) -> {
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
                Minecraft.getInstance().fontRenderer.drawString(warning, Minecraft.getInstance().mainWindow.getScaledWidth() / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(warning) / 2, 4, 0xffffffff);
        });


    }

    @SubscribeEvent
    public void onPlaySound(PlaySoundEvent e) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null)
            return;

        RegenCap.get(mc.player).ifPresent((cap) -> {

            if (e.getName().equals("entity.generic.explode")) {
                ISound sound = SimpleSound.master(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 0.2F);

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
    public void onSetupFogDensity(EntityViewRenderEvent.RenderFogEvent.FogDensity event) {
        RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> {
            if (data.getState() == GRACE_CRIT) {
                GlStateManager.fogMode(GlStateManager.FogMode.EXP);
                event.setCanceled(true);
                float amount = MathHelper.cos(data.getPlayer().ticksExisted * 0.06F) * -0.09F;
                event.setDensity(amount);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientChatRecieved(ClientChatReceivedEvent e) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (e.getType() != ChatType.CHAT) return;

        IRegen data = RegenCap.get(player).orElse(null);
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
    public void keyInput(InputUpdateEvent e) {
        if (Minecraft.getInstance().player == null)
            return;

        RegenCap.get(Minecraft.getInstance().player).ifPresent((data -> {
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
    public void onDeath(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntityLiving();
            SkinManipulation.PLAYER_SKINS.remove(player.getUniqueID());

            if (player.getUniqueID().equals(Minecraft.getInstance().player.getUniqueID())) {
                ClientUtil.sendSkinResetPacket();
            }
        }
    }

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent e) {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = Minecraft.getInstance().player;

        float factor = 0.2F;
        if (player.getHeldItemMainhand().getItem() != Items.AIR || mc.gameSettings.thirdPersonView > 0)
            return;

        RegenCap.get(player).ifPresent((data) -> {

            boolean flag = data.getType() == TypeManager.Type.CONFUSED && data.getState() == REGENERATING;
            e.setCanceled(flag);

            if (!data.areHandsGlowing())//|| !flag)
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
