package me.swirtzly.regeneration.common.capability;

import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.apache.commons.lang3.tuple.Pair;

public interface IRegenerationStateManager extends INBTSerializable<NBTTagCompound> {

    // Event proxy methods
    boolean onKilled(DamageSource source);

    void onPunchEntity(LivingHurtEvent entity);

    void onPunchBlock(PlayerInteractEvent.LeftClickBlock e);

    // Proxy methods for timing related stuff
    double getStateProgress();

    // Debug things
    @Deprecated
    Pair<PlayerUtil.RegenState.Transition, Long> getScheduledEvent();

    void fastForward();

    @Deprecated
    void fastForwardHandGlow();

}
