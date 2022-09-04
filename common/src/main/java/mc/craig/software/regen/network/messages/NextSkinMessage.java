package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class NextSkinMessage extends MessageC2S {
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

    public void handle(MessageContext context) {
        ServerPlayer serverPlayer = context.getPlayer();
        RegenerationData.get(serverPlayer).ifPresent(iRegen -> {
            iRegen.setNextSkin(this.skinByteArray);
            iRegen.setNextSkinType(this.isAlex);
            iRegen.syncToClients(null);
        });
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.SET_NEXT_SKIN;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeByteArray(this.skinByteArray);
        buffer.writeBoolean(this.isAlex);
    }
}
