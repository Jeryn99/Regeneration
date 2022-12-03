package mc.craig.software.regen.util;

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

    /**
     * Animates the given humanoid model based on the given animation definition, elapsed time, and weight.
     *
     * @param humanoidModel       the humanoid model to animate
     * @param animationDefinition the animation definition to use for animating the model
     * @param elapsedTime         the elapsed time, in milliseconds, since the animation started
     * @param weight              the weight of the animation, which determines how much it should be applied to the model
     * @param vec                 a vector that can be used to store temporary values while animating the model
     */
    public static void animate(HumanoidModel<?> humanoidModel, AnimationDefinition animationDefinition, long elapsedTime, float weight, Vector3f vec) {
        float elapsedSeconds = getElapsedSeconds(animationDefinition, elapsedTime);
        for (Map.Entry<String, List<AnimationChannel>> entry : animationDefinition.boneAnimations().entrySet()) {
            ModelPart modelPart = getAnyDescendantWithName(humanoidModel, entry.getKey());

            List<AnimationChannel> channels = entry.getValue();

            if (modelPart != null) {
                channels.forEach((channel) -> {
                    Keyframe[] keyframes = channel.keyframes();
                    int startKeyframeIndex = Math.max(0, Mth.binarySearch(0, keyframes.length, (i) -> elapsedSeconds <= keyframes[i].timestamp()) - 1);
                    int endKeyframeIndex = Math.min(keyframes.length - 1, startKeyframeIndex + 1);
                    Keyframe startKeyframe = keyframes[startKeyframeIndex];
                    Keyframe endKeyframe = keyframes[endKeyframeIndex];
                    float interval = elapsedSeconds - startKeyframe.timestamp();
                    float progress = Mth.clamp(interval / (endKeyframe.timestamp() - startKeyframe.timestamp()), 0.0F, 1.0F);
                    endKeyframe.interpolation().apply(vec, progress, keyframes, startKeyframeIndex, endKeyframeIndex, weight);
                    channel.target().apply(modelPart, vec);
                });
            } else {
                System.out.println("Could not find:" + entry.getKey());
            }
        }
    }

    /**
     * Returns the elapsed seconds for a given animation definition and time.
     *
     * @param animationDefinition the animation definition
     * @param time                the time in milliseconds
     * @return the elapsed seconds
     */
    private static float getElapsedSeconds(AnimationDefinition animationDefinition, long time) {
        float seconds = (float) time / 1000.0F;
        return animationDefinition.looping() ? seconds % animationDefinition.lengthInSeconds() : seconds;
    }

    public static void animate(HumanoidModel<?> model, AnimationState animationState, AnimationDefinition animationDefinition, float elapsedTime, float speed) {
        animationState.updateTime(elapsedTime, speed);
        animationState.ifStarted((state) -> {
            animate(model, animationDefinition, state.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
        });
    }

}