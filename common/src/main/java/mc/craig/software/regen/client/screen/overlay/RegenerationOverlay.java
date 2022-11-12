package mc.craig.software.regen.client.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Transformation;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
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

            int remaining = 12 - (cap.regens());
            String timeString = switch (remaining) {
                case 1 -> remaining + "st";
                case 2 -> remaining + "nd";
                case 3 -> remaining + "rd";
                default -> remaining + "th";
            };

        /*    if(Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON){
                if(cap.regenState() == RegenStates.POST && player.hurtTime > 0){
                    ScreenEffectRenderer.renderFire(Minecraft.getInstance(), poseStack);
                }
            }*/

            RegenStates currentState = cap.regenState();

            if (currentState == RegenStates.ALIVE)
                return; // We don't have anything to render when the user is not within a regeneration cycle

            // Render head & hat piece for use in post regeneration state
            if (currentState == RegenStates.POST) {
                RenderSystem.setShaderTexture(0, player.getSkinTextureLocation());
                GuiComponent.blit(poseStack, 8, 10, 8, 8, 8, 8, 64, 64);
                GuiComponent.blit(poseStack, 8, 10, 40, 8, 8, 8, 64, 64);
            }

            // Render Regeneration Status Icon
            RenderSystem.setShaderTexture(0, BACKGROUND);
            GuiComponent.blit(poseStack, 4, 4, cap.regenState().getSpriteSheet().getUOffset(), cap.regenState().getSpriteSheet().getYOffset(), 16, 16, 256, 256);

            // Alert User that their hand is glowing
            if (cap.glowing()) {
                RConstants.SpriteSheet handGlow = RConstants.SpriteSheet.HAND_GLOW;
                GuiComponent.blit(poseStack, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 8, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 23, handGlow.getUOffset(), handGlow.getYOffset(), 16, 16, 256, 256);
            }

            if (cap.handState() != IRegen.Hand.NOT_CUT) {
                RConstants.SpriteSheet handGlow = RConstants.SpriteSheet.MISSING_ARM;
                int positionOffset = player.getMainArm() == HumanoidArm.RIGHT ? Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - 91 - 29 : Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 101;
                GuiComponent.blit(poseStack, positionOffset, Minecraft.getInstance().getWindow().getGuiScaledHeight() - 19, handGlow.getUOffset(), handGlow.getYOffset(), 16, 16, 256, 256);
            }

            if (currentState == RegenStates.REGENERATING) {
                MultiBufferSource.BufferSource renderImpl = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                if (timeString != null) {
                    Minecraft.getInstance().font.drawInBatch(timeString, 21, 8, ChatFormatting.WHITE.getColor(), true, Transformation.identity().getMatrix(), renderImpl, false, 0, 15728880);
                }
                renderImpl.endBatch();
            }

        });
    }

}
