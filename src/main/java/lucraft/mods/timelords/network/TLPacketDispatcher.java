package lucraft.mods.timelords.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class TLPacketDispatcher {
  private static byte packetId = 0;
  
  private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel("timelords");
  
  public static final void registerPackets() {
    registerMessage(MessageSyncPlayerData.Handler.class, MessageSyncPlayerData.class, Side.CLIENT);
    registerMessage(MessageSpawnParticle.Handler.class, MessageSpawnParticle.class, Side.CLIENT);
  }
  
  private static final void registerMessage(Class handlerClass, Class messageClass, Side side) {
    packetId = (byte)(packetId + 1);
    dispatcher.registerMessage(handlerClass, messageClass, packetId, side);
  }
  
  public static final void sendTo(IMessage message, EntityPlayerMP player) {
    dispatcher.sendTo(message, player);
  }
  
  public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
    dispatcher.sendToAllAround(message, point);
  }
  
  public static final void sendToAll(IMessage message) {
    dispatcher.sendToAll(message);
  }
  
  public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
    sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
  }
  
  public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
    sendToAllAround(message, player.worldObj.provider.getDimensionId(), player.posX, player.posY, player.posZ, range);
  }
  
  public static final void sendToDimension(IMessage message, int dimensionId) {
    dispatcher.sendToDimension(message, dimensionId);
  }
  
  public static final void sendToServer(IMessage message) {
    dispatcher.sendToServer(message);
  }
}
