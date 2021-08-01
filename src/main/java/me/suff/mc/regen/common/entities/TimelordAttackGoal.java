package me.suff.mc.regen.common.entities;

import me.suff.mc.regen.common.item.GunItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;

/* Created by Craig on 01/03/2021 */
public class TimelordAttackGoal extends RangedAttackGoal {

    private final TimelordEntity timelord;

    public TimelordAttackGoal(RangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, maxAttackTime, maxAttackDistanceIn);
        this.timelord = (TimelordEntity) attacker;
    }

    public TimelordAttackGoal(RangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, p_i1650_4_, maxAttackTime, maxAttackDistanceIn);
        this.timelord = (TimelordEntity) attacker;
    }

    @Override
    public void stop() {
        super.stop();
        timelord.setAiming(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (timelord.getAiming() && timelord.getAimingTicks() < 1.0F) {
            timelord.setAimingTicks(timelord.getAimingTicks() + 0.1F);
        } else if (!timelord.getAiming() && timelord.getAimingTicks() > 0.0F) {
            timelord.setAimingTicks(timelord.getAimingTicks() - 0.1F);
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && timelord.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof GunItem;
    }

    @Override
    public void start() {
        super.start();
        timelord.setAiming(true);
    }
}
