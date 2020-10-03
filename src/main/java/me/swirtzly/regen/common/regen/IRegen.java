package me.swirtzly.regen.common.regen;

import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import me.swirtzly.regen.util.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface IRegen extends INBTSerializable<CompoundNBT> {

    int getRegens();

    void setRegens(int regens);

    void tick();

    int getTicksAnimating();

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
}
