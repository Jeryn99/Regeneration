package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class ColorChangeMessage extends MessageC2S {
    private final CompoundTag style;

    public ColorChangeMessage(CompoundTag style) {
        this.style = style;
    }

    public ColorChangeMessage(FriendlyByteBuf buffer) {
        style = buffer.readNbt();
    }

    public void handle(MessageContext context) {
        RegenerationData.get(context.getPlayer()).ifPresent(regenerationData -> {
            regenerationData.readStyle(this.style);
            regenerationData.syncToClients(null);
        });
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.COLOR_CHANGE;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.style);
    }

}
