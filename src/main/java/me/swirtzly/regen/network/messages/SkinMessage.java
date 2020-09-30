package me.swirtzly.regen.network.messages;

import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SkinMessage {
    private byte[] skinByteArray;
    private UUID playerUUID;

    public SkinMessage(PlayerEntity playerEntity, byte[] skinByteArray) {
        this.skinByteArray = skinByteArray;
        this.playerUUID = playerEntity.getUniqueID();
    }

    public SkinMessage(PacketBuffer buffer) {
        skinByteArray = buffer.readByteArray(Integer.MAX_VALUE);
        playerUUID = buffer.readUniqueId();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeByteArray(this.skinByteArray);
        buffer.writeUniqueId(this.playerUUID);
    }

    public static void handle(SkinMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            RegenCap.get(serverPlayer).ifPresent(iRegen -> {
                iRegen.setSkin(message.skinByteArray);
                iRegen.syncToClients(null);
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
