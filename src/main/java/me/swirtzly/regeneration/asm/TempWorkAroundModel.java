package me.swirtzly.regeneration.asm;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;

public class TempWorkAroundModel extends PlayerModel {

    public TempWorkAroundModel(float p_i46304_1_, boolean p_i46304_2_) {
        super(p_i46304_1_, p_i46304_2_);
    }

    @Override
    public void setRotationAngles(LivingEntity p_212844_1_, float p_212844_2_, float p_212844_3_, float p_212844_4_, float p_212844_5_, float p_212844_6_, float p_212844_7_) {
        super.setRotationAngles(p_212844_1_, p_212844_2_, p_212844_3_, p_212844_4_, p_212844_5_, p_212844_6_, p_212844_7_);
        RegenClientHooks.handleRotations(this, p_212844_2_, p_212844_3_, p_212844_4_, p_212844_5_, p_212844_6_, p_212844_7_, p_212844_1_);
    }
}
