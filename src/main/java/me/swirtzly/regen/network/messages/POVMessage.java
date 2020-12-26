package me.swirtzly.regen.network.messages;

import me.swirtzly.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class POVMessage {
    private final String pointOfView;

    public POVMessage(String pointOfView) {
        this.pointOfView = pointOfView;
    }

    public POVMessage(PacketBuffer buffer) {
        pointOfView = buffer.readString(32767);
    }

    public static void handle(POVMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().deferTask(() -> ClientUtil.setPlayerPerspective(message.pointOfView));
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.pointOfView);
    }

}

