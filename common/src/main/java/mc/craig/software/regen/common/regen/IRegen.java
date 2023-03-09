package mc.craig.software.regen.common.regen;

import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface IRegen extends Serializable<CompoundTag> {

    AnimationState getAnimationState(RegenAnimation regenAnimation);

    int regens();

    void setRegens(int regens);

    void tick();

    int updateTicks();

    void setUpdateTicks(int ticksAnimating);

    boolean canRegenerate();

    boolean glowing();

    RegenStates regenState();

    RegenerationData.StateManager stateManager();

    void readStyle(CompoundTag compoundNBT);

    CompoundTag getOrWriteStyle();

    void extractRegens(int amount);

    void addRegens(int amount);

    LivingEntity getLiving();

    void syncToClients(@Nullable ServerPlayer serverPlayerEntity);

    TransitionType transitionType();

    void setTransitionType(TransitionType transitionType);

    void forceRegeneration();

    byte[] skin();

    void setSkin(byte[] skin);

    boolean isSkinValidForUse();

    Vec3 getPrimaryColors();

    Vec3 getSecondaryColors();

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

    TimelordSound getTimelordSound();

    void setTimelordSound(TimelordSound timelordSound);

    Hand handState();

    void setHandState(Hand handState);

    boolean isTraitActive();

    void toggleTrait();

    TraitBase getCurrentTrait();
    void setCurrentTrait(TraitBase trait);

    TraitBase getNextTrait();
    void setNextTrait(TraitBase trait);

    enum RegenAnimation {
        REGEN, GRACE
    }

    enum Hand {
        CUT,
        NOT_CUT
    }

    enum TimelordSound implements RegenUtil.IEnum<TimelordSound> {
        DRUM(RSounds.DRUM_BEAT), HUM(RSounds.GRACE_HUM), SAXON_ENGLAND(RSounds.SAXONS_ENGLAND);

        private final Supplier<SoundEvent> sound;

        TimelordSound(Supplier<SoundEvent> soundEventSupplier) {
            this.sound = soundEventSupplier;
        }

        public SoundEvent getSound() {
            return sound.get();
        }
    }
}
