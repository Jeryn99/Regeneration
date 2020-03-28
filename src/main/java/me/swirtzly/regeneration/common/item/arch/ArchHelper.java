package me.swirtzly.regeneration.common.item.arch;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.item.ItemArchInterface;
import me.swirtzly.regeneration.common.item.arch.capability.ArchProvider;
import me.swirtzly.regeneration.common.item.arch.capability.CapabilityArch;
import me.swirtzly.regeneration.common.item.arch.capability.IArch;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

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
            IArch data = CapabilityArch.getForStack(event.getItemStack());
            DnaHandler.IDna trait = DnaHandler.getDnaEntry(data.getSavedTrait());
            ItemArchInterface.readSync(event.getItemStack());
            if (data.getArchStatus() == IArch.ArchStatus.ARCH_ITEM) {
                event.getToolTip().add(new TextComponentTranslation("regeneration.tooltip.arch_trait", new TextComponentTranslation(trait.getLangKey()).getUnformattedComponentText()).getUnformattedComponentText());
                event.getToolTip().add(new TextComponentTranslation("regeneration.tooltip.stored_regens", data.getRegenAmount()).getUnformattedComponentText());
            }
        }
    }

    public static void onArchUse(EntityPlayer player) {
        if (player.world.isRemote) return;

        ItemStack headSlot = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        IItemHandler invData = headSlot.getCapability(ITEM_HANDLER_CAPABILITY, null);
        ItemStack stack = invData.getStackInSlot(0);
        if (!stack.hasCapability(CapabilityArch.CAPABILITY, null)) return;

        IRegeneration playerData = CapabilityRegeneration.getForPlayer(player);

        boolean shouldntContinue = playerData.getState() != PlayerUtil.RegenState.ALIVE && !player.world.isBlockPowered(player.getPosition());

        if (shouldntContinue) return;

        IArch stackData = CapabilityArch.getForStack(stack);

        if (stackData.getArchStatus() == IArch.ArchStatus.NORMAL_ITEM) {
            stackData.setRegenAmount(playerData.getRegenerationsLeft());
            stackData.setSavedTrait(playerData.getDnaType());
            stackData.setArchStatus(IArch.ArchStatus.ARCH_ITEM);
            ItemArchInterface.sync(stack);
            playerData.setDnaType(DnaHandler.DNA_BORING.getRegistryName());
            playerData.extractRegeneration(playerData.getRegenerationsLeft());
            playerData.synchronise();
            player.inventory.add(stack.getCount(), stack.copy());
            stack.setCount(0);
            return;
        }

        if (stackData.getArchStatus() == IArch.ArchStatus.ARCH_ITEM && playerData.getRegenerationsLeft() == 0) {
            playerData.receiveRegenerations(stackData.getRegenAmount());
            playerData.setDnaType(stackData.getSavedTrait());
            playerData.synchronise();
            stackData.setArchStatus(IArch.ArchStatus.NORMAL_ITEM);
            stackData.setRegenAmount(0);
            stackData.setSavedTrait(DnaHandler.DNA_BORING.getRegistryName());
            ItemArchInterface.sync(stack);
            player.inventory.add(stack.getCount(), stack.copy());
            stack.setCount(0);
            return;
        }


    }

}
