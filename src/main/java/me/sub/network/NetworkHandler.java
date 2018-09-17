package me.sub.network;

import me.sub.Regeneration;
import me.sub.network.packets.MessageEnterGrace;
import me.sub.network.packets.MessageUpdateRegen;
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
    }

}
