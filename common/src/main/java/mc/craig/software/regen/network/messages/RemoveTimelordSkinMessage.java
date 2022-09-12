package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.client.rendering.entity.TimelordRenderer;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageS2C;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RemoveTimelordSkinMessage extends MessageS2C {

    private final UUID livingEntity;

    public RemoveTimelordSkinMessage(Timelord livingEntity) {
        this.livingEntity = livingEntity.getUUID();
    }

    public RemoveTimelordSkinMessage(FriendlyByteBuf buffer) {
        livingEntity = buffer.readUUID();
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.REMOVE_LOCAL_TIMELORD_SKIN;
    }

    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUUID(livingEntity);
    }

    @Override
    public void handle(MessageContext context) {
        Minecraft.getInstance().submit(() -> TimelordRenderer.TIMELORDS.remove(this.livingEntity));

    }

}
