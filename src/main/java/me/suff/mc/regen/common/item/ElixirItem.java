package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.traits.Traits;
import me.suff.mc.regen.util.RegenSources;
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
        super(new Item.Properties().setNoRepair().group(RItems.MAIN).maxStackSize(1));
    }

    public static Traits.ITrait getTrait(ItemStack stack) {
        return Traits.fromID(stack.getOrCreateTag().getString("trait"));
    }

    public static void setTrait(ItemStack stack, Traits.ITrait iTrait) {
        stack.getOrCreateTag().putString("trait", iTrait.getRegistryName().toString());
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        StringTextComponent prefix = new StringTextComponent("Elixir: ");
        return prefix.appendSibling(getTrait(stack).getTranslation());
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList< ItemStack > items) {
        if (isInGroup(group)) {
            for (Traits.ITrait trait : Traits.REGISTRY.getValues()) {
                if (trait.getRegistryName() != Traits.BORING.get().getRegistryName()) {
                    ItemStack stack = new ItemStack(this);
                    setTrait(stack, trait);
                    items.add(stack);
                }
            }
        }
        items.removeIf(stack -> getTrait(stack).getRegistryName() == Traits.BORING.get().getRegistryName());
    }

    @Override
    public ActionResult< ItemStack > onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (itemstack.getItem() instanceof ElixirItem) {
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemstack);
        }
        return ActionResult.resultFail(itemstack);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (worldIn.isRemote) return super.onItemUseFinish(stack, worldIn, entityLiving);
        RegenCap.get(entityLiving).ifPresent(iRegen -> {
            if (iRegen.canRegenerate()) {
                iRegen.setNextTrait(getTrait(stack));
                iRegen.syncToClients(null);
                entityLiving.attackEntityFrom(RegenSources.REGEN_DMG_FORCED, Integer.MAX_VALUE);
                stack.shrink(1);
                entityLiving.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.3F, 1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.4F);
            }
        });
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List< ITextComponent > tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(getTrait(stack).getDescription());
    }
}
