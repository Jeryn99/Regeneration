package craig.software.mc.regen.client.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import craig.software.mc.regen.client.rendering.model.ArmModel;
import craig.software.mc.regen.client.rendering.model.ContainerModel;
import craig.software.mc.regen.client.rendering.model.RModels;
import craig.software.mc.regen.client.skin.SkinHandler;
import craig.software.mc.regen.common.block.JarBlock;
import craig.software.mc.regen.common.item.HandItem;
import craig.software.mc.regen.common.blockentity.BioContainerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static craig.software.mc.regen.util.RegenUtil.round;

/* Created by Craig on 05/03/2021 */
public class JarTileRender implements BlockEntityRenderer<BioContainerBlockEntity> {

    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    public static HashMap<BioContainerBlockEntity, ResourceLocation> TEXTURES = new HashMap<>();
    private final ArmModel alexArm;
    private final ArmModel steveArm;
    private final ContainerModel jarModel;

    public JarTileRender(BlockEntityRendererProvider.Context context) {
        alexArm = new ArmModel(context.bakeLayer(RModels.ARM_ALEX));
        steveArm = new ArmModel(context.bakeLayer(RModels.ARM_STEVE));
        jarModel = new ContainerModel(context.bakeLayer(RModels.CONTAINER));
    }

    @Override
    public void render(BioContainerBlockEntity blockEntity, float p_112308_, PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int ov) {

        matrixStackIn.pushPose();
        BlockState blockstate = blockEntity.getBlockState();
        float rotation = 22.5F * (float) blockstate.getValue(JarBlock.ROTATION);

        // Render remaining lindos amount
        if (blockEntity.getHand().getItem() instanceof HandItem && !blockEntity.isValid(BioContainerBlockEntity.Action.CREATE) && Minecraft.renderNames()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5D, 1.2, 0.5D);
            matrixStackIn.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrixStackIn.last().pose();
            Font fontrenderer = Minecraft.getInstance().font;
            float f2 = (float) (-fontrenderer.width(Component.translatable(String.valueOf(round(blockEntity.getLindos(), 2)))) / 2);
            fontrenderer.drawInBatch(Component.translatable(String.valueOf(round(blockEntity.getLindos(), 2))), f2, (float) 1, -1, false, matrix4f, bufferIn, false, 0, combinedLightIn);
            matrixStackIn.popPose();
        }


        if (blockEntity.pendingSkinUpdate()) {
            TEXTURES.remove(blockEntity);
        }

        // Render Hand
        if (blockEntity.getHand().getItem() instanceof HandItem) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5D, 0, 0.5D);
            boolean isAlex = HandItem.isAlex(blockEntity.getHand());
            ArmModel mainModel = isAlex ? alexArm : steveArm;
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(rotation));
            matrixStackIn.scale(0.8F, 0.8F, 0.8F);
            matrixStackIn.translate(0, -1.5F, -0.02);
            mainModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(getOrCreateTexture(blockEntity))), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            matrixStackIn.popPose();
        } else {
            TEXTURES.remove(blockEntity);
        }

        // Render Block
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(0.5, -1.5, 0.5);
        matrixStackIn.translate(-1, 0, -1);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(rotation));
        jarModel.animate(blockEntity);
        jarModel.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityTranslucent(ContainerModel.CONTAINER_TEXTURE)), combinedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStackIn.popPose();


        matrixStackIn.popPose();

    }

    public ResourceLocation getOrCreateTexture(BioContainerBlockEntity tileEntityHandInJar) {

        if (!tileEntityHandInJar.getHand().getOrCreateTag().contains("skin")) {
            boolean isAlex = HandItem.isAlex(tileEntityHandInJar.getHand());
            return isAlex ? TEXTURE_ALEX : TEXTURE_STEVE;
        }

        if (!TEXTURES.containsKey(tileEntityHandInJar)) {
            NativeImage image = SkinHandler.genSkinNative(HandItem.getSkin(tileEntityHandInJar.getHand()));
            ResourceLocation res = Minecraft.getInstance().getTextureManager().register("hand_", new DynamicTexture(image));
            TEXTURES.put(tileEntityHandInJar, res);
            return res;
        }
        return TEXTURES.get(tileEntityHandInJar);
    }

    private int getCombinedAverageLight(BlockAndTintGetter lightReaderIn, BlockPos posIn) {
        int i = LevelRenderer.getLightColor(lightReaderIn, posIn);
        int j = LevelRenderer.getLightColor(lightReaderIn, posIn.above());
        int k = i & 255;
        int l = j & 255;
        int i1 = i >> 16 & 255;
        int j1 = j >> 16 & 255;
        return (Math.max(k, l)) | (Math.max(i1, j1)) << 16;
    }
}
