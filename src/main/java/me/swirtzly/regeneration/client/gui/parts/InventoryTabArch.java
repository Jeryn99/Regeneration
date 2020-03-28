package me.swirtzly.regeneration.client.gui.parts;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.item.ItemArchInterface;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.network.MessageOpenArch;
import me.swirtzly.regeneration.network.NetworkHandler;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Created by Swirtzly
 * on 28/03/2020 @ 15:40
 */
public class InventoryTabArch extends AbstractTab {

    public InventoryTabArch() {
        super(0, 0, 0, new ItemStack(RegenObjects.Items.ARCH));
    }

    @Override
    public void onTabClicked() {
        NetworkHandler.INSTANCE.sendToServer(new MessageOpenArch());
    }

    @Override
    public boolean shouldAddToList() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        return player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemArchInterface;
    }
}
