package me.suff.regeneration.network;

import me.suff.regeneration.RegenerationMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class NetworkHandler {
	
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(RegenerationMod.MODID, "main_channel"))
			.clientAcceptedVersions(PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(PROTOCOL_VERSION::equals)
			.networkProtocolVersion(() -> PROTOCOL_VERSION)
			.simpleChannel();
	
	
	public static void init() {
		int id = 0;
		INSTANCE.registerMessage(id++, MessageSaveStyle.class, MessageSaveStyle::encode, MessageSaveStyle::decode, MessageSaveStyle.Handler::handle);
		INSTANCE.registerMessage(id++, MessageSetPerspective.class, MessageSetPerspective::encode, MessageSetPerspective::decode, MessageSetPerspective.Handler::handle);
		INSTANCE.registerMessage(MessageRegenStateEvent.Handler.class, MessageRegenStateEvent.class, 2, Dist.CLIENT);
		INSTANCE.registerMessage(MessageTriggerRegeneration.Handler.class, MessageTriggerRegeneration.class, 3, Side.SERVER);
		INSTANCE.registerMessage(MessageSynchronisationRequest.Handler.class, MessageSynchronisationRequest.class, 4, Side.SERVER);
		INSTANCE.registerMessage(MessageSynchroniseRegeneration.Handler.class, MessageSynchroniseRegeneration.class, 5, Dist.CLIENT);
		INSTANCE.registerMessage(MessageUpdateSkin.Handler.class, MessageUpdateSkin.class, 6, Side.SERVER);
		INSTANCE.registerMessage(MessageRemovePlayer.Handler.class, MessageRemovePlayer.class, 7, Dist.CLIENT);
		INSTANCE.registerMessage(MessagePlayRegenerationSound.Handler.class, MessagePlayRegenerationSound.class, 8, Dist.CLIENT);
		INSTANCE.registerMessage(MessageUpdateModel.Handler.class, MessageUpdateModel.class, 9, Side.SERVER);
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
