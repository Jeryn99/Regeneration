package me.swirtzly.regeneration.common.entity;

import me.swirtzly.regeneration.common.item.ItemLindos;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.ClientUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLindos extends FlyingEntity {
	
	private static final DataParameter<Integer> AMOUNT = EntityDataManager.createKey(EntityLindos.class, DataSerializers.VARINT);
	
	public EntityLindos(EntityType type, World world){
		this(world);
	}

	public EntityLindos(World worldIn) {
		super(RegenObjects.EntityEntries.ITEM_LINDOS_TYPE, worldIn);
		//setEntitySize(0.5F, 0.5F);
		this.moveController = new FlyingMovementController(this);
		noClip = true;
	}


	@Override
	protected void registerData() {
		super.registerData();
		getDataManager().register(AMOUNT, rand.nextInt(100));
	}

	
	public int getAmount() {
		return getDataManager().get(AMOUNT);
	}
	
	public void setAmount(int amount) {
		getDataManager().set(AMOUNT, amount);
	}
	
	@Override
	protected PathNavigator createNavigator(World worldIn) {
		FlyingPathNavigator pathnavigateflying = new FlyingPathNavigator(this, worldIn);
		pathnavigateflying.setCanOpenDoors(false);
		pathnavigateflying.setCanSwim(true);
		pathnavigateflying.setCanEnterDoors(true);
		return pathnavigateflying;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		setNoGravity(true);
		
		if (world.isRemote && ticksExisted == 2) {
			ClientUtil.playSound(this, RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.AMBIENT, true, () -> !isAlive(), 1.0F);
		}
		
		if (ticksExisted < 60) {
			getMotion().add(0,0.3, 0);
		}
		
		if (ticksExisted % 100 == 0) {
			jump();
		}
		
		if (ticksExisted % 2200 == 0) {
			remove();
		}
	}
	
	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand) {
		
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			if (stack.getItem() == RegenObjects.Items.LINDOS_VIAL) {
				ItemLindos.setAmount(stack, ItemLindos.getAmount(stack) + getAmount());
				remove();
			}
		}
		
		return super.processInteract(player, hand);
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {
	}
	
	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		super.damageEntity(damageSrc, damageAmount);
	}


	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		getAttributes().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0D);
		this.getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4000000059604645D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
	}


	@Override
	public IPacket<?> createSpawnPacket() {
		return super.createSpawnPacket();
	}
}
