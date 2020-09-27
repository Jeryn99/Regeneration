package me.swirtzly.regen.common.regen;

import me.swirtzly.regen.common.regen.acting.ActingForwarder;
import me.swirtzly.regen.common.regen.state.IStateManager;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.common.regen.transitions.TransitionType;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.network.Dispatcher;
import me.swirtzly.regen.network.SyncMessage;
import me.swirtzly.regen.util.PlayerUtil;
import me.swirtzly.regen.util.RConstants;
import me.swirtzly.regen.util.RegenSources;
import me.swirtzly.regen.util.schedule.RegenScheduledAction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RegenCap implements IRegen {

    //Injection
    @CapabilityInject(IRegen.class)
    public static final Capability<IRegen> CAPABILITY = null;

    //Data
    private final LivingEntity livingEntity;
    private int regensLeft = 0;

    //State
    private final StateManager stateManager;
    private RegenStates currentState = RegenStates.ALIVE;
    private TransitionTypes transitionType = TransitionTypes.FIERY;

    // Color Data
    private float primaryRed = 0.93f, primaryGreen = 0.61f, primaryBlue = 0.0f;
    private float secondaryRed = 1f, secondaryGreen = 0.5f, secondaryBlue = 0.18f;
    private String deathMessage = "";

    public RegenCap() {
        this.livingEntity = null;
        this.stateManager = null;
    }

    public RegenCap(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
        if (!livingEntity.world.isRemote)
            this.stateManager = new StateManager();
        else
            this.stateManager = null;
    }


    // ==== Setters and Getters ====
    @Override
    public void setRegens(int regens) {
        this.regensLeft = regens;
    }

    @Override
    public int getRegens() {
        return regensLeft;
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean canRegenerate() {
        return getRegens() > 0 && livingEntity.getPosY() > 0;
    }

    @Override
    public boolean areHandsGlowing() {
        return false;
    }

    @Override
    public RegenStates getCurrentState() {
        return currentState;
    }

    @Override
    public StateManager getStateManager() {
        return stateManager;
    }

    @Override
    public void readStyle(CompoundNBT colors) {
        primaryRed = colors.getFloat(RConstants.PRIMARY_RED);
        primaryGreen = colors.getFloat(RConstants.PRIMARY_GREEN);
        primaryBlue = colors.getFloat(RConstants.PRIMARY_BLUE);

        secondaryRed = colors.getFloat(RConstants.SECONDARY_RED);
        secondaryGreen = colors.getFloat(RConstants.SECONDARY_GREEN);
        secondaryBlue = colors.getFloat(RConstants.SECONDARY_BLUE);
    }

    @Override
    public CompoundNBT getOrWriteStyle() {
        CompoundNBT colors = new CompoundNBT();
        colors.putFloat(RConstants.PRIMARY_RED, primaryRed);
        colors.putFloat(RConstants.PRIMARY_GREEN, primaryGreen);
        colors.putFloat(RConstants.PRIMARY_BLUE, primaryBlue);
        colors.putFloat(RConstants.SECONDARY_RED, secondaryRed);
        colors.putFloat(RConstants.SECONDARY_GREEN, secondaryGreen);
        colors.putFloat(RConstants.SECONDARY_BLUE, secondaryBlue);
        return colors;
    }

    @Override
    public void extractRegens(int amount) {
        regensLeft = regensLeft - amount;
    }

    @Override
    public void addRegens(int amount) {
        regensLeft = regensLeft + amount;
    }

    @Override
    public LivingEntity getLiving() {
        return livingEntity;
    }

    @Override
    public void syncToClients(@Nullable ServerPlayerEntity serverPlayerEntity) {
        if (serverPlayerEntity == null) {
            Dispatcher.NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncMessage(this.livingEntity.getEntityId(), this.serializeNBT()));
        } else {
            Dispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayerEntity), new SyncMessage(this.livingEntity.getEntityId(), this.serializeNBT()));
        }
    }


    @Nonnull
    public static LazyOptional<IRegen> get(LivingEntity player) {
        return player.getCapability(RegenCap.CAPABILITY, null);
    }


    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt(RConstants.REGENS_LEFT, regensLeft);
        compoundNBT.putString(RConstants.CURRENT_STATE, currentState.name());
        compoundNBT.put(RConstants.STYLE, getOrWriteStyle());
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        serializeNBT();
        setRegens(nbt.getInt(RConstants.REGENS_LEFT));
        currentState = nbt.contains(RConstants.CURRENT_STATE) ? RegenStates.valueOf(nbt.getString(RConstants.CURRENT_STATE)) : RegenStates.ALIVE;
       //TODO crashes readStyle((CompoundNBT) Objects.requireNonNull(nbt.get(RConstants.STYLE)));
    }

    @Override
    public TransitionTypes getTransitionType() {
        return transitionType;
    }

    @Override
    public void setTransitionType(TransitionTypes transitionType) {
        this.transitionType = transitionType;
    }

    @Override
    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    @Override
    public String getDeathMessage() {
        return this.deathMessage;
    }

    public class StateManager implements IStateManager {

        private final Map<RegenStates.Transition, Runnable> transitionCallbacks;
        private RegenScheduledAction nextTransition, handGlowTimer;

        private StateManager() {
            this.transitionCallbacks = new HashMap<>();
            transitionCallbacks.put(RegenStates.Transition.ENTER_CRITICAL, this::enterCriticalPhase);
            transitionCallbacks.put(RegenStates.Transition.CRITICAL_DEATH, this::midSequenceKill);
            transitionCallbacks.put(RegenStates.Transition.FINISH_REGENERATION, this::finishRegeneration);
            transitionCallbacks.put(RegenStates.Transition.END_POST, this::endPost);

            Runnable err = () -> {
                throw new IllegalStateException("Can't use HAND_GLOW_* transitions as state transitions");
            };
            transitionCallbacks.put(RegenStates.Transition.HAND_GLOW_START, err);
            transitionCallbacks.put(RegenStates.Transition.HAND_GLOW_TRIGGER, err);
        }

        @SuppressWarnings("deprecation")
        private void scheduleTransitionInTicks(RegenStates.Transition transition, long inTicks) {
            if (nextTransition != null && nextTransition.getTicksLeft() > 0)
                throw new IllegalStateException("Overwriting non-completed/cancelled transition: " + "\n Attempted Transition: " + transition.name() + "\n Current: " + nextTransition.transition.name() + "\n Affected Player: " + livingEntity.getName());

            if (transition == RegenStates.Transition.HAND_GLOW_START || transition == RegenStates.Transition.HAND_GLOW_TRIGGER)
                throw new IllegalStateException("Can't use HAND_GLOW_* transitions as state transitions");

            nextTransition = new RegenScheduledAction(transition, livingEntity, transitionCallbacks.get(transition), inTicks);
        }

        private void scheduleTransitionInSeconds(RegenStates.Transition transition, long inSeconds) {
            scheduleTransitionInTicks(transition, inSeconds * 20);
        }

        @SuppressWarnings("deprecation")
        private void scheduleNextHandGlow() {
            if (currentState.isGraceful() && handGlowTimer.getTicksLeft() > 0)
                throw new IllegalStateException("Overwriting running hand-glow timer with new next hand glow");
            handGlowTimer = new RegenScheduledAction(RegenStates.Transition.HAND_GLOW_START, livingEntity, this::scheduleHandGlowTrigger, RegenConfig.COMMON.handGlowInterval.get() * 20);
            syncToClients(null);
        }

        @SuppressWarnings("deprecation")
        private void scheduleHandGlowTrigger() {
            if (currentState.isGraceful() && handGlowTimer.getTicksLeft() > 0)
                throw new IllegalStateException("Overwriting running hand-glow timer with trigger timer prematurely");
            handGlowTimer = new RegenScheduledAction(RegenStates.Transition.HAND_GLOW_TRIGGER, livingEntity, this::triggerRegeneration, RegenConfig.COMMON.handGlowTriggerDelay.get() * 20);
            ActingForwarder.onHandsStartGlowing(RegenCap.this);
            syncToClients(null);
        }

        @Override
        public boolean onKilled(DamageSource source) {

            if (source == DamageSource.IN_WALL || source == DamageSource.CRAMMING) {
                return false;
            }

            if (currentState == RegenStates.ALIVE) {
                if (!canRegenerate()) // that's too bad :(
                    return false;

                // We're entering grace period...
                scheduleTransitionInSeconds(RegenStates.Transition.ENTER_CRITICAL, RegenConfig.COMMON.gracePhaseLength.get());
                scheduleHandGlowTrigger();

                currentState = RegenStates.GRACE;
                syncToClients(null);
                ActingForwarder.onEnterGrace(RegenCap.this);
                return true;

            } else if (currentState == RegenStates.GRACE || currentState == RegenStates.GRACE_CRIT) {
                // We're being forced to regenerate...
                triggerRegeneration();
                return true;

            } else if (currentState == RegenStates.REGENERATING) {
                // We've been killed mid regeneration!
                nextTransition.cancel(); // ... cancel the finishing of the regeneration
                midSequenceKill();
                return false;

            } else if (currentState == RegenStates.POST) {
                currentState = RegenStates.ALIVE;
                nextTransition.cancel();
                midSequenceKill();
                return false;
            } else
                throw new IllegalStateException("Unknown state: " + currentState);
        }

        @Override
        public void onPunchEntity(LivingHurtEvent event) {
            LivingEntity entity = event.getEntityLiving();
            // We're healing mobs...
            if (currentState.isGraceful() && entity.getHealth() < entity.getMaxHealth() && areHandsGlowing() && livingEntity.isSneaking()) { // ... check if we're in grace and if the mob needs health
                float healthNeeded = entity.getMaxHealth() - entity.getHealth();
                entity.heal(healthNeeded);
                if (livingEntity instanceof PlayerEntity) {
                    PlayerUtil.sendMessage(livingEntity, new TranslationTextComponent("regeneration.messages.healed", entity.getName()), true);
                }
                event.setAmount(0.0F);
                livingEntity.attackEntityFrom(RegenSources.REGEN_DMG_HEALING, healthNeeded);
            }
        }

        @Override
        public void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {
            if (currentState.isGraceful() && areHandsGlowing()) {

                BlockState block = e.getWorld().getBlockState(e.getPos());

                if (block.getBlock() == Blocks.SNOW || block.getBlock() == Blocks.SNOW_BLOCK) {
                    e.getWorld().playSound(null, e.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
                }

                handGlowTimer.cancel();
                scheduleNextHandGlow();
                if (!livingEntity.world.isRemote) {
                    if (livingEntity instanceof PlayerEntity) {
                        PlayerUtil.sendMessage(livingEntity, new TranslationTextComponent("regeneration.messages.regen_delayed"), true);
                    }
                }
                e.setCanceled(true); // It got annoying in creative to break something
            }
        }

        private void tick() {
            if (livingEntity.world.isRemote)
                throw new IllegalStateException("Ticking state manager on the client"); // the state manager shouldn't even exist on the client
            if (currentState == RegenStates.ALIVE)
                throw new IllegalStateException("Ticking dormant state manager (state == ALIVE)"); // would NPE when ticking the transition, but this is a more clear message

            if (currentState.isGraceful()) handGlowTimer.tick();

            ActingForwarder.onRegenTick(RegenCap.this);
            nextTransition.tick();

            if (currentState == RegenStates.POST) {
                ActingForwarder.onPerformingPost(RegenCap.this);
            }
        }

        private void triggerRegeneration() {
            // We're starting a regeneration!
            currentState = RegenStates.REGENERATING;

            if (RegenConfig.COMMON.sendRegenDeathMessages.get()) {
                if (livingEntity instanceof PlayerEntity) {
                    TranslationTextComponent text = new TranslationTextComponent("regeneration.messages.regen_chat_message", livingEntity.getName());
                    //TODO Objects.requireNonNull(text.getStyle().getHoverEvent()).func_240662_a_(new HoverEvent(HoverEvent.Action.field_230552_c_, new StringTextComponent(getDeathMessage()));
                    PlayerUtil.sendMessageToAll(text);
                }
            }

            nextTransition.cancel(); // ... cancel any state shift we had planned
            if (currentState.isGraceful()) handGlowTimer.cancel();
            scheduleTransitionInTicks(RegenStates.Transition.FINISH_REGENERATION, transitionType.create().getAnimationLength());

            ActingForwarder.onRegenTrigger(RegenCap.this);
            transitionType.create().onStartRegeneration(RegenCap.this);
            syncToClients(null);
        }

        private void enterCriticalPhase() {
            // We're entering critical phase...
            currentState = RegenStates.GRACE_CRIT;
            scheduleTransitionInSeconds(RegenStates.Transition.CRITICAL_DEATH, RegenConfig.COMMON.criticalPhaseLength.get());
            ActingForwarder.onGoCritical(RegenCap.this);
            syncToClients(null);
        }

        private void midSequenceKill() {
            currentState = RegenStates.ALIVE;
            nextTransition = null;
            handGlowTimer = null;
            transitionType.create().onFinishRegeneration(RegenCap.this);
            if (currentState == RegenStates.GRACE_CRIT) {
                livingEntity.attackEntityFrom(RegenSources.REGEN_DMG_CRITICAL, Integer.MAX_VALUE);
            } else {
                livingEntity.attackEntityFrom(RegenSources.REGEN_DMG_KILLED, Integer.MAX_VALUE);
            }

            if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                extractRegens(getRegens());
            }
            syncToClients(null);
        }

        private void endPost() {
            currentState = RegenStates.ALIVE;
            syncToClients(null);
            nextTransition = null;
            if (livingEntity instanceof PlayerEntity) {
                PlayerUtil.sendMessage(livingEntity, new TranslationTextComponent("regeneration.messages.post_ended"), true);
            }
            //TODO setDroppedHand(false);
        }

        private void finishRegeneration() {
            currentState = RegenStates.POST;
            scheduleTransitionInSeconds(RegenStates.Transition.END_POST, livingEntity.world.rand.nextInt(300));
            handGlowTimer = null;
            transitionType.create().onFinishRegeneration(RegenCap.this);
            ActingForwarder.onRegenFinish(RegenCap.this);
            syncToClients(null);
        }

        @Override
        @Deprecated
        /** @deprecated Debug purposes */
        public Pair<RegenStates.Transition, Long> getScheduledEvent() {
            return nextTransition == null ? null : Pair.of(nextTransition.transition, nextTransition.getTicksLeft());
        }

        @Override
        @Deprecated
        /** @deprecated Debug purposes */
        public void fastForward() {
            while (!nextTransition.tick()) ;
        }

        @Override
        @Deprecated
        /** @deprecated Debug purposes */
        public void fastForwardHandGlow() {
            while (!handGlowTimer.tick()) ;
        }

        @Override
        public double getStateProgress() {
            return nextTransition.getProgress();
        }

        @SuppressWarnings("deprecation")
        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
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
        public void deserializeNBT(CompoundNBT nbt) {
            if (nbt.contains("transitionId"))
                scheduleTransitionInTicks(RegenStates.Transition.valueOf(nbt.getString("transitionId")), nbt.getLong("transitionInTicks"));

            if (nbt.contains("handGlowState")) {
                RegenStates.Transition transition = RegenStates.Transition.valueOf(nbt.getString("handGlowState"));

                Runnable callback;
                if (transition == RegenStates.Transition.HAND_GLOW_START)
                    callback = this::scheduleHandGlowTrigger;
                else if (transition == RegenStates.Transition.HAND_GLOW_TRIGGER)
                    callback = this::triggerRegeneration;
                else
                    throw new IllegalStateException("Illegal hand glow timer transition");

                handGlowTimer = new RegenScheduledAction(transition, livingEntity, callback, nbt.getLong("handGlowScheduledTicks"));
            }
        }
    }


}
