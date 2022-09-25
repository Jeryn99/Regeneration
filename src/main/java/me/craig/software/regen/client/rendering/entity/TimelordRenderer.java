package me.craig.software.regen.client.rendering.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.craig.software.regen.client.rendering.model.TimelordGuardModel;
import me.craig.software.regen.client.rendering.model.TimelordModel;
import me.craig.software.regen.common.entities.TimelordEntity;
import me.craig.software.regen.common.regen.IRegen;
import me.craig.software.regen.common.regen.RegenCap;
import me.craig.software.regen.config.RegenConfig;
import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.client.rendering.layers.HandLayer;
import me.craig.software.regen.client.rendering.layers.RenderRegenLayer;
import me.craig.software.regen.client.rendering.layers.TimelordHeadLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Swirtzly
 * on 03/05/2020 @ 19:02
 */
public class TimelordRenderer extends LivingRenderer<TimelordEntity, BipedModel<TimelordEntity>> {

    public static EntityModel<TimelordEntity> mainModel = new TimelordModel();
    public static EntityModel<TimelordEntity> councilModel = new TimelordModel();
    public static EntityModel<TimelordEntity> guardModel = new TimelordGuardModel();

    public static HashMap<UUID, ResourceLocation> TIMELORDS = new HashMap<>();

    public TimelordRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new BipedModel(1), 0.1F);
        addLayer(new RenderRegenLayer(this));
        addLayer(new HandLayer(this));
        addLayer(new HeldItemLayer<>(this));
        addLayer(new ArrowLayer(this));
        addLayer(new TimelordHeadLayer(this));
        addLayer(new HeadLayer<>(this));
        addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
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
    public void render(TimelordEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        switch (entityIn.getTimelordType()) {
            case GUARD:
                mainModel = guardModel;
                break;
            case COUNCIL:
                mainModel = councilModel;
                break;
        }
        model = (BipedModel<TimelordEntity>) mainModel;
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