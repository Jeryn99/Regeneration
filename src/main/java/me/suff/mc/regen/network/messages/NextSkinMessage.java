package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.regen.RegenCap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class NextSkinMessage {
    private final byte[] skinByteArray;
    private final boolean isAlex;

    public NextSkinMessage(byte[] skinByteArray, boolean isAlex) {
        this.skinByteArray = skinByteArray;
        this.isAlex = isAlex;
    }

    public NextSkinMessage(FriendlyByteBuf buffer) {
        skinByteArray = buffer.readByteArray(Integer.MAX_VALUE);
        isAlex = buffer.readBoolean();
    }

    public static void handle(NextSkinMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.get().getSender();
            RegenCap.get(serverPlayer).ifPresent(iRegen -> {
                iRegen.setNextSkin(message.skinByteArray);
                iRegen.setNextSkinType(message.isAlex);
                iRegen.syncToClients(null);
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeByteArray(this.skinByteArray);
        buffer.writeBoolean(this.isAlex);
    }
}
