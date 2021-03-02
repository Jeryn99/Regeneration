package me.swirtzly.regen.common.entities;

import me.swirtzly.regen.common.item.GunItem;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.inventory.EquipmentSlotType;

/* Created by Craig on 01/03/2021 */
public class TimelordAttackGoal extends RangedAttackGoal {

    private final TimelordEntity timelord;

    public TimelordAttackGoal(IRangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, maxAttackTime, maxAttackDistanceIn);
        this.timelord = (TimelordEntity) attacker;
    }

    public TimelordAttackGoal(IRangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn) {
        super(attacker, movespeed, p_i1650_4_, maxAttackTime, maxAttackDistanceIn);
        this.timelord = (TimelordEntity) attacker;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        timelord.setAiming(false);
    }

    @Override
    public void tick() {
        super.tick();
        if(timelord.getAiming() && timelord.getAimingTicks() < 1.0F){
            timelord.setAimingTicks(timelord.getAimingTicks() + 0.1F);
        } else if(!timelord.getAiming() && timelord.getAimingTicks() > 0.0F) {
            timelord.setAimingTicks(timelord.getAimingTicks() - 0.1F);
        }
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && timelord.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() instanceof GunItem;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        timelord.setAiming(true);
    }
}
