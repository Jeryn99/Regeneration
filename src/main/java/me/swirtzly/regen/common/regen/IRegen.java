package me.swirtzly.regen.common.regen;

import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public interface IRegen extends INBTSerializable<CompoundNBT> {

    void setRegens(int regens);
    int getRegens();

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
    void setDeathMessage(String deathMessage);
    String getDeathMessage();
}
