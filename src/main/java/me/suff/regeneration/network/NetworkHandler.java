package me.suff.regeneration.network;

import me.suff.regeneration.RegenerationMod;
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
		INSTANCE.registerMessage(MessageSaveStyle.Handler.class, MessageSaveStyle.class, 0, Side.SERVER);
		INSTANCE.registerMessage(MessageSetPerspective.Handler.class, MessageSetPerspective.class, 1, Dist.CLIENT);
		INSTANCE.registerMessage(MessageRegenStateEvent.Handler.class, MessageRegenStateEvent.class, 2, Dist.CLIENT);
		INSTANCE.registerMessage(MessageTriggerRegeneration.Handler.class, MessageTriggerRegeneration.class, 3, Side.SERVER);
		INSTANCE.registerMessage(MessageSynchronisationRequest.Handler.class, MessageSynchronisationRequest.class, 4, Side.SERVER);
		INSTANCE.registerMessage(MessageSynchroniseRegeneration.Handler.class, MessageSynchroniseRegeneration.class, 5, Dist.CLIENT);
		INSTANCE.registerMessage(MessageUpdateSkin.Handler.class, MessageUpdateSkin.class, 6, Side.SERVER);
		INSTANCE.registerMessage(MessageRemovePlayer.Handler.class, MessageRemovePlayer.class, 7, Dist.CLIENT);
		INSTANCE.registerMessage(MessagePlayRegenerationSound.Handler.class, MessagePlayRegenerationSound.class, 8, Dist.CLIENT);
		INSTANCE.registerMessage(MessageUpdateModel.Handler.class, MessageUpdateModel.class, 9, Side.SERVER);
	}
	
}
