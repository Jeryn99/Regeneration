package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.util.client.ClientUtil;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Suffril on 20/01/2019.
 */
public class PlaySFXMessage {

    private ResourceLocation sound;
    private UUID playerUUID;

    public PlaySFXMessage(ResourceLocation sound, UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.sound = sound;
    }

    public static void encode(PlaySFXMessage message, PacketBuffer buffer) {
        buffer.writeResourceLocation(message.sound);
        buffer.writeUUID(message.playerUUID);
    }

    public static PlaySFXMessage decode(PacketBuffer buffer) {
        return new PlaySFXMessage(buffer.readResourceLocation(), buffer.readUUID());
    }

    public static class Handler {

        public static void handle(PlaySFXMessage message, Supplier<NetworkEvent.Context> ctx) {
            Minecraft.getInstance().submitAsync(() -> {
                PlayerEntity player = Minecraft.getInstance().level.getPlayerByUUID(message.playerUUID);
                if (player != null) {
                    RegenCap.get(player).ifPresent((data) -> ClientUtil.playSound(player, message.sound, SoundCategory.PLAYERS, true, () -> !data.getState().equals(PlayerUtil.RegenState.REGENERATING), 1.0F));
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }

}
