package me.suff.regeneration.common.item;

import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.handlers.RegenObjects;
import me.suff.regeneration.util.PlayerUtil;
import me.suff.regeneration.util.RegenState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLindos extends ItemOverrideBase {
	
	public ItemLindos() {
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
	
	public static NBTTagCompound getStackTag(ItemStack stack) {
		if (stack.getTag() == null) {
			stack.setTag(new NBTTagCompound());
			stack.getTag().setInt("amount", 0);
		}
		return stack.getTag();
	}
	
	public static int getAmount(ItemStack stack) {
		return getStackTag(stack).getInt("amount");
	}
	
	public static void setAmount(ItemStack stack, int amount) {
		getStackTag(stack).setInt("amount", MathHelper.clamp(amount, 0, 100));
	}
	
	public static boolean hasWater(ItemStack stack) {
		return getStackTag(stack).getBoolean("water");
	}
	
	public static void setWater(ItemStack stack, boolean water) {
		getStackTag(stack).setBoolean("water", water);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		super.onCreated(stack, worldIn, playerIn);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		if (stack.getTag() == null) {
			stack.setTag(new NBTTagCompound());
			stack.getTag().setBoolean("live", true);
		} else {
			stack.getTag().setBoolean("live", true);
		}
		
		if (!worldIn.isRemote) {
			//Entiies around
			worldIn.getEntitiesWithinAABB(EntityPlayer.class, entityIn.getBoundingBox().expand(10, 10, 10)).forEach(player -> {
				CapabilityRegeneration.getForPlayer((EntityPlayer) entityIn).ifPresent((data) -> {
					if (data.getState() == RegenState.REGENERATING) {
						if (worldIn.rand.nextInt(100) > 50 && isSelected) {
							setAmount(stack, getAmount(stack) + 1);
						}
					}
				});
			});
			
			//Player glowing
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;
				CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> {
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
	public EnumActionResult onItemUse(ItemUseContext useContext) {
		EntityPlayer player = useContext.getPlayer();
		
		if (!player.world.isRemote) {
			ItemStack itemStack = useContext.getItem();
			RayTraceResult raytraceresult = this.rayTrace(player.world, player, true);
			
			if (raytraceresult == null || raytraceresult.getBlockPos() == null) {
				return EnumActionResult.FAIL;
			}
			
			BlockPos blockPos = raytraceresult.getBlockPos();
			IBlockState iblockstate = player.world.getBlockState(blockPos);
			Material material = iblockstate.getMaterial();
			
			if (material == Material.WATER) {
				player.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
				player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
				
				if (!hasWater(itemStack)) {
					setWater(itemStack, true);
					player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
					PlayerUtil.sendMessage(player, new TextComponentTranslation("nbt.item.water_filled"), true);
				} else {
					PlayerUtil.sendMessage(player, new TextComponentTranslation("nbt.item.water_already_filled"), true);
				}
				return EnumActionResult.SUCCESS;
			}
		}
		
		return super.onItemUse(useContext);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		ItemStack stack = player.getHeldItem(handIn);
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player).orElse(null);
		if (!worldIn.isRemote) {
			
			//If the player is in POST or Regenerating, stop them from drinking it
			if (cap.getState() == RegenState.POST || cap.getState() == RegenState.REGENERATING || player.isCreative()) {
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.cannot_use"), true);
				return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(handIn));
			}
			
			if (hasWater(stack)) {
				//If the stack has enough, basically kill them
				if (getAmount(stack) == 100) {
					if (cap.getRegenerationsLeft() < 1)
						cap.receiveRegenerations(1);
					
					player.attackEntityFrom(RegenObjects.REGEN_DMG_LINDOS, Integer.MAX_VALUE);
					setAmount(stack, 0);
					setWater(stack, false);
					return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(handIn));
				} else {
					PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.empty_vial"), true);
					return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(handIn));
				}
			} else {
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.no_water"), true);
				return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(handIn));
			}
		}
		return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(handIn));
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag idk) {
		super.addInformation(stack, world, tooltip, idk);
		tooltip.add(new TextComponentTranslation("nbt.item.lindos", getAmount(stack)));
		tooltip.add(new TextComponentTranslation("nbt.item.water", hasWater(stack)));
	}
	
	
	@Override
	public void update(EntityItemOverride itemOverride) {
		if (itemOverride.world.isRemote) return;
		ItemStack itemStack = itemOverride.getItem();
		if (itemStack.getItem() == this) {
			if (itemOverride.isInWater()) {
				if (itemStack.getTag() != null) {
					setWater(itemStack, true);
				}
			}
		}
	}
	
	
}
