package mc.craig.software.regen.common.item;

import mc.craig.software.regen.common.item.tooltip.fob.FobTooltip;
import mc.craig.software.regen.common.objects.RParticles;
import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.util.ClientUtil;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.constants.RMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FobWatchItem extends Item {

    public FobWatchItem() {
        super(new Item.Properties().stacksTo(1).durability(12));
    }

    public static CompoundTag getStackTag(ItemStack stack) {
        CompoundTag stackTag = stack.getOrCreateTag();
        if (!stackTag.contains("is_open")) stackTag.putBoolean("is_open", false);
        if (!stackTag.contains("is_gold")) stackTag.putBoolean("is_gold", RegenUtil.RAND.nextBoolean());
        return stackTag;
    }

    public static boolean getEngrave(ItemStack stack) {
        return getStackTag(stack).getBoolean("is_gold");
    }

    public static void setEngrave(ItemStack stack, boolean isGold) {
        getStackTag(stack).putBoolean("is_gold", isGold);
    }

    public static boolean isOpen(ItemStack stack) {
        return getStackTag(stack).getBoolean("is_open");
    }

    public static void setOpen(ItemStack stack, boolean isOpen) {
        getStackTag(stack).putBoolean("is_open", isOpen);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        stack.setDamageValue(0);
        setOpen(stack, false);
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getItem() instanceof FobWatchItem && isOpen(stack) && entityIn.tickCount % 600 == 0) {
            setOpen(stack, false);
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        IRegen cap = RegenerationData.get(player).orElse(null);
        if (cap == null) return InteractionResultHolder.fail(stack);

        if (!player.isShiftKeyDown()) { // Transfer from watch to player
            if (stack.getDamageValue() == getMaxDamage())
                return msgUsageFailed(player, RMessages.TRANSFER_EMPTY_WATCH, stack);
            if (cap.regens() >= getMaxDamage())
                return msgUsageFailed(player, RMessages.TRANSFER_MAX_REGENS, stack);

            int supply = getMaxDamage() - stack.getDamageValue();
            int needed = getMaxDamage() - cap.regens();
            int used = Math.min(supply, needed);

            if (cap.isWasPreviouslyATimelord()) used = Math.min(12, needed);

            if (!world.isClientSide) {
                if (cap.canRegenerate()) {
                    cap.addRegens(used);
                    PlayerUtil.sendMessage(player, Component.translatable(RMessages.GAINED_REGENERATIONS, used), true);
                } else {
                    PlayerUtil.sendMessage(player, Component.translatable(RMessages.TIMELORD_STATUS), true);
                    cap.addRegens(12);
                }

                setOpen(stack, true);
                cap.syncToClients(null);

                ServerLevel serverWorld = (ServerLevel) world;
                BlockPos pos = player.blockPosition();
                serverWorld.sendParticles(RParticles.CONTAINER.get(), pos.getX(), pos.getY() + 1D, pos.getZ(), 8, 0.5D, 0.25D, 0.5D, 0.0D);

                stack.setDamageValue(stack.getDamageValue() + used);
            } else {
                ClientUtil.playPositionedSoundRecord(RSounds.FOB_WATCH.get(), 1.0F, 2.0F);
            }

        } else { // Store regenerations
            if (cap.regenState() != RegenStates.ALIVE)
                return msgUsageFailed(player, RMessages.TRANSFER_INVALID_STATE, stack);
            if (cap.regens() == 0)
                return msgUsageFailed(player, RMessages.TRANSFER_NO_REGENERATIONS, stack);
            if (stack.getDamageValue() == 0)
                return msgUsageFailed(player, RMessages.TRANSFER_FULL_WATCH, stack);

            if (!world.isClientSide) {
                stack.setDamageValue(stack.getDamageValue() - 1);
                setOpen(stack, true);
                cap.extractRegens(1);
                cap.syncToClients(null);
                PlayerUtil.sendMessage(player, RMessages.TRANSFER_SUCCESSFUL, true);
            } else {
                ClientUtil.playPositionedSoundRecord(SoundEvents.FIRE_EXTINGUISH, 5.0F, 2.0F);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }

    private InteractionResultHolder<ItemStack> msgUsageFailed(Player player, String message, ItemStack stack) {
        PlayerUtil.sendMessage(player, message, true);
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return super.canBeDepleted();
    }

    @Override
    public boolean shouldOverrideMultiplayerNbt() {
        return true;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (!stack.isDamaged()) return Optional.empty();
        return Optional.of(new FobTooltip(stack.getDamageValue()));
    }
}
