package me.swirtzly.regen.network.messages;

import me.swirtzly.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class POVMessage {
    private String pointOfView;

    public POVMessage(PointOfView pointOfView) {
        this.pointOfView = pointOfView.name();
    }

    public POVMessage(PacketBuffer buffer) {
        pointOfView = buffer.readString();
    }

    public static void handle(POVMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().deferTask(() -> {
            ClientUtil.setPlayerPerspective(PointOfView.valueOf(message.pointOfView));
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.pointOfView);
    }

}

