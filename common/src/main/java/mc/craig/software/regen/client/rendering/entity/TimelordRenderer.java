package mc.craig.software.regen.client.rendering.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.rendering.layers.HandLayer;
import mc.craig.software.regen.client.rendering.layers.RenderRegenLayer;
import mc.craig.software.regen.client.rendering.model.ModifiedPlayerModel;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Craig
 * on 03/05/2020 @ 19:02
 */
public class TimelordRenderer extends MobRenderer<Timelord, PlayerModel<Timelord>> {

    public static HashMap<UUID, ResourceLocation> TIMELORDS = new HashMap<>();

    public TimelordRenderer(EntityRendererProvider.Context entityRendererManager) {
        super(entityRendererManager, new ModifiedPlayerModel(Minecraft.getInstance().getEntityModels().bakeLayer(RModels.MOD_PLAYER), true), 0.1F);
        addLayer(new RenderRegenLayer(this));
        addLayer(new HumanoidArmorLayer(this, new HumanoidModel(entityRendererManager.bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR)), new HumanoidModel(entityRendererManager.bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
        addLayer(new HandLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, entityRendererManager.getItemInHandRenderer()) {

            @Override
            public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, Timelord livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                super.render(matrixStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        });
        addLayer(new ArrowLayer(entityRendererManager, this));
    }


    public static ResourceLocation getTimelordTexture(Timelord timelord) {
        IRegen data = RegenerationData.get(timelord).orElseGet(null);

        if (data.updateTicks() > 100 && data.updateTicks() < 105) {
            TIMELORDS.remove(timelord.getUUID());
        }

        if (TIMELORDS.containsKey(timelord.getUUID())) {
            return TIMELORDS.get(timelord.getUUID());
        }

        if(TIMELORDS.get(timelord.getUUID()) == DefaultPlayerSkin.getDefaultSkin()){
            TIMELORDS.remove(timelord.getUUID());
            return DefaultPlayerSkin.getDefaultSkin();
        }

        NativeImage nativeImage = null;
        try {
            if (data.isSkinValidForUse()) {
                nativeImage = NativeImage.read(new ByteArrayInputStream(data.skin()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (nativeImage == null) {
            return DefaultPlayerSkin.getDefaultSkin();
        }
        ResourceLocation location = Minecraft.getInstance().getTextureManager().register("timelord_", new DynamicTexture(nativeImage));
        TIMELORDS.put(timelord.getUUID(), location);
        return location;

    }


    @Nullable
    @Override
    protected RenderType getRenderType(Timelord livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(getTextureLocation(livingEntity));
    }

    @Override
    public void render(Timelord entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        model.hat.visible = true;

        model.rightSleeve.visible = false;
        model.leftSleeve.visible = false;
        model.leftPants.visible = false;
        model.rightPants.visible = false;
        model.jacket.visible = false;

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(Timelord entity) {
        return TimelordRenderer.getTimelordTexture(entity);
    }
}