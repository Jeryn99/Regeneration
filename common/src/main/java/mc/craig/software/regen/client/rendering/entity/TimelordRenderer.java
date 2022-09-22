package mc.craig.software.regen.client.rendering.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.rendering.layers.HandLayer;
import mc.craig.software.regen.client.rendering.layers.RenderRegenLayer;
import mc.craig.software.regen.client.rendering.layers.TimelordHeadLayer;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.client.rendering.model.TimelordGuardModel;
import mc.craig.software.regen.client.rendering.model.TimelordModel;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
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
        this.addLayer(new ItemInHandLayer<>(this, entityRendererManager.getItemInHandRenderer()) {

            @Override
            public void render(PoseStack p_114569_, MultiBufferSource p_114570_, int p_114571_, Timelord p_114572_, float p_114573_, float p_114574_, float p_114575_, float p_114576_, float p_114577_, float p_114578_) {
                super.render(p_114569_, p_114570_, p_114571_, p_114572_, p_114573_, p_114574_, p_114575_, p_114576_, p_114577_, p_114578_);
            }
        });
        addLayer(new ArrowLayer(entityRendererManager, this));
        addLayer(new TimelordHeadLayer(this));
    }


    public static ResourceLocation getTimelordFace(Timelord timelord) {
        IRegen data = RegenerationData.get(timelord).orElseGet(null);

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


    @Nullable
    @Override
    protected RenderType getRenderType(Timelord p_115322_, boolean p_115323_, boolean p_115324_, boolean p_115325_) {
        return RenderType.entityTranslucent(getTextureLocation(p_115322_));
    }

    @Override
    public void render(Timelord entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        switch (entityIn.getTimelordType()) {
            case GUARD -> mainModel = guardModel;
            case COUNCIL -> mainModel = councilModel;
        }
        model = mainModel;
        model.setAllVisible(false);
        model.hat.visible = !RegenConfig.CLIENT.renderTimelordHeadwear.get();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(Timelord entity) {
        String gender = entity.male() ? "male" : "female";
        return switch (entity.getTimelordType()) {
            case COUNCIL ->
                    new ResourceLocation(RConstants.MODID, "textures/entity/timelords/timelord/timelord_council_" + gender + ".png");
            case GUARD -> new ResourceLocation(RConstants.MODID, "textures/entity/timelords/guards/timelord_guard.png");
        };
    }
}