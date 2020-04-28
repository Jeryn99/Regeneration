package me.swirtzly.regeneration.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * Created by Swirtzly
 * on 22/04/2020 @ 13:56
 */
@Mod.EventBusSubscriber
public class ArchHelper {

    public static boolean storeRegenerations(ItemStack stack, int amount) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putInt("storedRegens", amount);
        return true;
    }

    public static int getRegenerations(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        if (tag.contains("storedRegens")) {
            return tag.getInt("storedRegens");
        }
        return 0;
    }

    @SubscribeEvent
    public static void onItemToolTip(ItemTooltipEvent event) {
        List<ITextComponent> tooltip = event.getToolTip();
        ItemStack stack = event.getItemStack();
        if (getRegenerations(stack) > 0) {
            tooltip.add(new TranslationTextComponent("stored.regens", getRegenerations(stack)));
        }
    }
}
