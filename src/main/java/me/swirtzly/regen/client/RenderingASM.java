package me.swirtzly.regen.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RenderingASM {

    @OnlyIn(Dist.CLIENT)
    public static void preRenderCallback(Entity entityIn, MatrixStack matrixStackIn, float partialTicks) {
        if(entityIn instanceof LivingEntity) {
            matrixStackIn.push();
            LivingEntity livingEntity = (LivingEntity) entityIn;
            RegenCap.get(livingEntity).ifPresent(iRegen -> {
                TransitionType<?> type = iRegen.getTransitionType().create();
                type.getRenderer().onBefore(livingEntity, matrixStackIn, partialTicks);
            });
        }
    }


    @OnlyIn(Dist.CLIENT)
    public static void postRenderCallback(Entity entityIn, MatrixStack matrixStackIn, float partialTicks) {
        if(entityIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entityIn;
            RegenCap.get(livingEntity).ifPresent(iRegen -> {
                TransitionType<?> type = iRegen.getTransitionType().create();
                type.getRenderer().onAfter(livingEntity, matrixStackIn, partialTicks);
            });
            matrixStackIn.pop();
        }
    }


}
