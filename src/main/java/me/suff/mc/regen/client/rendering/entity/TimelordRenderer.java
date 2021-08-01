package me.suff.mc.regen.client.rendering.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.client.rendering.layers.HandLayer;
import me.suff.mc.regen.client.rendering.layers.RenderRegenLayer;
import me.suff.mc.regen.client.rendering.layers.TimelordHeadLayer;
import me.suff.mc.regen.client.rendering.model.TimelordGuardModel;
import me.suff.mc.regen.client.rendering.model.TimelordModel;
import me.suff.mc.regen.common.entities.TimelordEntity;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Swirtzly
 * on 03/05/2020 @ 19:02
 */
public class TimelordRenderer extends LivingEntityRenderer<TimelordEntity, HumanoidModel<TimelordEntity>> {

    public static EntityModel<TimelordEntity> mainModel = new TimelordModel();
    public static EntityModel<TimelordEntity> councilModel = new TimelordModel();
    public static EntityModel<TimelordEntity> guardModel = new TimelordGuardModel();

    public static HashMap<UUID, ResourceLocation> TIMELORDS = new HashMap<>();

    public TimelordRenderer(EntityRenderDispatcher entityRendererManager) {
        super(entityRendererManager, new HumanoidModel(1), 0.1F);
        addLayer(new RenderRegenLayer(this));
        addLayer(new HandLayer(this));
        addLayer(new ItemInHandLayer<>(this));
        addLayer(new ArrowLayer(this));
        addLayer(new TimelordHeadLayer(this));
        addLayer(new CustomHeadLayer<>(this));
        addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(0.5F), new HumanoidModel<>(1.0F)));
    }


    public static ResourceLocation getTimelordFace(TimelordEntity timelordEntity) {
        IRegen data = RegenCap.get(timelordEntity).orElseGet(null);

        if (data.updateTicks() > 100 && data.updateTicks() < 105) {
            TIMELORDS.remove(timelordEntity.getUUID());
        }

        if (TIMELORDS.containsKey(timelordEntity.getUUID())) {
            return TIMELORDS.get(timelordEntity.getUUID());
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
        TIMELORDS.put(timelordEntity.getUUID(), location);
        return location;

    }

    @Override
    public void render(TimelordEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        switch (entityIn.getTimelordType()) {
            case GUARD:
                mainModel = guardModel;
                break;
            case COUNCIL:
                mainModel = councilModel;
                break;
        }
        model = (HumanoidModel<TimelordEntity>) mainModel;
        model.head.visible = false;
        model.hat.visible = !RegenConfig.CLIENT.renderTimelordHeadwear.get();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    @Nullable
    @Override
    public ResourceLocation getTextureLocation(TimelordEntity entity) {
        String gender = entity.male() ? "male" : "female";
        switch (entity.getTimelordType()) {
            case COUNCIL:
                return new ResourceLocation(RConstants.MODID, "textures/entity/timelords/timelord/timelord_council_" + gender + ".png");
            case GUARD:
                return new ResourceLocation(RConstants.MODID, "textures/entity/timelords/guards/timelord_guard.png");
        }
        return null;
    }
}