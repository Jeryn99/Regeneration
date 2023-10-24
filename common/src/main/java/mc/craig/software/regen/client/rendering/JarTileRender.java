package mc.craig.software.regen.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mc.craig.software.regen.client.rendering.model.ArmModel;
import mc.craig.software.regen.client.rendering.model.ContainerModel;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.client.skin.VisualManipulator;
import mc.craig.software.regen.common.block.JarBlock;
import mc.craig.software.regen.common.blockentity.BioContainerBlockEntity;
import mc.craig.software.regen.common.item.HandItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.HashMap;

import static mc.craig.software.regen.util.RegenUtil.round;

/* Created by Craig on 05/03/2021 */
public class JarTileRender implements BlockEntityRenderer<BioContainerBlockEntity> {

    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    public static HashMap<BioContainerBlockEntity, ResourceLocation> TEXTURES = new HashMap<>();
    private final ArmModel alexArm, steveArm;
    private final ContainerModel jarModel;

    public JarTileRender(BlockEntityRendererProvider.Context context) {
        alexArm = new ArmModel(context.bakeLayer(RModels.ARM_ALEX));
        steveArm = new ArmModel(context.bakeLayer(RModels.ARM_STEVE));
        jarModel = new ContainerModel(context.bakeLayer(RModels.CONTAINER));
    }

    @Override
    public void render(BioContainerBlockEntity blockEntity, float p_112308_, PoseStack poseStack, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int ov) {

        poseStack.pushPose();
        BlockState blockstate = blockEntity.getBlockState();
        float rotation = 22.5F * (float) blockstate.getValue(JarBlock.ROTATION);

        // Render remaining lindos amount
        if (blockEntity.getHand().getItem() instanceof HandItem && !blockEntity.isValid(BioContainerBlockEntity.Action.CREATE) && Minecraft.renderNames()) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.2, 0.5D);
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = poseStack.last().pose();
            Font fontrenderer = Minecraft.getInstance().font;
            float f2 = (float) (-fontrenderer.width(Component.translatable(String.valueOf(round(blockEntity.getLindos(), 2)))) / 2);
            fontrenderer.drawInBatch(Component.translatable(String.valueOf(round(blockEntity.getLindos(), 2))), f2, (float) 1, -1, false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, 0, combinedLightIn);
            poseStack.popPose();
        }


        if (blockEntity.pendingSkinUpdate()) {
            TEXTURES.remove(blockEntity);
        }

        // Render Hand
        if (blockEntity.getHand().getItem() instanceof HandItem) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 0, 0.5D);
            boolean isAlex = HandItem.isAlex(blockEntity.getHand());
            ArmModel mainModel = isAlex ? alexArm : steveArm;
            poseStack.mulPose(Axis.ZP.rotationDegrees(180));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.scale(0.8F, 0.8F, 0.8F);
            poseStack.translate(0, -1.5F, -0.02);
            mainModel.renderToBuffer(poseStack, bufferIn.getBuffer(RenderType.entityTranslucent(getOrCreateTexture(blockEntity))), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            poseStack.popPose();
        } else {
            TEXTURES.remove(blockEntity);
        }

        // Render Block
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.translate(0.5, -1.5, 0.5);
        poseStack.translate(-1, 0, -1);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        jarModel.animate(blockEntity);
        jarModel.renderToBuffer(poseStack, bufferIn.getBuffer(RenderType.entityTranslucent(ContainerModel.CONTAINER_TEXTURE)), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        poseStack.popPose();


        poseStack.popPose();

    }

    public ResourceLocation getOrCreateTexture(BioContainerBlockEntity tileEntityHandInJar) {

        if (!tileEntityHandInJar.getHand().getOrCreateTag().contains("skin")) {
            boolean isAlex = HandItem.isAlex(tileEntityHandInJar.getHand());
            return isAlex ? TEXTURE_ALEX : TEXTURE_STEVE;
        }

        if (!TEXTURES.containsKey(tileEntityHandInJar)) {
            NativeImage image = VisualManipulator.genSkinNative(HandItem.getSkin(tileEntityHandInJar.getHand()));
            ResourceLocation res = Minecraft.getInstance().getTextureManager().register("hand_", new DynamicTexture(image));
            TEXTURES.put(tileEntityHandInJar, res);
            return res;
        }
        return TEXTURES.get(tileEntityHandInJar);
    }
}
