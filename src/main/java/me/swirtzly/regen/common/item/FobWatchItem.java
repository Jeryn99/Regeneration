package me.swirtzly.regen.common.item;

import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.client.gui.ColorScreen;
import me.swirtzly.regen.client.gui.GuiPreferences;
import me.swirtzly.regen.common.entities.OverrideEntity;
import me.swirtzly.regen.common.objects.RItems;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.util.ClientUtil;
import me.swirtzly.regen.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.Color;
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
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
            stack.getTag().putInt("open", 0);
            stack.getTag().putInt("engrave", random.nextInt(2));
        }
        return stack.getTag();
    }

    public static int getEngrave(ItemStack stack) {
        return getStackTag(stack).getInt("engrave");
    }

    public static void setEngrave(ItemStack stack, int engrave) {
        getStackTag(stack).putInt("engrave", engrave);
    }

    public static int getOpen(ItemStack stack) {
        return getStackTag(stack).getInt("open");
    }

    public static void setOpen(ItemStack stack, int amount) {
        getStackTag(stack).putInt("open", amount);
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        setDamage(stack, 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
            stack.getTag().putBoolean("live", true);
        } else {
            stack.getTag().putBoolean("live", true);
        }

        if (getOpen(stack) == 1) {
            if (entityIn.ticksExisted % 600 == 0) {
                setOpen(stack, 0);
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        Minecraft.getInstance().deferTask(() -> {
            Minecraft.getInstance().displayGuiScreen(new GuiPreferences()); //TODO NO NO
        });

        ItemStack stack = player.getHeldItem(hand);
        IRegen cap = RegenCap.get(player).orElseGet(null);

        if (!player.isSneaking()) { // transferring watch->player
            if (getDamage(stack) == RegenConfig.COMMON.regenCapacity.get())
                return msgUsageFailed(player, "regeneration.messages.transfer.empty_watch", stack);
            else if (cap.getRegens() == RegenConfig.COMMON.regenCapacity.get())
                return msgUsageFailed(player, "regeneration.messages.transfer.max_regens", stack);

            int supply = RegenConfig.COMMON.regenCapacity.get() - getDamage(stack), needed = RegenConfig.COMMON.regenCapacity.get() - cap.getRegens(), used = Math.min(supply, needed);

            if (cap.canRegenerate()) {
                setOpen(stack, 1);
                PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.gained_regens", used), true);
            } else {
                if (!world.isRemote) {
                    setOpen(stack, 1);
                    PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.now_timelord"), true);
                }
            }

            if (used < 0)
                Regeneration.LOG.warn(player.getName() + ": Fob watch used <0 regens (supply: " + supply + ", needed:" + needed + ", used:" + used + ", capacity:" + RegenConfig.COMMON.regenCapacity.get() + ", damage:" + getDamage(stack) + ", regens:" + cap.getRegens());

            setDamage(stack, stack.getDamage() + used);

            if (world.isRemote) {
                setOpen(stack, 1);
                ClientUtil.playPositionedSoundRecord(RSounds.FOB_WATCH.get(), 1.0F, 2.0F);
            } else {
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
                setOpen(stack, 1);
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
    public int getMaxDamage(ItemStack stack) {
        return RegenConfig.COMMON.regenCapacity.get();
    }
}