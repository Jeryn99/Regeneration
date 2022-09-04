package mc.craig.software.regen.common.regeneration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.RegenConfig;
import mc.craig.software.regen.common.RegenLogic;
import mc.craig.software.regen.common.regeneration.acting.ActingForwarder;
import mc.craig.software.regen.common.regeneration.state.IStateManager;
import mc.craig.software.regen.common.regeneration.state.RegenStates;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenConstants;
import mc.craig.software.regen.util.RegenDmgSource;
import mc.craig.software.regen.util.Serializable;
import mc.craig.software.regen.util.schedule.RegenScheduledAction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegenerationData implements Serializable<CompoundTag> {

    private final Player player;
    //State
    private final StateManager stateManager;
    // ===== Data =====

    //Don't save to disk
    private boolean didSetup = false;
    // Color Data
    private float primaryRed = 0.69411767f, primaryGreen = 0.74509805f, primaryBlue = 0.23529412f;
    private float secondaryRed = 0.7137255f, secondaryGreen = 0.75686276f, secondaryBlue = 0.25490198f;
    private int regensLeft = 12;
    private RegenStates currentState = RegenStates.ALIVE;
    private boolean areHandsGlowing = false;

    public RegenerationData(Player player) {
        this.player = player;
        if (!player.level.isClientSide)
            this.stateManager = new StateManager();
        else
            this.stateManager = null;
    }


    public void tick(Player player) {
        if(player.level.isClientSide) return;
        System.out.println(getCurrentState());
    }


    public Player getPlayer() {
        return player;
    }

    public RegenStates getCurrentState() {
        return currentState;
    }

    public int getRemainingRegens() {
        return this.regensLeft;
    }

    public void setRemainingRegens(int remainingRegens) {
        this.regensLeft = remainingRegens;
    }

    @ExpectPlatform
    public static Optional<RegenerationData> get(Player player) {
        throw new AssertionError();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(RegenConstants.REMAINING_REGENS, regensLeft);
        return nbt;
    }

    public boolean canRegenerate() {
        return getRemainingRegens() > 0 && RegenLogic.isSafeToRegenHere(player);
    }

    @Override
    public void deserializeNBT(CompoundTag arg) {
        this.regensLeft = arg.getInt(RegenConstants.REMAINING_REGENS);
    }

    public StateManager stateManager() {
        return stateManager;
    }

    public void extractRegens(int amount) {
        setRemainingRegens(Mth.clamp(getRemainingRegens() - amount, 0, 12));
    }


    public class StateManager implements IStateManager {

        private final Map<RegenStates.Transition, Runnable> transitionCallbacks;
        private RegenScheduledAction nextTransition, handGlowTimer;

        public StateManager() {
            this.transitionCallbacks = new HashMap<>();
            transitionCallbacks.put(RegenStates.Transition.ENTER_CRITICAL, this::enterCriticalPhase);
            transitionCallbacks.put(RegenStates.Transition.CRITICAL_DEATH, () -> midSequenceKill(true));
            transitionCallbacks.put(RegenStates.Transition.FINISH_REGENERATION, this::finishRegeneration);
            transitionCallbacks.put(RegenStates.Transition.END_POST, this::endPost);

            Runnable err = () -> {
                throw new IllegalStateException("Can't use HAND_GLOW_* transitions as state transitions");
            };
            transitionCallbacks.put(RegenStates.Transition.HAND_GLOW_START, err);
            transitionCallbacks.put(RegenStates.Transition.HAND_GLOW_TRIGGER, err);
        }

        private void triggerRegeneration() {
            // We're starting a regeneration!
            currentState = RegenStates.REGENERATING;

            nextTransition.cancel(); // ... cancel any state shift we had planned
            if (currentState.isGraceful()) handGlowTimer.cancel();
            //TODO    scheduleTransitionInTicks(RegenStates.Transition.FINISH_REGENERATION, transitionType.getAnimationLength());

            ActingForwarder.onRegenTrigger(RegenerationData.this);
            //TODO transitionType.onStartRegeneration(RegenerationData.this);
            //TODO syncToClients(null);
        }

        private void endPost() {
            currentState = RegenStates.ALIVE;
            //TODO syncToClients(null);
            nextTransition = null;
            PlayerUtil.sendMessage(player, Component.translatable("regen.messages.post_ended"), true);
        }

        private void finishRegeneration() {
            currentState = RegenStates.POST;
            scheduleTransitionInSeconds(RegenStates.Transition.END_POST, player.level.random.nextInt(300) + 10);
            handGlowTimer = null;
            //TODO    transitionType.onFinishRegeneration(RegenerationData.this);
            ActingForwarder.onRegenFinish(RegenerationData.this);
            //TODO   syncToClients(null);
        }

        private void midSequenceKill(boolean isGrace) {
            currentState = RegenStates.ALIVE;
            nextTransition = null;
            handGlowTimer = null;
            //TODO    transitionType.onFinishRegeneration(RegenerationData.this);
            player.hurt(isGrace ? RegenDmgSource.REGEN_DMG_CRITICAL : RegenDmgSource.REGEN_DMG_KILLED, Integer.MAX_VALUE);
            if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                extractRegens(getRemainingRegens());
            }
        }

        private void scheduleHandGlowTrigger() {

        }

        private void enterCriticalPhase() {

        }

        private void scheduleTransitionInTicks(RegenStates.Transition transition, long inTicks) {
            if (nextTransition != null && nextTransition.getTicksLeft() > 0)
                throw new IllegalStateException("Overwriting non-completed/cancelled transition: " + "\n Attempted Transition: " + transition.name() + "\n Current: " + nextTransition.transition.name() + "\n Affected Player: " + player.getName());

            if (transition == RegenStates.Transition.HAND_GLOW_START || transition == RegenStates.Transition.HAND_GLOW_TRIGGER)
                throw new IllegalStateException("Can't use HAND_GLOW_* transitions as state transitions");

            nextTransition = new RegenScheduledAction(transition, player, transitionCallbacks.get(transition), inTicks);
        }

        private void scheduleTransitionInSeconds(RegenStates.Transition transition, long inSeconds) {
            scheduleTransitionInTicks(transition, inSeconds * 20);
        }


        @Override
        public boolean onKilled(DamageSource source) {
            if (source == DamageSource.IN_WALL || source == DamageSource.CRAMMING) {
                return false;
            }

            if (source == RegenDmgSource.REGEN_DMG_CRITICAL) {
                if (nextTransition != null) {
                    nextTransition.cancel();
                }
                return false;
            }

            switch (currentState) {
                case ALIVE -> {
                    if (!canRegenerate()) // that's too bad :(
                        return false;

                    // We're entering grace period...
                    scheduleTransitionInSeconds(RegenStates.Transition.ENTER_CRITICAL, RegenConfig.COMMON.gracePhaseLength.get());
                    scheduleHandGlowTrigger();
                    currentState = RegenStates.GRACE;
                    //TODO   syncToClients(null);
                    ActingForwarder.onEnterGrace(RegenerationData.this);
                    return true;
                }
                case REGENERATING -> {
                    // We've been killed mid regeneration!
                    nextTransition.cancel(); // ... cancel the finishing of the regeneration
                    midSequenceKill(false);
                    return false;
                }
                case GRACE_CRIT -> {
                    nextTransition.cancel();
                    if (source == RegenDmgSource.REGEN_DMG_FORCED) {
                        triggerRegeneration();
                        return true;
                    } else {
                        midSequenceKill(true);
                        return false;
                    }
                }
                case POST -> {
                    currentState = RegenStates.ALIVE;
                    nextTransition.cancel();
                    return false;
                }
                case GRACE -> {
                    // We're being forced to regenerate...
                    triggerRegeneration();
                    return true;
                }
                default -> {
                }
            }
            return false;
        }

        @Override
        public boolean onPunchEntity(LivingEntity entity) {
            return false;
        }

        @Override
        public boolean onPunchBlock(BlockState blockState, BlockPos blockPos) {
            return false;
        }

        @Override
        public double stateProgress() {
            if (nextTransition != null) {
                return nextTransition.getProgress();
            }
            return 0;
        }

        @Override
        public Pair<RegenStates.Transition, Long> getScheduledEvent() {
            return nextTransition == null ? null : Pair.of(nextTransition.transition, nextTransition.getTicksLeft());
        }

        @Override
        public void skip() {
            while (!nextTransition.tick()) ;
        }

        @Override
        public void fastForwardHandGlow() {
            while (!handGlowTimer.tick()) ;
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            if (nextTransition != null && nextTransition.getTicksLeft() >= 0) {
                nbt.putString("transitionId", nextTransition.transition.toString());
                nbt.putLong("transitionInTicks", nextTransition.getTicksLeft());
            }

            if (handGlowTimer != null && handGlowTimer.getTicksLeft() >= 0) {
                nbt.putString("handGlowState", handGlowTimer.transition.toString());
                nbt.putLong("handGlowScheduledTicks", handGlowTimer.getTicksLeft());
            }
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            if (nbt.contains("transitionId"))
                scheduleTransitionInTicks(RegenStates.Transition.valueOf(nbt.getString("transitionId")), nbt.getLong("transitionInTicks"));

            if (nbt.contains("handGlowState")) {
                RegenStates.Transition transition = RegenStates.Transition.valueOf(nbt.getString("handGlowState"));

                Runnable callback = switch (transition) {
                    case HAND_GLOW_START -> this::scheduleHandGlowTrigger;
                    case HAND_GLOW_TRIGGER -> this::triggerRegeneration;
                    default -> throw new IllegalStateException("Illegal hand glow timer transition");
                };

                handGlowTimer = new RegenScheduledAction(transition, player, callback, nbt.getLong("handGlowScheduledTicks"));
            }
        }

    }

}
