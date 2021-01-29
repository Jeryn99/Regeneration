package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class TabRegistry
{
	public static ArrayList<AbstractTab> tabList = new ArrayList<AbstractTab>();
	private static Class<?> clazzJEIConfig = null;
    public static Class<?> clazzNEIConfig = null;
	
	static
	{
	    try 
	    {
	        //Checks for JEI by looking for this class instead of a Loader.isModLoaded() check
	        clazzJEIConfig = Class.forName("mezz.jei.config.Config");
	    }
	    catch (Exception ignore)
	    {
	        //no log spam
	    }
	    //Only activate NEI feature if NEI is standalone
	    if (clazzJEIConfig == null)
	    {
	        try 
	        {
	            clazzNEIConfig = Class.forName("codechicken.nei.NEIClientConfig");
	        }
	        catch (Exception ignore)
	        {
	            //no log spam
	        }
	    }
	}

	public static void registerTab(AbstractTab tab)
	{
		TabRegistry.tabList.add(tab);
	}

	public static ArrayList<AbstractTab> getTabList()
	{
		return TabRegistry.tabList;
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void guiPostInit (GuiScreenEvent.InitGuiEvent.Post event)
	{
		if (event.getGui() instanceof InventoryScreen)
		{
			int guiLeft = (event.getGui().width - 176) / 2;
			int guiTop = (event.getGui().height - 166) / 2;
			guiLeft += getPotionOffset();

			TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
			for (AbstractTab tab : TabRegistry.tabList)
			{
				if (tab.shouldAddToList())
				{
					event.addWidget(tab);
				}
			}
		}
	}

	private static boolean initWithPotion;

	public static void openInventoryGui()
	{
		Minecraft mc = Minecraft.getInstance();
		mc.player.connection.sendPacket(new CCloseWindowPacket(mc.player.openContainer.windowId));
		InventoryScreen inventory = new InventoryScreen(mc.player);
		mc.displayGuiScreen(inventory);
	}

	public static void updateTabValues(int cornerX, int cornerY, Class<?> selectedButton)
	{
		int count = 2;
		for (int i = 0; i < TabRegistry.tabList.size(); i++)
		{
			AbstractTab t = TabRegistry.tabList.get(i);

			if (t.shouldAddToList())
			{
				t.id = count;
				t.x = cornerX + (count - 2) * 28;
				t.y = cornerY - 28;
				t.active = !t.getClass().equals(selectedButton);
				t.potionOffsetLast = getPotionOffsetNEI();
				count++;
			}
		}
	}
	
	public static int getPotionOffset()
	{
		// If at least one potion is active...
		if (!Minecraft.getInstance().player.getActivePotionEffects().isEmpty())
		{
			initWithPotion = true;
			return 60 + getPotionOffsetJEI() + getPotionOffsetNEI();
		}
		
		// No potions, no offset needed
		initWithPotion = false;
		return 0;
	}

    public static int getPotionOffsetJEI()
    {
        if (clazzJEIConfig != null)
        {
            try 
            {
                Object enabled = clazzJEIConfig.getMethod("isOverlayEnabled").invoke(null);
                if (enabled instanceof Boolean)
                {
                    if (!((Boolean)enabled))
                    {
                        // If JEI is disabled, no special change to getPotionOffset()
                        return 0;
                    }
                    //Active JEI undoes the standard potion offset (they listen for ScreenEvent.PotionShiftEvent)
                    return -60;
                }
            } 
            catch (Exception ignore) 
            {
                //no log spam
            }
        }
        return 0;
    }

	public static int getPotionOffsetNEI()
	{
		if (initWithPotion && clazzNEIConfig != null)
		{
		    try 
		    {
		        // Check whether NEI is hidden and enabled
		        Object hidden = clazzNEIConfig.getMethod("isHidden").invoke(null);
		        Object enabled = clazzNEIConfig.getMethod("isEnabled").invoke(null);
		        if (hidden instanceof Boolean && enabled instanceof Boolean)
		        {
		            if ((Boolean)hidden || !((Boolean)enabled))
		            {
		                // If NEI is disabled or hidden, it does not affect the tabs offset with potions 
		                return 0;
		            }
		            //But active NEI undoes the standard potion offset
		            return -60;
		        }
		    } 
		    catch (Exception ignore) 
		    {
		        //no log spam
		    }
		}
		//No NEI, no change
		return 0;
	}
}
