package craig.software.mc.regen.network.messages;

import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.transitions.TransitionType;
import craig.software.mc.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TypeMessage {

    private final String type;

    public TypeMessage(TransitionType type) {
        this.type = TransitionTypes.getTransitionId(type).toString();
    }

    public TypeMessage(FriendlyByteBuf buffer) {
        type = buffer.readUtf(32767);
    }

    public static void handle(TypeMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.setTransitionType(TransitionTypes.TRANSITION_TYPES_REGISTRY.get().getValue(new ResourceLocation(message.type)));
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.type);
    }
}