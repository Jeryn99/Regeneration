package me.swirtzly.regeneration.network.messages;

import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by Craig on 16/09/2018.
 */
public class SyncRegenDataToClientMessage {

    private final Entity player;
    private final CompoundNBT data;

    public SyncRegenDataToClientMessage(Entity player, CompoundNBT data) {
        this.player = player;
        this.data = data;
    }

    public static void encode(SyncRegenDataToClientMessage message, PacketBuffer buffer) {
        buffer.writeInt(message.player.getId());
        buffer.writeNbt(message.data);
    }

    public static SyncRegenDataToClientMessage decode(PacketBuffer packetBuffer) {
        return new SyncRegenDataToClientMessage(Minecraft.getInstance().player.level.getEntity(packetBuffer.readInt()), packetBuffer.readNbt());
    }

    public static class Handler {
        public static void handle(SyncRegenDataToClientMessage message, Supplier<NetworkEvent.Context> ctx) {
            Entity player = message.player;
            if (player != null)
                Minecraft.getInstance().submitAsync(() -> RegenCap.get(player).ifPresent((data) -> data.deserializeNBT(message.data)));
            ctx.get().setPacketHandled(true);
        }
    }

}
