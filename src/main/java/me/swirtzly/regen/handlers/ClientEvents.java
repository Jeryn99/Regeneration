package me.swirtzly.regen.handlers;

import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.client.RKeybinds;
import me.swirtzly.regen.client.gui.PreferencesScreen;
import me.swirtzly.regen.client.rendering.entity.TimelordRenderer;
import me.swirtzly.regen.client.skin.SkinHandler;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import me.swirtzly.regen.util.RConstants;
import me.swirtzly.regen.util.RegenSources;
import me.swirtzly.regen.util.RenderHelp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static me.swirtzly.regen.common.regen.state.RegenStates.GRACE_CRIT;
import static me.swirtzly.regen.common.regen.state.RegenStates.REGENERATING;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    private static final ResourceLocation BUTTON_TEX = new ResourceLocation(RConstants.MODID, "textures/gui/gui_button_customize.png");


    @SubscribeEvent
    public static void onGui(GuiScreenEvent.InitGuiEvent event) {
        if (event.getGui() instanceof InventoryScreen) {
            RegenCap.get(Minecraft.getInstance().player).ifPresent((data) -> {
                if (data.canRegenerate()) {
                    event.addWidget(new ImageButton(((InventoryScreen) event.getGui()).getGuiLeft() + 134, event.getGui().height / 2 - 22, 20, 20, 0, 0, 20, BUTTON_TEX, 32, 64, (p_213088_1_) -> {
                        Minecraft.getInstance().displayGuiScreen(new PreferencesScreen());
                    }));
                }
            });
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre playerEvent) {
        SkinHandler.tick((AbstractClientPlayerEntity) playerEvent.getPlayer());
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

            if (data.getCurrentState() == GRACE_CRIT) {
                e.setRed(0.5F);
                e.setBlue(0.5F);
                e.setGreen(0.5F);
            }

            if (data.getTransitionType() == TransitionTypes.TROUGHTON && data.getCurrentState() == REGENERATING) {
                e.setRed(0);
                e.setGreen(0);
                e.setBlue(0);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRenderOverlay(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
            ClientPlayerEntity player = Minecraft.getInstance().player;

            RegenCap.get(player).ifPresent((cap) -> {
                String warning = null;

                ITextComponent forceKeybind = new TranslationTextComponent(RKeybinds.FORCE_REGEN.getTranslationKey().replace("key.keyboard.", ""));

                switch (cap.getCurrentState()) {
                    case GRACE:
                        RenderHelp.renderVig(cap.getPrimaryColors(), 0.3F);
                        warning = new TranslationTextComponent("regeneration.messages.warning.grace", forceKeybind.getString()).getString();
                        break;
                    case GRACE_CRIT:
                        RenderHelp.renderVig(new Vector3d(1, 0, 0), 0.5F);
                        warning = new TranslationTextComponent("regeneration.messages.warning.grace_critical", forceKeybind.getString()).getString();
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

                if (cap.areHandsGlowing()) {
                    RenderHelp.renderVig(TransitionTypes.FIERY.get().getDefaultPrimaryColor(), 0.5F);
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
