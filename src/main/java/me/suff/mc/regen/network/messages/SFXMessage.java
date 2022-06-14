package me.suff.mc.regen.network.messages;

import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SFXMessage {
    private final ResourceLocation sound;
    private final int playerUUID;

    public SFXMessage(ResourceLocation sound, int playerUUID) {
        this.playerUUID = playerUUID;
        this.sound = sound;
    }

    public SFXMessage(FriendlyByteBuf buffer) {
        sound = buffer.readResourceLocation();
        playerUUID = buffer.readInt();
    }

    public static void handle(SFXMessage message, Supplier<NetworkEvent.Context> ctx) {
        Minecraft.getInstance().submitAsync(() -> {
            Entity player = Minecraft.getInstance().level.getEntity(message.playerUUID);
            if (player != null) {
                RegenCap.get((LivingEntity) player).ifPresent((data) -> ClientUtil.playSound(player, message.sound, SoundSource.PLAYERS, true, () -> !data.regenState().equals(RegenStates.REGENERATING), 1.0F, RandomSource.create()));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.sound);
        buffer.writeInt(this.playerUUID);
    }

}

