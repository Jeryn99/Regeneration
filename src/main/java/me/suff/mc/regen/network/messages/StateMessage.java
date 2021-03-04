package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.acting.ActingForwarder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StateMessage {

    private final int livingEntity;
    private final String event;

    public StateMessage(LivingEntity livingEntity, ActingForwarder.RegenEvent event) {
        this.livingEntity = livingEntity.getEntityId();
        this.event = event.name();
    }

    public StateMessage(PacketBuffer buffer) {
        livingEntity = buffer.readInt();
        event = buffer.readString(32767);
    }

    public static void handle(StateMessage message, Supplier< NetworkEvent.Context > ctx) {
        Minecraft.getInstance().deferTask(() -> {

            Entity entity = Minecraft.getInstance().world.getEntityByID(message.livingEntity);
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                RegenCap.get(livingEntity).ifPresent(iRegen -> {
                    ActingForwarder.onClient(ActingForwarder.RegenEvent.valueOf(message.event), iRegen);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(livingEntity);
        packetBuffer.writeString(event);
    }

}
