package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
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
        ctx.get().getSender().getServer().submitAsync(() -> RegenerationData.get(ctx.get().getSender()).ifPresent((cap) -> {
            cap.setTimelordSound(IRegen.TimelordSound.valueOf(message.type));
            cap.syncToClients(null);
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.type);
    }
}