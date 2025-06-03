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


public class RegenerationModule implements SwitchyModule, SwitchySerializable, SwitchyEvents.Init {

    private float absorption = 0;
    private float health = 0;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    @Override
    public void updateFromPlayer(ServerPlayer player, @Nullable String nextPreset) {
        if (isTimelord(player)) {
            absorption = player.getAbsorptionAmount();
            health = player.getHealth();
        }
    }

    @Override
    public void applyToPlayer(ServerPlayer player) {
        player.setAbsorptionAmount(absorption);

        if (isTimelord(player)) {
            player.setHealth(health);
        } else {
            player.setHealth(player.getMaxHealth());
        }
    }

    private boolean isTimelord(ServerPlayer player) {
        return RegenerationData.get(player)
                .map(RegenerationData::canRegenerate)
                .orElse(false);
    }

    @Override
    public void onInitialize() {
        SwitchyModuleRegistry.registerModule(new ResourceLocation(Regeneration.MOD_ID, "regeneration"), () -> CardinalSerializerModule.from(RegenerationComponents.REGENERATION_DATA, (k, p) -> {
                    RegenerationData.get(p).ifPresent(regenerationData -> regenerationData.syncToClients(null));
                    new RemoveSkinPlayerMessage(p.getUUID()).sendToAll();
                }, (k, p) -> {
                    RegenerationData.get(p).ifPresent(regenerationData -> regenerationData.syncToClients(null));
                    new RemoveSkinPlayerMessage(p.getUUID()).sendToAll();
                }),
                new SwitchyModuleInfo(
                        false,
                        SwitchyModuleEditable.OPERATOR,
                        Feedback.translatable("switchy.modules.regeneration.description"))
                        .withDescriptionWhenEnabled(Feedback.translatable("switchy.modules.regeneration.enabled"))
                        .withDescriptionWhenDisabled(Feedback.translatable("switchy.modules.regeneration.disabled"))
                        .withDeletionWarning(Feedback.translatable("switchy.modules.regeneration.warning"))
        );


        SwitchyModuleRegistry.registerModule(new ResourceLocation(Regeneration.MOD_ID, "regeneration_additional"), RegenerationModule::new,
                new SwitchyModuleInfo(
                        false,
                        SwitchyModuleEditable.OPERATOR,
                        Feedback.translatable("switchy.modules.regeneration_additional.description"))
                        .withDescriptionWhenEnabled(Feedback.translatable("switchy.modules.regeneration_additional.regeneration.enabled"))
                        .withDescriptionWhenDisabled(Feedback.translatable("switchy.modules.regeneration_additional.regeneration.disabled"))
                        .withDeletionWarning(Feedback.translatable("switchy.modules.regeneration_additional.regeneration.warning"))
        );

    }


    @Override
    public CompoundTag toNbt() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putFloat("regen_absorption", getAbsorption());
        compoundTag.putFloat("regen_health", getHealth());
        return compoundTag;
    }

    @Override
    public void fillFromNbt(CompoundTag compoundTag) {
        if (compoundTag.contains("regen_absorption")) {
            setAbsorption(compoundTag.getFloat("regen_absorption"));
        }

        if (compoundTag.contains("regen_health")) {
            setHealth(compoundTag.getFloat("regen_health"));
        }
    }

    public float getAbsorption() {
        return absorption;
    }

    public void setAbsorption(float absorption) {
        this.absorption = absorption;
    }
}
