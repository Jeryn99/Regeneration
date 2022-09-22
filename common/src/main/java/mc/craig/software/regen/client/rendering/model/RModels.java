package mc.craig.software.regen.client.rendering.model;

import com.google.common.base.Supplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.client.rendering.model.armor.GuardArmorModel;
import mc.craig.software.regen.client.rendering.model.armor.RobesModel;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

public class RModels {

    //Entities
    public static ModelLayerLocation TIMELORD, TIMELORD_GUARD, MOD_PLAYER, ARM_ALEX, ARM_STEVE, COUNCIL_ROBES, COUNCIL_ROBES_STEVE, GUARD_ARMOR, GUARD_ARMOR_STEVE, CONTAINER;


    public static void init() {
        TIMELORD = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "council"), TimelordModel::getModelData);
        TIMELORD_GUARD = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "guard"), TimelordGuardModel::getModelData);
        MOD_PLAYER = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "mod_player"), "mod_player"), () -> ModifiedPlayerModel.createMesh(new CubeDeformation(-0.25F)));

        //Arms
        ARM_ALEX = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_alex"), "arm_alex"), () -> ArmModel.createMesh(true));
        ARM_STEVE = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_steve"), "arm_steve"), () -> ArmModel.createMesh(false));

        // Armor
        COUNCIL_ROBES = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "robes"), "robes"), () -> RobesModel.createBodyLayer(true));
        COUNCIL_ROBES_STEVE = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "council_robes_steve"), "council_robes_steve"), () -> RobesModel.createBodyLayer(false));
        GUARD_ARMOR = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "guard_armor"), "guard_armor"), () -> GuardArmorModel.createBodyLayer(true));
        GUARD_ARMOR_STEVE = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "guard_armor_steve"), "guard_armor_steve"), () -> GuardArmorModel.createBodyLayer(false));
        CONTAINER = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "container"), "container"), ContainerModel::createBodyLayer);

    }

    @ExpectPlatform
    public static ModelLayerLocation register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        throw new RuntimeException("Uh oh! Wrong platform!");
    }

}
