package me.swirtzly.regeneration.network;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.network.messages.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class NetworkDispatcher {

    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(RegenerationMod.MODID, "main_channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();


    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(id++, UpdateColorMessage.class, UpdateColorMessage::encode, UpdateColorMessage::decode, UpdateColorMessage.Handler::handle);
        INSTANCE.registerMessage(id++, ThirdPersonMessage.class, ThirdPersonMessage::encode, ThirdPersonMessage::decode, ThirdPersonMessage.Handler::handle);
        INSTANCE.registerMessage(id++, UpdateStateMessage.class, UpdateStateMessage::encode, UpdateStateMessage::decode, UpdateStateMessage.Handler::handle);
        INSTANCE.registerMessage(id++, RegenerateMessage.class, RegenerateMessage::encode, RegenerateMessage::decode, RegenerateMessage.Handler::handle);
        INSTANCE.registerMessage(id++, SyncDataMessage.class, SyncDataMessage::encode, SyncDataMessage::decode, SyncDataMessage.Handler::handle);
        INSTANCE.registerMessage(id++, SyncClientPlayerMessage.class, SyncClientPlayerMessage::encode, SyncClientPlayerMessage::decode, SyncClientPlayerMessage.Handler::handle);
        INSTANCE.registerMessage(id++, UpdateSkinMessage.class, UpdateSkinMessage::encode, UpdateSkinMessage::decode, UpdateSkinMessage.Handler::handle);
        INSTANCE.registerMessage(id++, InvalidatePlayerDataMessage.class, InvalidatePlayerDataMessage::encode, InvalidatePlayerDataMessage::decode, InvalidatePlayerDataMessage.Handler::handle);
        INSTANCE.registerMessage(id++, PlaySFXMessage.class, PlaySFXMessage::encode, PlaySFXMessage::decode, PlaySFXMessage.Handler::handle);
        INSTANCE.registerMessage(id++, UpdateSkinMapMessage.class, UpdateSkinMapMessage::encode, UpdateSkinMapMessage::decode, UpdateSkinMapMessage.Handler::handle);
        INSTANCE.registerMessage(id++, ForceRegenerationMessage.class, ForceRegenerationMessage::encode, ForceRegenerationMessage::decode, ForceRegenerationMessage.Handler::handle);
        INSTANCE.registerMessage(id++, NextSkinMessage.class, NextSkinMessage::encode, NextSkinMessage::decode, NextSkinMessage.Handler::handle);
        INSTANCE.registerMessage(id++, UpdateTypeMessage.class, UpdateTypeMessage::encode, UpdateTypeMessage::decode, UpdateTypeMessage.Handler::handle);
     }

    /**
     * Sends a packet to the server.<br>
     * Must be called Client side.
     */
    public static void sendToServer(Object msg) {
        INSTANCE.sendToServer(msg);
    }

    /**
     * Send a packet to a specific player.<br>
     * Must be called Server side.
     */
    public static void sendTo(Object msg, ServerPlayerEntity player) {
        if (!(player instanceof FakePlayer)) {
            INSTANCE.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static void sendPacketToAll(Object packet) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendTo(packet, player);
        }
    }
}
