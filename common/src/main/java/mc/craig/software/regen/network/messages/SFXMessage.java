package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageS2C;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class SFXMessage extends MessageS2C {
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

    public void handle(MessageContext context) {
        Minecraft.getInstance().submit(() -> {
            if (Minecraft.getInstance().level != null) {
                Entity player = Minecraft.getInstance().level.getEntity(this.playerUUID);
                if (player != null) {
                    RegenerationData.get((LivingEntity) player).ifPresent((data) -> ClientUtil.playSound(player, this.sound, SoundSource.PLAYERS, true, () -> !data.regenState().equals(RegenStates.REGENERATING), 1.0F, RandomSource.create()));
                }
            }
        });
    }

    @NotNull
    @Override
    public MessageType getType() {
        return null;
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(this.sound);
        buffer.writeInt(this.playerUUID);
    }

}

