package me.suff.mc.regen.common.entities;

import me.suff.mc.regen.client.skin.CommonSkin;
import me.suff.mc.regen.common.item.ElixirItem;
import me.suff.mc.regen.common.item.SpawnItem;
import me.suff.mc.regen.common.objects.*;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.common.traits.Traits;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.RemoveTimelordSkinMessage;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenSources;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Random;

/**
 * Created by Swirtzly
 * on 03/05/2020 @ 18:50
 */
public class TimelordEntity extends AbstractVillagerEntity implements IRangedAttackMob {

    private static final DataParameter< String > TYPE = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.STRING);
    private static final DataParameter< String > PERSONALITY = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.STRING);
    private static final DataParameter< Boolean > AIMING = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter< Boolean > IS_MALE = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter< Float > AIMING_TICKS = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.FLOAT);

    private final SwimmerPathNavigator waterNavigator;
    private final GroundPathNavigator groundNavigator;

    public TimelordEntity(World world) {
        this(REntities.TIMELORD.get(), world);
    }

    public TimelordEntity(EntityType< TimelordEntity > entityEntityType, World world) {
        super(entityEntityType, world);
        this.waterNavigator = new SwimmerPathNavigator(this, world);
        this.groundNavigator = new GroundPathNavigator(this, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.func_234295_eP_().
                createMutableAttribute(Attributes.FOLLOW_RANGE, 35D).
                createMutableAttribute(Attributes.MOVEMENT_SPEED, (double) 0.23F).
                createMutableAttribute(Attributes.ATTACK_DAMAGE, 3F).
                createMutableAttribute(Attributes.MAX_HEALTH, 20D).
                createMutableAttribute(Attributes.ARMOR, 2.0D);
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(TYPE, rand.nextBoolean() ? TimelordType.COUNCIL.name() : TimelordType.GUARD.name());
        getDataManager().register(AIMING, false);
        getDataManager().register(AIMING_TICKS, 0.0F);
        getDataManager().register(IS_MALE, rand.nextBoolean());
        getDataManager().register(PERSONALITY, RSoundSchemes.getRandom(isMale()).identify().toString());
        setup();
    }

    @Override
    protected void onVillagerTrade(MerchantOffer offer) {

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
        this.goalSelector.addGoal(1, new SwimGoal(this));
        if (getTimelordType() == TimelordType.GUARD) {
            this.goalSelector.addGoal(2, new TimelordAttackGoal(this, 1.0D, 20, 20.0F));
        } else {
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        }
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(TimelordEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SkeletonEntity.class, false));
    }

    public void setup() {
        if (!world.isRemote()) {

            RegenCap.get(this).ifPresent((data) -> {
                data.addRegens(world.getRandom().nextInt(12));
                CompoundNBT nbt = new CompoundNBT();
                nbt.putFloat(RConstants.PRIMARY_RED, rand.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_GREEN, rand.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_BLUE, rand.nextInt(255) / 255.0F);

                nbt.putFloat(RConstants.SECONDARY_RED, rand.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_GREEN, rand.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_BLUE, rand.nextInt(255) / 255.0F);
                data.readStyle(nbt);
                data.setTransitionType(TransitionTypes.getRandomTimelordType());
                initSkin(data);
                genName();
                setEquipmentBasedOnDifficulty(world.getDifficultyForLocation(getPosition()));
            });
        }
    }

    @Override
    protected SoundEvent getVillagerYesNoSound(boolean getYesSound) {
        SoundScheme personality = getPersonality();
        return getYesSound ? personality.getTradeAcceptSound() : personality.getTradeDeclineSound();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return getPersonality().getDeathSound();
    }

    public void genName() {
        if (RegenUtil.USERNAMES.length <= 0) {
            RegenUtil.setupNames();
        }
        setCustomName(new TranslationTextComponent(RegenUtil.USERNAMES[rand.nextInt(RegenUtil.USERNAMES.length - 1)]));
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        if (!world.isRemote) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), new RemoveTimelordSkinMessage(this));
        }
    }

    /*Setup initial skins for the timelords*/
    public void initSkin(IRegen data) {
        world.getServer().runAsync(() -> {
            File file = CommonSkin.chooseRandomSkin(world.rand, !isMale(), true);
            if (file != null) {
                data.setSkin(RegenUtil.fileToBytes(file));
            }
            data.setAlexSkin(true);
            data.syncToClients(null);
        });
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

                if (!data.isSkinValidForUse()) {
                    initSkin(data);
                    data.syncToClients(null);
                }

                if (ticksExisted < 20) {
                    data.syncToClients(null);
                }

                if (data.getCurrentState() == RegenStates.REGENERATING) {

                    if (data.getTicksAnimating() == 10) {
                        if (getPersonality().getScreamSound() != null) {
                            playSound(getPersonality().getScreamSound(), 1, 1);
                        }
                    }
                    if (data.getTicksAnimating() == 100) {
                        setMale(rand.nextBoolean());
                        setPersonality(RSoundSchemes.getRandom(isMale()).identify().toString());
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
    protected float getSoundPitch() {
        return 1;
    }

    @Override
    public void onKillCommand() {
        if (!world.isRemote) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), new RemoveTimelordSkinMessage(this));
        }
        remove();
    }

    public SoundScheme getPersonality() {
        return RSoundSchemes.get(new ResourceLocation(getDataManager().get(PERSONALITY)));
    }

    public void setPersonality(String per) {
        getDataManager().set(PERSONALITY, per);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putString("timelord_type", getTimelordType().name());
        compound.putBoolean("is_male", isMale());
        compound.putString("personality", getPersonality().identify().toString());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("timelord_type")) {
            setTimelordType(TimelordType.valueOf(compound.getString("timelord_type")));
        }

        if (compound.contains("is_male")) {
            setMale(compound.getBoolean("is_male"));
        }

        if (compound.contains("personality")) {
            setPersonality(compound.getString("personality"));
        }
    }

    public boolean isMale() {
        return getDataManager().get(IS_MALE);
    }

    public void setMale(boolean male) {
        getDataManager().set(IS_MALE, male);
    }

    @Override
    protected void populateTradeData() {
        if (getTimelordType() == TimelordType.COUNCIL) {
            MerchantOffers merchantoffers = this.getOffers();

            for (int i = rand.nextInt(7); i > 0; i--) {
                Traits.ITrait trait = Traits.getRandomTrait(rand, false);
                ItemStack item = new ItemStack(RItems.ELIXIR.get());
                Item[] currency = new Item[]{
                        Items.GOLD_INGOT,
                        Items.BONE,
                        Items.EMERALD,
                        Items.IRON_INGOT
                };
                ElixirItem.setTrait(item, trait);
                TimelordTrade[] trades = new TimelordTrade[]{new TimelordEntity.TimelordTrade(new ItemStack(currency[rand.nextInt(currency.length - 1)], MathHelper.clamp(rand.nextInt(10), 6, 20)), item, rand.nextInt(7), 5)};
                this.addTrades(merchantoffers, trades, 5);
            }

            TimelordTrade[] tradetrades = new TimelordTrade[]{
                    new TimelordEntity.TimelordTrade(new ItemStack(Items.DIAMOND, 3), new ItemStack(Items.GUNPOWDER, 4), new ItemStack(RItems.RIFLE.get()), rand.nextInt(7), 5),
                    new TimelordEntity.TimelordTrade(new ItemStack(Items.NETHERITE_INGOT, 4), new ItemStack(Items.BLAZE_ROD, 4), new ItemStack(RItems.PISTOL.get()), rand.nextInt(7), 5)
            };
            this.addTrades(merchantoffers, tradetrades, 5);

        }
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        if (getTimelordType() == TimelordType.GUARD) {
            Item stack = rand.nextBoolean() ? RItems.RIFLE.get() : RItems.PISTOL.get();
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(stack));
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return getPersonality().getHurtSound();
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        boolean isPistol = getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem() == RItems.PISTOL.get();

        LaserProjectile laserProjectile = new LaserProjectile(REntities.LASER.get(), this, world);
        laserProjectile.setDamage(isPistol ? 4 : 10);
        laserProjectile.setDamageSource(isPistol ? RegenSources.REGEN_DMG_STASER : RegenSources.REGEN_DMG_RIFLE);
        double d0 = target.getPosYEye() - (double) 1.1F;
        double d1 = target.getPosX() - this.getPosX();
        double d2 = d0 - laserProjectile.getPosY();
        double d3 = target.getPosZ() - this.getPosZ();
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        laserProjectile.shoot(d1, d2 + (double) f, d3, 1.6F, 0);
        this.playSound(isPistol ? RSounds.STASER.get() : RSounds.RIFLE.get(), 1.0F, 0.4F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(laserProjectile);
    }

    public boolean getAiming() {
        return getDataManager().get(AIMING);
    }

    public void setAiming(boolean isAiming) {
        getDataManager().set(AIMING, isAiming);
    }

    public float getAimingTicks() {
        return getDataManager().get(AIMING_TICKS);
    }

    public void setAimingTicks(float isAiming) {
        getDataManager().set(AIMING_TICKS, isAiming);
    }

    @Override
    public ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
        if (itemstack.getItem() != RItems.SPAWN_ITEM.get() && this.isAlive() && !this.hasCustomer() && !this.isChild()) {
            if (this.getOffers().isEmpty()) {
                return ActionResultType.func_233537_a_(this.world.isRemote);
            } else {
                if (!this.world.isRemote) {
                    this.setCustomer(p_230254_1_);
                    this.openMerchantContainer(p_230254_1_, this.getDisplayName(), 1);
                }
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        } else {
            return super.func_230254_b_(p_230254_1_, p_230254_2_);
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        switch (getTimelordType()) {
            case GUARD:
                ItemStack guardStack = new ItemStack(RItems.SPAWN_ITEM.get());
                SpawnItem.setType(guardStack, SpawnItem.Timelord.GUARD);
                return guardStack;
            case COUNCIL:
                ItemStack councilStack = new ItemStack(RItems.SPAWN_ITEM.get());
                SpawnItem.setType(councilStack, isMale() ? SpawnItem.Timelord.MALE_COUNCIL : SpawnItem.Timelord.FEMALE_COUNCIL);
                return councilStack;
        }
        return null;
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

    public static class TimelordTrade implements VillagerTrades.ITrade {

        private ItemStack coin2;
        private ItemStack coin;
        private ItemStack wares;

        private int xp;
        private int stock;

        public TimelordTrade(ItemStack coin, ItemStack coin2, ItemStack wares, int stock, int xp) {
            this.xp = xp;
            this.stock = stock + 1;
            this.wares = wares;
            this.coin = coin;
            this.coin2 = coin2;
        }

        public TimelordTrade(ItemStack coin, ItemStack wares, int stock, int xp) {
            this(coin, ItemStack.EMPTY, wares, stock, xp);
        }

        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            return new MerchantOffer(coin, coin2, wares, stock, xp, 0F);
        }
    }

}