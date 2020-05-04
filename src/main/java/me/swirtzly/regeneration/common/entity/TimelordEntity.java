package me.swirtzly.regeneration.common.entity;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.trades.Trades;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly
 * on 03/05/2020 @ 18:50
 */
public class TimelordEntity extends AbstractVillagerEntity {

    private static final DataParameter<Integer> REGENBERATIONS = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Float> ANIMATIONS_TICKS = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.FLOAT);
    private PlayerUtil.RegenState regenState = PlayerUtil.RegenState.ALIVE;

    public TimelordEntity(World world) {
        this(RegenObjects.EntityEntries.TIMELORD.get(), world);
    }

    public TimelordEntity(EntityType<TimelordEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(REGENBERATIONS, rand.nextInt(12));
        this.dataManager.register(ANIMATIONS_TICKS, 0F);
    }

    public int getRegenerations() {
        return getDataManager().get(REGENBERATIONS);
    }

    public void setRegenerations(int regenerations) {
        getDataManager().set(REGENBERATIONS, regenerations);
    }

    public void setAnimationsTicks(float regenerations) {
        getDataManager().set(ANIMATIONS_TICKS, regenerations);
    }

    public float getAnimationTicks() {
        return getDataManager().get(ANIMATIONS_TICKS);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(ZombiePigmanEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TurtleEntity.class, 10, true, false, TurtleEntity.TARGET_DRY_BABY));
    }

    public PlayerUtil.RegenState getRegenState() {
        return regenState;
    }

    public void setRegenState(PlayerUtil.RegenState regenState) {
        this.regenState = regenState;
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.23F);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (!worldIn.isRemote()) {
            RegenCap.get(this).ifPresent((data) -> {
                data.receiveRegenerations(12);
            });
        }
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void tick() {
        super.tick();

        RegenCap.get(this).ifPresent((data) -> {
            if (!world.isRemote) {
                data.synchronise();
                setAnimationsTicks(data.getAnimationTicks());
                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                    setNoAI(true);
                    setInvulnerable(true);
                } else {
                    setNoAI(false);
                    setInvulnerable(false);
                }
            }
        });
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    @Override
    protected void func_213713_b(MerchantOffer p_213713_1_) {
        if (p_213713_1_.func_222221_q()) {
            int i = 3 + this.rand.nextInt(4);
            this.world.addEntity(new ExperienceOrbEntity(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
        }

    }

    @Override
    public boolean func_213705_dZ() {
        return false;
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;
        if (flag) {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        } else if (itemstack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.func_213716_dX() && !this.isChild()) {
            if (hand == Hand.MAIN_HAND) {
                player.addStat(Stats.TALKED_TO_VILLAGER);
            }

            if (this.getOffers().isEmpty()) {
                return super.processInteract(player, hand);
            } else {
                if (!this.world.isRemote) {
                    this.setCustomer(player);
                    this.func_213707_a(player, this.getDisplayName(), 1);
                }

                return true;
            }
        } else {
            return super.processInteract(player, hand);
        }
    }

    protected void populateTradeData() {
        VillagerTrades.ITrade[] trades = Trades.genTrades();
        if (trades != null) {
            MerchantOffers merchantoffers = this.getOffers();
            this.addTrades(merchantoffers, trades, 5);
        }

       /* if(ModList.get().isLoaded("tardis")) {
            VillagerTrades.ITrade[] tardisTrades = TardisCompat.genTrades();
            if (trades != null) {
                MerchantOffers merchantoffers = this.getOffers();
                this.addTrades(merchantoffers, tardisTrades, 5);
            }
        }*/
    }
}
