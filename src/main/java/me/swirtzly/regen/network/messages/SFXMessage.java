package me.swirtzly.regen.network.messages;

import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SFXMessage {
    private final ResourceLocation sound;
    private final UUID playerUUID;

    public SFXMessage(ResourceLocation sound, UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.sound = sound;
    }

    public SFXMessage(PacketBuffer buffer) {
        sound = buffer.readResourceLocation();
        playerUUID = buffer.readUniqueId();
    }

    public static void handle(SFXMessage message, Supplier< NetworkEvent.Context > ctx) {
        Minecraft.getInstance().deferTask(() -> {
            PlayerEntity player = Minecraft.getInstance().world.getPlayerByUuid(message.playerUUID);
            if (player != null) {
                RegenCap.get(player).ifPresent((data) -> ClientUtil.playSound(player, message.sound, SoundCategory.PLAYERS, true, () -> !data.getCurrentState().equals(RegenStates.REGENERATING), 1.0F));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeResourceLocation(this.sound);
        buffer.writeUniqueId(this.playerUUID);
    }

}

