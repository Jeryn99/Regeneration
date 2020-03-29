package lucraft.mods.timelords.proxies;

import lucraft.mods.timelords.TimelordsConfig;
import lucraft.mods.timelords.network.TLPacketDispatcher;
import lucraft.mods.timelords.util.TLCommonEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TimelordsProxy {
  public void preInit(FMLPreInitializationEvent event) {
    TimelordsConfig.preInit(event);
  }
  
  public void init(FMLInitializationEvent event) {
    TLPacketDispatcher.registerPackets();
    MinecraftForge.EVENT_BUS.register(new TLCommonEventHandler());
  }
  
  public void postInit(FMLPostInitializationEvent event) {
    TimelordsConfig.postInit();
  }
  
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    return (EntityPlayer)(ctx.getServerHandler()).playerEntity;
  }
}
