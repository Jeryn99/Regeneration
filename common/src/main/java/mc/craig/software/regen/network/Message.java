package mc.craig.software.regen.network;

import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;


public abstract class Message {

    @NotNull
    public abstract MessageType getType();

    public abstract void toBytes(FriendlyByteBuf buf);

    public abstract void handle(MessageContext context);
}
