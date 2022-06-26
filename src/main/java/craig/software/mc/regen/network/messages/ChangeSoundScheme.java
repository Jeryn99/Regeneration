package craig.software.mc.regen.network.messages;

import craig.software.mc.regen.common.regen.IRegen;
import craig.software.mc.regen.common.regen.RegenCap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/* Created by Craig on 29/01/2021 */
public class ChangeSoundScheme {

    private final String type;

    public ChangeSoundScheme(IRegen.TimelordSound type) {
        this.type = type.name();
    }

    public ChangeSoundScheme(FriendlyByteBuf buffer) {
        type = buffer.readUtf(32767);
    }

    public static void handle(ChangeSoundScheme message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> RegenCap.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.setTimelordSound(IRegen.TimelordSound.valueOf(message.type));
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.type);
    }
}