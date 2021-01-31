package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class TabRegistry {
    public static ArrayList< AbstractTab > tabList = new ArrayList< AbstractTab >();
    private static Class< ? > clazzJEIConfig = null;
    public static Class< ? > clazzNEIConfig = null;

    static {
        try {
            //Checks for JEI by looking for this class instead of a Loader.isModLoaded() check
            clazzJEIConfig = Class.forName("mezz.jei.config.Config");
        } catch (Exception ignore) {
            //no log spam
        }
        //Only activate NEI feature if NEI is standalone
        if (clazzJEIConfig == null) {
            try {
                clazzNEIConfig = Class.forName("codechicken.nei.NEIClientConfig");
            } catch (Exception ignore) {
                //no log spam
            }
        }
    }

    public static void registerTab(AbstractTab tab) {
        TabRegistry.tabList.add(tab);
    }

    public static ArrayList< AbstractTab > getTabList() {
        return TabRegistry.tabList;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof InventoryScreen) {
            int guiLeft = (event.getGui().width - 176) / 2;
            int guiTop = (event.getGui().height - 166) / 2;

            TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
            for (AbstractTab tab : TabRegistry.tabList) {
                if (tab.shouldAddToList()) {
                    event.addWidget(tab);
                }
            }
        }
    }


    public static void openInventoryGui() {
        Minecraft mc = Minecraft.getInstance();
        mc.player.connection.sendPacket(new CCloseWindowPacket(mc.player.openContainer.windowId));
        InventoryScreen inventory = new InventoryScreen(mc.player);
        mc.displayGuiScreen(inventory);
    }

    public static void updateTabValues(int cornerX, int cornerY, Class< ? > selectedButton) {
        int count = 2;
        for (int i = 0; i < TabRegistry.tabList.size(); i++) {
            AbstractTab t = TabRegistry.tabList.get(i);

            if (t.shouldAddToList()) {
                t.id = count;
                t.x = cornerX + (count - 2) * 28;
                t.y = cornerY - 28;
                t.active = !t.getClass().equals(selectedButton);
                count++;
            }
        }
    }


}
