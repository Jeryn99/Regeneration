package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.IRegen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class TraitLongArms extends AbstractTrait {

    public static final UUID REACH_UUID = UUID.fromString("4a204916-e836-4b7c-b133-0da469f8b9ec");

    @Override
    public void apply(IRegen data) {
        LivingEntity living = data.getLiving();
        AttributeInstance reach = living.getAttribute(ForgeMod.REACH_DISTANCE.get());
        if (reach != null) {
            reach.addPermanentModifier(new AttributeModifier(REACH_UUID, "Reach modifier", 15, AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public void remove(IRegen data) {
        LivingEntity living = data.getLiving();
        AttributeInstance reach = living.getAttribute(ForgeMod.REACH_DISTANCE.get());
        if (reach != null) {
            reach.removeModifier(REACH_UUID);
        }
    }

    @Override
    public void tick(IRegen data) {

    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public int color() {
        return 9740385;
    }
}
