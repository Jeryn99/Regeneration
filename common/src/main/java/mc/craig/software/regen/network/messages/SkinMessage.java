package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.samo_lego.fabrictailor.command.SkinCommand;
import org.samo_lego.fabrictailor.util.SkinFetcher;

public class SkinMessage extends MessageC2S {

    private final String skinFilePath;
    private final boolean isAlex;

    public SkinMessage(String skinByteArray, boolean isAlex) {
        this.skinFilePath = skinByteArray;
        this.isAlex = isAlex;
    }

    public SkinMessage(FriendlyByteBuf buffer) {
        skinFilePath = buffer.readUtf();
        isAlex = buffer.readBoolean();
    }

    public void handle(MessageContext context) {
            ServerPlayer serverPlayer = context.getPlayer();
            RegenerationData.get(serverPlayer).ifPresent(iRegen -> {

                if(skinFilePath.contains("reset")){
                    SkinCommand.clearSkin(serverPlayer);
                    iRegen.syncToClients(null);
                    return;
                }

                iRegen.setHasSetSkin(true);
                SkinCommand.setSkin(serverPlayer, () -> SkinFetcher.setSkinFromFile(skinFilePath, isAlex));

                iRegen.syncToClients(null);

            });
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.UPLOAD_SKIN;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.skinFilePath);
        buffer.writeBoolean(this.isAlex);
    }
}
