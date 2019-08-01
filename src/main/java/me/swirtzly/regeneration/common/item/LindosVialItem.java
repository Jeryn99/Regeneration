package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.OverrideEntity;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class LindosVialItem extends OverrideItem {

	public LindosVialItem() {
		super(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1));
		addPropertyOverride(new ResourceLocation("amount"), (stack, world, entityLivingBase) -> {
			if (stack.getTag() != null) {
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
		});
	}

	public static CompoundNBT getStackTag(ItemStack stack) {
		if (stack.getTag() == null) {
			stack.setTag(new CompoundNBT());
			stack.getTag().putInt("amount", 0);
		}
		return stack.getTag();
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
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

		if (stack.getTag() == null) {
			stack.setTag(new CompoundNBT());
			stack.getTag().putBoolean("live", true);
		} else {
			stack.getTag().putBoolean("live", true);
		}

		if (!worldIn.isRemote) {
			//Entiies around
			worldIn.getEntitiesWithinAABB(PlayerEntity.class, entityIn.getBoundingBox().expand(10, 10, 10)).forEach(player -> {
                RegenCap.get((PlayerEntity) entityIn).ifPresent((data) -> {
					if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
						if (worldIn.rand.nextInt(100) > 50 && isSelected) {
							setAmount(stack, getAmount(stack) + 1);
						}
					}
				});
			});

			//Player glowing
			if (entityIn instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) entityIn;
                RegenCap.get(player).ifPresent((data) -> {
					if (isSelected) {
						if (data.areHandsGlowing() && player.ticksExisted % 40 == 0) {
							setAmount(stack, getAmount(stack) + 2);
						}
					}
				});
			}
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}


	@Override
	public ActionResultType onItemUse(ItemUseContext useContext) {
		PlayerEntity player = useContext.getPlayer();

		if (!player.world.isRemote) {
			ItemStack itemStack = useContext.getItem();
			RayTraceResult raytraceresult = rayTrace(player.world, player, RayTraceContext.FluidMode.ANY);


			if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
				BlockRayTraceResult blockResult = (BlockRayTraceResult) raytraceresult;
				if (raytraceresult == null || blockResult.getPos() == null) {
					return ActionResultType.FAIL;
				}

				BlockPos blockPos = blockResult.getPos();
				BlockState iblockstate = player.world.getBlockState(blockPos);
				Material material = iblockstate.getMaterial();

				if (material == Material.WATER) {
					player.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
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
		}

		return super.onItemUse(useContext);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
		ItemStack stack = player.getHeldItem(handIn);
        IRegen cap = RegenCap.get(player).orElse(null);
		if (!worldIn.isRemote) {

			//If the player is in POST or Regenerating, stop them from drinking it
			if (cap.getState() == PlayerUtil.RegenState.POST || cap.getState() == PlayerUtil.RegenState.REGENERATING || player.isCreative()) {
				PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.cannot_use"), true);
				return ActionResult.newResult(ActionResultType.FAIL, player.getHeldItem(handIn));
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
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag idk) {
		super.addInformation(stack, world, tooltip, idk);
		tooltip.add(new TranslationTextComponent("nbt.item.lindos", getAmount(stack)));
		tooltip.add(new TranslationTextComponent("nbt.item.water", hasWater(stack)));
	}


	@Override
	public void update(OverrideEntity itemOverride) {
		if (itemOverride.world.isRemote) return;
		ItemStack itemStack = itemOverride.getItem();
		if (itemStack.getItem().getItem() == this) {
			if (itemOverride.isInWater()) {
				if (itemStack.getTag() != null) {
					setWater(itemStack, true);
				}
			}
		}
	}


}