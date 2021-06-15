package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.client.rendering.entity.TimelordRenderer;
import me.suff.mc.regen.common.entities.TimelordEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RemoveTimelordSkinMessage {

    private final UUID livingEntity;

    public RemoveTimelordSkinMessage(TimelordEntity livingEntity) {
        this.livingEntity = livingEntity.getUUID();
    }

    public RemoveTimelordSkinMessage(PacketBuffer buffer) {
        livingEntity = buffer.readUUID();
    }

    public static void handle(RemoveTimelordSkinMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().submitAsync(() -> {
            TimelordRenderer.TIMELORDS.remove(message.livingEntity);
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer packetBuffer) {
        packetBuffer.writeUUID(livingEntity);
    }

}
