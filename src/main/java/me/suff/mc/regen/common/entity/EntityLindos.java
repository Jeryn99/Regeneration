package me.suff.mc.regen.common.entity;

import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.ClientUtil;
import me.suff.mc.regen.common.item.ItemLindos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLindos extends EntityFlying {

    private static final DataParameter<Integer> AMOUNT = EntityDataManager.createKey(EntityLindos.class, DataSerializers.VARINT);

    public EntityLindos(World worldIn) {
        super(worldIn);
        setSize(0.5F, 0.5F);
        this.moveHelper = new EntityFlyHelper(this);
        noClip = true;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4000000059604645D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
    }

    @Override
    protected PathNavigate createNavigator(World worldIn) {
        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanOpenDoors(false);
        pathnavigateflying.setCanFloat(true);
        pathnavigateflying.setCanEnterDoors(true);
        return pathnavigateflying;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(AMOUNT, RegenConfig.postRegen.minimumLindos + RegenConfig.postRegen.maximumLindos > RegenConfig.postRegen.minimumLindos ? rand.nextInt(RegenConfig.postRegen.maximumLindos - RegenConfig.postRegen.minimumLindos) : 0);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        setNoGravity(true);

        if (world.isRemote && ticksExisted == 2) {
            ClientUtil.playSound(this, RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.AMBIENT, true, () -> isDead, 0.3F);
        }

        if (ticksExisted < 60) {
            motionX *= 0.3D;
        }

        if (ticksExisted % 100 == 0) {
            jump();
        }

        if (ticksExisted % 2200 == 0) {
            setDead();
        }
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {

        if (!world.isRemote) {
            ItemStack stack = player.getHeldItem(hand);
            if (stack.getItem() == RegenObjects.Items.LINDOS_VIAL) {
                ItemLindos.setAmount(stack, ItemLindos.getAmount(stack) + getAmount());
                setDead();
            }
        }

        return super.processInteract(player, hand);
    }

    public int getAmount() {
        return getDataManager().get(AMOUNT);
    }

    public void setAmount(int amount) {
        getDataManager().set(AMOUNT, amount);
    }

}
