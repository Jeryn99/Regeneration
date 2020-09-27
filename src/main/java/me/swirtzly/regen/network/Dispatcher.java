package me.swirtzly.regen.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static me.swirtzly.regen.util.RConstants.MODID;

public class Dispatcher {

    public static SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> "1.0", "1.0"::equals, "1.0"::equals);

    public static void setUp() {
        int id = 0;
        NETWORK_CHANNEL.registerMessage(id++, SyncMessage.class, SyncMessage::toBytes, SyncMessage::new, SyncMessage::handle);
    }

}
