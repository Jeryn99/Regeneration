package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.RegenConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Maybe a bit overkill, but at least it'll be stable & clear
 */
public class ThirdPersonMessage {

    private boolean thirdPerson;

    public ThirdPersonMessage(boolean thirdPerson) {
        this.thirdPerson = thirdPerson;
    }

    public static void encode(ThirdPersonMessage thirdPersonMessage, PacketBuffer buffer) {
        buffer.writeBoolean(thirdPersonMessage.thirdPerson);
    }

    public static ThirdPersonMessage decode(PacketBuffer buffer) {
        return new ThirdPersonMessage(buffer.readBoolean());
    }

    public static class Handler {

        public static void handle(ThirdPersonMessage message, Supplier<NetworkEvent.Context> ctx) {
            Minecraft.getInstance().submitAsync(() -> {
                if (RegenConfig.CLIENT.changePerspective.get()) {
                    Minecraft.getInstance().options.thirdPersonView = message.thirdPerson ? 2 : 0;
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
