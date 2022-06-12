package me.suff.mc.regen.common.regen;

import me.suff.mc.regen.common.advancement.TriggerManager;
import me.suff.mc.regen.common.regen.acting.ActingForwarder;
import me.suff.mc.regen.common.regen.state.IStateManager;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.common.traits.AbstractTrait;
import me.suff.mc.regen.common.traits.RegenTraitRegistry;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.SyncMessage;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenSources;
import me.suff.mc.regen.util.schedule.RegenScheduledAction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegenCap implements IRegen {

    //Injection

    public static final Capability<IRegen> CAPABILITY = CapabilityManager.get(new CapabilityToken<IRegen>() {
    });
    //State
    private final StateManager stateManager;
    //Data
    private final LivingEntity livingEntity;
    //Don't save to disk
    private boolean didSetup = false;
    // Color Data
    private float primaryRed = 0.69411767f, primaryGreen = 0.74509805f, primaryBlue = 0.23529412f;
    private float secondaryRed = 0.7137255f, secondaryGreen = 0.75686276f, secondaryBlue = 0.25490198f;
    private boolean isAlex = false;
    private byte[] skinArray = new byte[0];
    private int regensLeft = 0, animationTicks = 0;
    private String deathMessage = "";
    private RegenStates currentState = RegenStates.ALIVE;
    private TransitionType transitionType = TransitionTypes.FIERY.get();
    private boolean areHandsGlowing = false, traitActive = true;
    private PlayerUtil.SkinType preferredSkinType = PlayerUtil.SkinType.ALEX;
    private boolean nextSkinTypeAlex = false;
    private byte[] nextSkin = new byte[0];
    private AbstractTrait currentTrait = RegenTraitRegistry.BORING.get();
    private AbstractTrait nextTrait = RegenTraitRegistry.BORING.get();
    private TimelordSound timelordSound = TimelordSound.HUM;
    private Hand handState = Hand.NO_GONE;

    public RegenCap() {
        this.livingEntity = null;
        this.stateManager = null;
    }

    public RegenCap(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
        if (!livingEntity.level.isClientSide)
            this.stateManager = new StateManager();
        else
            this.stateManager = null;
    }

    @Nonnull
    public static LazyOptional<IRegen> get(LivingEntity player) {
        return player.getCapability(RegenCap.CAPABILITY, null);
    }

    @Override
    public int regens() {
        return regensLeft;
    }

    // ==== Setters and Getters ====
    @Override
    public void setRegens(int regens) {
        this.regensLeft = regens;
    }

    @Override
    public void tick() {
        if (livingEntity.level.isClientSide) return;
        //Login setup
        if (!didSetup) {
            syncToClients(null);
            didSetup = true;
        }

        //Tick Trait
        if (traitActive) {
            currentTrait.tick(this);
        }
        if (stateManager != null && currentState != RegenStates.ALIVE) {
            stateManager.tick();
        }

        //Tick Regenerating
        if (currentState == RegenStates.REGENERATING) {
            animationTicks++;
            transitionType.onUpdateMidRegen(this);
            syncToClients(null);
            return;
        }
        animationTicks = 0;
    }

    @Override
    public int updateTicks() {
        return animationTicks;
    }

    @Override
    public void setUpdateTicks(int animationTicks) {
        this.animationTicks = animationTicks;
    }

    @Override
    public boolean canRegenerate() {
        if (livingEntity != null) {
            return regens() > 0 && livingEntity.getY() > livingEntity.getCommandSenderWorld().getMinBuildHeight() && currentState != RegenStates.POST;
        }
        return false;
    }

    @Override
    public boolean glowing() {
        return areHandsGlowing;
    }

    @Override
    public RegenStates regenState() {
        return currentState;
    }

    @Override
    public StateManager stateManager() {
        return stateManager;
    }

    @Override
    public void readStyle(CompoundTag colors) {
        primaryRed = colors.getFloat(RConstants.PRIMARY_RED);
        primaryGreen = colors.getFloat(RConstants.PRIMARY_GREEN);
        primaryBlue = colors.getFloat(RConstants.PRIMARY_BLUE);

        secondaryRed = colors.getFloat(RConstants.SECONDARY_RED);
        secondaryGreen = colors.getFloat(RConstants.SECONDARY_GREEN);
        secondaryBlue = colors.getFloat(RConstants.SECONDARY_BLUE);
    }

    @Override
    public CompoundTag getOrWriteStyle() {
        CompoundTag colors = new CompoundTag();
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
    public void syncToClients(@Nullable ServerPlayer serverPlayerEntity) {
        if (livingEntity != null && livingEntity.level.isClientSide)
            throw new IllegalStateException("Don't sync client -> server");

        areHandsGlowing = regenState().isGraceful() && stateManager.handGlowTimer.getTransition() == RegenStates.Transition.HAND_GLOW_TRIGGER;

        CompoundTag nbt = serializeNBT();
        nbt.remove(RConstants.STATE_MANAGER);

        if (serverPlayerEntity == null) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(() -> livingEntity.getCommandSenderWorld().dimension()), new SyncMessage(this.livingEntity.getId(), nbt));
        } else {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayerEntity), new SyncMessage(this.livingEntity.getId(), nbt));
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putInt(RConstants.REGENS_LEFT, regens());
        compoundNBT.putString(RConstants.CURRENT_STATE, regenState().name());
        compoundNBT.putInt(RConstants.ANIMATION_TICKS, updateTicks());

        if (transitionType == null) {
            transitionType = TransitionTypes.FIERY.get();
        }

        compoundNBT.putString(RConstants.TRANSITION_TYPE, TransitionTypes.getTransitionId(transitionType).toString());
        compoundNBT.putString(RConstants.PREFERENCE, preferredModel().name());
        compoundNBT.putBoolean(RConstants.IS_ALEX, currentlyAlex());
        compoundNBT.putBoolean(RConstants.GLOWING, glowing());
        compoundNBT.putString(RConstants.CURRENT_TRAIT, RegenTraitRegistry.getTraitLocation(currentTrait).toString());
        compoundNBT.putString(RConstants.NEXT_TRAIT, RegenTraitRegistry.getTraitLocation(nextTrait).toString());
        compoundNBT.putString(RConstants.SOUND_SCHEME, getTimelordSound().name());
        compoundNBT.putString(RConstants.HAND_STATE, handState().name());
        compoundNBT.putBoolean(RConstants.IS_TRAIT_ACTIVE, traitActive);
        compoundNBT.putBoolean("next_" + RConstants.IS_ALEX, isNextSkinTypeAlex());
        if (isSkinValidForUse()) {
            compoundNBT.putByteArray(RConstants.SKIN, skin());
        }

        if (isNextSkinValid()) {
            compoundNBT.putByteArray("next_" + RConstants.SKIN, nextSkin());
        }

        if (!livingEntity.level.isClientSide) {
            if (stateManager != null) {
                compoundNBT.put(RConstants.STATE_MANAGER, stateManager.serializeNBT());
            }
        }

        compoundNBT.put(RConstants.COLORS, getOrWriteStyle());

        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setRegens(nbt.getInt(RConstants.REGENS_LEFT));
        currentState = nbt.contains(RConstants.CURRENT_STATE) ? RegenStates.valueOf(nbt.getString(RConstants.CURRENT_STATE)) : RegenStates.ALIVE;
        setUpdateTicks(nbt.getInt(RConstants.ANIMATION_TICKS));
        setSkin(nbt.getByteArray(RConstants.SKIN));
        setNextSkin(nbt.getByteArray("next_" + RConstants.SKIN));
        setAlexSkin(nbt.getBoolean(RConstants.IS_ALEX));
        traitActive = nbt.getBoolean(RConstants.IS_TRAIT_ACTIVE);
        setNextSkinType(nbt.getBoolean("next_" + RConstants.IS_ALEX));
        if (nbt.contains(RConstants.SOUND_SCHEME)) {
            setTimelordSound(TimelordSound.valueOf(nbt.getString(RConstants.SOUND_SCHEME)));
        }

        if (nbt.contains(RConstants.HAND_STATE)) {
            setHandState(Hand.valueOf(nbt.getString(RConstants.HAND_STATE)));
        }
        areHandsGlowing = nbt.getBoolean(RConstants.GLOWING);
        setTrait(RegenTraitRegistry.fromID(nbt.getString(RConstants.CURRENT_TRAIT)));
        setNextTrait(RegenTraitRegistry.fromID(nbt.getString(RConstants.NEXT_TRAIT)));
        if (nbt.contains(RConstants.PREFERENCE)) {
            setPreferredModel(PlayerUtil.SkinType.valueOf(nbt.getString(RConstants.PREFERENCE)));
        }
        //RegenType
        if (nbt.contains(RConstants.TRANSITION_TYPE)) {
            transitionType = TransitionTypes.TRANSITION_TYPES_REGISTRY.get().getValue(new ResourceLocation(nbt.getString(RConstants.TRANSITION_TYPE)));
        }


        //State Manager
        if (nbt.contains(RConstants.STATE_MANAGER)) if (stateManager != null) {
            stateManager.deserializeNBT((CompoundTag) nbt.get(RConstants.STATE_MANAGER));
        }

        if (nbt.contains(RConstants.COLORS)) {
            readStyle((CompoundTag) nbt.get(RConstants.COLORS));
        }
    }

    @Override
    public TransitionType transitionType() {
        return transitionType;
    }

    @Override
    public void setTransitionType(TransitionType transitionType) {
        this.transitionType = transitionType;
    }

    @Override
    public String deathMessage() {
        return this.deathMessage;
    }

    @Override
    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    @Override
    public void forceRegeneration() {
        if (livingEntity != null) {
            livingEntity.hurt(RegenSources.REGEN_DMG_FORCED, Integer.MAX_VALUE);
        }
    }

    @Override
    public byte[] skin() {
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
    public Vec3 getPrimaryColors() {
        return new Vec3(primaryRed, primaryGreen, primaryBlue);
    }

    @Override
    public Vec3 getSecondaryColors() {
        return new Vec3(secondaryRed, secondaryGreen, secondaryBlue);
    }

    @Override
    public boolean currentlyAlex() {
        return isAlex;
    }

    @Override
    public void setAlexSkin(boolean isAlex) {
        this.isAlex = isAlex;
    }

    @Override
    public PlayerUtil.SkinType preferredModel() {
        return preferredSkinType;
    }

    @Override
    public void setPreferredModel(PlayerUtil.SkinType skinType) {
        this.preferredSkinType = skinType;
    }

    @Override
    public byte[] nextSkin() {
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
    public AbstractTrait trait() {
        return currentTrait;
    }

    @Override
    public boolean traitActive() {
        return traitActive;
    }

    @Override
    public void toggleTrait() {
        this.traitActive = !traitActive;
    }

    @Override
    public void setTrait(AbstractTrait trait) {
        this.currentTrait = trait;
    }

    @Override
    public AbstractTrait getNextTrait() {
        return nextTrait;
    }

    @Override
    public void setNextTrait(AbstractTrait trait) {
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
    public Hand handState() {
        return handState;
    }

    @Override
    public void setHandState(Hand handState) {
        this.handState = handState;
    }

    public class StateManager implements IStateManager {

        private final Map<RegenStates.Transition, Runnable> transitionCallbacks;
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

            switch (currentState) {
                case ALIVE:
                    if (!canRegenerate()) // that's too bad :(
                        return false;

                    // We're entering grace period...
                    scheduleTransitionInSeconds(RegenStates.Transition.ENTER_CRITICAL, RegenConfig.COMMON.gracePhaseLength.get());
                    scheduleHandGlowTrigger();

                    currentState = RegenStates.GRACE;
                    syncToClients(null);
                    ActingForwarder.onEnterGrace(RegenCap.this);
                    return true;
                case REGENERATING:
                    // We've been killed mid regeneration!
                    nextTransition.cancel(); // ... cancel the finishing of the regeneration
                    midSequenceKill(false);
                    return false;
                case GRACE_CRIT:
                    nextTransition.cancel();
                    if (source == RegenSources.REGEN_DMG_FORCED) {
                        triggerRegeneration();
                        return true;
                    } else {
                        midSequenceKill(true);
                        return false;
                    }
                case POST:
                    currentState = RegenStates.ALIVE;
                    nextTransition.cancel();
                    return false;
                case GRACE:
                    // We're being forced to regenerate...
                    triggerRegeneration();
                    return true;
                default:
                    break;
            }
            return false;
        }


        @Override
        public void onPunchEntity(LivingHurtEvent event) {
            LivingEntity entity = event.getEntityLiving();
            // We're healing mobs...
            if (currentState.isGraceful() && entity.getHealth() < entity.getMaxHealth() && glowing() && livingEntity.isShiftKeyDown()) { // ... check if we're in grace and if the mob needs health
                float healthNeeded = entity.getMaxHealth() - entity.getHealth();
                entity.heal(healthNeeded);
                if (livingEntity instanceof Player) {
                    PlayerUtil.sendMessage(livingEntity, Component.translatable("regen.messages.healed", entity.getName()), true);
                }
                event.setAmount(0.0F);
                livingEntity.hurt(RegenSources.REGEN_DMG_HEALING, healthNeeded);
            }
        }

        @Override
        public void onPunchBlock(PlayerInteractEvent.LeftClickBlock e) {

            if (currentState.isGraceful() && glowing()) {

                BlockState block = e.getWorld().getBlockState(e.getPos());

                if (block.getBlock() == Blocks.SNOW || block.getBlock() == Blocks.SNOW_BLOCK) {
                    e.getWorld().playSound(null, e.getPos(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
                }
                if (e.getEntityLiving() instanceof ServerPlayer) {
                    ServerPlayer playerEntity = (ServerPlayer) livingEntity;
                    TriggerManager.CHANGE_REFUSAL.trigger(playerEntity);
                }

                handGlowTimer.cancel();
                scheduleNextHandGlow();
                if (!e.getEntityLiving().level.isClientSide) {
                    if (e.getEntityLiving() instanceof Player) {
                        PlayerUtil.sendMessage(e.getEntityLiving(), Component.literal("regen.messages.regen_delayed"), true);
                    }
                }
                e.setCanceled(true); // It got annoying in creative to break something
            }
        }

        private void tick() {
            if (livingEntity.level.isClientSide)
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
                if (livingEntity instanceof Player) {
                    MutableComponent text = Component.translatable("regen.messages.regen_death_msg", livingEntity.getName());
                    text.setStyle(text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(deathMessage()))));
                    PlayerUtil.globalChat(text, Objects.requireNonNull(livingEntity.getServer()));
                }
            }

            nextTransition.cancel(); // ... cancel any state shift we had planned
            if (currentState.isGraceful()) handGlowTimer.cancel();
            scheduleTransitionInTicks(RegenStates.Transition.FINISH_REGENERATION, transitionType.getAnimationLength());

            ActingForwarder.onRegenTrigger(RegenCap.this);
            transitionType.onStartRegeneration(RegenCap.this);
            syncToClients(null);
        }

        private void enterCriticalPhase() {
            // We're entering critical phase...
            currentState = RegenStates.GRACE_CRIT;
            scheduleTransitionInSeconds(RegenStates.Transition.CRITICAL_DEATH, RegenConfig.COMMON.criticalPhaseLength.get());
            ActingForwarder.onGoCritical(RegenCap.this);
            if (livingEntity instanceof ServerPlayer playerEntity) {
                TriggerManager.CRITICAL.trigger(playerEntity);
            }
            syncToClients(null);
        }

        private void midSequenceKill(boolean isGrace) {
            currentState = RegenStates.ALIVE;
            nextTransition = null;
            handGlowTimer = null;
            transitionType.onFinishRegeneration(RegenCap.this);
            livingEntity.hurt(isGrace ? RegenSources.REGEN_DMG_CRITICAL : RegenSources.REGEN_DMG_KILLED, Integer.MAX_VALUE);
            if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                extractRegens(regens());
            }
            setTrait(RegenTraitRegistry.BORING.get());
            setSkin(new byte[0]);
            syncToClients(null);
        }

        private void endPost() {
            currentState = RegenStates.ALIVE;
            syncToClients(null);
            nextTransition = null;
            if (livingEntity instanceof Player) {
                PlayerUtil.sendMessage(livingEntity, Component.translatable("regen.messages.post_ended"), true);
            }
            handState = Hand.NO_GONE;
        }

        private void finishRegeneration() {
            currentState = RegenStates.POST;
            scheduleTransitionInSeconds(RegenStates.Transition.END_POST, livingEntity.level.random.nextInt(300) + 10);
            handGlowTimer = null;
            transitionType.onFinishRegeneration(RegenCap.this);
            ActingForwarder.onRegenFinish(RegenCap.this);
            syncToClients(null);
        }

        @Override
        @Deprecated
        /* @deprecated Debug purposes */
        public Pair<RegenStates.Transition, Long> getScheduledEvent() {
            return nextTransition == null ? null : Pair.of(nextTransition.transition, nextTransition.getTicksLeft());
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        @Deprecated
        /* @deprecated Debug purposes */
        public void skip() {
            while (!nextTransition.tick()) ;
        }

        @Override
        @Deprecated
        /* @deprecated Debug purposes */
        public void fastForwardHandGlow() {
            while (!handGlowTimer.tick()) ;
        }

        @Override
        public double stateProgress() {
            if (nextTransition != null) {
                return nextTransition.getProgress();
            }
            return 0;
        }

        @SuppressWarnings("deprecation")
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

                Runnable callback;

                switch (transition) {
                    case HAND_GLOW_START:
                        callback = this::scheduleHandGlowTrigger;
                        break;
                    case HAND_GLOW_TRIGGER:
                        callback = this::triggerRegeneration;
                        break;
                    default:
                        throw new IllegalStateException("Illegal hand glow timer transition");
                }

                handGlowTimer = new RegenScheduledAction(transition, livingEntity, callback, nbt.getLong("handGlowScheduledTicks"));
            }
        }
    }


}
