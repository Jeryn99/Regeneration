package me.swirtzly.regeneration.common.entity;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.ai.TimelordMelee;
import me.swirtzly.regeneration.common.trades.Trades;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.tardis.mod.entity.DalekEntity;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Swirtzly
 * on 03/05/2020 @ 18:50
 */
public class TimelordEntity extends AbstractVillagerEntity {

    public TimelordEntity(World world) {
        this(RegenObjects.EntityEntries.TIMELORD.get(), world);
    }

    public TimelordEntity(EntityType<TimelordEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new TimelordMelee(this, (double) 1.2F, true));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, DalekEntity.class, false));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.23F);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if (!worldIn.isRemote()) {
            RegenCap.get(this).ifPresent((data) -> {
                data.receiveRegenerations(worldIn.getRandom().nextInt(12));

                CompoundNBT nbt = new CompoundNBT();
                nbt.putFloat("PrimaryRed", rand.nextInt(255) / 255.0F);
                nbt.putFloat("PrimaryGreen", rand.nextInt(255) / 255.0F);
                nbt.putFloat("PrimaryBlue", rand.nextInt(255) / 255.0F);

                nbt.putFloat("SecondaryRed", rand.nextInt(255) / 255.0F);
                nbt.putFloat("SecondaryGreen", rand.nextInt(255) / 255.0F);
                nbt.putFloat("SecondaryBlue", rand.nextInt(255) / 255.0F);
                data.setStyle(nbt);

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

    @Override
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
