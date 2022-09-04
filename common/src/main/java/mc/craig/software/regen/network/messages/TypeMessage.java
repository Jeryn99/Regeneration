package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class TypeMessage extends MessageC2S {

    private final String type;

    public TypeMessage(TransitionType type) {
        this.type = TransitionTypes.getTransitionId(type).toString();
    }

    public TypeMessage(FriendlyByteBuf buffer) {
        type = buffer.readUtf(32767);
    }

    public void handle(MessageContext context) {
       /* context.getPlayer().getServer().submit(() -> RegenerationData.get(context.getPlayer()).ifPresent((cap) -> {
            cap.setTransitionType(TransitionTypes.TRANSITION_TYPES_REGISTRY.get().getValue(new ResourceLocation(this.type)));
            cap.syncToClients(null);
        }));*/ //TODO
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.TRANSITION_TYPE;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.type);
    }
}