package mc.craig.software.regen.client.rendering.model;

import mc.craig.software.regen.client.rendering.model.armor.GuardArmorModel;
import mc.craig.software.regen.client.rendering.model.armor.RobesModel;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class RModels {

    //Entities
    public static ModelLayerLocation TIMELORD = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "council");
    public static ModelLayerLocation TIMELORD_GUARD = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "guard");
    public static ModelLayerLocation CYBERMAN = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "cyberman"), "cyberman");
    public static ModelLayerLocation CYBERMAN_GLOW = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "cyberman_glow"), "cyberman_glow");
    public static ModelLayerLocation MOD_PLAYER = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "mod_player"), "mod_player");

    //Arms
    public static ModelLayerLocation ARM_ALEX = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_alex"), "arm_alex");
    public static ModelLayerLocation ARM_STEVE = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_steve"), "arm_steve");

    // Armor
    public static ModelLayerLocation COUNCIL_ROBES = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "robes"), "robes");
    public static ModelLayerLocation COUNCIL_ROBES_STEVE = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "council_robes_steve"), "council_robes_steve");
    public static ModelLayerLocation GUARD_ARMOR = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "guard_armor"), "guard_armor");
    public static ModelLayerLocation GUARD_ARMOR_STEVE = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "guard_armor_steve"), "guard_armor_steve");
    public static ModelLayerLocation CONTAINER = new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "container"), "container");


    public static void addModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TIMELORD, TimelordModel::getModelData);
        event.registerLayerDefinition(MOD_PLAYER, () -> ModifiedPlayerModel.createMesh(new CubeDeformation(-0.25F)));
        event.registerLayerDefinition(CYBERMAN, () -> CybermanModel.createBodyLayer(true));
        event.registerLayerDefinition(CYBERMAN_GLOW, () -> CybermanModel.createBodyLayer(false));
        event.registerLayerDefinition(TIMELORD_GUARD, TimelordGuardModel::getModelData);
        event.registerLayerDefinition(GUARD_ARMOR, () -> GuardArmorModel.createBodyLayer(true));
        event.registerLayerDefinition(GUARD_ARMOR_STEVE, () -> GuardArmorModel.createBodyLayer(false));
        event.registerLayerDefinition(COUNCIL_ROBES, () -> RobesModel.createBodyLayer(true));
        event.registerLayerDefinition(COUNCIL_ROBES_STEVE, () -> RobesModel.createBodyLayer(false));
        event.registerLayerDefinition(ARM_ALEX, () -> ArmModel.createMesh(true));
        event.registerLayerDefinition(ARM_STEVE, () -> ArmModel.createMesh(false));
        event.registerLayerDefinition(CONTAINER, ContainerModel::createBodyLayer);
    }
}
