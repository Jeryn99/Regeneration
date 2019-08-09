package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.entity.EntityItemOverride;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class ItemFobWatch extends ItemOverrideBase {

    public ItemFobWatch() {
        setMaxDamage(RegenConfig.regenCapacity);
        setCreativeTab(CreativeTabs.MISC);
        setMaxStackSize(1);

        addPropertyOverride(new ResourceLocation("open"), (stack, worldIn, entityIn) -> {
            if (getStackTag(stack) == null || !getStackTag(stack).hasKey("open")) {
                return 0F; //Closed
            }
            return getOpen(stack);
        });

        addPropertyOverride(new ResourceLocation("engrave"), (stack, worldIn, entityIn) -> {
            if (getStackTag(stack) == null || !getStackTag(stack).hasKey("engrave")) {
                return 0F; //Default
            }
            return getEngrave(stack);
        });

    }

    public static int getEngrave(ItemStack stack) {
        return getStackTag(stack).getInteger("engrave");
    }

    public static void setEngrave(ItemStack stack, int engrave) {
        getStackTag(stack).setInteger("engrave", engrave);
    }

    public static NBTTagCompound getStackTag(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("open", 0);
            stack.getTagCompound().setInteger("engrave", itemRand.nextInt(2));
        }
        return stack.getTagCompound();
    }

    public static int getOpen(ItemStack stack) {
        return getStackTag(stack).getInteger("open");
    }

    public static void setOpen(ItemStack stack, int amount) {
        getStackTag(stack).setInteger("open", amount);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        if (RegenConfig.craftWithRegens) {
            stack.setItemDamage(0);
            setEngrave(stack, worldIn.rand.nextInt(3));
            setOpen(stack, 0);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setBoolean("live", false);
        } else {
            stack.getTagCompound().setBoolean("live", false);
        }

        if (getOpen(stack) == 1) {
            if (entityIn.ticksExisted % 600 == 0) {
                setOpen(stack, 0);
            }
        }

        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
        ItemStack stack = player.getHeldItem(hand);

        if (!player.isSneaking()) { // transferring watch->player
            if (stack.getItemDamage() == RegenConfig.regenCapacity)
                return msgUsageFailed(player, "regeneration.messages.transfer.empty_watch", stack);
            else if (cap.getRegenerationsLeft() == RegenConfig.regenCapacity)
                return msgUsageFailed(player, "regeneration.messages.transfer.max_regens", stack);

            int supply = RegenConfig.regenCapacity - stack.getItemDamage(), needed = RegenConfig.regenCapacity - cap.getRegenerationsLeft(), used = Math.min(supply, needed);

            if (cap.canRegenerate()) {
                setOpen(stack, 1);
                PlayerUtil.sendMessage(player, new TextComponentTranslation("regeneration.messages.gained_regens", used), true);
            } else {
                if (!world.isRemote) {
                    setOpen(stack, 1);
                } else {
                    ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.timelord"), new TextComponentTranslation("regeneration.toast.to_use", RegenConfig.regenCapacity));
                }
            }

            if (used < 0)
                RegenerationMod.LOG.warn(player.getName() + ": Fob watch used <0 regens (supply: " + supply + ", needed:" + needed + ", used:" + used + ", capacity:" + RegenConfig.regenCapacity + ", damage:" + stack.getItemDamage() + ", regens:" + cap.getRegenerationsLeft());


            if (!cap.getPlayer().isCreative()) {
                stack.setItemDamage(stack.getItemDamage() + used);
            }

            if (world.isRemote) {
                setOpen(stack, 1);
                ClientUtil.playPositionedSoundRecord(RegenObjects.Sounds.FOB_WATCH, 1.0F, 2.0F);
            } else {
                cap.receiveRegenerations(used);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else { // transferring player->watch
            if (!cap.canRegenerate())
                return msgUsageFailed(player, "regeneration.messages.transfer.no_regens", stack);

            if (cap.getState() != PlayerUtil.RegenState.ALIVE) {
                return msgUsageFailed(player, "regeneration.messages.not_alive", stack);
            }

            if (stack.getItemDamage() == 0)
                return msgUsageFailed(player, "regeneration.messages.transfer.full_watch", stack);

            stack.setItemDamage(stack.getItemDamage() - 1);
            PlayerUtil.sendMessage(player, "regeneration.messages.transfer.success", true);

            if (world.isRemote) {
                ClientUtil.playPositionedSoundRecord(SoundEvents.BLOCK_FIRE_EXTINGUISH, 5.0F, 2.0F);
            } else {
                setOpen(stack, 1);
                cap.extractRegeneration(1);
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
    }

    private ActionResult<ItemStack> msgUsageFailed(EntityPlayer player, String message, ItemStack stack) {
        PlayerUtil.sendMessage(player, message, true);
        return ActionResult.newResult(EnumActionResult.FAIL, stack);
    }

    @Override
    public void update(EntityItemOverride itemOverride) {
        if (!itemOverride.world.isRemote) return;
        ItemStack itemStack = itemOverride.getItem();
        if (itemStack.getItem() == this && itemStack.getItemDamage() != RegenConfig.regenCapacity) {
            if (itemOverride.ticksExisted % 5000 == 0 || itemOverride.ticksExisted == 2) {
                ClientUtil.playSound(itemOverride, RegenObjects.Sounds.FOB_WATCH_DIALOGUE.getRegistryName(), SoundCategory.AMBIENT, false, () -> itemOverride.isDead, 1.5F);
            }
        }
    }

    @Override
    public boolean isRepairable() {
        return false;
    }
}
