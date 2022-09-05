package mc.craig.software.regen.client.rendering.model;

import com.google.common.base.Supplier;
import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.client.rendering.model.armor.GuardArmorModel;
import mc.craig.software.regen.client.rendering.model.armor.RobesModel;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

public class RModels {

    //Entities
    public static ModelLayerLocation TIMELORD = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "council"), TimelordModel::getModelData);
    public static ModelLayerLocation TIMELORD_GUARD = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "timelord"), "guard"), GuardArmorModel::createBodyLayer);
    public static ModelLayerLocation MOD_PLAYER = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "mod_player"), "mod_player"), () ->  ModifiedPlayerModel.createMesh(new CubeDeformation(-0.25F)));

    //Arms
    public static ModelLayerLocation ARM_ALEX = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_alex"), "arm_alex"), new Supplier<LayerDefinition>() {
        @Override
        public LayerDefinition get() {
            return null;
        }
    });
    public static ModelLayerLocation ARM_STEVE = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "arm_steve"), "arm_steve"), new Supplier<LayerDefinition>() {
        @Override
        public LayerDefinition get() {
            return null;
        }
    });

    // Armor
    public static ModelLayerLocation COUNCIL_ROBES = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "robes"), "robes"), new Supplier<LayerDefinition>() {
        @Override
        public LayerDefinition get() {
            return null;
        }
    });
    public static ModelLayerLocation COUNCIL_ROBES_STEVE = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "council_robes_steve"), "council_robes_steve"), new Supplier<LayerDefinition>() {
        @Override
        public LayerDefinition get() {
            return null;
        }
    });
    public static ModelLayerLocation GUARD_ARMOR = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "guard_armor"), "guard_armor"), new Supplier<LayerDefinition>() {
        @Override
        public LayerDefinition get() {
            return null;
        }
    });
    public static ModelLayerLocation GUARD_ARMOR_STEVE = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "guard_armor_steve"), "guard_armor_steve"), new Supplier<LayerDefinition>() {
        @Override
        public LayerDefinition get() {
            return null;
        }
    });
    public static ModelLayerLocation CONTAINER = register(new ModelLayerLocation(new ResourceLocation(RConstants.MODID, "container"), "container"), new Supplier<LayerDefinition>() {
        @Override
        public LayerDefinition get() {
            return null;
        }
    });


    @ExpectPlatform
    public static ModelLayerLocation register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
        throw new RuntimeException("Uh oh! Wrong platform!");
    }

    //TODO
  /*  public static void addModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MOD_PLAYER, () -> ModifiedPlayerModel.createMesh(new CubeDeformation(-0.25F)));
        event.registerLayerDefinition(TIMELORD_GUARD, TimelordGuardModel::getModelData);
        event.registerLayerDefinition(GUARD_ARMOR, () -> GuardArmorModel.createBodyLayer(true));
        event.registerLayerDefinition(GUARD_ARMOR_STEVE, () -> GuardArmorModel.createBodyLayer(false));
        event.registerLayerDefinition(COUNCIL_ROBES, () -> RobesModel.createBodyLayer(true));
        event.registerLayerDefinition(COUNCIL_ROBES_STEVE, () -> RobesModel.createBodyLayer(false));
        event.registerLayerDefinition(ARM_ALEX, () -> ArmModel.createMesh(true));
        event.registerLayerDefinition(ARM_STEVE, () -> ArmModel.createMesh(false));
        event.registerLayerDefinition(CONTAINER, ContainerModel::createBodyLayer);
    }*/
}
