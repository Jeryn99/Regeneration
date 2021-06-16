package me.suff.mc.regen.handlers;

import com.mojang.blaze3d.platform.GlStateManager;
import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.client.skinhandling.SkinManipulation;
import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.dimension.biomes.GallifrayanWastelands;
import me.suff.mc.regen.common.types.RegenTypes;
import me.suff.mc.regen.proxy.ClientProxy;
import me.suff.mc.regen.util.client.ClientUtil;
import me.suff.mc.regen.util.client.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.HandSide;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static me.suff.mc.regen.compat.ArchHelper.getRegenerations;
import static me.suff.mc.regen.compat.ArchHelper.hasRegenerations;
import static me.suff.mc.regen.util.common.PlayerUtil.RegenState;
import static me.suff.mc.regen.util.common.PlayerUtil.isZeroRoom;

/**
 * Created by Craig on 16/09/2018.
 */
public class ClientHandler {

    public static String getColoredText(String msg) {
        return msg.replaceAll("&", String.valueOf('\u00a7'));
    }

    private static void createWorldAmbience(PlayerEntity player) {
        Random random = player.level.random;
        double originX = player.x;
        double originY = player.y;
        double originZ = player.z;
        for (int i = 0; i < 55; i++) {
            double particleX = originX + (random.nextInt(24) - random.nextInt(24));
            double particleY = originY + (random.nextInt(24) - random.nextInt(24));
            double particleZ = originZ + (random.nextInt(24) - random.nextInt(24));
            double velocityX = (random.nextDouble() - 0.5) * 0.02;
            double velocityY = (random.nextDouble() - 0.5) * 0.02;
            double velocityZ = (random.nextDouble() - 0.5) * 0.02;

            BasicParticleType[] TYPES = new BasicParticleType[]{ParticleTypes.SMOKE, ParticleTypes.CRIT, ParticleTypes.BARRIER};
            IParticleData currentType = TYPES[player.level.random.nextInt(TYPES.length - 1)];
            player.level.addParticle(currentType, particleX, particleY, particleZ, velocityX, velocityY, velocityZ);
            if (player.level.random.nextInt(30) < 10 && player.level.random.nextBoolean()) {
                BlockPos pos = new BlockPos(particleX + random.nextInt(500), particleY + random.nextInt(500), particleZ + random.nextInt(500));
                player.level.addFreshEntity(new LightningBoltEntity(player.level, pos.getX(), pos.getY(), pos.getZ(), true));
            }
        }
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) return;
        if (Minecraft.getInstance().level == null) {
            if (SkinManipulation.PLAYER_SKINS.size() > 0) {
                SkinManipulation.PLAYER_SKINS.forEach(((uuid, skinInfo) -> Minecraft.getInstance().getTextureManager().release(skinInfo.getTextureLocation())));
                SkinManipulation.PLAYER_SKINS.clear();
                Regeneration.LOG.warn("CLEARED CACHE OF PLAYER_SKINS");
            }
        }
    }

    @SubscribeEvent
    public void onClientUpdate(LivingEvent.LivingUpdateEvent e) {
        if (!(e.getEntity() instanceof PlayerEntity) || Minecraft.getInstance().player == null) return;

        PlayerEntity player = (PlayerEntity) e.getEntity();
        Minecraft.getInstance().submit(() -> {

            if (player.tickCount == 50) {

                UUID clientUUID = Minecraft.getInstance().player.getUUID();
                RegenCap.get(player).ifPresent((data) -> {
                    if (data.areHandsGlowing()) {
                        ClientUtil.playSound(data.getLivingEntity(), RegenObjects.Sounds.HAND_GLOW.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !data.areHandsGlowing(), 0.2F);
                    }

                    if (data.getState() == RegenState.REGENERATING) {
                        ClientUtil.playSound(data.getLivingEntity(), RegenObjects.Sounds.REGENERATION_0.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !data.getState().equals(RegenState.REGENERATING), 1.0F);
                    }

                    if (data.getState().isGraceful() && clientUUID == player.getUUID()) {
                        ClientUtil.playSound(data.getLivingEntity(), RegenObjects.Sounds.CRITICAL_STAGE.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !data.getState().equals(RegenState.GRACE_CRIT), 1F);
                        ClientUtil.playSound(data.getLivingEntity(), RegenObjects.Sounds.HEART_BEAT.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !data.getState().isGraceful(), 0.2F);
                        ClientUtil.playSound(data.getLivingEntity(), RegenObjects.Sounds.GRACE_HUM.get().getRegistryName(), SoundCategory.AMBIENT, true, () -> data.getState() != RegenState.GRACE, 1.5F);
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

        RegenCap.get(player).ifPresent((cap) -> {
            String warning = null;

            switch (cap.getState()) {
                case ALIVE:
                    break;
                case GRACE:
                    RenderUtil.renderVignette(cap.getPrimaryColor(), 0.3F, cap.getState());
                    warning = new TranslationTextComponent("regeneration.messages.warning.grace", new TranslationTextComponent(ClientUtil.keyBind)).getContents();
                    break;

                case GRACE_CRIT:
                    RenderUtil.renderVignette(new Vec3d(1, 0, 0), 0.5F, cap.getState());
                    warning = new TranslationTextComponent("regeneration.messages.warning.grace_critical", ClientUtil.keyBind).getContents();
                    break;

                case REGENERATING:
                    RenderUtil.renderVignette(cap.getSecondaryColor(), 0.5F, cap.getState());
                    break;

                case POST:
                    if (player.hurtTime > 0 || player.getEffect(Effects.CONFUSION) != null) {
                        RenderUtil.renderVignette(cap.getSecondaryColor(), 0.5F, cap.getState());
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + cap.getState());
            }

            if (warning != null)
                Minecraft.getInstance().font.draw(warning, Minecraft.getInstance().window.getGuiScaledWidth() / 2 - Minecraft.getInstance().font.width(warning) / 2, 4, 0xffffffff);
        });

    }

    @SubscribeEvent
    public void onColorFog(EntityViewRenderEvent.RenderFogEvent.FogColors e) {
        RegenCap.get(Minecraft.getInstance().getCameraEntity()).ifPresent((data) -> {
            if (data.getRegenType() == RegenTypes.HARTNELL && data.getState() == RegenState.REGENERATING) {
                e.setRed((float) data.getPrimaryColor().x);
                e.setGreen((float) data.getPrimaryColor().y);
                e.setBlue((float) data.getPrimaryColor().z);
            }
        });
    }

    @SubscribeEvent
    public void onSetupFogDensity(EntityViewRenderEvent.RenderFogEvent.FogDensity event) {
        Entity viewer = Minecraft.getInstance().getCameraEntity();
        if (viewer != null) {
            RegenCap.get(viewer).ifPresent((data) -> {
                if (data.getState() == RegenState.GRACE_CRIT) {
                    event.setCanceled(true);
                    float amount = MathHelper.cos(data.getLivingEntity().tickCount * 0.06F) * -0.09F;
                    event.setDensity(amount);
                }

                if (data.getRegenType() == RegenTypes.HARTNELL && data.getAnimationTicks() > 0) {
                    event.setCanceled(true);
                    float opacity = MathHelper.clamp(MathHelper.sin((viewer.tickCount + Minecraft.getInstance().getFrameTime()) / 10F) * 0.1F + 0.1F, 0.11F, 1F);
                    event.setDensity(opacity);
                }

            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientChatRecieved(ClientChatReceivedEvent e) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (e.getType() != ChatType.CHAT || isZeroRoom(player)) return;

        IRegen data = RegenCap.get(player).orElse(null);
        if (data.getState() != RegenState.POST) return;

        if (player.level.random.nextBoolean()) {
            String message = e.getMessage().getContents();
            StringTextComponent newMessage = new StringTextComponent("");
            String[] words = message.split(" ");
            for (String word : words) {
                if (word.equals(words[0])) {
                    StringTextComponent name = new StringTextComponent(word + " ");
                    newMessage.append(name);
                    continue;
                }
                if (player.level.random.nextBoolean()) {
                    StringTextComponent txtComp = new StringTextComponent(getColoredText("&k" + word + "&r "));
                    txtComp.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(word)));
                    newMessage.append(txtComp);
                } else {
                    StringTextComponent txtComp = new StringTextComponent(word + " ");
                    newMessage.append(txtComp);
                }
            }
            e.setMessage(newMessage);
        }
    }

    @SubscribeEvent
    public void keyInput(InputUpdateEvent e) {
        if (Minecraft.getInstance().player == null) return;

        RegenCap.get(Minecraft.getInstance().player).ifPresent((data -> {
            if (data.getState() == RegenState.REGENERATING || data.isSyncingToJar()) { // locking user
                MovementInput moveType = e.getMovementInput();
                moveType.right = false;
                moveType.left = false;
                moveType.down = false;
                moveType.jumping = false;
                moveType.forwardImpulse = 0.0F;
                moveType.sneakKeyDown = false;
                moveType.leftImpulse = 0.0F;
            }
        }));
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntityLiving();
            SkinManipulation.PLAYER_SKINS.remove(player.getUUID());

            if (player.getUUID().equals(Minecraft.getInstance().player.getUUID())) {
                ClientUtil.sendSkinResetPacket();
            }
        }
    }


    @SubscribeEvent
    public void onRenderHand(RenderHandEvent e) {
        Minecraft mc = Minecraft.getInstance();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        float factor = 0.2F;
        if (player.getMainHandItem().getItem() != Items.AIR || mc.options.thirdPersonView > 0) return;

        BipedModel model = ClientProxy.getArmorModel(player.getItemBySlot(EquipmentSlotType.CHEST));

        RegenCap.get(player).ifPresent((data) -> {

            boolean flag = data.getState() == RegenState.REGENERATING;
            e.setCanceled(flag);

            if (!data.areHandsGlowing())// || !flag)
                return;

            GlStateManager.pushMatrix();

            float leftHandedFactor = mc.options.mainHand.equals(HandSide.RIGHT) ? 1 : -1;
            GlStateManager.translatef(0.33F * leftHandedFactor, -0.23F, -0.5F); // move in place
            GlStateManager.translatef(-.8F * player.attackAnim * leftHandedFactor, -.8F * player.attackAnim, -.4F * player.attackAnim); // compensate for 'punching' motion
            GlStateManager.translatef(-(player.yBob - player.yBobO) / 400F, (player.xBob - player.xBobO) / 500F, 0); // compensate for 'swinging' motion

            RenderUtil.setupRenderLightning();
            GlStateManager.rotatef((mc.player.tickCount + RenderUtil.renderTick) / 2F, 0, 1, 0);
            for (int i = 0; i < 15; i++) {
                GlStateManager.rotatef((mc.player.tickCount + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
                Vec3d primaryColor = data.getPrimaryColor();

                Random rand = player.level.random;
                RenderUtil.drawGlowingLine(new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, primaryColor, 0);
            }
            RenderUtil.finishRenderLightning();

            GlStateManager.popMatrix();
        });
    }

    @SubscribeEvent
    public void onItemToolTip(ItemTooltipEvent event) {
        List<ITextComponent> tooltip = event.getToolTip();
        ItemStack stack = event.getItemStack();
        if (hasRegenerations(stack)) {
            tooltip.add(new TranslationTextComponent("nbt.regeneration.item.stored_regens", getRegenerations(stack)));
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null) return;
        PlayerEntity player = Minecraft.getInstance().player;

        if (player.level.getBiome(player.getCommandSenderBlockPosition()) instanceof GallifrayanWastelands) {
            createWorldAmbience(player);
        }
    }

}
