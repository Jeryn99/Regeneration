package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.common.item.arch.IDontStore;
import me.swirtzly.regeneration.common.item.arch.capability.CapabilityArch;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Swirtzly on 29/01/2020 @ 20:41
 */
public class ItemArchInterface extends Item implements IDontStore {


    public ItemArchInterface() {
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack headSlot = playerIn.getItemStackFromSlot(entityequipmentslot);
        if (headSlot.isEmpty()) {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemstack);
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound oldCapNbt) {
        return new InvProvider();
    }


    public static void sync(ItemStack stack) {
        if (stack.getTagCompound() != null) {
            stack.getTagCompound().merge(stack.getItem().getNBTShareTag(stack));
        }
    }

    public static void readSync(ItemStack stack) {
        if (stack.getTagCompound() != null) {
            stack.getItem().readNBTShareTag(stack, stack.getTagCompound());
        }
    }

    @Override
    public boolean getShareTag() {
        return super.getShareTag();
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            tag.setTag("arch_sync", CapabilityArch.getForStack(stack).serializeNBT());
            return tag;
        }
        return super.getNBTShareTag(stack);
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        super.readNBTShareTag(stack, nbt);
        if (nbt != null && stack != null) {
            if (nbt.hasKey("cap_sync")) {
                CapabilityArch.getForStack(stack).deserializeNBT((NBTTagCompound) nbt.getTag("arch_sync"));
            }
        }
    }


    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        return super.onEntitySwing(entityLiving, stack);
    }

    @Nullable
    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EntityEquipmentSlot.HEAD;
    }

    private static class InvProvider implements ICapabilitySerializable<NBTBase> {

        private final IItemHandler inv = new ItemStackHandler(1) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack toInsert, boolean simulate) {
                if (!toInsert.isEmpty()) {
                    boolean isUseAble = toInsert.hasCapability(CapabilityArch.CAPABILITY, null) && toInsert.getCount() == 1;
                    if (isUseAble)
                        return super.insertItem(slot, toInsert, simulate);
                }
                return toInsert;
            }
        };

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
            else return null;
        }

        @Override
        public NBTBase serializeNBT() {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TextComponentTranslation("item.info.purpose").getUnformattedComponentText());

        if (GuiInventory.isShiftKeyDown()) {
            tooltip.add(new TextComponentTranslation("item.info.arch_power").getUnformattedComponentText());
            tooltip.add(new TextComponentTranslation("item.info.arch_power2").getUnformattedComponentText());
            tooltip.add(new TextComponentTranslation("item.info.arch_use").getUnformattedComponentText());
        } else {
            tooltip.add(new TextComponentTranslation("item.info.shift").getUnformattedComponentText());
        }

    }
}
