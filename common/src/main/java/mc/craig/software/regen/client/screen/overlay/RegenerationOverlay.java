package mc.craig.software.regen.client.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.RKeybinds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;

public class RegenerationOverlay {

    public static ResourceLocation BACKGROUND = new ResourceLocation(RConstants.MODID, "textures/gui/regen_state/icons.png");

    public static ResourceLocation CUSTOM_ICONS = new ResourceLocation(RConstants.MODID, "textures/gui/hearts.png");

    public static void renderAll(PoseStack poseStack) {
        renderUi(poseStack);
    }

    public static void renderUi(PoseStack poseStack) {
        LocalPlayer player = Minecraft.getInstance().player;

        RegenerationData.get(player).ifPresent((cap) -> {

            RegenStates currentState = cap.regenState();

            if (currentState == RegenStates.ALIVE) return; // We don't have anything to render when the user is not within a regeneration cycle

            // Render head & hat piece for use in post regeneration state
            if (currentState == RegenStates.POST) {
                RenderSystem.setShaderTexture(0, player.getSkinTextureLocation());
                GuiComponent.blit(poseStack, 8, 10, 8, 8, 8, 8, 64, 64);
                GuiComponent.blit(poseStack, 8, 10, 40, 8, 8, 8, 64, 64);
            }

            // Render Status
            RenderSystem.setShaderTexture(0, BACKGROUND);
            GuiComponent.blit(poseStack, 4, 4, cap.regenState().getSpriteSheet().getUOffset(), cap.regenState().getSpriteSheet().getYOffset(), 16, 16, 256, 256);

            // Alert User that their hand is glowing
            if (cap.glowing()) {
                RConstants.SpriteSheet handGlow = RConstants.SpriteSheet.HAND_GLOW;
                GuiComponent.blit(poseStack, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 8, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 23, handGlow.getUOffset(), handGlow.getYOffset(), 16, 16, 256, 256);
            }

            if(cap.handState() != IRegen.Hand.NO_GONE){
                RConstants.SpriteSheet handGlow = RConstants.SpriteSheet.MISSING_ARM;
                int positionOffset = player.getMainArm() == HumanoidArm.RIGHT ? Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 91 - 29 : Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 91 + 29;
                GuiComponent.blit(poseStack, positionOffset, Minecraft.getInstance().getWindow().getGuiScaledHeight() - 19, handGlow.getUOffset(), handGlow.getYOffset(), 16, 16, 256, 256);
            }

            // OLD
        /*    String warning = null;

            if (cap.regenState() != RegenStates.ALIVE) {
                warning = cap.regenState().name();
            }

            switch (cap.regenState()) {
                case GRACE:
                    RenderHelp.renderVig(poseStack, cap.getPrimaryColors(), 0.3F);
                    break;
                case GRACE_CRIT:
                    RenderHelp.renderVig(poseStack, new Vec3(1, 0, 0), 0.5F);
                    break;
                case REGENERATING:
                    RenderHelp.renderVig(poseStack, cap.getSecondaryColors(), 0.5F);
                    break;
                case POST:
                    if (player.hurtTime > 0 || player.getEffect(MobEffects.CONFUSION) != null) {
                        RenderHelp.renderVig(poseStack, cap.getSecondaryColors(), 0.5F);
                    }
                    break;
            }

            if (cap.glowing()) {
                RenderHelp.renderVig(poseStack, TransitionTypes.FIERY.getDefaultPrimaryColor(), 0.5F);
            }

            int remaining = 12 - (cap.regens());
            String time = "" + remaining;
            switch (remaining) {
                case 1:
                    time = time + "st";
                    break;
                case 2:
                    time = time + "nd";
                    break;
                case 3:
                    time = time + "rd";
                    break;
                default:
                    time = time + "th";
                    break;
            }
            warning = time;

            MultiBufferSource.BufferSource renderImpl = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            if (warning != null) {
                RenderSystem.setShaderTexture(0, BACKGROUND);
                GuiComponent.blit(poseStack, Minecraft.getInstance().getWindow().getGuiScaledWidth() - 140, 4, 0, 0, 15, 14, 15, 14);

                Minecraft.getInstance().font.drawInBatch(warning, Minecraft.getInstance().getWindow().getGuiScaledWidth() - Minecraft.getInstance().font.width(warning) - 50, 11, ChatFormatting.WHITE.getColor(), false, Transformation.identity().getMatrix(), renderImpl, false, 0, 15728880);
                renderImpl.endBatch();
            }*/
        });
    }

}
