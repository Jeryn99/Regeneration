package me.suff.mc.regen.common.regen;

import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.common.traits.AbstractTrait;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface IRegen extends INBTSerializable<CompoundTag> {

    int regens();

    void setRegens(int regens);

    void tick();

    int updateTicks();

    void setUpdateTicks(int ticksAnimating);

    boolean canRegenerate();

    boolean glowing();

    RegenStates regenState();

    RegenCap.StateManager stateManager();

    void readStyle(CompoundTag compoundNBT);

    CompoundTag getOrWriteStyle();

    void extractRegens(int amount);

    void addRegens(int amount);

    LivingEntity getLiving();

    void syncToClients(@Nullable ServerPlayer serverPlayerEntity);

    TransitionType transitionType();

    void setTransitionType(TransitionType transitionType);

    String deathMessage();

    void setDeathMessage(String deathMessage);

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
