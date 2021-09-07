package me.suff.mc.regen.client.rendering.model;

import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class RModels {
    public static ModelLayerLocation TIMELORD = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "council");
    public static ModelLayerLocation TIMELORD_GUARD = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "guard");
    public static ModelLayerLocation ARM_ALEX = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_alex"), "arm_alex");
    public static ModelLayerLocation ARM_STEVE = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_steve"), "arm_steve");

    public static void addModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TIMELORD, TimelordModel::getModelData);
        event.registerLayerDefinition(TIMELORD_GUARD, TimelordGuardModel::getModelData);
        event.registerLayerDefinition(ARM_ALEX, () -> ArmModel.createMesh(true));
        event.registerLayerDefinition(ARM_STEVE, () -> ArmModel.createMesh(false));
    }
}
