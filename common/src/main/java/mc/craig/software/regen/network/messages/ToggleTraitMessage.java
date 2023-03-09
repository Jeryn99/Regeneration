package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ToggleTraitMessage extends MessageC2S {
    public ToggleTraitMessage() {
    }

    public ToggleTraitMessage(FriendlyByteBuf buffer) {
    }

    public void handle(MessageContext context) {
        ServerPlayer player = context.getPlayer();

        RegenerationData.get(player).ifPresent(cap -> {
            cap.toggleTrait();

            TraitBase trait = cap.getCurrentTrait();

            if (cap.isTraitActive()) {
                trait.onAdded(player, cap);
            } else {
                trait.onRemoved(player, cap);
            }
            cap.syncToClients(null);
        });
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.TOGGLE_TRAIT;
    }

    public void toBytes(FriendlyByteBuf buf) {
    }
}
