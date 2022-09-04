package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.util.RegenSources;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForceRegenMessage {

    public ForceRegenMessage() {
    }

    public ForceRegenMessage(FriendlyByteBuf buffer) {
    }

    public static void handle(ForceRegenMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getSender().getServer().submitAsync(() -> RegenerationData.get(ctx.get().getSender()).ifPresent((cap) -> {
            if (cap.regenState() == RegenStates.ALIVE || cap.regenState().isGraceful()) {
                if (cap.canRegenerate()) {
                    cap.getLiving().hurt(RegenSources.REGEN_DMG_FORCED, Integer.MAX_VALUE);
                }
            }
        }));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

}
