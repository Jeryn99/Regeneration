package mc.craig.software.regen.switchy;

import folk.sisby.switchy.api.SwitchyEvents;
import folk.sisby.switchy.api.SwitchySerializable;
import folk.sisby.switchy.api.module.*;
import folk.sisby.switchy.api.modules.CardinalSerializerModule;
import folk.sisby.switchy.util.Feedback;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.fabric.RegenerationComponents;
import mc.craig.software.regen.network.messages.RemoveSkinPlayerMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;


public class RegenerationModule implements SwitchyModule, SwitchySerializable, SwitchyModuleTransferable, SwitchyEvents.Init {

    private CompoundTag nbt = new CompoundTag();

    @Override
    public void updateFromPlayer(ServerPlayer player, @Nullable String nextPreset) {
        RegenerationData.get(player).ifPresent(regenerationData -> {
            CompoundTag newNbt = regenerationData.serializeNBT();
            if (!newNbt.isEmpty()) { // Update with new data instead of checking the old one
                nbt = newNbt;
                regenerationData.syncToClients(null);
                Regeneration.LOGGER.info("NBT UPDATE: {}", nbt);
            }
        });
    }

    @Override
    public void applyToPlayer(ServerPlayer player) {
        RegenerationData.get(player).ifPresent(regenerationData -> {
            if (!nbt.isEmpty()) {
                Regeneration.LOGGER.info("NBT APPLIED: {}", nbt);
                regenerationData.deserializeNBT(nbt);
                regenerationData.syncToClients(null);
            } else {
                Regeneration.LOGGER.info("NBT IS EMPTY, NOT APPLYING: {}", nbt);
            }
        });
    }

    @Override
    public void onInitialize() {
        SwitchyModuleRegistry.registerModule(new ResourceLocation(Regeneration.MOD_ID, "regeneration"), () -> CardinalSerializerModule.from(RegenerationComponents.REGENERATION_DATA, (k, p) -> {

                }, (k, p) -> {
                    RegenerationData.get(p).ifPresent(regenerationData -> regenerationData.syncToClients(null));
                    new RemoveSkinPlayerMessage(p.getUUID()).sendToAll();
                }),
                new SwitchyModuleInfo(
                        false,
                        SwitchyModuleEditable.OPERATOR,
                        Feedback.translatable("switchy.modules.switchy_inventories.regeneration.description"))
                        .withDescriptionWhenEnabled(Feedback.translatable("switchy.modules.switchy_inventories.regeneration.enabled"))
                        .withDescriptionWhenDisabled(Feedback.translatable("switchy.modules.switchy_inventories.regeneration.disabled"))
                        .withDeletionWarning(Feedback.translatable("switchy.modules.switchy_inventories.regeneration.warning"))
        );

    }

    public CompoundTag getNbt() {
        return nbt;
    }

    public void setNbt(CompoundTag nbt) {
        this.nbt = nbt;
    }

    @Override
    public CompoundTag toNbt() {
        CompoundTag compoundTag = new CompoundTag();
        if (!nbt.isEmpty()) {
            compoundTag.put("regen_switchy", getNbt());
        }
        return compoundTag;
    }

    @Override
    public void fillFromNbt(CompoundTag compoundTag) {
        if (compoundTag.contains("regen_switchy")) {
            setNbt(compoundTag.getCompound("regen_switchy"));
        }
    }
}
