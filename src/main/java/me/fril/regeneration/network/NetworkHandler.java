package me.fril.regeneration.network;

import me.fril.regeneration.RegenerationMod;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class NetworkHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(RegenerationMod.MODID);
	
	public static void init() {
		int discrimiator = 1;
		INSTANCE.registerMessage(MessageSaveStyle.Handler.class, MessageSaveStyle.class, discrimiator++, Side.SERVER);
		INSTANCE.registerMessage(MessageSetPerspective.Handler.class, MessageSetPerspective.class, discrimiator++, Side.CLIENT);
		INSTANCE.registerMessage(MessageRegenStateEvent.Handler.class, MessageRegenStateEvent.class, discrimiator++, Side.CLIENT);
		INSTANCE.registerMessage(MessageTriggerRegeneration.Handler.class, MessageTriggerRegeneration.class, discrimiator++, Side.SERVER);
		INSTANCE.registerMessage(MessageSynchronisationRequest.Handler.class, MessageSynchronisationRequest.class, discrimiator++, Side.SERVER);
		INSTANCE.registerMessage(MessageSynchroniseRegeneration.Handler.class, MessageSynchroniseRegeneration.class, discrimiator++, Side.CLIENT);
		INSTANCE.registerMessage(MessageUpdateSkin.Handler.class, MessageUpdateSkin.class, discrimiator++, Side.SERVER);
		INSTANCE.registerMessage(MessageTellEveryone.Handler.class, MessageTellEveryone.class, discrimiator++, Side.CLIENT);
	}
	
}
