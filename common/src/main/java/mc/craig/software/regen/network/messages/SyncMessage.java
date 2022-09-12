package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageS2C;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class SyncMessage extends MessageS2C {
    public int entityID;
    public CompoundTag nbt;

    public SyncMessage(int entityID, CompoundTag nbt) {
        this.entityID = entityID;
        this.nbt = nbt;
    }

    public SyncMessage(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.nbt = buf.readNbt();
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.SYNC_CAP;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeNbt(this.nbt);
    }

    @Override
    public void handle(MessageContext context) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        Entity entity = level.getEntity(this.entityID);
        if (entity != null)
            RegenerationData.get((LivingEntity) entity).ifPresent((c) -> c.deserializeNBT(this.nbt));
    }
}