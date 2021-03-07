package me.suff.mc.regen.common.regen;

import me.suff.mc.regen.common.regen.acting.ActingForwarder;
import me.suff.mc.regen.common.regen.state.IStateManager;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.common.traits.Traits;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.SyncMessage;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenSources;
import me.suff.mc.regen.util.schedule.RegenScheduledAction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class RegenCap implements IRegen {

    //Injection
    @CapabilityInject(IRegen.class)
    public static final Capability< IRegen > CAPABILITY = null;
    //State
    private final StateManager stateManager;
    //Data
    private final LivingEntity livingEntity;
    //Don't save to disk
    private boolean didSetup = false;
    // Color Data
    private float primaryRed = 0.93f, primaryGreen = 0.61f, primaryBlue = 0.0f;
    private float secondaryRed = 1f, secondaryGreen = 0.5f, secondaryBlue = 0.18f;
    private boolean isAlex = false;
    private byte[] skinArray = new byte[0];
    private int regensLeft = 0, ticksAnimating = 0;
    private String deathMessage = "";
    private RegenStates currentState = RegenStates.ALIVE;
    private TransitionTypes transitionType = TransitionTypes.FIERY;
    private boolean areHandsGlowing = false;
    private PlayerUtil.SkinType preferredSkinType = PlayerUtil.SkinType.ALEX;
    private boolean nextSkinTypeAlex = false;
    private byte[] nextSkin = new byte[0];
    private Traits.ITrait currentTrait = Traits.BORING.get();
    private Traits.ITrait nextTrait = Traits.BORING.get();
    private TimelordSound timelordSound = TimelordSound.HUM;
    private Hand handState = Hand.NO_GONE;

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

    @Nonnull
    public static LazyOptional< IRegen > get(LivingEntity player) {
        return player.getCapability(RegenCap.CAPABILITY, null);
    }

    @Override
    public int getRegens() {
        return regensLeft;
    }

    // ==== Setters and Getters ====
    @Override
    public void setRegens(int regens) {
        this.regensLeft = regens;
    }

    @Override
    public void tick() {

        if (!livingEntity.world.isRemote) {

            //Login setup
            if (!didSetup) {
                syncToClients(null);
                didSetup = true;
            }

            //Tick Trait
            currentTrait.tick(this);

            //Tick State Manager
            if (currentState != RegenStates.ALIVE) {
                if (stateManager != null) {
                    stateManager.tick();
                }
            }

            //Update Regeneratinh
            if (currentState == RegenStates.REGENERATING) {
                ticksAnimating++;
                transitionType.get().onUpdateMidRegen(this);
                syncToClients(null);
            } else {
                ticksAnimating = 0;
            }
        }
    }

    @Override
    public int getTicksAnimating() {
        return ticksAnimating;
    }

    @Override
    public void setAnimationTicks(int ticksAnimating) {
        this.ticksAnimating = ticksAnimating;
    }

    @Override
    public boolean canRegenerate() {
        return getRegens() > 0 && livingEntity.getPosY() > 0 && currentState != RegenStates.POST;
    }

    @Override
    public boolean areHandsGlowing() {
        return areHandsGlowing;
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
        if (livingEntity != null && livingEntity.world.isRemote)
            throw new IllegalStateException("Don't sync client -> server");

        areHandsGlowing = getCurrentState().isGraceful() && stateManager.handGlowTimer.getTransition() == RegenStates.Transition.HAND_GLOW_TRIGGER;

        CompoundNBT nbt = serializeNBT();
        nbt.remove(RConstants.STATE_MANAGER);

        if (serverPlayerEntity == null) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(() -> livingEntity.getEntityWorld().getDimensionKey()), new SyncMessage(this.livingEntity.getEntityId(), nbt));
        } else {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayerEntity), new SyncMessage(this.livingEntity.getEntityId(), nbt));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt(RConstants.REGENS_LEFT, regensLeft);
        compoundNBT.putString(RConstants.CURRENT_STATE, currentState.name());
        compoundNBT.putInt(RConstants.ANIMATION_TICKS, ticksAnimating);
        compoundNBT.putString(RConstants.TRANSITION_TYPE, transitionType.get().getRegistryName().toString());
        compoundNBT.putString(RConstants.PREFERENCE, preferredSkinType.name());
        compoundNBT.putBoolean(RConstants.IS_ALEX, isAlexSkinCurrently());
        compoundNBT.putBoolean(RConstants.GLOWING, areHandsGlowing());
        compoundNBT.putString(RConstants.CURRENT_TRAIT, currentTrait.getRegistryName().toString());
        compoundNBT.putString(RConstants.NEXT_TRAIT, nextTrait.getRegistryName().toString());
        compoundNBT.putString(RConstants.SOUND_SCHEME, getTimelordSound().name());
        compoundNBT.putString(RConstants.HAND_STATE, getHandState().name());
        compoundNBT.putBoolean("next_" + RConstants.IS_ALEX, isNextSkinTypeAlex());
        if (isSkinValidForUse()) {
            compoundNBT.putByteArray(RConstants.SKIN, skinArray);
        }

        if (isNextSkinValid()) {
            compoundNBT.putByteArray("next_" + RConstants.SKIN, nextSkin);
        }

        if (!livingEntity.world.isRemote) {
            if (stateManager != null) {
                compoundNBT.put(RConstants.STATE_MANAGER, stateManager.serializeNBT());
            }
        }

        compoundNBT.put(RConstants.COLORS, getOrWriteStyle());

        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setRegens(nbt.getInt(RConstants.REGENS_LEFT));
        currentState = nbt.contains(RConstants.CURRENT_STATE) ? RegenStates.valueOf(nbt.getString(RConstants.CURRENT_STATE)) : RegenStates.ALIVE;
        setAnimationTicks(nbt.getInt(RConstants.ANIMATION_TICKS));
        setSkin(nbt.getByteArray(RConstants.SKIN));
        setNextSkin(nbt.getByteArray("next_" + RConstants.SKIN));
        setAlexSkin(nbt.getBoolean(RConstants.IS_ALEX));
        setNextSkinType(nbt.getBoolean("next_" + RConstants.IS_ALEX));
        if (nbt.contains(RConstants.SOUND_SCHEME)) {
            setTimelordSound(TimelordSound.valueOf(nbt.getString(RConstants.SOUND_SCHEME)));
        }

        if (nbt.contains(RConstants.HAND_STATE)) {
            setHandState(Hand.valueOf(nbt.getString(RConstants.HAND_STATE)));
        }
        areHandsGlowing = nbt.getBoolean(RConstants.GLOWING);
        setTrait(Traits.fromID(nbt.getString(RConstants.CURRENT_TRAIT)));
        setNextTrait(Traits.fromID(nbt.getString(RConstants.NEXT_TRAIT)));
        if (nbt.contains(RConstants.PREFERENCE)) {
            setPreferredModel(PlayerUtil.SkinType.valueOf(nbt.getString(RConstants.PREFERENCE)));
        }
        //RegenType
        if (nbt.contains(RConstants.TRANSITION_TYPE)) {
            transitionType = TransitionTypes.REGISTRY.getValue(new ResourceLocation(nbt.getString(RConstants.TRANSITION_TYPE)));
        }


        //Statemanager
        if (nbt.contains(RConstants.STATE_MANAGER)) if (stateManager != null) {
            stateManager.deserializeNBT((CompoundNBT) nbt.get(RConstants.STATE_MANAGER));
        }

        if (nbt.contains(RConstants.COLORS)) {
            readStyle((CompoundNBT) nbt.get(RConstants.COLORS));
        }
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
    public String getDeathMessage() {
        return this.deathMessage;
    }

    @Override
    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    @Override
    public void regen() {
        if (livingEntity != null) {
            livingEntity.attackEntityFrom(RegenSources.REGEN_DMG_FORCED, Integer.MAX_VALUE);
        }
    }

    @Override
    public byte[] getSkin() {
        return skinArray;
    }

    @Override
    public void setSkin(byte[] skin) {
        this.skinArray = skin;
    }

    @Override
    public boolean isSkinValidForUse() {
        return !Arrays.equals(skinArray, new byte[0]);
    }

    @Override
    public Vector3d getPrimaryColors() {
        return new Vector3d(primaryRed, primaryGreen, primaryBlue);
    }

    @Override
    public Vector3d getSecondaryColors() {
        return new Vector3d(secondaryRed, secondaryGreen, secondaryBlue);
    }

    @Override
    public boolean isAlexSkinCurrently() {
        return isAlex;
    }

    @Override
    public void setAlexSkin(boolean isAlex) {
        this.isAlex = isAlex;
    }

    @Override
    public PlayerUtil.SkinType getPreferredModel() {
        return preferredSkinType;
    }

    @Override
    public void setPreferredModel(PlayerUtil.SkinType skinType) {
        this.preferredSkinType = skinType;
    }

    @Override
    public byte[] getNextSkin() {
        return nextSkin;
    }

    @Override
    public void setNextSkin(byte[] bytes) {
        this.nextSkin = bytes;
    }

    @Override
    public boolean isNextSkinValid() {
        return !Arrays.equals(nextSkin, new byte[0]);
    }

    @Override
    public void setNextSkinType(boolean isAlex) {
        this.nextSkinTypeAlex = isAlex;
    }

    @Override
    public boolean isNextSkinTypeAlex() {
        return nextSkinTypeAlex;
    }

    @Override
    public Traits.ITrait getTrait() {
        return currentTrait;
    }

    @Override
    public void setTrait(Traits.ITrait trait) {
        this.currentTrait = trait;
    }

    @Override
    public Traits.ITrait getNextTrait() {
        return nextTrait;
    }

    @Override
    public void setNextTrait(Traits.ITrait trait) {
        this.nextTrait = trait;
    }

    @Override
    public TimelordSound getTimelordSound() {
        return timelordSound;
    }

    @Override
    public void setTimelordSound(TimelordSound timelordSound) {
        this.timelordSound = timelordSound;
    }

    @Override
    public Hand getHandState() {
        return handState;
    }

    @Override
    public void setHandState(Hand handState) {
        this.handState = handState;
    }

    public class StateManager implements IStateManager {

        private final Map< RegenStates.Transition, Runnable > transitionCallbacks;
        private RegenScheduledAction nextTransition, handGlowTimer;

        private StateManager() {
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

            if (source == RegenSources.REGEN_DMG_CRITICAL) {
                if (nextTransition != null) {
                    nextTransition.cancel();
                }
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

            } else if (currentState == RegenStates.GRACE) {
                // We're being forced to regenerate...
                triggerRegeneration();
                return true;

            } else if (currentState == RegenStates.REGENERATING) {
                // We've been killed mid regeneration!
                nextTransition.cancel(); // ... cancel the finishing of the regeneration
                midSequenceKill(false);
                return false;

            } else if (currentState == RegenStates.GRACE_CRIT) {
                nextTransition.cancel();
                if (source == RegenSources.REGEN_DMG_FORCED) {
                    triggerRegeneration();
                    return true;
                } else {
                    midSequenceKill(true);
                    return false;
                }
            } else if (currentState == RegenStates.POST) {
                currentState = RegenStates.ALIVE;
                nextTransition.cancel();
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
                    PlayerUtil.sendMessage(livingEntity, new TranslationTextComponent("regen.messages.healed", entity.getName()), true);
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
                        PlayerUtil.sendMessage(livingEntity, new TranslationTextComponent("regen.messages.regen_delayed"), true);
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

            if (nextTransition != null) {
                nextTransition.tick();
            }

            if (currentState == RegenStates.POST) {
                ActingForwarder.onPerformingPost(RegenCap.this);
            }
        }

        private void triggerRegeneration() {
            // We're starting a regeneration!
            currentState = RegenStates.REGENERATING;

            if (RegenConfig.COMMON.sendRegenDeathMessages.get()) {
                if (livingEntity instanceof PlayerEntity) {
                    TranslationTextComponent text = new TranslationTextComponent("regen.messages.regen_death_msg", livingEntity.getName());
                    text.setStyle(text.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(getDeathMessage()))));
                    PlayerUtil.sendMessageToAll(text);
                }
            }

            nextTransition.cancel(); // ... cancel any state shift we had planned
            if (currentState.isGraceful()) handGlowTimer.cancel();
            scheduleTransitionInTicks(RegenStates.Transition.FINISH_REGENERATION, transitionType.get().getAnimationLength());

            ActingForwarder.onRegenTrigger(RegenCap.this);
            transitionType.get().onStartRegeneration(RegenCap.this);
            syncToClients(null);
        }

        private void enterCriticalPhase() {
            // We're entering critical phase...
            currentState = RegenStates.GRACE_CRIT;
            scheduleTransitionInSeconds(RegenStates.Transition.CRITICAL_DEATH, RegenConfig.COMMON.criticalPhaseLength.get());
            ActingForwarder.onGoCritical(RegenCap.this);
            syncToClients(null);
        }

        private void midSequenceKill(boolean isGrace) {
            currentState = RegenStates.ALIVE;
            nextTransition = null;
            handGlowTimer = null;
            transitionType.get().onFinishRegeneration(RegenCap.this);
            livingEntity.attackEntityFrom(isGrace ? RegenSources.REGEN_DMG_CRITICAL : RegenSources.REGEN_DMG_KILLED, Integer.MAX_VALUE);
            if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                extractRegens(getRegens());
            }
            setSkin(new byte[0]);
            syncToClients(null);
        }

        private void endPost() {
            currentState = RegenStates.ALIVE;
            syncToClients(null);
            nextTransition = null;
            if (livingEntity instanceof PlayerEntity) {
                PlayerUtil.sendMessage(livingEntity, new TranslationTextComponent("regen.messages.post_ended"), true);
            }
            handState = Hand.NO_GONE;
        }

        private void finishRegeneration() {
            currentState = RegenStates.POST;
            scheduleTransitionInSeconds(RegenStates.Transition.END_POST, livingEntity.world.rand.nextInt(300) + 10);
            handGlowTimer = null;
            transitionType.get().onFinishRegeneration(RegenCap.this);
            ActingForwarder.onRegenFinish(RegenCap.this);
            syncToClients(null);
        }

        @Override
        @Deprecated
        /** @deprecated Debug purposes */
        public Pair< RegenStates.Transition, Long > getScheduledEvent() {
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
            if (nextTransition != null) {
                return nextTransition.getProgress();
            }
            return 0;
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
