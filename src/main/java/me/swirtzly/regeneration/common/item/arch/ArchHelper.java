package me.swirtzly.regeneration.common.item.arch;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.item.arch.capability.ArchProvider;
import me.swirtzly.regeneration.common.item.arch.capability.CapabilityArch;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Swirtzly on 29/02/2020 @ 22:32
 */
@Mod.EventBusSubscriber
public class ArchHelper {

    @SubscribeEvent
    public static void onItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof ItemBlock) return;
        event.addCapability(CapabilityArch.ARCH_ID, new ArchProvider(new CapabilityArch(event.getObject())));
    }

    @SubscribeEvent
    public static void onToolTip(ItemTooltipEvent event) {
        if (event.getItemStack().hasCapability(CapabilityArch.CAPABILITY, null)) {
            IArch data = CapabilityArch.getForPlayer(event.getItemStack());
            DnaHandler.IDna trait = DnaHandler.getDnaEntry(data.getSavedTrait());
            if (data.getArchStatus() == IArch.ArchStatus.ARCH_ITEM) {
                event.getToolTip().add(new TextComponentTranslation(trait.getLangKey()).getUnformattedComponentText());
                event.getToolTip().add("" + data.getRegenAmount());
            }
        }
    }

    public static void onArchUse(EntityPlayer player, ItemStack stack) {
        if (player.world.isRemote) return;
        IRegeneration playerData = CapabilityRegeneration.getForPlayer(player);
        IArch stackData = CapabilityArch.getForPlayer(stack);
        if (stackData.getArchStatus() == IArch.ArchStatus.NORMAL_ITEM) {
            stackData.setRegenAmount(playerData.getRegenerationsLeft());
            stackData.setSavedTrait(playerData.getDnaType());
            stackData.setArchStatus(IArch.ArchStatus.ARCH_ITEM);
            playerData.setDnaType(DnaHandler.DNA_BORING.getRegistryName());
            playerData.extractRegeneration(playerData.getRegenerationsLeft());
            stackData.sync();
        }
    }

}
