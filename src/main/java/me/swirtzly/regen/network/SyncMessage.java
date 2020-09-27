package me.swirtzly.regen.network;

import me.swirtzly.regen.common.regen.RegenCap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncMessage {
    public int entityID;
    public CompoundNBT nbt;

    public SyncMessage(int entityID, CompoundNBT nbt) {
        this.entityID = entityID;
        this.nbt = nbt;
    }

    public SyncMessage(PacketBuffer buf) {
        this.entityID = buf.readInt();
        this.nbt = buf.readCompoundTag();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.entityID);
        buf.writeCompoundTag(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        Entity entity = Minecraft.getInstance().world.getEntityByID(this.entityID);

        ctx.get().enqueueWork(() -> {
            if (entity != null)
                RegenCap.get((LivingEntity) entity).ifPresent((c) -> c.deserializeNBT(this.nbt));
        });
        ctx.get().setPacketHandled(true);
    }
}