package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.regen.RegenCap;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncMessage {
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

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        Entity entity = Minecraft.getInstance().level.getEntity(this.entityID);

        ctx.get().enqueueWork(() -> {
            if (entity != null)
                RegenCap.get((LivingEntity) entity).ifPresent((c) -> c.deserializeNBT(this.nbt));
        });
        ctx.get().setPacketHandled(true);
    }
}