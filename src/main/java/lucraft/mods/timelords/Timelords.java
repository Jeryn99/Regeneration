package lucraft.mods.timelords;

import lucraft.mods.timelords.commands.CommandSetRegenerations;
import lucraft.mods.timelords.commands.CommandTriggerRegeneration;
import lucraft.mods.timelords.proxies.TimelordsProxy;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "timelords", version = "1.8-1.0.0", name = "Timelords", acceptedMinecraftVersions = "[1.8]")
public class Timelords {
  public static final String MODID = "timelords";
  
  public static final String NAME = "Timelords";
  
  public static final String VERSION = "1.8-1.0.0";
  
  @Instance("timelords")
  public static Timelords instance;
  
  @SidedProxy(clientSide = "lucraft.mods.timelords.proxies.TimelordsClientProxy", serverSide = "lucraft.mods.timelords.proxies.TimelordsProxy")
  public static TimelordsProxy proxy;
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    proxy.preInit(event);
  }
  
  @EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
  }
  
  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }
  
  @EventHandler
  public void init(FMLServerStartingEvent e) {
    e.registerServerCommand((ICommand)new CommandSetRegenerations());
    e.registerServerCommand((ICommand)new CommandTriggerRegeneration());
  }
}
