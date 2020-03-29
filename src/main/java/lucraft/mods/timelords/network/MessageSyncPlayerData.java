package lucraft.mods.timelords.network;

import io.netty.buffer.ByteBuf;
import lucraft.mods.timelords.entity.TimelordPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSyncPlayerData implements IMessage {
  private int entityId;
  
  private NBTTagCompound data;
  
  public MessageSyncPlayerData() {}
  
  public MessageSyncPlayerData(EntityPlayer player) {
    this.entityId = player.getEntityId();
    this.data = new NBTTagCompound();
    TimelordPlayerData.get(player).saveNBTData(this.data);
  }
  
  public void fromBytes(ByteBuf buf) {
    this.entityId = buf.readInt();
    this.data = ByteBufUtils.readTag(buf);
  }
  
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.entityId);
    ByteBufUtils.writeTag(buf, this.data);
  }
  
  public static class Handler extends AbstractClientMessageHandler<MessageSyncPlayerData> {
    public IMessage handleClientMessage(EntityPlayer player, MessageSyncPlayerData message, MessageContext ctx) {
      if (player != null && message != null && ctx != null) {
        EntityPlayer en = (EntityPlayer)player.worldObj.getEntityByID(message.entityId);
        if (en != null)
          TimelordPlayerData.get(en).loadNBTData(message.data); 
      } 
      return null;
    }
  }
}
