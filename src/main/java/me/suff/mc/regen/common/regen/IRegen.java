package me.suff.mc.regen.common.regen;

import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.common.traits.Traits;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface IRegen extends INBTSerializable< CompoundNBT > {

    int getRegens();

    void setRegens(int regens);

    void tick();

    int getAnimationTicks();

    void setAnimationTicks(int ticksAnimating);

    boolean canRegenerate();

    boolean areHandsGlowing();

    RegenStates getCurrentState();

    RegenCap.StateManager getStateManager();

    void readStyle(CompoundNBT compoundNBT);

    CompoundNBT getOrWriteStyle();

    void extractRegens(int amount);

    void addRegens(int amount);

    LivingEntity getLiving();

    void syncToClients(@Nullable ServerPlayerEntity serverPlayerEntity);

    TransitionTypes getTransitionType();

    void setTransitionType(TransitionTypes transitionType);

    String getDeathMessage();

    void setDeathMessage(String deathMessage);

    void regen();

    byte[] getSkin();

    void setSkin(byte[] skin);

    boolean isSkinValidForUse();

    Vector3d getPrimaryColors();

    Vector3d getSecondaryColors();

    boolean isAlexSkinCurrently();

    void setAlexSkin(boolean isAlex);

    PlayerUtil.SkinType getPreferredModel();

    void setPreferredModel(PlayerUtil.SkinType skinType);

    byte[] getNextSkin();

    //Next Skin
    void setNextSkin(byte[] bytes);

    boolean isNextSkinValid();

    void setNextSkinType(boolean isAlex);

    boolean isNextSkinTypeAlex();

    Traits.ITrait getTrait();

    void setTrait(Traits.ITrait trait);

    Traits.ITrait getNextTrait();

    void setNextTrait(Traits.ITrait trait);

    TimelordSound getTimelordSound();

    void setTimelordSound(TimelordSound timelordSound);

    Hand getHandState();

    void setHandState(Hand handState);

    enum Hand {
        LEFT_GONE,
        RIGHT_GONE,
        NO_GONE
    }

    enum TimelordSound {
        DRUM, HUM
    }
}
