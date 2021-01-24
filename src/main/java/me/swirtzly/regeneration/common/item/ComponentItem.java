package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.util.common.ICompatObject;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Swirtzly
 * on 29/04/2020 @ 12:04
 */
public class ComponentItem extends SolidItem implements ICompatObject {
    public ComponentItem() {
        super(new Properties().maxStackSize(1).maxDamage(2));
    }

    @Override
    protected boolean isInGroup(ItemGroup group) {
        return ModList.get().isLoaded("tardis") && group == ItemGroups.REGEN_TAB;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List< ITextComponent > tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!ModList.get().isLoaded("tardis")) {
            tooltip.add(new StringTextComponent("This item is useless without the New Tardis Mod Installed."));
        }
    }
}
