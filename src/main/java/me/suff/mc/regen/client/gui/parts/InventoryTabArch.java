package me.suff.mc.regen.client.gui.parts;

import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.common.item.ItemArchInterface;
import me.suff.mc.regen.network.MessageOpenArch;
import me.suff.mc.regen.network.NetworkHandler;
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
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemArchInterface) {
            NetworkHandler.INSTANCE.sendToServer(new MessageOpenArch());
        }
    }

    @Override
    public boolean shouldAddToList() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        return player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemArchInterface;
    }
}
