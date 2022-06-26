package craig.software.mc.regen.common.item;

import craig.software.mc.regen.common.objects.RItems;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.traits.AbstractTrait;
import craig.software.mc.regen.common.traits.RegenTraitRegistry;
import craig.software.mc.regen.util.RegenSources;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ElixirItem extends Item {

    public ElixirItem() {
        super(new Item.Properties().setNoRepair().tab(RItems.MAIN).stacksTo(1));
    }

    public static AbstractTrait getTrait(ItemStack stack) {
        return RegenTraitRegistry.fromID(stack.getOrCreateTag().getString("trait"));
    }

    public static void setTrait(ItemStack stack, AbstractTrait iTrait) {
        stack.getOrCreateTag().putString("trait", RegenTraitRegistry.getTraitLocation(iTrait).toString());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        MutableComponent prefix = Component.literal("Elixir: ");
        return prefix.append((getTrait(stack).translation()));
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
        if (allowedIn(group)) {
            for (AbstractTrait trait : RegenTraitRegistry.TRAIT_REGISTRY.get().getValues()) {
                if (RegenTraitRegistry.getTraitLocation(trait) != RegenTraitRegistry.getTraitLocation(RegenTraitRegistry.BORING.get())) {
                    ItemStack stack = new ItemStack(this);
                    setTrait(stack, trait);
                    items.add(stack);
                }//Do not call removeIf outside the for loop as this will loop back around to the start of the item group and "delete" other items like the fobwatch
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getItem() instanceof ElixirItem) {
            playerIn.startUsingItem(handIn);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 32;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level worldIn, @NotNull LivingEntity entityLiving) {
        if (worldIn.isClientSide) return super.finishUsingItem(stack, worldIn, entityLiving);
        RegenCap.get(entityLiving).ifPresent(iRegen -> {
            if (iRegen.canRegenerate()) {
                iRegen.setNextTrait(getTrait(stack));
                iRegen.syncToClients(null);
                entityLiving.hurt(RegenSources.REGEN_DMG_FORCED, Integer.MAX_VALUE);
                stack.shrink(1);
                entityLiving.playSound(SoundEvents.GENERIC_DRINK, 0.3F, 1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.4F);
            }
        });
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(getTrait(stack).description());
    }
}
