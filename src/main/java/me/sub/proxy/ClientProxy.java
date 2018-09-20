package me.sub.proxy;

import me.sub.client.RKeyBinds;
import me.sub.client.gui.TabRegeneration;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        super.init();
        RKeyBinds.init();

        //Galacticraft API for TABS
        if (TabRegistry.getTabList().isEmpty()) {
            MinecraftForge.EVENT_BUS.register(new TabRegistry());
            TabRegistry.registerTab(new InventoryTabVanilla());
        }

        TabRegistry.registerTab(new TabRegeneration());

    }

    @Override
    public void postInit() {
        super.postInit();
    }


}
