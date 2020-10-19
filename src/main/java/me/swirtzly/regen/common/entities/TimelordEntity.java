package me.swirtzly.regen.common.entities;

import me.swirtzly.regen.client.skin.CommonSkin;
import me.swirtzly.regen.common.objects.REntities;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import me.swirtzly.regen.util.RConstants;
import me.swirtzly.regen.util.RegenUtil;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import java.io.File;

import static me.swirtzly.regen.common.regen.state.RegenStates.REGENERATING;

/**
 * Created by Swirtzly
 * on 03/05/2020 @ 18:50
 */
public class TimelordEntity extends CreatureEntity {

    private static final DataParameter<String> TYPE = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.BOOLEAN);

    private final SwimmerPathNavigator waterNavigator;
    private final GroundPathNavigator groundNavigator;

    public TimelordEntity(World world) {
        this(REntities.TIMELORD.get(), world);
    }

    public TimelordEntity(EntityType<TimelordEntity> entityEntityType, World world) {
        super(entityEntityType, world);
        this.waterNavigator = new SwimmerPathNavigator(this, world);
        this.groundNavigator = new GroundPathNavigator(this, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.func_234295_eP_().

                createMutableAttribute(Attributes.FOLLOW_RANGE, 35D).
                createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23F).
                createMutableAttribute(Attributes.ATTACK_DAMAGE, 3F).
                createMutableAttribute(Attributes.MAX_HEALTH, 20D).
                createMutableAttribute(Attributes.ARMOR, 2.0D);
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(TYPE, rand.nextBoolean() ? TimelordType.COUNCIL.name() : TimelordType.GUARD.name());
        getDataManager().register(SWINGING_ARMS, false);
    }

    @Override
    public void updateSwimming() {
        if (!this.world.isRemote) {
            if (this.isServerWorld() && this.isInWater()) {
                this.navigator = this.waterNavigator;
                this.setSwimming(true);
            } else {
                this.navigator = this.groundNavigator;
                this.setSwimming(false);
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        this.goalSelector.addGoal(1, new SwimGoal(this));
     /*   if (getTimelordType() == TimelordType.GUARD) {
            this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25D, 15, 20.0F));
        }*/ //TODO
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(TimelordEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, false));
    }

    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @org.jetbrains.annotations.Nullable ILivingEntityData spawnDataIn, @org.jetbrains.annotations.Nullable CompoundNBT dataTag) {

        if (!worldIn.isRemote()) {

            RegenCap.get(this).ifPresent((data) -> {
                data.addRegens(worldIn.getRandom().nextInt(12));
                CompoundNBT nbt = new CompoundNBT();
                nbt.putFloat(RConstants.PRIMARY_RED, rand.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_GREEN, rand.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_BLUE, rand.nextInt(255) / 255.0F);

                nbt.putFloat(RConstants.SECONDARY_RED, rand.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_GREEN, rand.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_BLUE, rand.nextInt(255) / 255.0F);
                data.readStyle(nbt);
                data.setTransitionType(TransitionTypes.FIERY);
                initSkin(data);
            });
        }

        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    /*Setup initial skins for the timelords*/
    public void initSkin(IRegen data) {
        File file = CommonSkin.chooseRandomSkin(world.rand, rand.nextBoolean());
        data.setSkin(RegenUtil.fileToBytes(file));
        data.setAlexSkin(true);
        data.syncToClients(null);
    }

    public TimelordType getTimelordType() {
        String type = getDataManager().get(TYPE);
        for (TimelordType value : TimelordType.values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return TimelordType.GUARD;
    }

    public void setTimelordType(TimelordType type) {
        getDataManager().set(TYPE, type.name());
    }

    @Override
    public void tick() {
        super.tick();

        RegenCap.get(this).ifPresent((data) -> {
            if (!world.isRemote) {

                if (ticksExisted < 20) {
                    data.syncToClients(null);
                }

                if (data.getCurrentState() == REGENERATING) {
                    if (data.getTicksAnimating() == 100) {
                        initSkin(data);
                    }
                    setNoAI(true);
                    setInvulnerable(true);
                } else {
                    setNoAI(false);
                    setInvulnerable(false);
                }
            }
        });
    }


    @Override
    public void onKillCommand() {
        remove();
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putString("timelord_type", getTimelordType().name());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("timelord_type")) {
            setTimelordType(TimelordType.valueOf(compound.getString("timelord_type")));
        }
    }

    public enum TimelordType {
        COUNCIL("timelord"), GUARD("guards");

        private final String name;

        TimelordType(String guard) {
            this.name = guard;
        }

        public String getName() {
            return name;
        }
    }
}