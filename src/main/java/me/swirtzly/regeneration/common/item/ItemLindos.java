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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
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
                    if (worldIn.rand.nextInt(100) > 50 && isSelected) {
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

            if (iblockstate.getBlock() instanceof BlockHandInJar && player.isSneaking()) {
                if (worldIn.getTileEntity(blockPos) instanceof TileEntityHandInJar) {
                    TileEntityHandInJar jar = (TileEntityHandInJar) worldIn.getTileEntity(blockPos);
                    setAmount(itemStack, getAmount(itemStack) + jar.getLindosAmont());
                    jar.setLindosAmont(0);
                }
                return EnumActionResult.SUCCESS;
            }

            if (material == Material.WATER) {
                worldIn.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
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
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
        ItemStack stack = player.getHeldItem(handIn);
        IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
        if (!worldIn.isRemote) {

            //If the player is in POST or Regenerating, stop them from drinking it
            if (getAmount(stack) > 100) {
                if (cap.getState() == PlayerUtil.RegenState.POST || cap.getState() == PlayerUtil.RegenState.REGENERATING || player.isCreative()) {
                    PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.cannot_use"), true);
                    return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(handIn));
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
