package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.client.skin.SkinHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RemoveSkinPlayerMessage {

    private final UUID livingEntity;

    public RemoveSkinPlayerMessage(UUID playerEntity) {
        this.livingEntity = playerEntity;
    }

    public RemoveSkinPlayerMessage(FriendlyByteBuf buffer) {
        livingEntity = buffer.readUUID();
    }

    public static void handle(RemoveSkinPlayerMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().submitAsync(() -> SkinHandler.removePlayerSkin(message.livingEntity));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUUID(livingEntity);
    }

}
