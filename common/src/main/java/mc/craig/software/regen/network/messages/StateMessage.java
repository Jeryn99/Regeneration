package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageS2C;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class StateMessage extends MessageS2C {

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

    public void handle(MessageContext context) {
        Minecraft.getInstance().submit(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity entity = Minecraft.getInstance().level.getEntity(this.livingEntity);
                if (entity instanceof LivingEntity livingEntity) {
                    RegenerationData.get(livingEntity).ifPresent(iRegen -> ActingForwarder.onClient(ActingForwarder.RegenEvent.valueOf(this.event), iRegen));
                }
            }
        });
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.UPDATE_LOCAL_STATE;
    }

    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeInt(livingEntity);
        packetBuffer.writeUtf(event);
    }

}
