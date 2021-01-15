package me.swirtzly.regen.common.item;

import me.swirtzly.regen.common.objects.RItems;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.traits.Traits;
import me.swirtzly.regen.util.RegenSources;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElixirItem extends Item {

    public static Traits.ITrait getTrait(ItemStack stack) {
        return Traits.fromID(stack.getOrCreateTag().getString("trait"));
    }

    public static void setTrait(ItemStack stack, Traits.ITrait iTrait) {
        stack.getOrCreateTag().putString("trait", iTrait.getRegistryName().toString());
    }


    public ElixirItem() {
        super(new Item.Properties().setNoRepair().group(RItems.MAIN).maxStackSize(1));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        StringTextComponent prefix = new StringTextComponent("Elixir: ");
        return prefix.append(getTrait(stack).getTranslation());
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            for (Traits.ITrait trait : Traits.REGISTRY.getValues()) {
                if (trait != Traits.BORING.get()) {
                    ItemStack stack = new ItemStack(this);
                    setTrait(stack, trait);
                    items.add(stack);
                }
            }
        }
        super.fillItemGroup(group, items);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        RegenCap.get(playerIn).ifPresent(iRegen -> {
            if (iRegen.canRegenerate()) {
                ItemStack stack = playerIn.getActiveItemStack();
                iRegen.setNextTrait(getTrait(stack));
                playerIn.attackEntityFrom(RegenSources.REGEN_DMG_FORCED, Integer.MAX_VALUE);
                stack.shrink(1);
            }
        });
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(getTrait(stack).getDescription());
    }
}
