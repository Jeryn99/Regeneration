package craig.software.mc.regen.network.messages;

import craig.software.mc.regen.common.regen.RegenCap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleTraitMessage {

    public ToggleTraitMessage() {
    }

    public ToggleTraitMessage(FriendlyByteBuf buffer) {
    }

    public static void handle(ToggleTraitMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.toggleTrait();
            if (cap.traitActive()) {
                cap.trait().apply(cap);
            } else {
                cap.trait().remove(cap);
            }
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

}
