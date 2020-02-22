package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.common.advancements.RegenTriggers;
import me.swirtzly.regeneration.common.block.BlockHandInJar;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.entity.EntityItemOverride;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
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
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        if (!playerIn.world.isRemote) {
            RegenTriggers.LINDOS_VIAL.trigger((EntityPlayerMP) playerIn);
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (!(entityLiving instanceof EntityPlayer)) return stack;

        EntityPlayer player = (EntityPlayer) entityLiving;
        IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
        if (!worldIn.isRemote) {

            //If the player is in POST or Regenerating, stop them from drinking it
            if (getAmount(stack) > 100 && hasWater(stack)) {
                if (cap.getState() == PlayerUtil.RegenState.POST || cap.getState() == PlayerUtil.RegenState.REGENERATING || player.isCreative()) {
                    PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.cannot_use"), true);
                    return stack;
                }
            }

            if (hasWater(stack)) {
                //If the stack has enough, basically kill them
                if (getAmount(stack) == 100) {
                    if (cap.getRegenerationsLeft() < 1)
                        cap.receiveRegenerations(1);
                    setAmount(stack, 0);
                    setWater(stack, false);
                    return stack;
                } else {
                    PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.empty_vial"), true);
                    return stack;
                }
            } else {
                PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.no_water"), true);
                return stack;
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
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
                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                    if (hasWater(stack) && worldIn.rand.nextInt(100) > 70 && PlayerUtil.isInEitherHand(player, this)) {
                        setAmount(stack, getAmount(stack) + 1);
                    }
                }
            });

            //Player glowing
            if (entityIn instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityIn;
                if (PlayerUtil.isInEitherHand(player, this)) {
                    if (hasWater(stack) && CapabilityRegeneration.getForPlayer(player).areHandsGlowing() && player.ticksExisted % 100 == 0) {
                        setAmount(stack, getAmount(stack) + 2);
                    }
                }
            }
        }
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack itemStack = player.getHeldItem(hand);
            RayTraceResult raytraceresult = this.rayTrace(worldIn, player, true);

            if (raytraceresult == null || raytraceresult.getBlockPos() == null) {
                return EnumActionResult.FAIL;
            }

            BlockPos blockPos = raytraceresult.getBlockPos();
            IBlockState iblockstate = worldIn.getBlockState(blockPos);
            Material material = iblockstate.getMaterial();

            //TODO fix the logic of this
            if (iblockstate.getBlock() instanceof BlockHandInJar && player.isSneaking()) {
                if (worldIn.getTileEntity(blockPos) instanceof TileEntityHandInJar) {
                    TileEntityHandInJar jar = (TileEntityHandInJar) worldIn.getTileEntity(blockPos);
                    setAmount(itemStack, getAmount(itemStack) + jar.getLindosAmont());
                    jar.setLindosAmont(0);
                }
                return EnumActionResult.SUCCESS;
            }

            if (material == Material.WATER) {
                if (!hasWater(itemStack)) {
                    worldIn.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
                    player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                    setWater(itemStack, true);
                    PlayerUtil.sendMessage(player, new TextComponentTranslation("nbt.item.water_filled"), true);
                } else {
                    PlayerUtil.sendMessage(player, new TextComponentTranslation("nbt.item.water_already_filled"), true);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (CapabilityRegeneration.getForPlayer(playerIn).getState() != PlayerUtil.RegenState.POST) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
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
