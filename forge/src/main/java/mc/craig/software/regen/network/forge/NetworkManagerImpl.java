package mc.craig.software.regen.network.forge;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageS2C;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Supplier;

public class NetworkManagerImpl extends NetworkManager {

    private final SimpleChannel channel;

    public static NetworkManager create(ResourceLocation channelName) {
        return new NetworkManagerImpl(channelName);
    }

    public NetworkManagerImpl(ResourceLocation channelName) {
        super(channelName);
        this.channel = NetworkRegistry.newSimpleChannel(channelName, () -> "1.0.0", (s) -> true, (s) -> true);
        this.channel.registerMessage(0, ToServer.class, ToServer::toBytes, ToServer::new, ToServer::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        this.channel.registerMessage(1, ToClient.class, ToClient::toBytes, ToClient::new, ToClient::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    @Override
    public void sendToServer(MessageC2S message) {
        if (!this.toServer.containsValue(message.getType())) {
            Regeneration.LOGGER.warn("Message type not registered: " + message.getType().getId());
            return;
        }

        this.channel.sendToServer(new ToServer(message));
    }

    @Override
    public void sendToPlayer(ServerPlayer player, MessageS2C message) {
        if (!this.toClient.containsValue(message.getType())) {
            Regeneration.LOGGER.warn("Message type not registered: " + message.getType().getId());
            return;
        }

        this.channel.send(PacketDistributor.PLAYER.with(() -> player), new ToClient(message));
    }

    public class ToServer {

        private final MessageC2S message;

        public ToServer(MessageC2S message) {
            this.message = message;
        }

        public ToServer(FriendlyByteBuf buf) {
            var msgId = buf.readUtf();

            if (!NetworkManagerImpl.this.toServer.containsKey(msgId)) {
                Regeneration.LOGGER.warn("Unknown message id received on server: " + msgId);
                this.message = null;
                return;
            }

            MessageType type = NetworkManagerImpl.this.toServer.get(msgId);
            this.message = (MessageC2S) type.getDecoder().decode(buf);
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeUtf(this.message.getType().getId());
            this.message.toBytes(buf);
        }

        public static void handle(ToServer msg, Supplier<NetworkEvent.Context> ctx) {
            if(msg.message != null) {
                ctx.get().enqueueWork(() -> msg.message.handle(() -> ctx.get().getSender()));
            }
            ctx.get().setPacketHandled(true);
        }

    }

    public class ToClient {

        private final MessageS2C message;

        public ToClient(MessageS2C message) {
            this.message = message;
        }

        public ToClient(FriendlyByteBuf buf) {
            var msgId = buf.readUtf();

            if (!NetworkManagerImpl.this.toClient.containsKey(msgId)) {
                Regeneration.LOGGER.warn("Unknown message id received on client: " + msgId);
                this.message = null;
                return;
            }

            MessageType type = NetworkManagerImpl.this.toClient.get(msgId);
            this.message = (MessageS2C) type.getDecoder().decode(buf);
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeUtf(this.message.getType().getId());
            this.message.toBytes(buf);
        }

        public static void handle(ToClient msg, Supplier<NetworkEvent.Context> ctx) {
            if(msg.message != null) {
                ctx.get().enqueueWork(() -> msg.message.handle(() -> null));
            }
            ctx.get().setPacketHandled(true);
        }

    }

    public static Packet<?> spawnPacket(Entity livingEntity) {
        return NetworkHooks.getEntitySpawningPacket(livingEntity);
    }

}
