package mc.craig.software.regen.client.visual;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FogTracker {

    public static Vec3 getSitutionalFogColor() {
        Entity renderView = Minecraft.getInstance().getCameraEntity();

        AtomicReference<Vec3> vec3Color = new AtomicReference<>();
        vec3Color.set(null);

        RegenerationData.get((LivingEntity) renderView).ifPresent((data) -> {
            if (data.regenState() == RegenStates.GRACE_CRIT) {
                vec3Color.set(new Vec3(0.5F, 0.5F, 0.5F));
            }
            if (data.transitionType() == TransitionTypes.TROUGHTON && data.regenState() == RegenStates.REGENERATING) {
                vec3Color.set(new Vec3(0, 0, 0));
            }
        });

        return vec3Color.get();
    }

    public static boolean setUpFog(LivingEntity livingEntity) {
        AtomicBoolean cancelEvent = new AtomicBoolean(false);
        RegenerationData.get(livingEntity).ifPresent((data) -> {

            if (data.regenState() == RegenStates.GRACE_CRIT) {
                RenderSystem.setShaderFogStart(-8);
                RenderSystem.setShaderFogEnd(22 * 0.5F);
                RenderSystem.setShaderFogShape(FogShape.SPHERE);
                cancelEvent.set(true);
            }
            if (data.transitionType() == TransitionTypes.TROUGHTON && data.updateTicks() > 0) {
                RenderSystem.setShaderFogStart(-8);
                RenderSystem.setShaderFogEnd(17 * 0.5F);
                RenderSystem.setShaderFogShape(FogShape.SPHERE);
                cancelEvent.set(true);
            }
        });
        return cancelEvent.get();
    }
}
