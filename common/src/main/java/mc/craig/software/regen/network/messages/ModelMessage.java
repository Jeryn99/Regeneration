package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import mc.craig.software.regen.util.PlayerUtil;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class ModelMessage extends MessageC2S {

    private final String type;

    public ModelMessage(PlayerUtil.SkinType type) {
        this.type = type.name();
    }

    public ModelMessage(FriendlyByteBuf buffer) {
        type = buffer.readUtf(32767);
    }

    public void handle(MessageContext context) {
        RegenerationData.get(context.getPlayer()).ifPresent((cap) -> {
            cap.setPreferredModel(PlayerUtil.SkinType.valueOf(this.type));
            cap.syncToClients(null);
        });
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.CHANGE_MODEL;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.type);
    }
}