package mc.craig.software.regen.client.rendering.model;

import com.google.common.base.Supplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.client.rendering.entity.CybermanModel;
import mc.craig.software.regen.client.rendering.model.armor.ArmorModel;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

public class RModels {

    //Entities
    public static ModelLayerLocation MOD_PLAYER, ARM_ALEX, ARM_STEVE, COUNCIL_ROBES, COUNCIL_ROBES_STEVE, CONTAINER, CYBERMAN;


    public static void init() {
        MOD_PLAYER = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "mod_player"), "mod_player"), () -> LayerDefinition.create(PlayerModel.createMesh(CubeDeformation.NONE, true), 64, 64));

        //Arms
        ARM_ALEX = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_alex"), "arm_alex"), () -> ArmModel.createMesh(true));
        ARM_STEVE = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_steve"), "arm_steve"), () -> ArmModel.createMesh(false));

        // Armor
        COUNCIL_ROBES = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "robes"), "robes"), ArmorModel::createAlex);
        COUNCIL_ROBES_STEVE = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "council_robes_steve"), "council_robes_steve"), ArmorModel::createSteve);
        CONTAINER = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "container"), "container"), ContainerModel::createBodyLayer);
        CYBERMAN = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "cyberman"), "cyberman"), CybermanModel::createBodyLayer);

    }

    @ExpectPlatform
    public static ModelLayerLocation register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        throw new RuntimeException("Uh oh! Wrong platform!");
    }

}
