package craig.software.mc.regen.network.messages;

import craig.software.mc.regen.common.regen.RegenCap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SkinMessage {

    private final byte[] skinByteArray;
    private final boolean isAlex;

    public SkinMessage(byte[] skinByteArray, boolean isAlex) {
        this.skinByteArray = skinByteArray;
        this.isAlex = isAlex;
    }

    public SkinMessage(FriendlyByteBuf buffer) {
        skinByteArray = buffer.readByteArray(Integer.MAX_VALUE);
        isAlex = buffer.readBoolean();
    }

    public static void handle(SkinMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.get().getSender();
            RegenCap.get(serverPlayer).ifPresent(iRegen -> {
                iRegen.setSkin(message.skinByteArray);
                iRegen.setAlexSkin(message.isAlex);
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
