package me.craig.software.regen.common.item;

import me.craig.software.regen.common.objects.RItems;
import me.craig.software.regen.common.regen.RegenCap;
import me.craig.software.regen.common.traits.AbstractTrait;
import me.craig.software.regen.common.traits.RegenTraitRegistry;
import me.craig.software.regen.util.RegenSources;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElixirItem extends Item {

    public ElixirItem() {
        super(new Item.Properties().setNoRepair().tab(RItems.MAIN).stacksTo(1));
    }

    public static AbstractTrait getTrait(ItemStack stack) {
        return RegenTraitRegistry.fromID(stack.getOrCreateTag().getString("trait"));
    }

    public static void setTrait(ItemStack stack, AbstractTrait iTrait) {
        stack.getOrCreateTag().putString("trait", iTrait.getRegistryName().toString());
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        StringTextComponent prefix = new StringTextComponent("Elixir: ");
        return prefix.append(getTrait(stack).translation());
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            for (AbstractTrait trait : RegenTraitRegistry.TRAIT_REGISTRY.get().getValues()) {
                if (trait.getRegistryName() != RegenTraitRegistry.BORING.get().getRegistryName()) {
                    ItemStack stack = new ItemStack(this);
                    setTrait(stack, trait);
                    items.add(stack);
                }//Do not call removeIf outside the for loop as this will loop back around to the start of the item group and "delete" other items like the fobwatch
            }
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getItem() instanceof ElixirItem) {
            playerIn.startUsingItem(handIn);
            return ActionResult.consume(itemstack);
        }
        return ActionResult.fail(itemstack);
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(getTrait(stack).description());
    }
}
