package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.client.visual.VisualManipulator;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageS2C;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RemoveSkinPlayerMessage extends MessageS2C {

    private final UUID livingEntity;

    public RemoveSkinPlayerMessage(UUID playerEntity) {
        this.livingEntity = playerEntity;
    }

    public RemoveSkinPlayerMessage(FriendlyByteBuf buffer) {
        livingEntity = buffer.readUUID();
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.REMOVE_LOCAL_SKIN;
    }

    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUUID(livingEntity);
    }

    @Override
    public void handle(MessageContext context) {
        VisualManipulator.clearPlayersSkin(this.livingEntity);
    }

}
