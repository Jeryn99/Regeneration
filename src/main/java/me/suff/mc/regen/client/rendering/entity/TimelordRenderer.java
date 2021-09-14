package me.suff.mc.regen.client.rendering.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import me.suff.mc.regen.client.rendering.layers.HandLayer;
import me.suff.mc.regen.client.rendering.layers.RenderRegenLayer;
import me.suff.mc.regen.client.rendering.layers.TimelordHeadLayer;
import me.suff.mc.regen.client.rendering.model.RModels;
import me.suff.mc.regen.client.rendering.model.TimelordGuardModel;
import me.suff.mc.regen.client.rendering.model.TimelordModel;
import me.suff.mc.regen.common.entities.Timelord;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
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
 * Created by Craig
 * on 03/05/2020 @ 19:02
 */
public class TimelordRenderer extends LivingEntityRenderer<Timelord, PlayerModel<Timelord>> {

    public static PlayerModel mainModel;
    public static TimelordModel councilModel;
    public static TimelordGuardModel guardModel;

    public static HashMap<UUID, ResourceLocation> TIMELORDS = new HashMap<>();

    public TimelordRenderer(EntityRendererProvider.Context entityRendererManager) {
        super(entityRendererManager, new TimelordModel(Minecraft.getInstance().getEntityModels().bakeLayer(RModels.TIMELORD)), 0.1F);
        councilModel = new TimelordModel(Minecraft.getInstance().getEntityModels().bakeLayer(RModels.TIMELORD));
        guardModel = new TimelordGuardModel(Minecraft.getInstance().getEntityModels().bakeLayer(RModels.TIMELORD_GUARD));
        mainModel = councilModel;
        addLayer(new RenderRegenLayer(this));
        addLayer(new HandLayer(this));
        addLayer(new ItemInHandLayer<>(this));
        addLayer(new ArrowLayer(entityRendererManager, this));
        addLayer(new TimelordHeadLayer(this));
    }


    public static ResourceLocation getTimelordFace(Timelord timelord) {
        IRegen data = RegenCap.get(timelord).orElseGet(null);

        if (data.updateTicks() > 100 && data.updateTicks() < 105) {
            TIMELORDS.remove(timelord.getUUID());
        }

        if (TIMELORDS.containsKey(timelord.getUUID())) {
            return TIMELORDS.get(timelord.getUUID());
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

    @Override
    public void render(Timelord entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        switch (entityIn.getTimelordType()) {
            case GUARD -> mainModel = guardModel;
            case COUNCIL -> mainModel = councilModel;
        }
        model = mainModel;
        model.setAllVisible(false);
        model.hat.visible = !RegenConfig.CLIENT.renderTimelordHeadwear.get();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    @Nullable
    @Override
    public ResourceLocation getTextureLocation(Timelord entity) {
        String gender = entity.male() ? "male" : "female";
        return switch (entity.getTimelordType()) {
            case COUNCIL -> new ResourceLocation(RConstants.MODID, "textures/entity/timelords/timelord/timelord_council_" + gender + ".png");
            case GUARD -> new ResourceLocation(RConstants.MODID, "textures/entity/timelords/guards/timelord_guard.png");
        };
    }
}