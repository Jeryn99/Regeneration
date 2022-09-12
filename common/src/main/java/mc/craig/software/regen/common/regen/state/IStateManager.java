package mc.craig.software.regen.common.regen.state;

import mc.craig.software.regen.util.Serializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

public interface IStateManager extends Serializable<CompoundTag> {

    // Event proxy methods
    boolean onKilled(DamageSource source);

    boolean onPunchBlock(BlockPos pos, BlockState blockState, Player entity);

    // Proxy methods for timing related stuff
    double stateProgress();

    // Debug things
    @Deprecated
    Pair<RegenStates.Transition, Long> getScheduledEvent();

    @Deprecated
    void skip();

    @Deprecated
    void fastForwardHandGlow();

    @Override
    CompoundTag serializeNBT();

    @Override
    void deserializeNBT(CompoundTag arg);
}
