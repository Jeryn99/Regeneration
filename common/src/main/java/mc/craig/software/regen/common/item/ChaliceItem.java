package mc.craig.software.regen.common.item;

import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.util.RegenSources;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
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

public class ChaliceItem extends Item {

    public ChaliceItem() {
        super(new Item.Properties().tab(RItems.MAIN).stacksTo(1));
    }

    public static TraitBase getTrait(ItemStack stack) {
        return TraitRegistry.TRAITS_REGISTRY.get(new ResourceLocation(stack.getOrCreateTag().getString("trait")));
    }

    public static void setTrait(ItemStack stack, TraitBase iTrait) {
        stack.getOrCreateTag().putString("trait", TraitRegistry.TRAITS_REGISTRY.getKey(iTrait).toString());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        MutableComponent prefix = Component.literal("Chalice of ");
        return prefix.append((getTrait(stack).getTitle()));
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
        if (allowedIn(group)) {
            for (TraitBase trait : TraitRegistry.TRAITS_REGISTRY.getValues()) {
                if (TraitRegistry.TRAITS_REGISTRY.getKey(trait) != TraitRegistry.TRAITS_REGISTRY.getKey(TraitRegistry.HUMAN.get())) {
                    ItemStack stack = new ItemStack(this);
                    setTrait(stack, trait);
                    items.add(stack);
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getItem() instanceof ChaliceItem) {
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
        RegenerationData.get(entityLiving).ifPresent(iRegen -> {
            if (iRegen.canRegenerate()) {
               //TODO iRegen.setNextTrait(getTrait(stack));
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
       tooltip.add(getTrait(stack).getDescription());
    }
}