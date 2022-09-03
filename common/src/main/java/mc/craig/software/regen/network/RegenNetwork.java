package mc.craig.software.regen.network;

import mc.craig.software.regen.Regeneration;
import net.minecraft.resources.ResourceLocation;

public class RegenNetwork {

    public static final NetworkManager NETWORK = NetworkManager.create(new ResourceLocation(Regeneration.MOD_ID, "channel"));

//    public static MessageType UPDATE_CATACOMB;

    public static void init() {
//        UPDATE_CATACOMB = NETWORK.registerS2C("update_catacomb", UpdateCatacombMessage::new);
    }

}
