package me.suff.regeneration.network;

import me.suff.regeneration.RegenerationMod;
import net.minecraft.entity.player.EntityPlayerMP;
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
public class NetworkHandler {
	
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(RegenerationMod.MODID, "main_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();
	
	
	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(id++, MessageSaveStyle.class, MessageSaveStyle::encode, MessageSaveStyle::decode, MessageSaveStyle.Handler::handle);
		INSTANCE.registerMessage(id++, MessageSetPerspective.class, MessageSetPerspective::encode, MessageSetPerspective::decode, MessageSetPerspective.Handler::handle);
		INSTANCE.registerMessage(id++, MessageRegenStateEvent.class, MessageRegenStateEvent::encode, MessageRegenStateEvent::decode, MessageRegenStateEvent.Handler::handle);
		INSTANCE.registerMessage(id++, MessageTriggerRegeneration.class, MessageTriggerRegeneration::encode, MessageTriggerRegeneration::decode, MessageTriggerRegeneration.Handler::handle);
		INSTANCE.registerMessage(id++,MessageSynchronisationRequest.class, MessageSynchronisationRequest::encode, MessageSynchronisationRequest::decode, MessageSynchronisationRequest.Handler::handle);
		INSTANCE.registerMessage(id++,MessageSynchroniseRegeneration.class, MessageSynchroniseRegeneration::encode, MessageSynchroniseRegeneration::decode, MessageSynchroniseRegeneration.Handler::handle);
		INSTANCE.registerMessage(id++,MessageUpdateSkin.class, MessageUpdateSkin::encode, MessageUpdateSkin::decode, MessageUpdateSkin.Handler::handle);
		INSTANCE.registerMessage(id++,MessageRemovePlayer.class, MessageRemovePlayer::encode, MessageRemovePlayer::decode, MessageRemovePlayer.Handler::handle);
		INSTANCE.registerMessage(id++,MessagePlayRegenerationSound.class, MessagePlayRegenerationSound::encode, MessagePlayRegenerationSound::decode, MessagePlayRegenerationSound.Handler::handle);
		INSTANCE.registerMessage(id++,MessageUpdateModel.class, MessageUpdateModel::encode, MessageUpdateModel::decode, MessageUpdateModel.Handler::handle);
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
	public static void sendTo(Object msg, EntityPlayerMP player) {
		if (!(player instanceof FakePlayer)) {
			INSTANCE.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
		}
	}
	
	public static void sendPacketToAll(Object packet) {
		for (EntityPlayerMP player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
			sendTo(packet, player);
		}
	}
}
