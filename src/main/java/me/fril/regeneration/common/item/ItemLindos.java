package me.fril.regeneration.common.item;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.entity.EntityItemOverride;
import me.fril.regeneration.common.entity.IEntityOverride;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLindos extends Item implements IEntityOverride {
	
	public ItemLindos() {
		this.addPropertyOverride(new ResourceLocation("amount"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				if (stack.getTagCompound() == null) {
					return 0.0F;
				} else {
					int amount = getAmount(stack);
					
					if (amount >= 90) {
						return 1.0F;
					}
					
					if (amount >= 50) {
						return 0.5F;
					}
					
					return 0.0F;
				}
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
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setBoolean("die", false);
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
		if (!worldIn.isRemote) {
			ItemStack stack = player.getHeldItem(handIn);
			IRegeneration data = CapabilityRegeneration.getForPlayer(player);
			
			if (data.getState() == RegenState.POST || data.getState() == RegenState.REGENERATING) {
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.cannot_use"), true);
			}
			if (getAmount(stack) == 100) {
				if (data.canRegenerate()) {
					player.attackEntityFrom(RegenObjects.REGEN_DMG_LINDOS, Integer.MAX_VALUE);
					setAmount(stack, 0);
				} else {
					data.receiveRegenerations(1);
					player.attackEntityFrom(RegenObjects.REGEN_DMG_LINDOS, Integer.MAX_VALUE);
					setAmount(stack, 0);
				}
			} else {
				PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.empty_vial"), true);
			}
		} else {
			PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.no_water"), true);
		}
		
		return super.onItemRightClick(worldIn, player, handIn);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TextComponentTranslation("nbt.item.lindos", getAmount(stack)).getUnformattedText());
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}
	
	@Nullable
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		EntityItemOverride item = new EntityItemOverride(world, location.posX, location.posY, location.posZ, itemstack);
		item.setEntitySize(item.getHeight(), item.getWidth());
		item.motionX = location.motionX;
		item.motionY = location.motionY;
		item.motionZ = location.motionZ;
		return item;
	}
}
