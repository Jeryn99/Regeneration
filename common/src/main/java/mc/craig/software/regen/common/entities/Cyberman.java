package mc.craig.software.regen.common.entities;

import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Cyberman extends PathfinderMob implements RangedAttackMob {

    public Cyberman(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    /*@Override
    public boolean wasKilled(ServerLevel serverLevel, LivingEntity livingEntity) {
        boolean wasKilled = super.wasKilled(serverLevel, livingEntity);
        if ((serverLevel.getDifficulty() == Difficulty.NORMAL || serverLevel.getDifficulty() == Difficulty.HARD) && livingEntity instanceof Timelord timelord) {
            if (serverLevel.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                return wasKilled;
            }

            Component customName = timelord.getCustomName();
            Cyberman cyberman = timelord.convertTo(REntities.CYBER.get(), true);
            cyberman.setCustomName(Component.literal("Cyber-" + customName.getString()));

            wasKilled = false;
        }

        return wasKilled;
    }*/

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor p_21434_, @NotNull DifficultyInstance p_21435_, @NotNull MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        RegenerationData.get(this).ifPresent((data) -> {
            data.addRegens(level().getRandom().nextInt(12));
            data.setTransitionType(TransitionTypes.FIERY);
        });
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 20, 20.0F));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Cyberman.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Timelord.class, true));
    }

    @Override
    public void tick() {
        RegenerationData.get(this).ifPresent(iRegen -> {
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
    public @NotNull AttributeMap getAttributes() {
        return new AttributeMap(Timelord.createAttributes().build());
    }

    @Override
    protected void playStepSound(@NotNull BlockPos blockPos, BlockState blockState) {
        if (blockState.isSolid()) {
            this.playSound(RSounds.CYBER_WALK.get(), 0.15F, 1);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity livingEntity, float p_33318_) {
        RegenerationData.get(livingEntity).ifPresent(iRegen -> {
            if (iRegen.regenState() != RegenStates.REGENERATING) {
                Laser laser = new Laser(REntities.LASER.get(), this, level());
                laser.setColors(0, 0, 0.2F);
                double d0 = livingEntity.getEyeY() - (double) 1.1F;
                double d1 = livingEntity.getX() - this.getX();
                double d2 = d0 - laser.getY();
                double d3 = livingEntity.getZ() - this.getZ();
                double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double) 0.2F;
                laser.shoot(d1, d2 + d4, d3, 0.5F, 14 - livingEntity.level().getDifficulty().getId() * 4);
                this.playSound(RSounds.CYBER_FIRE.get(), 0.5F, 0.0F);
                this.level().addFreshEntity(laser);
            }
        });
    }
}