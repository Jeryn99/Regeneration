package me.suff.mc.regen.common.traits.positive;

import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.traits.TraitManager;
import net.minecraft.entity.LivingEntity;

public class WallClimbingTrait extends TraitManager.IDna {

    public WallClimbingTrait() {
        super("wallclimbing");
    }

    @Override
    public void onUpdate(IRegen cap) {
        LivingEntity player = cap.getLivingEntity();
        if (player.horizontalCollision) {
            player.getDeltaMovement().add(0, 0.2F, 0);
        }
    }

    @Override
    public void onAdded(IRegen cap) {

    }

    @Override
    public void onRemoved(IRegen cap) {

    }

}
