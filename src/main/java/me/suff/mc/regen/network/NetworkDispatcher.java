package me.suff.mc.regen.network;

import me.suff.mc.regen.network.messages.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static me.suff.mc.regen.util.RConstants.MODID;

public class NetworkDispatcher {

    public static SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> "1.0", "1.0"::equals, "1.0"::equals);

    public static void init() {
        int id = 0;
        NETWORK_CHANNEL.registerMessage(id++, SyncMessage.class, SyncMessage::toBytes, SyncMessage::new, SyncMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, SFXMessage.class, SFXMessage::toBytes, SFXMessage::new, SFXMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, POVMessage.class, POVMessage::toBytes, POVMessage::new, POVMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, StateMessage.class, StateMessage::toBytes, StateMessage::new, StateMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, SkinMessage.class, SkinMessage::toBytes, SkinMessage::new, SkinMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, ColorChangeMessage.class, ColorChangeMessage::toBytes, ColorChangeMessage::new, ColorChangeMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, TypeMessage.class, TypeMessage::toBytes, TypeMessage::new, TypeMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, ModelMessage.class, ModelMessage::toBytes, ModelMessage::new, ModelMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, NextSkinMessage.class, NextSkinMessage::toBytes, NextSkinMessage::new, NextSkinMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, ForceRegenMessage.class, ForceRegenMessage::toBytes, ForceRegenMessage::new, ForceRegenMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, ChangeSoundScheme.class, ChangeSoundScheme::toBytes, ChangeSoundScheme::new, ChangeSoundScheme::handle);
        NETWORK_CHANNEL.registerMessage(id++, RemoveTimelordSkinMessage.class, RemoveTimelordSkinMessage::toBytes, RemoveTimelordSkinMessage::new, RemoveTimelordSkinMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, RemoveSkinPlayerMessage.class, RemoveSkinPlayerMessage::toBytes, RemoveSkinPlayerMessage::new, RemoveSkinPlayerMessage::handle);
        NETWORK_CHANNEL.registerMessage(id++, ToggleTraitMessage.class, ToggleTraitMessage::toBytes, ToggleTraitMessage::new, ToggleTraitMessage::handle);
    }

}
