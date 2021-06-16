package me.suff.mc.regen.common.entity;

import me.suff.mc.regen.common.advancements.TriggerManager;
import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.entity.ai.TimelordMelee;
import me.suff.mc.regen.common.item.GunItem;
import me.suff.mc.regen.common.skin.HandleSkins;
import me.suff.mc.regen.common.trades.TimelordTrades;
import me.suff.mc.regen.common.types.RegenTypes;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.PlayerUtil;
import me.suff.mc.regen.util.common.RegenUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Craig
 * on 03/05/2020 @ 18:50
 */
public class TimelordEntity extends AbstractVillagerEntity implements IRangedAttackMob {

    private static final DataParameter<String> TYPE = EntityDataManager.defineId(TimelordEntity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.defineId(TimelordEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> VILLAGER = EntityDataManager.defineId(TimelordEntity.class, DataSerializers.BOOLEAN);

    private final SwimmerPathNavigator waterNavigator;
    private final GroundPathNavigator groundNavigator;

    public TimelordEntity(World world) {
        this(RegenObjects.EntityEntries.TIMELORD.get(), world);
    }

    public TimelordEntity(EntityType<TimelordEntity> entityEntityType, World world) {
        super(entityEntityType, world);
        this.waterNavigator = new SwimmerPathNavigator(this, world);
        this.groundNavigator = new GroundPathNavigator(this, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(VILLAGER, false);
        getEntityData().define(TYPE, random.nextBoolean() ? TimelordType.COUNCIL.name() : TimelordType.GUARD.name());
        getEntityData().define(SWINGING_ARMS, false);
    }


    @Override
    public void updateSwimming() {
        if (!this.level.isClientSide) {
            if (this.isEffectiveAi() && this.isInWater()) {
                this.navigation = this.waterNavigator;
                this.setSwimming(true);
            } else {
                this.navigation = this.groundNavigator;
                this.setSwimming(false);
            }
        }
    }

    public boolean isSwingingArms() {
        return this.getEntityData().get(SWINGING_ARMS);
    }

    public void setSwingingArms(boolean swingingArms) {
        this.getEntityData().set(SWINGING_ARMS, swingingArms);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new TimelordMelee(this, 1.2F, true));
        this.goalSelector.addGoal(1, new SwimGoal(this));
        if (getTimelordType() == TimelordType.GUARD) {
            this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25D, 15, 20.0F));
        }
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(TimelordEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, false));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23F);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }


    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        if (!worldIn.isClientSide()) {

            if (getTimelordType() == TimelordType.GUARD) {
                setItemInHand(Hand.MAIN_HAND, new ItemStack(random.nextBoolean() ? RegenObjects.Items.PISTOL.get() : RegenObjects.Items.RIFLE.get()));
            }

            RegenCap.get(this).ifPresent((data) -> {
                data.receiveRegenerations(worldIn.getRandom().nextInt(12));
                CompoundNBT nbt = new CompoundNBT();
                nbt.putFloat("PrimaryRed", random.nextInt(255) / 255.0F);
                nbt.putFloat("PrimaryGreen", random.nextInt(255) / 255.0F);
                nbt.putFloat("PrimaryBlue", random.nextInt(255) / 255.0F);

                nbt.putFloat("SecondaryRed", random.nextInt(255) / 255.0F);
                nbt.putFloat("SecondaryGreen", random.nextInt(255) / 255.0F);
                nbt.putFloat("SecondaryBlue", random.nextInt(255) / 255.0F);
                data.setStyle(nbt);
                data.setRegenType(random.nextBoolean() ? RegenTypes.FIERY : RegenTypes.HARTNELL);
                initSkin(data);
                data.synchronise();
            });
        }
        setCustomName(new StringTextComponent(RegenUtil.TIMELORD_NAMES[random.nextInt(RegenUtil.TIMELORD_NAMES.length)]));

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }


    /*Setup initial skins for the timelords*/
    public void initSkin(IRegen data) {
        File file = null;
        try {
            file = HandleSkins.chooseRandomTimelordSkin(level.random);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file == null) return;
        data.setEncodedSkin(HandleSkins.imageToPixelData(file));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.getMainHandItem().getItem() instanceof GunItem) {
            GunItem gunItem = (GunItem) getMainHandItem().getItem();
            LaserEntity laserEntity = new LaserEntity(RegenObjects.EntityEntries.LASER.get(), this, this.level);
            laserEntity.setColor(new Vec3d(1, 0, 0));
            laserEntity.setDamage(gunItem.getDamage());
            double d0 = target.x - this.x;
            double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - laserEntity.y;
            double d2 = target.z - this.z;
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            laserEntity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));
            this.level.playSound(null, this.x, this.y, this.z, this.getMainHandItem().getItem() == RegenObjects.Items.PISTOL.get() ? RegenObjects.Sounds.STASER.get() : RegenObjects.Sounds.RIFLE.get(), SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(laserEntity);
        }
    }

    public TimelordType getTimelordType() {
        String type = getEntityData().get(TYPE);
        for (TimelordType value : TimelordType.values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return TimelordType.GUARD;
    }

    public void setTimelordType(TimelordType type) {
        getEntityData().set(TYPE, type.name());
    }

    @Override
    public void tick() {
        super.tick();

        RegenCap.get(this).ifPresent((data) -> {
            if (!level.isClientSide) {

                if (tickCount < 20) {
                    data.synchronise();
                }

                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                    if (data.getAnimationTicks() == 100) {
                        setVillager(false);
                        initSkin(data);
                    }
                    setNoAi(true);
                    setInvulnerable(true);
                } else {
                    setNoAi(false);
                    setInvulnerable(false);
                }
            }
        });
    }

    public Boolean isVillagerModel() {
        return getEntityData().get(VILLAGER);
    }

    public void setVillager(boolean villager) {
        getEntityData().set(VILLAGER, villager);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(AgeableEntity ageable) {
        return null;
    }

    @Override
    protected void rewardTradeXp(MerchantOffer merchantOffer) {
        if (merchantOffer.shouldRewardExp()) {
            int i = 3 + this.random.nextInt(4);
            this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.x, this.y + 0.5D, this.z, i));
        }

    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public boolean mobInteract(PlayerEntity player, Hand hand) {
        if (getTimelordType() == TimelordType.COUNCIL) {

            if (!level.isClientSide) {
                TriggerManager.TIMELORD_TRADE.trigger((ServerPlayerEntity) player);
            }

            AtomicBoolean atomicBoolean = new AtomicBoolean();
            atomicBoolean.set(false);

            RegenCap.get(this).ifPresent((data) -> {
                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                    atomicBoolean.set(true);
                }
            });

            if (atomicBoolean.get()) {
                return true;
            }

            ItemStack itemstack = player.getItemInHand(hand);
            boolean flag = itemstack.getItem() == Items.NAME_TAG;
            if (flag) {
                itemstack.interactEnemy(player, this, hand);
                return true;
            } else if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.isTrading() && !this.isBaby()) {
                if (hand == Hand.MAIN_HAND) {
                    player.awardStat(Stats.TALKED_TO_VILLAGER);
                }

                if (this.getOffers().isEmpty()) {
                    return super.mobInteract(player, hand);
                } else {
                    if (!this.level.isClientSide) {
                        this.setTradingPlayer(player);
                        this.openTradingScreen(player, this.getDisplayName(), 1);
                    }

                    return true;
                }
            } else {
                return super.mobInteract(player, hand);
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void kill() {
        remove();
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("villager", isVillagerModel());
        compound.putString("timelord_type", getTimelordType().name());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        setVillager(compound.getBoolean("villager"));
        if (compound.contains("timelord_type")) {
            setTimelordType(TimelordType.valueOf(compound.getString("timelord_type")));
        }
    }

    @Override
    protected void updateTrades() {
        if (getTimelordType() == TimelordType.COUNCIL) {
            VillagerTrades.ITrade[] trades = TimelordTrades.genTrades();
            if (trades != null) {
                MerchantOffers merchantoffers = this.getOffers();
                this.addOffersFromItemListings(merchantoffers, trades, 5);
            }
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
