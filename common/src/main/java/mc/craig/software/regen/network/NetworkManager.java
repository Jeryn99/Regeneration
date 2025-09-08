package mc.craig.software.regen.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class NetworkManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkManager.class);

    protected final ResourceLocation channelName;
    protected final Map<String, MessageType> toServer = new HashMap<>();
    protected final Map<String, MessageType> toClient = new HashMap<>();

    protected NetworkManager(ResourceLocation channelName) {
        this.channelName = Objects.requireNonNull(channelName, "Channel name cannot be null");
        LOGGER.debug("Initialized NetworkManager for channel: {}", channelName);
    }

    @ExpectPlatform
    public static NetworkManager create(ResourceLocation channelName) {
        throw new UnsupportedOperationException("Missing platform-specific NetworkManager#create");
    }

    @ExpectPlatform
    public static Packet<?> spawnPacket(Entity entity) {
        throw new UnsupportedOperationException("Missing platform-specific NetworkManager#spawnPacket");
    }

    public MessageType registerS2C(String id, MessageDecoder<MessageS2C> decoder) {
        var msgType = new MessageType(id, this, decoder, false);
        toClient.put(id, msgType);
        LOGGER.info("Registered S2C message type: {} on channel {}", id, channelName);
        return msgType;
    }

    public MessageType registerC2S(String id, MessageDecoder<MessageC2S> decoder) {
        var msgType = new MessageType(id, this, decoder, true);
        toServer.put(id, msgType);
        LOGGER.info("Registered C2S message type: {} on channel {}", id, channelName);
        return msgType;
    }

    public abstract void sendToServer(MessageC2S message);

    public abstract void sendToPlayer(ServerPlayer player, MessageS2C message);

    public void sendToDimension(Level level, MessageS2C message) {
        if (level.isClientSide) {
            LOGGER.debug("Skipped sending {} to dimension {} (client side)", message.getClass().getSimpleName(), level.dimension().location());
            return;
        }

        LOGGER.debug("Sending {} to all players in dimension {}", message.getClass().getSimpleName(), level.dimension().location());
        for (Player player : level.players()) {
            if (player instanceof ServerPlayer serverPlayer) {
                sendToPlayer(serverPlayer, message);
                LOGGER.trace("Sent {} to player {}", message.getClass().getSimpleName(), serverPlayer.getGameProfile().getName());
            }
        }
    }

    @FunctionalInterface
    public interface MessageDecoder<T extends Message> {
        T decode(FriendlyByteBuf buf);
    }
}
