package lucraft.mods.timelords.util;

import lucraft.mods.timelords.TimelordsConfig;
import lucraft.mods.timelords.entity.TimelordPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class TLCommonEventHandler {
  @SubscribeEvent
  public void onClonePlayer(PlayerEvent.Clone event) {
    NBTTagCompound compound = new NBTTagCompound();
    TimelordPlayerData.get(event.original).saveNBTData(compound);
    TimelordPlayerData.get(event.entityPlayer).loadNBTData(compound);
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onEntityConstructing(EntityEvent.EntityConstructing event) {
    if (event.entity instanceof EntityPlayer && TimelordPlayerData.get((EntityPlayer)event.entity) == null)
      TimelordPlayerData.register((EntityPlayer)event.entity); 
    if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties("Timelords") == null)
      event.entity.registerExtendedProperties("Timelords", (IExtendedEntityProperties)new TimelordPlayerData((EntityPlayer)event.entity)); 
  }
  
  @SubscribeEvent
  public void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent e) {
    TimelordPlayerData.get(e.player).sendDataToPlayer();
  }
  
  @SubscribeEvent
  public void onUpdate(LivingEvent.LivingUpdateEvent e) {
    if (e.entityLiving instanceof EntityPlayer)
      TimelordPlayerData.get((EntityPlayer)e.entityLiving).onUpdate(); 
  }
  
  @SubscribeEvent
  public void onPlayerDeath(LivingDeathEvent e) {
    if (e.entityLiving instanceof EntityPlayer)
      if (TimelordPlayerData.get((EntityPlayer)e.entityLiving).getRegenerations() > 0 && !TimelordPlayerData.get((EntityPlayer)e.entityLiving).isRegenerating()) {
        e.setCanceled(true);
        e.entityLiving.setHealth(3.0F);
        TimelordPlayerData.get((EntityPlayer)e.entityLiving).startRegeneration();
      } else {
        TimelordPlayerData.get((EntityPlayer)e.entityLiving).setRegenerations(TimelordsConfig.defaultRegenerations);
      }  
  }
  
  @SubscribeEvent
  public void onStartTracking(PlayerEvent.StartTracking e) {
    if (e.target instanceof EntityPlayer)
      TimelordPlayerData.get((EntityPlayer)e.target).sendDataToPlayer(e.entityPlayer); 
  }
}
