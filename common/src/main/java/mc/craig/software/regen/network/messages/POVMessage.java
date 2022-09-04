package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageS2C;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class POVMessage extends MessageS2C {
    private final String pointOfView;

    public POVMessage(String pointOfView) {
        this.pointOfView = pointOfView;
    }

    public POVMessage(FriendlyByteBuf buffer) {
        pointOfView = buffer.readUtf(32767);
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.UPDATE_POV;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.pointOfView);
    }

    @Override
    public void handle(MessageContext context) {
        ClientUtil.setPlayerPerspective(this.pointOfView);
    }

}

