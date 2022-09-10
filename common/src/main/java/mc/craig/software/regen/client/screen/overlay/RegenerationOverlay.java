package mc.craig.software.regen.client.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Transformation;
import mc.craig.software.regen.client.RKeybinds;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.util.RConstants;
import mc.craig.software.regen.util.RenderHelp;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

public class RegenerationOverlay {

    public static ResourceLocation BACKGROUND = new ResourceLocation(RConstants.MODID, "textures/gui/text_item.png");

    public static ResourceLocation CUSTOM_ICONS = new ResourceLocation(RConstants.MODID, "textures/gui/hearts.png");

    public static void renderAll(PoseStack poseStack){
        renderUi(poseStack);
    }

    public static void renderUi(PoseStack poseStack) {
        LocalPlayer player = Minecraft.getInstance().player;

        Component forceKeybind = RKeybinds.FORCE_REGEN.getTranslatedKeyMessage();

        RegenerationData.get(player).ifPresent((cap) -> {
            String warning = null;

            if(cap.regenState() != RegenStates.ALIVE){
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

            MultiBufferSource.BufferSource renderImpl = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            if (warning != null) {
                RenderSystem.setShaderTexture(0, BACKGROUND);
                GuiComponent.blit(poseStack, Minecraft.getInstance().getWindow().getGuiScaledWidth() - 140, 4, 0, 0, 134, 22, 134, 22);

                Minecraft.getInstance().font.drawInBatch(warning, Minecraft.getInstance().getWindow().getGuiScaledWidth() - Minecraft.getInstance().font.width(warning) - 50, 11, ChatFormatting.WHITE.getColor(), false, Transformation.identity().getMatrix(), renderImpl, false, 0, 15728880);
                renderImpl.endBatch();
            }
        });
    }

}
