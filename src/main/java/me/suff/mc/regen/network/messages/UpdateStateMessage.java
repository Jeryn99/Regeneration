package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.handlers.acting.ActingForwarder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateStateMessage {

    private final Entity player;
    private final String event;

    public UpdateStateMessage(Entity player, String event) {
        this.player = player;
        this.event = event;
    }

    public static void encode(UpdateStateMessage event, PacketBuffer packetBuffer) {
        packetBuffer.writeInt(event.player.getId());
        packetBuffer.writeUtf(event.event);
    }

    public static UpdateStateMessage decode(PacketBuffer buffer) {
        return new UpdateStateMessage(Minecraft.getInstance().player.level.getEntity(buffer.readInt()), buffer.readUtf(32767));
    }

    public static class Handler {
        public static void handle(UpdateStateMessage message, Supplier<NetworkEvent.Context> ctx) {
            Minecraft.getInstance().submitAsync(() -> RegenCap.get(message.player).ifPresent((data) -> ActingForwarder.onClient(ActingForwarder.RegenEvent.valueOf(message.event), data)));
            ctx.get().setPacketHandled(true);
        }
    }

}
