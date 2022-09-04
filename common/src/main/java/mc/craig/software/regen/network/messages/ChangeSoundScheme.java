package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

/* Created by Craig on 29/01/2021 */
public class ChangeSoundScheme extends MessageC2S {

    private final String type;

    public ChangeSoundScheme(IRegen.TimelordSound type) {
        this.type = type.name();
    }

    public ChangeSoundScheme(FriendlyByteBuf buffer) {
        type = buffer.readUtf(32767);
    }

    @Override
    public void handle(MessageContext context) {
        context.getPlayer().getServer().submit(() -> RegenerationData.get(context.getPlayer()).ifPresent((cap) -> {
            cap.setTimelordSound(IRegen.TimelordSound.valueOf(this.type));
            cap.syncToClients(null);
        }));
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.CHANGE_SOUNDSCHEME;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.type);
    }

}