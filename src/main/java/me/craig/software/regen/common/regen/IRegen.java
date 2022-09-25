package me.craig.software.regen.common.regen;

import me.craig.software.regen.common.objects.RSounds;
import me.craig.software.regen.common.regen.transitions.TransitionType;
import me.craig.software.regen.common.traits.AbstractTrait;
import me.craig.software.regen.util.PlayerUtil;
import me.craig.software.regen.util.RegenUtil;
import me.craig.software.regen.common.regen.state.RegenStates;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface IRegen extends INBTSerializable<CompoundNBT> {

    int regens();

    void setRegens(int regens);

    void tick();

    int updateTicks();

    void setUpdateTicks(int ticksAnimating);

    boolean canRegenerate();

    boolean glowing();

    RegenStates regenState();

    RegenCap.StateManager stateManager();

    void readStyle(CompoundNBT compoundNBT);

    CompoundNBT getOrWriteStyle();

    void extractRegens(int amount);

    void addRegens(int amount);

    LivingEntity getLiving();

    void syncToClients(@Nullable ServerPlayerEntity serverPlayerEntity);

    TransitionType transitionType();

    void setTransitionType(TransitionType transitionType);

    String deathMessage();

    void setDeathMessage(String deathMessage);

    void forceRegeneration();

    byte[] skin();

    void setSkin(byte[] skin);

    boolean isSkinValidForUse();

    Vector3d getPrimaryColors();

    Vector3d getSecondaryColors();

    boolean currentlyAlex();

    void setAlexSkin(boolean isAlex);

    PlayerUtil.SkinType preferredModel();

    void setPreferredModel(PlayerUtil.SkinType skinType);

    byte[] nextSkin();

    //Next Skin
    void setNextSkin(byte[] bytes);

    boolean isNextSkinValid();

    void setNextSkinType(boolean isAlex);

    boolean isNextSkinTypeAlex();

    AbstractTrait trait();

    boolean traitActive();

    void toggleTrait();

    void setTrait(AbstractTrait trait);

    AbstractTrait getNextTrait();

    void setNextTrait(AbstractTrait trait);

    TimelordSound getTimelordSound();

    void setTimelordSound(TimelordSound timelordSound);

    Hand handState();

    void setHandState(Hand handState);

    enum Hand {
        LEFT_GONE,
        RIGHT_GONE,
        NO_GONE
    }

    enum TimelordSound implements RegenUtil.IEnum<TimelordSound> {
        DRUM(RSounds.DRUM_BEAT::get), HUM(RSounds.GRACE_HUM::get), SAXON_ENGLAND(RSounds.SAXONS_ENGLAND::get);

        private final Supplier<SoundEvent> sound;

        TimelordSound(Supplier<SoundEvent> soundEventSupplier) {
            this.sound = soundEventSupplier;
        }

        public SoundEvent getSound() {
            return sound.get();
        }
    }
}
