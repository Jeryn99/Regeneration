package me.swirtzly.regeneration.common.entity;

import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityItemOverride extends Entity {

	private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(EntityItemOverride.class, DataSerializers.ITEM_STACK);
	private static final DataParameter<Float> HEIGHT = EntityDataManager.createKey(EntityItemOverride.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> WIDTH = EntityDataManager.createKey(EntityItemOverride.class, DataSerializers.FLOAT);

	public EntityItemOverride(World worldIn, double x, double y, double z, ItemStack stack) {
		this(worldIn);
		this.setPosition(x, y, z);
		this.setItem(stack);
		this.rotationYaw = (float) (Math.random() * 360.0D);
	}

	public EntityItemOverride(World worldIn, double x, double y, double z, ItemStack stack, float height, float width) {
		this(worldIn);
		this.setEntitySize(height, width);
		this.setPosition(x, y, z);
		this.setItem(stack);
		this.rotationYaw = (float) (Math.random() * 360.0D);
	}

	public EntityItemOverride(World worldIn) {
		super(RegenObjects.EntityEntries.ITEM_OVERRIDE_ENTITY_TYPE, worldIn);
		this.setEntitySize(getWidth(), getHeight());
	}


	public static void givePlayerItemStack(PlayerEntity player, ItemStack stack) {
		if (player.getHeldItemMainhand().isEmpty())
			player.setItemStackToSlot(EquipmentSlotType.MAINHAND, stack);
		else if (!player.inventory.addItemStackToInventory(stack)) {
			player.dropItem(stack, true);
		}
	}

	public void setEntitySize(float height, float width) {
		this.setHeight(height);
		this.setWidth(width);
		this.setEntitySize(width, height);
	}

	@Override
	protected void registerData() {
		this.getDataManager().register(ITEM, ItemStack.EMPTY);
		this.getDataManager().register(HEIGHT, 0.25F);
		this.getDataManager().register(WIDTH, 0.25F);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readAdditional(CompoundNBT compound) {
		CompoundNBT itemCompound = (CompoundNBT) compound.get("Item");
		this.setItem(ItemStack.read(itemCompound));

		if (this.getItem().isEmpty())
			this.onKillCommand();
		this.setHeight(compound.getFloat("Height"));
		this.setWidth(compound.getFloat("Width"));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeAdditional(CompoundNBT compound) {
		if (!this.getItem().isEmpty())
			compound.put("Item", this.getItem().write(new CompoundNBT()));

		compound.putFloat("Height", getHeight());
		compound.putFloat("Width", getWidth());
	}

	public ItemStack getItem() {
		return this.getDataManager().get(ITEM);
	}

	public void setItem(ItemStack stack) {
		this.getDataManager().set(ITEM, stack);
	}

	public float getHeight() {
		return this.getDataManager().get(HEIGHT);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return null;
	}

	public void setHeight(float height) {
		this.getDataManager().set(HEIGHT, height);
	}

	public float getWidth() {
		return this.getDataManager().get(WIDTH);
	}

	public void setWidth(float width) {
		this.getDataManager().set(WIDTH, width);
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	/**
	 * Will deal the specified amount of fire damage to the entity if the entity isn't immune to fire damage.
	 */
	@Override
	protected void dealFireDamage(int amount) {
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return false;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	/**
	 * Applies the given player interaction to this Entity.
	 */
	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vec3d vec, Hand hand) {
		givePlayerItemStack(player, this.getItem());
		this.remove();
		return ActionResultType.SUCCESS;
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void tick() {
		super.tick();
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		double d0 = this.motionX;
		double d1 = this.motionY;
		double d2 = this.motionZ;

		ItemStack itemStack = getItem();

		if (itemStack.getItem() instanceof IEntityOverride) {
			IEntityOverride iEntityOverride = (IEntityOverride) itemStack.getItem();
			if (iEntityOverride.shouldDie(itemStack)) {
				remove();
			}
			iEntityOverride.update(this);
		}

		this.setEntitySize(getWidth(), getHeight());

		if (!this.hasNoGravity()) {
			this.motionY -= 0.03999999910593033D;
		}

		if (this.world.isRemote) {
			this.noClip = false;
		} else {
			this.noClip = this.pushOutOfBlocks(this.posX, (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.posZ);
		}

		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY || (int) this.prevPosZ != (int) this.posZ;

		if (flag || this.ticksExisted % 25 == 0) {
			if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
				this.motionY = 0.20000000298023224D;
				this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
				this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
				this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
			}
		}

		float f = 0.98F;

		if (this.onGround) {
			BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
			BlockState underState = this.world.getBlockState(underPos);
			f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.98F;
		}

		this.motionX *= f;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= f;

		if (this.onGround) {
			this.motionY *= -0.5D;
		}

		this.handleWaterMovement();

		if (!this.world.isRemote) {
			double d3 = this.motionX - d0;
			double d4 = this.motionY - d1;
			double d5 = this.motionZ - d2;
			double d6 = d3 * d3 + d4 * d4 + d5 * d5;

			if (d6 > 0.01D) {
				this.isAirBorne = true;
			}
		}
	}

	public static interface IEntityOverride {

		void update(EntityItemOverride itemOverride);

		boolean shouldDie(ItemStack stack);
	}
}