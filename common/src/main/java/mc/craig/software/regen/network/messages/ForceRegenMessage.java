package mc.craig.software.regen.network.messages;

import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.network.MessageC2S;
import mc.craig.software.regen.network.MessageContext;
import mc.craig.software.regen.network.MessageType;
import mc.craig.software.regen.network.RegenNetwork;
import mc.craig.software.regen.util.RegenDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.damagesource.DamageSource;
import org.jetbrains.annotations.NotNull;

public class ForceRegenMessage extends MessageC2S {

    public ForceRegenMessage() {
    }

    public ForceRegenMessage(FriendlyByteBuf buffer) {
    }

    public void handle(MessageContext context) {
        RegenerationData.get(context.getPlayer()).ifPresent((cap) -> {
            if (cap.regenState() == RegenStates.ALIVE || cap.regenState().isGraceful()) {
                if (cap.canRegenerate()) {
                    cap.getLiving().hurt(new DamageSource(cap.getLiving().level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RegenDamageTypes.REGEN_DMG_FORCED)), Integer.MAX_VALUE);
                }
            }
        });
    }

    @NotNull
    @Override
    public MessageType getType() {
        return RegenNetwork.FORCE_REGENERATION;
    }

    public void toBytes(FriendlyByteBuf buf) {

    }

}
