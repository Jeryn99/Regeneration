package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.OverrideEntity;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.client.ClientUtil;
import me.swirtzly.regeneration.util.common.PlayerUtil;
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
        super(new Item.Properties().setNoRepair().group(ItemGroups.REGEN_TAB).maxStackSize(1));
        addPropertyOverride(new ResourceLocation(Regeneration.MODID, "model"), (stack, worldIn, entityIn) -> {
            boolean isGold = getEngrave(stack) == 1;
            boolean isOpen = getOpen(stack) == 1;
            if (isOpen && isGold) {
                return 0.2F;
            }

            if (!isOpen && !isGold) {
                return 0.3F;
            }

            if (isOpen) {
                return 0.4F;
            }


            return 0.1F;
        });
    }

    public static CompoundNBT getStackTag(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains("open")) {
            tag.putInt("open", 0);
        }
        if (!tag.contains("engrave")) {
            tag.putInt("engrave", random.nextInt(2));
        }
        return tag;
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
    public ItemStack getDefaultInstance() {
        return super.getDefaultInstance();
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        stack.setDamage(0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getItem() instanceof FobWatchItem) {
            stack.getOrCreateTag().putBoolean("live", true);
            if (getOpen(stack) == 1) {
                if (entityIn.ticksExisted % 600 == 0) {
                    setOpen(stack, 0);
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public ActionResult< ItemStack > onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        IRegen cap = RegenCap.get(player).orElseGet(null);

        if (!player.isSneaking()) { // transferring watch->player
            if (stack.getDamage() == RegenConfig.COMMON.regenCapacity.get())
                return msgUsageFailed(player, "regeneration.messages.transfer.empty_watch", stack);
            else if (cap.getRegenerationsLeft() == RegenConfig.COMMON.regenCapacity.get())
                return msgUsageFailed(player, "regeneration.messages.transfer.max_regens", stack);

            int supply = RegenConfig.COMMON.regenCapacity.get() - stack.getDamage(), needed = RegenConfig.COMMON.regenCapacity.get() - cap.getRegenerationsLeft(), used = Math.min(supply, needed);

            if (cap.canRegenerate()) {
                setOpen(stack, 1);
                if (world.isRemote) {
                    PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.gained_regens", used), true);
                }
            } else {
                if (!world.isRemote) {
                    setOpen(stack, 1);
                } else {
                    PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.now_timelord"), true);
                }
            }

            if (used < 0)
                Regeneration.LOG.warn(player.getName() + ": Fob watch used <0 regens (supply: " + supply + ", needed:" + needed + ", used:" + used + ", capacity:" + RegenConfig.COMMON.regenCapacity.get() + ", damage:" + stack.getDamage() + ", regens:" + cap.getRegenerationsLeft());

            stack.setDamage(stack.getDamage() + used);

            if (world.isRemote) {
                setOpen(stack, 1);
                ClientUtil.playPositionedSoundRecord(RegenObjects.Sounds.FOB_WATCH.get(), 1.0F, 2.0F);
            } else {
                cap.receiveRegenerations(used);
            }

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        } else { // transferring player->watch
            if (!cap.canRegenerate()) return msgUsageFailed(player, "regeneration.messages.transfer.no_regens", stack);

            if (cap.getState() != PlayerUtil.RegenState.ALIVE) {
                return msgUsageFailed(player, "regeneration.messages.not_alive", stack);
            }

            if (stack.getDamage() == 0)
                return msgUsageFailed(player, "regeneration.messages.transfer.full_watch", stack);

            stack.setDamage(stack.getDamage() - 1);
            PlayerUtil.sendMessage(player, "regeneration.messages.transfer.success", true);

            if (world.isRemote) {
                ClientUtil.playPositionedSoundRecord(SoundEvents.BLOCK_FIRE_EXTINGUISH, 5.0F, 2.0F);
            } else {
                setOpen(stack, 1);
                cap.extractRegeneration(1);
            }

            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
    }


    private ActionResult< ItemStack > msgUsageFailed(PlayerEntity player, String message, ItemStack stack) {
        PlayerUtil.sendMessage(player, message, true);
        return ActionResult.newResult(ActionResultType.FAIL, stack);
    }

    @Override
    public boolean onSolidEntityItemUpdate(OverrideEntity itemOverride) {
        if (!itemOverride.world.isRemote) return false;
        ItemStack itemStack = itemOverride.getItem();
        if (itemStack.getItem() == this && itemStack.getDamage() != RegenConfig.COMMON.regenCapacity.get()) {
            if (itemOverride.ticksExisted % 5000 == 0 || itemOverride.ticksExisted == 2) {
                ClientUtil.playSound(itemOverride, RegenObjects.Sounds.FOB_WATCH_DIALOGUE.get().getRegistryName(), SoundCategory.AMBIENT, false, () -> !itemOverride.isAlive(), 1.5F);
            }
        }
        return true;
    }


    @Override
    public int getMaxDamage(ItemStack stack) {
        return RegenConfig.COMMON.regenCapacity.get();
    }
}
