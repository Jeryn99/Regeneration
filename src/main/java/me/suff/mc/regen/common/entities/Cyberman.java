package me.suff.mc.regen.common.entities;

import me.suff.mc.regen.common.entities.ai.FixedMeleeGoal;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;

public class Cyberman extends PathfinderMob implements RangedAttackMob {

    public Cyberman(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void killed(ServerLevel p_19929_, LivingEntity livingEntity) {
        super.killed(p_19929_, livingEntity);

        if (ForgeEventFactory.canLivingConvert(livingEntity, REntities.TIMELORD.get(), (timer) -> {
        }) && livingEntity instanceof Timelord timelord) {
            Component customName = timelord.getCustomName();
            Cyberman cyberman = timelord.convertTo(REntities.CYBERLORD.get(), true);
            cyberman.setCustomName(new TextComponent("Cyber-" + customName.getString()));
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        RegenCap.get(this).ifPresent((data) -> data.addRegens(level.getRandom().nextInt(12)));
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.targetSelector.addGoal(8, new FixedMeleeGoal(this, 0.5, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Cyberman.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Timelord.class, true));
    }

    @Override
    public void tick() {
        RegenCap.get(this).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING) {
                setDeltaMovement(new Vec3(0, 0, 0));
            }
        });
        super.tick();
    }

    @Override
    public void kill() {
        remove(RemovalReason.KILLED);
    }

    @Override
    public AttributeMap getAttributes() {
        return new AttributeMap(Timelord.createAttributes().build());
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        if (!blockState.getMaterial().isLiquid()) {
            this.playSound(RSounds.CYBER_WALK.get(), 0.15F, 1);
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float p_33318_) {
        Laser laser = new Laser(REntities.LASER.get(), this, level);
        laser.setColors(1, 0, 0);
        double d0 = livingEntity.getEyeY() - (double) 1.1F;
        double d1 = livingEntity.getX() - this.getX();
        double d2 = d0 - laser.getY();
        double d3 = livingEntity.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double) 0.2F;
        laser.shoot(d1, d2 + d4, d3, 1.6F, 0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(laser);
    }
}
