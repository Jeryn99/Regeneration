package me.suff.mc.regen.client.rendering.model;

import me.suff.mc.regen.client.rendering.model.armor.GuardArmorModel;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class RModels {

    //Entities
    public static ModelLayerLocation TIMELORD = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "council");
    public static ModelLayerLocation TIMELORD_GUARD = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "guard");

    //Arms
    public static ModelLayerLocation ARM_ALEX = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_alex"), "arm_alex");
    public static ModelLayerLocation ARM_STEVE = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_steve"), "arm_steve");

    // Armor
    public static ModelLayerLocation COUNCIL_ROBES = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "robes"), "robes");
    public static ModelLayerLocation GUARD_ARMOR = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "guard_armor"), "guard_armor");


    public static void addModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TIMELORD, TimelordModel::getModelData);
        event.registerLayerDefinition(TIMELORD_GUARD, TimelordGuardModel::getModelData);
        //event.registerLayerDefinition(COUNCIL_ROBES, RobesModel.createMesh());
        event.registerLayerDefinition(GUARD_ARMOR, () -> GuardArmorModel.createBodyLayer(false));
        event.registerLayerDefinition(COUNCIL_ROBES, () -> GuardArmorModel.createBodyLayer(false));
        event.registerLayerDefinition(ARM_ALEX, () -> ArmModel.createMesh(true));
        event.registerLayerDefinition(ARM_STEVE, () -> ArmModel.createMesh(false));
    }
}
