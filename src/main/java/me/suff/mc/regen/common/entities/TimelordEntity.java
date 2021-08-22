package me.suff.mc.regen.common.entities;

import me.suff.mc.regen.client.skin.CommonSkin;
import me.suff.mc.regen.common.advancement.TriggerManager;
import me.suff.mc.regen.common.item.ElixirItem;
import me.suff.mc.regen.common.item.SpawnItem;
import me.suff.mc.regen.common.objects.*;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.common.traits.AbstractTrait;
import me.suff.mc.regen.common.traits.RegenTraitRegistry;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.RemoveTimelordSkinMessage;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenSources;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Random;

/**
 * Created by Suff
 * on 03/05/2020 @ 18:50
 */
public class TimelordEntity extends Villager implements RangedAttackMob {

    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(TimelordEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> PERSONALITY = SynchedEntityData.defineId(TimelordEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> AIMING = SynchedEntityData.defineId(TimelordEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_MALE = SynchedEntityData.defineId(TimelordEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_SETUP = SynchedEntityData.defineId(TimelordEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> AIMING_TICKS = SynchedEntityData.defineId(TimelordEntity.class, EntityDataSerializers.FLOAT);
    protected final WaterBoundPathNavigation waterNavigator;
    protected final GroundPathNavigation groundNavigator;

    public TimelordEntity(Level world) {
        this(REntities.TIMELORD.get(), world);
    }

    public TimelordEntity(EntityType<TimelordEntity> entityEntityType, Level world) {
        super(entityEntityType, world);
        this.waterNavigator = new WaterBoundPathNavigation(this, world);
        this.groundNavigator = new GroundPathNavigation(this, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().
                add(Attributes.FOLLOW_RANGE, 35D).
                add(Attributes.MOVEMENT_SPEED, 0.23F).
                add(Attributes.ATTACK_DAMAGE, 3F).
                add(Attributes.MAX_HEALTH, 20D).
                add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public Villager getBreedOffspring(ServerLevel world, AgeableMob mate) {
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(TYPE, random.nextBoolean() ? TimelordType.COUNCIL.name() : TimelordType.GUARD.name());
        getEntityData().define(AIMING, false);
        getEntityData().define(AIMING_TICKS, 0.0F);
        getEntityData().define(IS_MALE, random.nextBoolean());
        getEntityData().define(PERSONALITY, RSoundSchemes.getRandom(male()).identify().toString());
        getEntityData().define(HAS_SETUP, false);
        setup();
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

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));
        this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));


        if (getTimelordType() == TimelordType.GUARD) {
            this.goalSelector.addGoal(2, new TimelordAttackGoal(this, 1.0D, 20, 20.0F));
        }

        if (getTimelordType() == TimelordType.COUNCIL) {
            for (Item item : RegenUtil.TIMELORD_CURRENCY.getValues()) {
                this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, Ingredient.of(item), false));
            }
            this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
            this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        }

        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(TimelordEntity.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zombie.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Skeleton.class, false));
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int i = 3 + this.random.nextInt(4);
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), i));
        }
    }

    public void setup() {
        if (!level.isClientSide()) {

            RegenCap.get(this).ifPresent((data) -> {
                data.addRegens(level.getRandom().nextInt(12));
                CompoundTag nbt = new CompoundTag();
                nbt.putFloat(RConstants.PRIMARY_RED, random.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_GREEN, random.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_BLUE, random.nextInt(255) / 255.0F);

                nbt.putFloat(RConstants.SECONDARY_RED, random.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_GREEN, random.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_BLUE, random.nextInt(255) / 255.0F);
                data.readStyle(nbt);
                data.setTransitionType(TransitionTypes.getRandomTimelordType());
                initSkin(data);
                genName();
                populateDefaultEquipmentSlots(level.getCurrentDifficultyAt(blockPosition()));
                getEntityData().set(HAS_SETUP, true);
            });
        }
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean getYesSound) {
        SoundScheme personality = getPersonality();
        if (!getYesSound) {
            setUnhappyCounter(40);
        }
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

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (ModList.get().isLoaded("weeping_angels") && !level.isClientSide) {
            EntityType<?> weepingAngel = ForgeRegistries.ENTITIES.getValue(new ResourceLocation("weeping_angels", "weeping_angel"));
            if (weepingAngel != null) {
                if (level.random.nextInt(100) < 10) {
                    Entity entity = weepingAngel.create(level);
                    entity.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
                    level.addFreshEntity(entity);
                }
            }
        }
    }

    public void genName() {
        if (RegenUtil.USERNAMES.length <= 0) {
            RegenUtil.setupNames();
        }
        setCustomName(new TranslatableComponent(RegenUtil.USERNAMES[random.nextInt(RegenUtil.USERNAMES.length - 1)]));
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!level.isClientSide) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), new RemoveTimelordSkinMessage(this));
        }
    }


    /*Setup initial skins for the timelords*/
    public void initSkin(IRegen data) {
        level.getServer().submit(() -> {
            File file = CommonSkin.chooseRandomSkin(level.random, !male(), true);
            if (file != null) {
                data.setSkin(RegenUtil.fileToBytes(file));
            }
            data.setAlexSkin(true);
            data.syncToClients(null);
        });
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

        if (!getEntityData().get(HAS_SETUP)) {
            setup();
        }

        super.tick();

        RegenCap.get(this).ifPresent((data) -> {
            if (!level.isClientSide) {

                if (!data.isSkinValidForUse()) {
                    initSkin(data);
                    data.syncToClients(null);
                }

                if (tickCount < 20) {
                    data.syncToClients(null);
                }

                if (data.regenState().isGraceful() && data.glowing())

                    if (data.regenState() == RegenStates.REGENERATING) {
                        if (data.updateTicks() == 10) {
                            if (getPersonality().getScreamSound() != null) {
                                playSound(getPersonality().getScreamSound(), 1, 1);
                            }
                        }
                        if (data.updateTicks() == 100) {
                            setMale(random.nextBoolean());
                            setPersonality(RSoundSchemes.getRandom(male()).identify());
                            initSkin(data);
                        }
                        setNoAi(true);
                        setInvulnerable(true);
                        return;
                    }
                setNoAi(false);
                setInvulnerable(false);
            }
        });
    }


    @Override
    public float getVoicePitch() {
        return 1;
    }

    @Override
    public void kill() {
        if (!level.isClientSide) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), new RemoveTimelordSkinMessage(this));
        }
        super.kill();
    }

    public SoundScheme getPersonality() {
        return RSoundSchemes.get(new ResourceLocation(getEntityData().get(PERSONALITY)));
    }

    public void setPersonality(ResourceLocation per) {
        getEntityData().set(PERSONALITY, per.toString());
    }

    //Exists for easier NBT
    public void setPersonality(String per) {
        getEntityData().set(PERSONALITY, per);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("timelord_type", getTimelordType().name());
        compound.putBoolean("is_male", male());
        compound.putBoolean("setup", getEntityData().get(HAS_SETUP));
        compound.putString("personality", getPersonality().identify().toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("timelord_type")) {
            setTimelordType(TimelordType.valueOf(compound.getString("timelord_type")));
        }

        if (compound.contains("is_male")) {
            setMale(compound.getBoolean("is_male"));
        }

        if (compound.contains("personality")) {
            setPersonality(compound.getString("personality"));
        }

        if (compound.contains("setup")) {
            getEntityData().set(HAS_SETUP, compound.getBoolean("setup"));
        }
    }

    public boolean male() {
        return getEntityData().get(IS_MALE);
    }

    public void setMale(boolean male) {
        getEntityData().set(IS_MALE, male);
    }

    @Override
    protected void updateTrades() {
        if (getTimelordType() == TimelordType.COUNCIL) {
            MerchantOffers merchantoffers = this.getOffers();

            for (int i = random.nextInt(7); i > 0; i--) {
                AbstractTrait trait = RegenTraitRegistry.getRandomTrait(random, false);
                ItemStack item = new ItemStack(RItems.ELIXIR.get());
                Item[] currency = RegenUtil.TIMELORD_CURRENCY.getValues().toArray(new Item[0]);
                ElixirItem.setTrait(item, trait);
                TimelordTrade[] trades = new TimelordTrade[]{new TimelordEntity.TimelordTrade(new ItemStack(currency[random.nextInt(currency.length)], Mth.clamp(random.nextInt(10), 6, 20)), item, random.nextInt(7), 5)};
                this.addOffersFromItemListings(merchantoffers, trades, 5);
            }

            TimelordTrade[] tradetrades = new TimelordTrade[]{
                    new TimelordEntity.TimelordTrade(new ItemStack(Items.DIAMOND, 3), new ItemStack(RItems.ZINC.get(), 15), new ItemStack(RItems.RIFLE.get()), random.nextInt(7), 5),
                    new TimelordEntity.TimelordTrade(new ItemStack(Items.NETHERITE_INGOT, 4), new ItemStack(RItems.ZINC.get(), 15), new ItemStack(RItems.PISTOL.get()), random.nextInt(7), 5)
            };
            this.addOffersFromItemListings(merchantoffers, tradetrades, 5);
            super.updateTrades();
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        if (getTimelordType() == TimelordType.GUARD) {
            Item stack = random.nextBoolean() ? RItems.RIFLE.get() : RItems.PISTOL.get();
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(stack));
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return getPersonality().getHurtSound();
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        boolean isPistol = getItemBySlot(EquipmentSlot.MAINHAND).getItem() == RItems.PISTOL.get();

        LaserProjectile laserProjectile = new LaserProjectile(REntities.LASER.get(), this, level);
        laserProjectile.setDamage(isPistol ? 4 : 10);
        laserProjectile.setDamageSource(isPistol ? RegenSources.REGEN_DMG_STASER : RegenSources.REGEN_DMG_RIFLE);
        double d0 = target.getEyeY() - (double) 1.1F;
        double d1 = target.getX() - this.getX();
        double d2 = d0 - laserProjectile.getY();
        double d3 = target.getZ() - this.getZ();
        float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.2F;
        laserProjectile.shoot(d1, d2 + (double) f, d3, 1.6F, 0);
        this.playSound(isPistol ? RSounds.STASER.get() : RSounds.RIFLE.get(), 0.3F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(laserProjectile);
    }

    public boolean getAiming() {
        return getEntityData().get(AIMING);
    }

    public void setAiming(boolean isAiming) {
        getEntityData().set(AIMING, isAiming);
    }

    public float getAimingTicks() {
        return getEntityData().get(AIMING_TICKS);
    }

    public void setAimingTicks(float isAiming) {
        getEntityData().set(AIMING_TICKS, isAiming);
    }


    @Override
    public InteractionResult mobInteract(Player p_230254_1_, InteractionHand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        if (itemstack.getItem() != RItems.SPAWN_ITEM.get() && this.isAlive() && !this.isTrading() && !this.isBaby()) {
            if (this.getOffers().isEmpty()) {
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            } else {
                if (!this.level.isClientSide) {
                    this.setTradingPlayer(p_230254_1_);
                    if (p_230254_1_ instanceof ServerPlayer) {
                        ServerPlayer playerEntity = (ServerPlayer) p_230254_1_;
                        TriggerManager.TIMELORD_TRADE.trigger(playerEntity);
                    }
                    this.openTradingScreen(p_230254_1_, this.getDisplayName(), 1);
                }
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        } else {
            return super.mobInteract(p_230254_1_, p_230254_2_);
        }
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        switch (getTimelordType()) {
            case GUARD:
                ItemStack guardStack = new ItemStack(RItems.SPAWN_ITEM.get());
                SpawnItem.setType(guardStack, SpawnItem.Timelord.GUARD);
                return guardStack;
            case COUNCIL:
                ItemStack councilStack = new ItemStack(RItems.SPAWN_ITEM.get());
                SpawnItem.setType(councilStack, male() ? SpawnItem.Timelord.MALE_COUNCIL : SpawnItem.Timelord.FEMALE_COUNCIL);
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

    public static class TimelordTrade implements VillagerTrades.ItemListing {

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