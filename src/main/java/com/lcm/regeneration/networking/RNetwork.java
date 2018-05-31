package com.lcm.regeneration.networking;

import com.lcm.regeneration.Regeneration;
import com.lcm.regeneration.networking.packets.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class RNetwork {

	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Regeneration.MODID);

	public static void init() {
		INSTANCE.registerMessage(MessageChangeRegenState.Handler.class, MessageChangeRegenState.class, 1, Side.CLIENT);
		INSTANCE.registerMessage(MessageSyncTimelordData.Handler.class, MessageSyncTimelordData.class, 2, Side.CLIENT);
		INSTANCE.registerMessage(MessageRegenerationStyle.Handler.class, MessageRegenerationStyle.class, 3, Side.SERVER);
	}

}
