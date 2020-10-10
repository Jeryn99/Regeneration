package me.swirtzly.regen.common.item;

import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.common.entities.OverrideEntity;
import me.swirtzly.regen.common.objects.RItems;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.util.ClientUtil;
import me.swirtzly.regen.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;


/**
 * Created by Sub on 16/09/2018.
 */
public class FobWatchItem extends SolidItem {

    public FobWatchItem() {
        super(new Item.Properties().setNoRepair().group(RItems.MAIN).maxStackSize(1));
    }

    public static CompoundNBT getStackTag(ItemStack stack) {
        if (stack.getTag() == null || !stack.getTag().contains("is_open") || !stack.getTag().contains("is_gold")) {
            stack.setTag(new CompoundNBT());
            stack.getTag().putBoolean("is_open", false);
            stack.getTag().putBoolean("is_gold", random.nextBoolean());
        }
        return stack.getTag();
    }

    public static boolean getEngrave(ItemStack stack) {
        return getStackTag(stack).getBoolean("is_gold");
    }

    public static void setEngrave(ItemStack stack, boolean isGold) {
        getStackTag(stack).putBoolean("is_gold", isGold);
    }

    public static boolean getOpen(ItemStack stack) {
        return getStackTag(stack).getBoolean("is_open");
    }

    public static void setOpen(ItemStack stack, boolean isOpen) {
        getStackTag(stack).putBoolean("is_open", isOpen);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        setDamage(stack, 0);
        setOpen(stack, false);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getItem() instanceof FobWatchItem) {
            if (stack.getTag() == null) {
                stack.setTag(new CompoundNBT());
                stack.getTag().putBoolean("live", true);
            } else {
                stack.getTag().putBoolean("live", true);
            }

            if (getOpen(stack)) {
                if (entityIn.ticksExisted % 600 == 0) {
                    setOpen(stack, false);
                }
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItem(hand);
        IRegen cap = RegenCap.get(player).orElseGet(null);


        if (!player.isSneaking()) { // transferring watch->player
            if (getDamage(stack) == RegenConfig.COMMON.regenCapacity.get())
                return msgUsageFailed(player, "regeneration.messages.transfer.empty_watch", stack);
            else if (cap.getRegens() == RegenConfig.COMMON.regenCapacity.get())
                return msgUsageFailed(player, "regeneration.messages.transfer.max_regens", stack);

            int supply = RegenConfig.COMMON.regenCapacity.get() - getDamage(stack), needed = RegenConfig.COMMON.regenCapacity.get() - cap.getRegens(), used = Math.min(supply, needed);

            if (cap.canRegenerate()) {
                setOpen(stack, true);
                PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.gained_regens", used), true);
            } else {
                if (!world.isRemote) {
                    setOpen(stack, true);
                    PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.now_timelord"), true);
                }
            }

            if (used < 0)
                Regeneration.LOG.warn(player.getName().getString() + ": Fob watch used <0 regens (supply: " + supply + ", needed:" + needed + ", used:" + used + ", capacity:" + RegenConfig.COMMON.regenCapacity.get() + ", damage:" + getDamage(stack) + ", regens:" + cap.getRegens());

            setDamage(stack, stack.getDamage() + used);

            if (world.isRemote) {
                ClientUtil.playPositionedSoundRecord(RSounds.FOB_WATCH.get(), 1.0F, 2.0F);
            } else {
                setOpen(stack, true);
                cap.addRegens(used);
            }

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        } else { // transferring player->watch
            if (!cap.canRegenerate()) return msgUsageFailed(player, "regeneration.messages.transfer.no_regens", stack);

            if (cap.getCurrentState() != RegenStates.ALIVE) {
                return msgUsageFailed(player, "regeneration.messages.not_alive", stack);
            }

            if (getDamage(stack) == 0)
                return msgUsageFailed(player, "regeneration.messages.transfer.full_watch", stack);

            setDamage(stack, getDamage(stack) - 1);
            PlayerUtil.sendMessage(player, "regeneration.messages.transfer.success", true);

            if (world.isRemote) {
                ClientUtil.playPositionedSoundRecord(SoundEvents.BLOCK_FIRE_EXTINGUISH, 5.0F, 2.0F);
            } else {
                setOpen(stack, true);
                cap.extractRegens(1);
            }

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
    }


    private ActionResult<ItemStack> msgUsageFailed(PlayerEntity player, String message, ItemStack stack) {
        PlayerUtil.sendMessage(player, message, true);
        return ActionResult.resultFail(stack);
    }

    @Override
    public boolean onSolidEntityItemUpdate(OverrideEntity itemOverride) {
        if (!itemOverride.world.isRemote) return false;
        ItemStack itemStack = itemOverride.getItem();
        if (itemStack.getItem() == this && getDamage(itemStack) != RegenConfig.COMMON.regenCapacity.get()) {
            if (itemOverride.ticksExisted % 5000 == 0 || itemOverride.ticksExisted == 2) {
                ClientUtil.playSound(itemOverride, RSounds.FOB_WATCH_DIALOGUE.get().getRegistryName(), SoundCategory.AMBIENT, false, () -> !itemOverride.isAlive(), 1.5F);
            }
        }
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return super.isDamageable();
    }

    @Override
    public boolean shouldSyncTag() {
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return RegenConfig.COMMON.regenCapacity.get();
    }
}