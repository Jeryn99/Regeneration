package craig.software.mc.regen.util;

import com.mojang.math.Vector3f;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AnimationUtil {

    public static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

    public static ModelPart getAnyDescendantWithName(HumanoidModel<?> model, String partName) {
        return switch (partName) {
            case "RightLeg" -> model.rightLeg;
            case "LeftLeg" -> model.leftLeg;
            case "LeftArm" -> model.leftArm;
            case "RightArm" -> model.rightArm;
            case "Body" -> model.body;
            case "Head" -> model.head;
            default -> null;
        };
    }

    public static void animate(HumanoidModel<?> humanoidModel, AnimationDefinition animationDefinition, long p_232322_, float p_232323_, Vector3f p_232324_) {
        float elapsedSeconds = getElapsedSeconds(animationDefinition, p_232322_);

        humanoidModel.rightLeg.z = 0.1F;
        humanoidModel.leftLeg.z = 0.1F;
        humanoidModel.rightLeg.y = 12.0F;
        humanoidModel.leftLeg.y = 12.0F;

        humanoidModel.body.x = 0F;
        humanoidModel.body.y = 0F;
        humanoidModel.body.z = 0F;

        humanoidModel.leftArm.y = 2.0F;
        humanoidModel.rightArm.y = 2.0F;;
        humanoidModel.rightArm.z = 0.0F;
        humanoidModel.rightArm.x = -5.0F;
        humanoidModel.leftArm.z = 0.0F;
        humanoidModel.leftArm.x = 5.0F;

        for (Map.Entry<String, List<AnimationChannel>> entry : animationDefinition.boneAnimations().entrySet()) {
            ModelPart optional = getAnyDescendantWithName(humanoidModel, entry.getKey());

            List<AnimationChannel> list = entry.getValue();

            if (optional != null) {

                optional.xRot = 0;
                optional.yRot = 0;
                optional.zRot = 0;

                if (Objects.equals(entry.getKey(), "Body") || Objects.equals(entry.getKey(), "Head")) {
                    optional.x = 0;
                    optional.y = 0;
                    optional.z = 0;
                }


                list.forEach((p_232311_) -> {
                    Keyframe[] akeyframe = p_232311_.keyframes();
                    int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (p_232315_) -> {
                        return elapsedSeconds <= akeyframe[p_232315_].timestamp();
                    }) - 1);
                    int j = Math.min(akeyframe.length - 1, i + 1);
                    Keyframe keyframe = akeyframe[i];
                    Keyframe keyframe1 = akeyframe[j];
                    float f1 = elapsedSeconds - keyframe.timestamp();
                    float f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                    keyframe1.interpolation().apply(p_232324_, f2, akeyframe, i, j, p_232323_);
                    p_232311_.target().apply(optional, p_232324_);
                });
            } else {
                System.out.println("Could not find:" + entry.getKey());
            }
        }
    }

    private static float getElapsedSeconds(AnimationDefinition p_232317_, long p_232318_) {
        float f = (float) p_232318_ / 1000.0F;
        return p_232317_.looping() ? f % p_232317_.lengthInSeconds() : f;
    }

    public static Vector3f posVec(float p_232303_, float p_232304_, float p_232305_) {
        return new Vector3f(p_232303_, -p_232304_, p_232305_);
    }

    public static Vector3f degreeVec(float p_232332_, float p_232333_, float p_232334_) {
        return new Vector3f(p_232332_ * ((float) Math.PI / 180F), p_232333_ * ((float) Math.PI / 180F), p_232334_ * ((float) Math.PI / 180F));
    }

    public static Vector3f scaleVec(double p_232299_, double p_232300_, double p_232301_) {
        return new Vector3f((float) (p_232299_ - 1.0D), (float) (p_232300_ - 1.0D), (float) (p_232301_ - 1.0D));
    }


    public static void animate(HumanoidModel<?> model, AnimationState p_233386_, AnimationDefinition p_233387_, float p_233388_, float p_233389_) {
        p_233386_.updateTime(p_233388_, p_233389_);
        p_233386_.ifStarted((p_233392_) -> {
            animate(model, p_233387_, p_233392_.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
        });
    }

}