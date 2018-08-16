package me.sub.regeneration.networking;

import me.sub.regeneration.Regeneration;
import me.sub.regeneration.networking.packets.MessageChangeRegenState;
import me.sub.regeneration.networking.packets.MessageChangeView;
import me.sub.regeneration.networking.packets.MessageRegenerationStyle;
import me.sub.regeneration.networking.packets.MessageSyncTimelordData;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class RNetwork {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Regeneration.MODID);

	public static void init() {
		INSTANCE.registerMessage(MessageChangeRegenState.Handler.class, MessageChangeRegenState.class, 1, Side.CLIENT);
		INSTANCE.registerMessage(MessageSyncTimelordData.Handler.class, MessageSyncTimelordData.class, 2, Side.CLIENT);
		INSTANCE.registerMessage(MessageRegenerationStyle.Handler.class, MessageRegenerationStyle.class, 3, Side.SERVER);
		INSTANCE.registerMessage(MessageChangeView.Handler.class, MessageChangeView.class, 4, Side.CLIENT);
	}

}
