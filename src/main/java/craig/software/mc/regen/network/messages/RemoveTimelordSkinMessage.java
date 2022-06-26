package craig.software.mc.regen.network.messages;

import craig.software.mc.regen.client.rendering.entity.TimelordRenderer;
import craig.software.mc.regen.common.entities.Timelord;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RemoveTimelordSkinMessage {

    private final UUID livingEntity;

    public RemoveTimelordSkinMessage(Timelord livingEntity) {
        this.livingEntity = livingEntity.getUUID();
    }

    public RemoveTimelordSkinMessage(FriendlyByteBuf buffer) {
        livingEntity = buffer.readUUID();
    }

    public static void handle(RemoveTimelordSkinMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().submitAsync(() -> TimelordRenderer.TIMELORDS.remove(message.livingEntity));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUUID(livingEntity);
    }

}
