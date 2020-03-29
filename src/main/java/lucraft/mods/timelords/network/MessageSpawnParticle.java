package lucraft.mods.timelords.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSpawnParticle implements IMessage {
  private EnumParticleTypes type;
  
  private double xCoord;
  
  private double yCoord;
  
  private double zCoord;
  
  private double xOffset;
  
  private double yOffset;
  
  private double zOffset;
  
  public MessageSpawnParticle() {}
  
  public MessageSpawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset) {
    this.type = particleType;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.zCoord = zCoord;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.zOffset = zOffset;
  }
  
  public void fromBytes(ByteBuf buf) {
    this.type = EnumParticleTypes.getParticleFromId(buf.readInt());
    this.xCoord = buf.readDouble();
    this.yCoord = buf.readDouble();
    this.zCoord = buf.readDouble();
    this.xOffset = buf.readDouble();
    this.yOffset = buf.readDouble();
    this.zOffset = buf.readDouble();
  }
  
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.type.getParticleID());
    buf.writeDouble(this.xCoord);
    buf.writeDouble(this.yCoord);
    buf.writeDouble(this.zCoord);
    buf.writeDouble(this.xOffset);
    buf.writeDouble(this.yOffset);
    buf.writeDouble(this.zOffset);
  }
  
  public static class Handler extends AbstractClientMessageHandler<MessageSpawnParticle> {
    public IMessage handleClientMessage(EntityPlayer player, MessageSpawnParticle message, MessageContext ctx) {
      if (player != null && message != null && ctx != null)
        player.worldObj.spawnParticle(message.type, message.xCoord, message.yCoord, message.zCoord, message.xOffset, message.yOffset, message.zOffset, new int[0]); 
      return null;
    }
  }
}
