package me.fril.regeneration.common.item;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.entity.EntityItemOverride;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
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
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				
				if (stack.getTagCompound() != null) {
					int amount = getAmount(stack);
					
					if (!hasWater(stack)) {
						return 0.0F;
					}
					
					if (hasWater(stack) && getAmount(stack) <= 0) {
						return 2F;
					}
					
					if(amount == 100){
						return 1.0F;
					}
					
					if (amount >= 90) {
						return 0.2F;
					}
					
					if (amount >= 50) {
						return 0.5F;
					}
					
					if(amount >= 10){
						return 0.1F;
					}
				}
				
				return 2F;
			}
		});
	}
	
	public static NBTTagCompound getStackTag(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("amount", 0);
		}
		return stack.getTagCompound();
	}
	
	public static int getAmount(ItemStack stack) {
		return getStackTag(stack).getInteger("amount");
	}
	
	public static void setAmount(ItemStack stack, int amount) {
		getStackTag(stack).setInteger("amount", MathHelper.clamp(amount, 0, 100));
	}
	
	public static boolean hasWater(ItemStack stack) {
		return getStackTag(stack).getBoolean("water");
	}
	
	public static void setWater(ItemStack stack, boolean water) {
		getStackTag(stack).setBoolean("water", water);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setBoolean("live", true);
		} else {
			stack.getTagCompound().setBoolean("live", true);
		}
		
		if (!worldIn.isRemote) {
			//Entiies around
			worldIn.getEntitiesWithinAABB(EntityPlayer.class, entityIn.getEntityBoundingBox().expand(10, 10, 10)).forEach(player -> {
				IRegeneration data = CapabilityRegeneration.getForPlayer((EntityPlayer) entityIn);
				if (data.getState() == RegenState.REGENERATING) {
					if (worldIn.rand.nextBoolean() && isSelected) {
						setAmount(stack, getAmount(stack) + 1);
					}
				}
			});
			
			//Player glowing
			if (entityIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityIn;
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		
		
		ItemStack stack = player.getHeldItem(handIn);
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
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
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TextComponentTranslation("nbt.item.lindos", getAmount(stack)).getUnformattedText());
		tooltip.add(new TextComponentTranslation("nbt.item.water", hasWater(stack)).getUnformattedText());
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
