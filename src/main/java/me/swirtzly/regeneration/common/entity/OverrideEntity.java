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

    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(OverrideEntity.class, DataSerializers.ITEMSTACK);

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
        this.setPosition(x, y, z);
        this.rotationYaw = this.rand.nextFloat() * 360.0F;
        this.setMotion(this.rand.nextDouble() * 0.2D - 0.1D, 0.2D, this.rand.nextDouble() * 0.2D - 0.1D);
    }

    public OverrideEntity(World worldIn, double x, double y, double z, ItemStack stack) {
        this(worldIn, x, y, z);
        this.setItem(stack);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void registerData() {
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    @Override
    public ITextComponent getName() {
        ITextComponent itextcomponent = this.getCustomName();
        return (itextcomponent != null ? itextcomponent : new TranslationTextComponent(this.getItem().getTranslationKey()));
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        Item item = this.getItem().getItem();
        if (item instanceof SolidItem) {
            EntitySize size = ((SolidItem) item).getEntitySize(this, this.getItem());
            return size != null ? size : super.getSize(poseIn);
        }
        return super.getSize(poseIn);
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
        if (player.getHeldItem(hand).isEmpty() && this.isAlive()) {
            player.setHeldItem(hand, this.getItem());
            this.remove();
            return ActionResultType.SUCCESS;
        }
        return super.applyPlayerInteraction(player, vec, hand);
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

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            Vec3d vec3d = this.getMotion();
            if (this.areEyesInFluid(FluidTags.WATER)) {
                this.applyFloatMotion();
            } else if (!this.hasNoGravity()) {
                this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
            }

            if (this.world.isRemote) {
                this.noClip = false;
            } else {
                this.noClip = !this.world.areCollisionShapesEmpty(this);
                if (this.noClip) {
                    this.pushOutOfBlocks(this.posX, (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.posZ);
                }
            }

            if (!this.onGround || func_213296_b(this.getMotion()) > (double) 1.0E-5F || (this.ticksExisted + this.getEntityId()) % 4 == 0) {
                this.move(MoverType.SELF, this.getMotion());
                float f = 0.98F;
                if (this.onGround) {
                    BlockPos pos = new BlockPos(this.posX, this.getBoundingBox().minY - 1.0D, this.posZ);
                    f = this.world.getBlockState(pos).getSlipperiness(this.world, pos, this) * 0.98F;
                }

                this.setMotion(this.getMotion().mul(f, 0.98D, f));
                if (this.onGround) {
                    this.setMotion(this.getMotion().mul(1.0D, -0.5D, 1.0D));
                }
            }

            boolean flag = MathHelper.floor(this.prevPosX) != MathHelper.floor(this.posX) || MathHelper.floor(this.prevPosY) != MathHelper.floor(this.posY) || MathHelper.floor(this.prevPosZ) != MathHelper.floor(this.posZ);
            int i = flag ? 2 : 40;
            if (this.ticksExisted % i == 0) {
                if (this.world.getFluidState(new BlockPos(this)).isTagged(FluidTags.LAVA)) {
                    this.setMotion((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F, 0.2F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
                }
            }

            this.isAirBorne |= this.handleWaterMovement();
            if (!this.world.isRemote) {
                double d0 = this.getMotion().subtract(vec3d).lengthSquared();
                if (d0 > 0.01D) {
                    this.isAirBorne = true;
                }
            }

            if (this.getItem().isEmpty()) {
                this.remove();
            }

        }
    }

    private void applyFloatMotion() {
        Vec3d vec3d = this.getMotion();
        this.setMotion(vec3d.x * (double) 0.99F, vec3d.y + (double) (vec3d.y < (double) 0.06F ? 5.0E-4F : 0.0F), vec3d.z * (double) 0.99F);
    }

    @Override
    protected void dealFireDamage(int amount) {
        this.attackEntityFrom(DamageSource.IN_FIRE, (float) amount);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.world.isRemote || this.isAlive()) return false;
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.getItem().isEmpty() && this.getItem().getItem() == Items.NETHER_STAR && source.isExplosion()) {
            return false;
        } else {
            this.markVelocityChanged();
            this.health = (int) ((float) this.health - amount);
            if (this.health <= 0) {
                this.remove();
            }

            return false;
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.health = compound.getShort("Health");

        if (compound.contains("Owner", Constants.NBT.TAG_COMPOUND))
            this.owner = NBTUtil.readUniqueId(compound.getCompound("Owner"));

        if (compound.contains("Thrower", Constants.NBT.TAG_COMPOUND))
            this.thrower = NBTUtil.readUniqueId(compound.getCompound("Thrower"));

        CompoundNBT compoundnbt = compound.getCompound("Item");
        this.setItem(ItemStack.read(compoundnbt));
        if (this.getItem().isEmpty())
            this.remove();
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("Health", (short) this.health);

        if (this.getThrowerId() != null)
            compound.put("Thrower", NBTUtil.writeUniqueId(this.getThrowerId()));

        if (this.getOwnerId() != null)
            compound.put("Owner", NBTUtil.writeUniqueId(this.getOwnerId()));

        if (!this.getItem().isEmpty())
            compound.put("Item", this.getItem().write(new CompoundNBT()));
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public ItemStack getItem() {
        return this.getDataManager().get(ITEM);
    }

    public void setItem(ItemStack stack) {
        this.getDataManager().set(ITEM, stack);
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
