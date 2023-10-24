package mc.craig.software.regen.common.entities;

import com.google.common.collect.Sets;
import mc.craig.software.regen.client.skin.SkinRetriever;
import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.entities.ai.TimelordAttackGoal;
import mc.craig.software.regen.common.item.ChaliceItem;
import mc.craig.software.regen.common.item.SpawnItem;
import mc.craig.software.regen.common.objects.*;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.network.messages.RemoveTimelordSkinMessage;
import mc.craig.software.regen.util.Platform;
import mc.craig.software.regen.util.constants.RConstants;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Set;

/**
 * Created by Suff
 * on 03/05/2020 @ 18:50
 */
public class Timelord extends PathfinderMob implements RangedAttackMob, Merchant {

    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(Timelord.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> AIMING = SynchedEntityData.defineId(Timelord.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_MALE = SynchedEntityData.defineId(Timelord.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_SETUP = SynchedEntityData.defineId(Timelord.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> AIMING_TICKS = SynchedEntityData.defineId(Timelord.class, EntityDataSerializers.FLOAT);
    protected final WaterBoundPathNavigation waterNavigator;
    protected final GroundPathNavigation groundNavigator;
    @Nullable
    protected MerchantOffers offers;
    @Nullable
    private Player tradingPlayer;

    public Timelord(Level world) {
        this(REntities.TIMELORD.get(), world);
    }

    public Timelord(EntityType<Timelord> entityEntityType, Level world) {
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(TYPE, random.nextBoolean() ? TimelordType.COUNCIL.name() : TimelordType.GUARD.name());
        getEntityData().define(AIMING, false);
        getEntityData().define(AIMING_TICKS, 0.0F);
        getEntityData().define(IS_MALE, random.nextBoolean());
        getEntityData().define(HAS_SETUP, false);
        setup();
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_35282_, DifficultyInstance p_35283_, MobSpawnType p_35284_, @org.jetbrains.annotations.Nullable SpawnGroupData p_35285_, @org.jetbrains.annotations.Nullable CompoundTag p_35286_) {
        populateDefaultEquipmentSlots(random, level().getCurrentDifficultyAt(blockPosition()));
        return super.finalizeSpawn(p_35282_, p_35283_, p_35284_, p_35285_, p_35286_);
    }

    @Override
    public void updateSwimming() {
        if (!this.level().isClientSide) {
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
            for (Holder<Item> holder : BuiltInRegistries.ITEM.getTag(RegenUtil.TIMELORD_CURRENCY).get()) {
                this.goalSelector.addGoal(4, new TemptGoal(this, 1.0D, Ingredient.of(holder.value()), false));
            }

            this.goalSelector.addGoal(1, new PanicGoal(this, 0.5D));
            this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2F, true));
        }

        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(Timelord.class));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zombie.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Skeleton.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Cyberman.class, false));
    }

    public void setup() {
        if (!level().isClientSide()) {

            RegenerationData.get(this).ifPresent((data) -> {
                data.addRegens(level().getRandom().nextInt(12));
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
                populateDefaultEquipmentSlots(random, level().getCurrentDifficultyAt(blockPosition()));
                getEntityData().set(HAS_SETUP, true);
            });
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (Platform.isModLoaded("weeping_angels") && !level().isClientSide) {
            EntityType<?> weepingAngel = BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation("weeping_angels", "weeping_angel"));
            if (weepingAngel != null) {
                if (level().random.nextInt(100) < 10) {
                    Entity entity = weepingAngel.create(level());
                    entity.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
                    level().addFreshEntity(entity);
                }
            }
        }
    }

    public void genName() {
        if (RegenUtil.USERNAMES.length == 0) {
            RegenUtil.setupNames();
        }
        setCustomName(Component.literal(RegenUtil.USERNAMES[random.nextInt(RegenUtil.USERNAMES.length - 1)]));
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        super.die(cause);
        if (!level().isClientSide) {
            new RemoveTimelordSkinMessage(this).sendToAll();
        }
    }


    /*Setup initial skins for the timelords*/
    public void initSkin(IRegen data) {
        level().getServer().submit(() -> {
            File file = SkinRetriever.chooseRandomSkin(level().random, true, true);
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
        RegenerationData.get(this).ifPresent((data) -> {
            if (!level().isClientSide) {

                if (!data.isSkinValidForUse()) {
                    initSkin(data);
                    data.syncToClients(null);
                }

                if (tickCount < 20) {
                    data.syncToClients(null);
                }


                if (data.regenState() == RegenStates.REGENERATING) {
                    if (data.updateTicks() == (data.transitionType().getAnimationLength() / 2)) {
                        setMale(random.nextBoolean());
                        initSkin(data);
                        new RemoveTimelordSkinMessage(this).sendToAll();
                    }
                    setDeltaMovement(0, 0, 0);
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
        if (!level().isClientSide) {
            new RemoveTimelordSkinMessage(this).sendToAll();
        }
        remove(RemovalReason.KILLED);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("timelord_type", getTimelordType().name());
        compound.putBoolean("is_male", male());
        compound.putBoolean("setup", getEntityData().get(HAS_SETUP));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("timelord_type")) {
            setTimelordType(TimelordType.valueOf(compound.getString("timelord_type")));
        }

        if (compound.contains("is_male")) {
            setMale(compound.getBoolean("is_male"));
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

    protected void updateTrades() {
        if (getTimelordType() != TimelordType.COUNCIL) return;
            MerchantOffers merchantoffers = this.getOffers();

            for (int i = random.nextInt(7); i > 0; i--) {
                TraitRegistry.getRandomTrait().ifPresent(traitBase -> {
                    ItemStack chalice = new ItemStack(RItems.GAUNTLET.get());
                    ChaliceItem.setTrait(chalice, traitBase);
                    BuiltInRegistries.ITEM.getTag(RegenUtil.TIMELORD_CURRENCY).flatMap(potentialCurrency -> potentialCurrency.getRandomElement(random)).ifPresent(itemHolder -> {
                        TimelordTrade[] trades = new TimelordTrade[]{new TimelordTrade(new ItemStack(itemHolder.value(), Mth.clamp(random.nextInt(10), 6, 20)), chalice, random.nextInt(7), 5)};
                        this.addOffersFromItemListings(merchantoffers, trades, 5);
                    });
                });
            }

        TimelordTrade[] baseTrades = new TimelordTrade[]{
                new Timelord.TimelordTrade(new ItemStack(Items.DIAMOND, 3), new ItemStack(RItems.ZINC.get(), 15), new ItemStack(RItems.RIFLE.get()), random.nextInt(7), 5),
                new Timelord.TimelordTrade(new ItemStack(Items.NETHERITE_INGOT, 4), new ItemStack(RItems.ZINC.get(), 15), new ItemStack(RItems.PISTOL.get()), random.nextInt(7), 5)
        };
        this.addOffersFromItemListings(merchantoffers, baseTrades, 5);

    }


    protected void addOffersFromItemListings(MerchantOffers merchantOffers, VillagerTrades.ItemListing[] itemListings, int p_35280_) {
        Set<Integer> set = Sets.newHashSet();
        if (itemListings.length > p_35280_) {
            while (set.size() < p_35280_) {
                set.add(this.random.nextInt(itemListings.length));
            }
        } else {
            for (int i = 0; i < itemListings.length; ++i) {
                set.add(i);
            }
        }

        for (Integer integer : set) {
            VillagerTrades.ItemListing villagerTrades = itemListings[integer];
            MerchantOffer merchantoffer = villagerTrades.getOffer(this, this.random);
            if (merchantoffer != null) {
                merchantOffers.add(merchantoffer);
            }
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource random, @NotNull DifficultyInstance difficulty) {
        if (getTimelordType() == TimelordType.GUARD) {
            Item stack = random.nextBoolean() ? RItems.RIFLE.get() : RItems.PISTOL.get();

            setItemSlot(EquipmentSlot.HEAD, new ItemStack(RItems.GUARD_HELMET.get()));
            setItemSlot(EquipmentSlot.CHEST, new ItemStack(RItems.GUARD_CHEST.get()));
            setItemSlot(EquipmentSlot.LEGS, new ItemStack(RItems.GUARD_LEGS.get()));
            setItemSlot(EquipmentSlot.FEET, new ItemStack(RItems.GUARD_FEET.get()));

            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(stack));
        }
        if (getTimelordType() == TimelordType.COUNCIL) {

            boolean isMale = male();
            setItemSlot(EquipmentSlot.HEAD, new ItemStack(isMale ? RItems.M_ROBES_HEAD.get() : RItems.F_ROBES_HEAD.get()));
            setItemSlot(EquipmentSlot.CHEST, new ItemStack(isMale ? RItems.M_ROBES_CHEST.get() : RItems.F_ROBES_CHEST.get()));
            setItemSlot(EquipmentSlot.LEGS, new ItemStack(isMale ? RItems.M_ROBES_LEGS.get() : RItems.F_ROBES_LEGS.get()));
            setItemSlot(EquipmentSlot.FEET, new ItemStack(RItems.ROBES_FEET.get()));

            this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.ENCHANTED_BOOK));
        }
    }


    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        boolean isPistol = getItemBySlot(EquipmentSlot.MAINHAND).getItem() == RItems.PISTOL.get();

        Laser laser = new Laser(REntities.LASER.get(), this, level());
        laser.setDamage(isPistol ? 4 : 10);
        laser.setDamageSource(isPistol ? new DamageSource(RegenDamageTypes.getHolder(level(), RegenDamageTypes.REGEN_DMG_STASER)) : new DamageSource(RegenDamageTypes.getHolder(level(), RegenDamageTypes.REGEN_DMG_RIFLE)));
        double d0 = target.getEyeY() - (double) 1.1F;
        double d1 = target.getX() - this.getX();
        double d2 = d0 - laser.getY();
        double d3 = target.getZ() - this.getZ();
        float f = Mth.sqrt((float) (d1 * d1 + d3 * d3)) * 0.2F;
        laser.shoot(d1, d2 + (double) f, d3, 1.6F, 0);
        this.playSound(isPistol ? RSounds.STASER.get() : RSounds.RIFLE.get(), 0.3F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(laser);
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

    public boolean isTrading() {
        return this.tradingPlayer != null;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (itemstack.getItem() != RItems.SPAWN_ITEM.get() && this.isAlive() && !this.isTrading() && !this.isBaby()) {
            if (!this.getOffers().isEmpty()) {
                if (!this.level().isClientSide) {
                    this.setTradingPlayer(player);
                    if (player instanceof ServerPlayer playerEntity) {
                        TriggerManager.TIMELORD_TRADE.trigger(playerEntity);
                    }
                    this.openTradingScreen(player, this.getDisplayName(), 1);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            return super.mobInteract(player, interactionHand);
        }
    }

    @Override
    public ItemStack getPickResult() {
        switch (getTimelordType()) {
            case GUARD -> {
                ItemStack guardStack = new ItemStack(RItems.SPAWN_ITEM.get());
                SpawnItem.setType(guardStack, SpawnItem.Timelord.GUARD);
                return guardStack;
            }
            case COUNCIL -> {
                ItemStack councilStack = new ItemStack(RItems.SPAWN_ITEM.get());
                SpawnItem.setType(councilStack, male() ? SpawnItem.Timelord.MALE_COUNCIL : SpawnItem.Timelord.FEMALE_COUNCIL);
                return councilStack;
            }
        }
        return null;
    }

    @Override
    public Player getTradingPlayer() {
        return this.tradingPlayer;
    }

    @Override
    public void setTradingPlayer(@Nullable Player player) {
        this.tradingPlayer = player;
    }

    @Override
    public MerchantOffers getOffers() {
        if (this.offers == null) {
            this.offers = new MerchantOffers();
            this.updateTrades();
        }

        return this.offers;
    }

    @Override
    public void overrideOffers(MerchantOffers merchantOffers) {

    }

    @Override
    public void notifyTrade(MerchantOffer merchantOffer) {
        merchantOffer.increaseUses();
        if (merchantOffer.shouldRewardExp()) {
            int i = 3 + this.random.nextInt(4);
            this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5D, this.getZ(), i));
        }
    }

    @Override
    public void notifyTradeUpdated(ItemStack stack) {

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public void overrideXp(int xp) {

    }

    @Override
    public boolean showProgressBar() {
        return true;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return null;
    }

    @Override
    public boolean isClientSide() {
        return this.level().isClientSide;
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

        private final ItemStack coin2;
        private final ItemStack coin;
        private final ItemStack wares;

        private final int xp;
        private final int stock;

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
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            return new MerchantOffer(coin, coin2, wares, stock, xp, 0F);
        }
    }

}