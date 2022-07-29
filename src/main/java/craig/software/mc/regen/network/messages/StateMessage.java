package craig.software.mc.regen.network.messages;

import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.acting.ActingForwarder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StateMessage {

    private final int livingEntity;
    private final String event;

    public StateMessage(LivingEntity livingEntity, ActingForwarder.RegenEvent event) {
        this.livingEntity = livingEntity.getId();
        this.event = event.name();
    }

    public StateMessage(FriendlyByteBuf buffer) {
        livingEntity = buffer.readInt();
        event = buffer.readUtf(32767);
    }

    public static void handle(StateMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().submitAsync(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(message.livingEntity);
                if (entity instanceof LivingEntity livingEntity) {
                    RegenCap.get(livingEntity).ifPresent(iRegen -> ActingForwarder.onClient(ActingForwarder.RegenEvent.valueOf(message.event), iRegen));
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(livingEntity);
        packetBuffer.writeUtf(event);
    }

}
