package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.client.animation.AnimationHandler;
import me.suff.mc.regen.client.skin.SkinHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class RemoveSkinPlayerMessage {

    private final UUID livingEntity;

    public RemoveSkinPlayerMessage(UUID playerEntity) {
        this.livingEntity = playerEntity;
    }

    public RemoveSkinPlayerMessage(PacketBuffer buffer) {
        livingEntity = buffer.readUniqueId();
    }

    public static void handle(RemoveSkinPlayerMessage message, Supplier< NetworkEvent.Context > ctx) {
        Minecraft.getInstance().deferTask(() -> {
            SkinHandler.removePlayerSkin(message.livingEntity);
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer packetBuffer) {
        packetBuffer.writeUniqueId(livingEntity);
    }

}
