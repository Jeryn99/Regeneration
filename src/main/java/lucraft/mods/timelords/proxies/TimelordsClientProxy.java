package lucraft.mods.timelords.proxies;

import java.util.Map;
import lucraft.mods.timelords.TimelordsConfig;
import lucraft.mods.timelords.client.render.LayerRendererTimelord;
import lucraft.mods.timelords.client.render.SkinFile;
import lucraft.mods.timelords.client.render.TLRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TimelordsClientProxy extends TimelordsProxy {
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);
  }
  
  public void init(FMLInitializationEvent event) {
    super.init(event);
    MinecraftForge.EVENT_BUS.register(new TLRenderer());
    Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
    RenderPlayer render = skinMap.get("default");
    render.addLayer((LayerRenderer)new LayerRendererTimelord(render));
    render = skinMap.get("slim");
    render.addLayer((LayerRenderer)new LayerRendererTimelord(render));
  }
  
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
    for (SkinFile file : TimelordsConfig.skins)
      file.init(); 
  }
  
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    return ctx.side.isClient() ? (EntityPlayer)(Minecraft.getMinecraft()).thePlayer : super.getPlayerEntity(ctx);
  }
}
