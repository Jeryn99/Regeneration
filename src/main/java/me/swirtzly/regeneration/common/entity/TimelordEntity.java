package me.swirtzly.regeneration.common.entity;

import me.swirtzly.regeneration.common.advancements.TriggerManager;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.ai.TimelordMelee;
import me.swirtzly.regeneration.common.item.GunItem;
import me.swirtzly.regeneration.common.skin.HandleSkins;
import me.swirtzly.regeneration.common.trades.TimelordTrades;
import me.swirtzly.regeneration.common.types.RegenTypes;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import me.swirtzly.regeneration.util.common.RegenUtil;
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
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Swirtzly
 * on 03/05/2020 @ 18:50
 */
public class TimelordEntity extends AbstractVillagerEntity implements IRangedAttackMob {

    private static final DataParameter<String> TYPE = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.STRING);
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> VILLAGER = EntityDataManager.createKey(TimelordEntity.class, DataSerializers.BOOLEAN);

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
    protected void registerData() {
        super.registerData();
        getDataManager().register(VILLAGER, false);
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

    public void setSwingingArms(boolean swingingArms) {
        this.getDataManager().set(SWINGING_ARMS, swingingArms);
    }

    public boolean isSwingingArms() {
        return this.getDataManager().get(SWINGING_ARMS);
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
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(TimelordEntity.class));
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
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {

        if (!worldIn.isRemote()) {

            if(getTimelordType() == TimelordType.GUARD){
                setHeldItem(Hand.MAIN_HAND, new ItemStack(rand.nextBoolean() ? RegenObjects.Items.PISTOL.get() : RegenObjects.Items.RIFLE.get()));
            }

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
                data.setType(rand.nextBoolean() ? RegenTypes.FIERY : RegenTypes.HARTNELL);
                initSkin(data);
            });
        }
        setCustomName(new StringTextComponent(RegenUtil.TIMELORD_NAMES[rand.nextInt(RegenUtil.TIMELORD_NAMES.length)]));

        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    /*Setup initial skins for the timelords*/
    public void initSkin(IRegen data) {
        long current = System.currentTimeMillis();
        URLConnection openConnection = null;
        try {
            openConnection = new URL(HandleSkins.SKINS.get(world.rand.nextInt(HandleSkins.SKINS.size() - 1))).openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            InputStream is = openConnection.getInputStream();
            File file = new File("./temp/" + current + ".png");
            FileUtils.copyInputStreamToFile(is, file);
            data.setEncodedSkin(HandleSkins.imageToPixelData(file));
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.getHeldItemMainhand().getItem() instanceof GunItem) {
            GunItem gunItem = (GunItem) getHeldItemMainhand().getItem();
            LaserEntity laserEntity = new LaserEntity(RegenObjects.EntityEntries.LASER.get(), this, this.world);
            laserEntity.setColor(new Vec3d(1, 0, 0));
            laserEntity.setDamage(gunItem.getDamage());
            double d0 = target.posX - this.posX;
            double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - laserEntity.posY;
            double d2 = target.posZ - this.posZ;
            double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
            laserEntity.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));
            this.world.playSound(null, this.posX, this.posY, this.posZ, this.getHeldItemMainhand().getItem() == RegenObjects.Items.PISTOL.get() ? RegenObjects.Sounds.STASER.get() : RegenObjects.Sounds.RIFLE.get(), SoundCategory.NEUTRAL, 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));
            this.world.addEntity(laserEntity);
        }
    }

    public enum TimelordType{
        COUNCIL("timelord"), GUARD("guards");

        private final String name;

        TimelordType(String guard) {
            this.name = guard;
        }

        public String getName() {
            return name;
        }
    }

    public void setTimelordType(TimelordType type){
        getDataManager().set(TYPE, type.name());
    }

    public TimelordType getTimelordType(){
        String type = getDataManager().get(TYPE);
        for (TimelordType value : TimelordType.values()) {
            if(value.name().equals(type)){
                return value;
            }
        }
        return TimelordType.GUARD;
    }

    @Override
    public void tick() {
        super.tick();

        RegenCap.get(this).ifPresent((data) -> {
            if (!world.isRemote) {
                data.synchronise();
                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {

                    if (data.getAnimationTicks() == 100) {
                        if (false) { //Ignore this, I need to re-implement something without breaking things
                            setVillager(true);
                        } else {
                            try {
                                setVillager(false);
                                long current = System.currentTimeMillis();
                                URLConnection openConnection = new URL(getRandomSkinURL()).openConnection();
                                openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                                InputStream is = openConnection.getInputStream();
                                File file = new File("./temp/" + current + ".png");
                                FileUtils.copyInputStreamToFile(is, file);
                                data.setEncodedSkin(HandleSkins.imageToPixelData(file));
                                file.delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
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

    private String getRandomSkinURL() {
        int skinSize = HandleSkins.SKINS.size() - 1;
        int randomPos = world.rand.nextInt(skinSize);
        if (randomPos > skinSize || randomPos < 0 /*why would this ever happen though*/) {
            /*fallback url, I hope this never happens. Sometimes peoples skins won't download when they play offline
            So I need to have something to return else game gets upset :(*/
            return "https://raw.githubusercontent.com/Swirtzly/Regeneration/skins/Skinpacks/ms_fallback.png";
        }
        return HandleSkins.SKINS.get(randomPos);
    }

    public Boolean isVillagerModel() {
        return getDataManager().get(VILLAGER);
    }

    public void setVillager(boolean villager) {
        getDataManager().set(VILLAGER, villager);
    }

    @Nullable
    @Override
    public AgeableEntity createChild(AgeableEntity ageable) {
        return null;
    }

    @Override
    protected void func_213713_b(MerchantOffer merchantOffer) {
        if (merchantOffer.func_222221_q()) {
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
        if (getTimelordType() == TimelordType.COUNCIL) {

            if (!world.isRemote) {
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
        return super.processInteract(player, hand);
    }

    @Override
    public void onKillCommand() {
        remove();
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("villager", isVillagerModel());
        compound.putString("timelord_type", getTimelordType().name());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setVillager(compound.getBoolean("villager"));
        if (compound.contains("timelord_type")) {
            setTimelordType(TimelordType.valueOf(compound.getString("timelord_type")));
        }
    }

    @Override
    protected void populateTradeData() {
        if (getTimelordType() == TimelordType.COUNCIL) {
            VillagerTrades.ITrade[] trades = TimelordTrades.genTrades();
            if (trades != null) {
                MerchantOffers merchantoffers = this.getOffers();
                this.addTrades(merchantoffers, trades, 5);
            }
        }
    }
}
