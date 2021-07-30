package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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

