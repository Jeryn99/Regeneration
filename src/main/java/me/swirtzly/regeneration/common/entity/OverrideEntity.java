package me.swirtzly.regeneration.common.entity;

import me.swirtzly.regeneration.common.item.SolidItem;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class OverrideEntity extends Entity {

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.defineId(OverrideEntity.class, DataSerializers.ITEM_STACK);

    private UUID thrower;
    private UUID owner;
    private int health = 5;


    public OverrideEntity(EntityType<? extends OverrideEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public OverrideEntity(World world) {
        this(RegenObjects.EntityEntries.ITEM_OVERRIDE_ENTITY_TYPE.get(), world);
    }

    public OverrideEntity(World worldIn, double x, double y, double z) {
        this(RegenObjects.EntityEntries.ITEM_OVERRIDE_ENTITY_TYPE.get(), worldIn);
        this.setPos(x, y, z);
        this.yRot = this.random.nextFloat() * 360.0F;
        this.setDeltaMovement(this.random.nextDouble() * 0.2D - 0.1D, 0.2D, this.random.nextDouble() * 0.2D - 0.1D);
    }

    public OverrideEntity(World worldIn, double x, double y, double z, ItemStack stack) {
        this(worldIn, x, y, z);
        this.setItem(stack);
    }

    @Override
    protected boolean makeStepSound() {
        return false;
    }

    @Override
    public void defineSynchedData() {
        this.getEntityData().define(ITEM, ItemStack.EMPTY);
    }

    @Override
    public ITextComponent getName() {
        ITextComponent itextcomponent = this.getCustomName();
        return (itextcomponent != null ? itextcomponent : new TranslationTextComponent(this.getItem().getDescriptionId()));
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        Item item = this.getItem().getItem();
        if (item instanceof SolidItem) {
            EntitySize size = ((SolidItem) item).getEntitySize(this, this.getItem());
            return size != null ? size : super.getDimensions(poseIn);
        }
        return super.getDimensions(poseIn);
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vec3d vec, Hand hand) {
        if (player.getItemInHand(hand).isEmpty() && this.isAlive()) {
            player.setItemInHand(hand, this.getItem());
            this.remove();
            return ActionResultType.SUCCESS;
        }
        return super.interactAt(player, vec, hand);
    }

    @Override
    public void tick() {

        CompoundNBT stackTag = getItem().getOrCreateTag();

        if (!getItem().isEmpty() && !stackTag.contains("live")) {
            this.remove();
            return;
        }

        if (getItem().getItem() instanceof SolidItem && ((SolidItem) getItem().getItem()).onSolidEntityItemUpdate(this))
            return;
        if (this.getItem().isEmpty()) {
            this.remove();
        } else {
            super.tick();

            this.xo = this.x;
            this.yo = this.y;
            this.zo = this.z;
            Vec3d vec3d = this.getDeltaMovement();
            if (this.isUnderLiquid(FluidTags.WATER)) {
                this.applyFloatMotion();
            } else if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }

            if (this.level.isClientSide) {
                this.noPhysics = false;
            } else {
                this.noPhysics = !this.level.noCollision(this);
                if (this.noPhysics) {
                    this.checkInBlock(this.x, (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.z);
                }
            }

            if (!this.onGround || getHorizontalDistanceSqr(this.getDeltaMovement()) > (double) 1.0E-5F || (this.tickCount + this.getId()) % 4 == 0) {
                this.move(MoverType.SELF, this.getDeltaMovement());
                float f = 0.98F;
                if (this.onGround) {
                    BlockPos pos = new BlockPos(this.x, this.getBoundingBox().minY - 1.0D, this.z);
                    f = this.level.getBlockState(pos).getSlipperiness(this.level, pos, this) * 0.98F;
                }

                this.setDeltaMovement(this.getDeltaMovement().multiply(f, 0.98D, f));
                if (this.onGround) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.5D, 1.0D));
                }
            }

            boolean flag = MathHelper.floor(this.xo) != MathHelper.floor(this.x) || MathHelper.floor(this.yo) != MathHelper.floor(this.y) || MathHelper.floor(this.zo) != MathHelper.floor(this.z);
            int i = flag ? 2 : 40;
            if (this.tickCount % i == 0) {
                if (this.level.getFluidState(new BlockPos(this)).is(FluidTags.LAVA)) {
                    this.setDeltaMovement((this.random.nextFloat() - this.random.nextFloat()) * 0.2F, 0.2F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                    this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
                }
            }

            this.hasImpulse |= this.updateInWaterState();
            if (!this.level.isClientSide) {
                double d0 = this.getDeltaMovement().subtract(vec3d).lengthSqr();
                if (d0 > 0.01D) {
                    this.hasImpulse = true;
                }
            }

            if (this.getItem().isEmpty()) {
                this.remove();
            }

        }
    }

    private void applyFloatMotion() {
        Vec3d vec3d = this.getDeltaMovement();
        this.setDeltaMovement(vec3d.x * (double) 0.99F, vec3d.y + (double) (vec3d.y < (double) 0.06F ? 5.0E-4F : 0.0F), vec3d.z * (double) 0.99F);
    }

    @Override
    protected void burn(int amount) {
        this.hurt(DamageSource.IN_FIRE, (float) amount);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level.isClientSide || this.isAlive()) return false;
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.getItem().isEmpty() && this.getItem().getItem() == Items.NETHER_STAR && source.isExplosion()) {
            return false;
        } else {
            this.markHurt();
            this.health = (int) ((float) this.health - amount);
            if (this.health <= 0) {
                this.remove();
            }

            return false;
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        this.health = compound.getShort("Health");

        if (compound.contains("Owner", Constants.NBT.TAG_COMPOUND))
            this.owner = NBTUtil.loadUUIDTag(compound.getCompound("Owner"));

        if (compound.contains("Thrower", Constants.NBT.TAG_COMPOUND))
            this.thrower = NBTUtil.loadUUIDTag(compound.getCompound("Thrower"));

        CompoundNBT compoundnbt = compound.getCompound("Item");
        this.setItem(ItemStack.of(compoundnbt));
        if (this.getItem().isEmpty())
            this.remove();
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putShort("Health", (short) this.health);

        if (this.getThrowerId() != null)
            compound.put("Thrower", NBTUtil.createUUIDTag(this.getThrowerId()));

        if (this.getOwnerId() != null)
            compound.put("Owner", NBTUtil.createUUIDTag(this.getOwnerId()));

        if (!this.getItem().isEmpty())
            compound.put("Item", this.getItem().save(new CompoundNBT()));
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public ItemStack getItem() {
        return this.getEntityData().get(ITEM);
    }

    public void setItem(ItemStack stack) {
        this.getEntityData().set(ITEM, stack);
    }

    @Nullable
    public UUID getOwnerId() {
        return this.owner;
    }

    public void setOwnerId(@Nullable UUID ownerUUID) {
        this.owner = ownerUUID;
    }

    @Nullable
    public UUID getThrowerId() {
        return this.thrower;
    }

    public void setThrowerId(@Nullable UUID throwerUUID) {
        this.thrower = throwerUUID;
    }
}
