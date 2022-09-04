package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class POVMessage {
    private final String pointOfView;

    public POVMessage(String pointOfView) {
        this.pointOfView = pointOfView;
    }

    public POVMessage(FriendlyByteBuf buffer) {
        pointOfView = buffer.readUtf(32767);
    }

    public static void handle(POVMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().submitAsync(() -> ClientUtil.setPlayerPerspective(message.pointOfView));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.pointOfView);
    }

}

