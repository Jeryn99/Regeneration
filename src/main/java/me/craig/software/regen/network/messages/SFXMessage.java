package me.craig.software.regen.network.messages;

import me.craig.software.regen.util.ClientUtil;
import me.craig.software.regen.common.regen.RegenCap;
import me.craig.software.regen.common.regen.state.RegenStates;
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
        playerUUID = buffer.readUUID();
    }

    public static void handle(SFXMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().submitAsync(() -> {
            PlayerEntity player = Minecraft.getInstance().level.getPlayerByUUID(message.playerUUID);
            if (player != null) {
                RegenCap.get(player).ifPresent((data) -> ClientUtil.playSound(player, message.sound, SoundCategory.PLAYERS, true, () -> !data.regenState().equals(RegenStates.REGENERATING), 1.0F));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeResourceLocation(this.sound);
        buffer.writeUUID(this.playerUUID);
    }

}

