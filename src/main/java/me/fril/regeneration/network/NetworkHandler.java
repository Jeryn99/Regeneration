package me.fril.regeneration.network;

import me.fril.regeneration.Regeneration;
import me.fril.regeneration.network.packets.MessageEnterGrace;
import me.fril.regeneration.network.packets.MessageRegenerationStyle;
import me.fril.regeneration.network.packets.MessageUpdateRegen;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class NetworkHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Regeneration.MODID);
	
	public static void init() {
		INSTANCE.registerMessage(MessageUpdateRegen.Handler.class, MessageUpdateRegen.class, 1, Side.CLIENT);
		INSTANCE.registerMessage(MessageEnterGrace.Handler.class, MessageEnterGrace.class, 2, Side.SERVER);
		INSTANCE.registerMessage(MessageRegenerationStyle.Handler.class, MessageRegenerationStyle.class, 3, Side.SERVER);
	}
	
}
