package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.common.advancements.RegenTriggers;
import me.swirtzly.regeneration.common.block.BlockHandInJar;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.entity.EntityItemOverride;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLindos extends ItemOverrideBase {
	
	public ItemLindos() {
		setMaxStackSize(1);
		addPropertyOverride(new ResourceLocation("amount"), new IItemPropertyGetter() {
			@Override
			@SideOnly(Dist.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
				
				if (stack.getTagCompound() != null) {
					int amount = getAmount(stack);
					
					if (!hasWater(stack)) {
						return 0.0F;
					}
					
					if (hasWater(stack) && getAmount(stack) <= 0) {
						return 2F;
					}
					
					if (amount == 100) {
						return 1.0F;
					}
					
					if (amount >= 90) {
						return 0.2F;
					}
					
					if (amount >= 50) {
						return 0.5F;
					}
					
					if (amount >= 10) {
						return 0.1F;
					}
				}
				
				return 2F;
			}
		});
	}
	
	public static CompoundNBT getStackTag(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			stack.putCompound(new CompoundNBT());
			stack.getTagCompound().putInt("amount", 0);
		}
		return stack.getTagCompound();
	}
	
	public static int getAmount(ItemStack stack) {
		return getStackTag(stack).getInt("amount");
	}
	
	public static void setAmount(ItemStack stack, int amount) {
		getStackTag(stack).putInt("amount", MathHelper.clamp(amount, 0, 100));
	}
	
	public static boolean hasWater(ItemStack stack) {
		return getStackTag(stack).getBoolean("water");
	}
	
	public static void setWater(ItemStack stack, boolean water) {
		getStackTag(stack).putBoolean("water", water);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
		super.onCreated(stack, worldIn, playerIn);
		if (!playerIn.world.isRemote) {
			RegenTriggers.LINDOS_VIAL.trigger((ServerPlayerEntity) playerIn);
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		if (stack.getTagCompound() == null) {
			stack.putCompound(new CompoundNBT());
			stack.getTagCompound().putBoolean("live", true);
		} else {
			stack.getTagCompound().putBoolean("live", true);
		}
		
		if (!worldIn.isRemote) {
			//Entiies around
			worldIn.getEntitiesWithinAABB(PlayerEntity.class, entityIn.getEntityBoundingBox().expand(10, 10, 10)).forEach(player -> {
				IRegeneration data = CapabilityRegeneration.getForPlayer((PlayerEntity) entityIn);
				if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
					if (worldIn.rand.nextInt(100) > 50 && isSelected) {
						setAmount(stack, getAmount(stack) + 1);
					}
				}
			});
			
			//Player glowing
			if (entityIn instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entityIn;
				if (isSelected) {
					if (CapabilityRegeneration.getForPlayer(player).areHandsGlowing() && player.ticksExisted % 40 == 0) {
						setAmount(stack, getAmount(stack) + 2);
					}
				}
			}
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
	public ActionResultType onItemUse(PlayerEntity player, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			ItemStack itemStack = player.getHeldItem(hand);
			RayTraceResult raytraceresult = this.rayTrace(worldIn, player, true);
			
			if (raytraceresult == null || raytraceresult.getBlockPos() == null) {
				return ActionResultType.FAIL;
			}
			
			BlockPos blockPos = raytraceresult.getBlockPos();
			BlockState iblockstate = worldIn.getBlockState(blockPos);
			Material material = iblockstate.getMaterial();
			
			if (iblockstate.getBlock() instanceof BlockHandInJar && player.isSneaking()) {
				if (worldIn.getTileEntity(blockPos) instanceof TileEntityHandInJar) {
					TileEntityHandInJar jar = (TileEntityHandInJar) worldIn.getTileEntity(blockPos);
					setAmount(itemStack, getAmount(itemStack) + jar.getLindosAmont());
					jar.setLindosAmont(0);
				}
				return ActionResultType.SUCCESS;
			}
			
			if (material == Material.WATER) {
				worldIn.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
				player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
				
				if (!hasWater(itemStack)) {
					setWater(itemStack, true);
					player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
					PlayerUtil.sendMessage(player, new TranslationTextComponent("nbt.item.water_filled"), true);
				} else {
					PlayerUtil.sendMessage(player, new TranslationTextComponent("nbt.item.water_already_filled"), true);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
		ItemStack stack = player.getHeldItem(handIn);
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		if (!worldIn.isRemote) {
			
			//If the player is in POST or Regenerating, stop them from drinking it
			if (getAmount(stack) > 100) {
				if (cap.getState() == PlayerUtil.RegenState.POST || cap.getState() == PlayerUtil.RegenState.REGENERATING || player.isCreative()) {
					PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.cannot_use"), true);
					return ActionResult.newResult(ActionResultType.FAIL, player.getHeldItem(handIn));
				}
			}
			
			if (hasWater(stack)) {
				//If the stack has enough, basically kill them
				if (getAmount(stack) == 100) {
					if (cap.getRegenerationsLeft() < 1)
						cap.receiveRegenerations(1);
					
					player.attackEntityFrom(RegenObjects.REGEN_DMG_LINDOS, Integer.MAX_VALUE);
					setAmount(stack, 0);
					setWater(stack, false);
					return ActionResult.newResult(ActionResultType.PASS, player.getHeldItem(handIn));
				} else {
					PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.empty_vial"), true);
					return ActionResult.newResult(ActionResultType.FAIL, player.getHeldItem(handIn));
				}
			} else {
				PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.no_water"), true);
				return ActionResult.newResult(ActionResultType.FAIL, player.getHeldItem(handIn));
			}
		}
		return ActionResult.newResult(ActionResultType.FAIL, player.getHeldItem(handIn));
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslationTextComponent("nbt.item.lindos", getAmount(stack)).getUnformattedComponentText());
		tooltip.add(new TranslationTextComponent("nbt.item.water", hasWater(stack)).getUnformattedComponentText());
	}
	
	@Override
	public void update(EntityItemOverride itemOverride) {
		if (itemOverride.world.isRemote) return;
		ItemStack itemStack = itemOverride.getItem();
		if (itemStack.getItem() == this) {
			if (itemOverride.isInWater()) {
				if (itemStack.getTagCompound() != null) {
					setWater(itemStack, true);
				}
			}
		}
	}
	
	
}
