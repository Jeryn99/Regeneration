package mc.craig.software.regen.common.regen;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.common.regen.state.IStateManager;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.network.messages.SyncMessage;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.constants.RConstants;
import mc.craig.software.regen.util.constants.RMessages;
import mc.craig.software.regen.util.schedule.RegenScheduledAction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static mc.craig.software.regen.util.RegenUtil.shouldGiveCouncilAdvancement;

public class RegenerationData implements IRegen {

    //State
    private final RegenerationData.StateManager stateManager;
    //Data
    private final LivingEntity livingEntity;
    public AnimationState regen = new AnimationState();
    public AnimationState grace = new AnimationState();
    //Don't save to disk
    private boolean didSetup = false, traitActive = true;
    private int regensLeft = 0, animationTicks = 0;
    public boolean areHandsGlowing = false, nextSkinTypeAlex = false, isAlex = false;

    // ===== Skin Data =====
    private byte[] nextSkin = new byte[0], skinArray = new byte[0];
    ;

    // ===== Enums =====
    private TransitionType transitionType = TransitionTypes.TRISTIS_IGNIS;
    private PlayerUtil.SkinType preferredSkinType = PlayerUtil.SkinType.ALEX;
    private RegenStates currentState = RegenStates.ALIVE;
    private IRegen.TimelordSound timelordSound = IRegen.TimelordSound.HUM;
    private IRegen.Hand handState = Hand.NOT_CUT;

    // ===== Color Data =====
    private float primaryRed = 0.69411767f, primaryGreen = 0.74509805f, primaryBlue = 0.23529412f;
    private float secondaryRed = 0.7137255f, secondaryGreen = 0.75686276f, secondaryBlue = 0.25490198f;

    // ===== Trait =====
    private TraitBase currentTrait, nextTrait = TraitRegistry.HUMAN.get();

    public RegenerationData() {
        this.livingEntity = null;
        this.stateManager = null;
    }

    public RegenerationData(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
        this.nextTrait = TraitRegistry.HUMAN.get();
        this.currentTrait = TraitRegistry.HUMAN.get();
        if (!livingEntity.level().isClientSide) {
            this.stateManager = new RegenerationData.StateManager();
        }
        else
            this.stateManager = null;
    }

    @ExpectPlatform
    public static Optional<RegenerationData> get(LivingEntity player) {
        throw new AssertionError();
    }

    @Override
    public AnimationState getAnimationState(IRegen.RegenAnimation regenAnimation) {
        return switch (regenAnimation) {
            case GRACE -> grace;
            case REGEN -> regen;
        };
    }

    @Override
    public int regens() {
        return regensLeft;
    }

    @Override
    public void setRegens(int regens) {
        this.regensLeft = regens;
    }

    @Override
    public void tick() {
        AnimationState regenAnimState = getAnimationState(IRegen.RegenAnimation.REGEN);
        AnimationState graceAnimState = getAnimationState(IRegen.RegenAnimation.GRACE);

        if (traitActive && getCurrentTrait() != null) {
            getCurrentTrait().tick(getLiving(), this);
        }

        if (livingEntity instanceof ServerPlayer serverPlayer) {

            if (handState() != Hand.NOT_CUT) {
                if (!livingEntity.getOffhandItem().isEmpty()) {
                    serverPlayer.spawnAtLocation(serverPlayer.getOffhandItem().copy());
                    serverPlayer.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
            }

            if (shouldGiveCouncilAdvancement(serverPlayer)) {
                TriggerManager.COUNCIL.trigger(serverPlayer);
            }
        }

        if (regenState() == RegenStates.REGENERATING) {
            if (!regenAnimState.isStarted()) {
                regenAnimState.start(livingEntity.tickCount);
            }
        } else {
            regenAnimState.stop();
        }

        if (regenState() == RegenStates.GRACE_CRIT) {
            if (!graceAnimState.isStarted()) {
                graceAnimState.start(livingEntity.tickCount);
            }
        } else {
            graceAnimState.stop();
        }

        if (livingEntity.level().isClientSide) return;
        //Login setup
        if (!didSetup) {
            syncToClients(null);
            didSetup = true;
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
        if (livingEntity != null && livingEntity.level().isClientSide)
            throw new IllegalStateException("Don't sync client -> server");

        areHandsGlowing = regenState().isGraceful() && stateManager.handGlowTimer.getTransition() == RegenStates.Transition.HAND_GLOW_TRIGGER;

        CompoundTag nbt = serializeNBT();
        nbt.remove(RConstants.STATE_MANAGER);

        SyncMessage message = new SyncMessage(this.livingEntity.getId(), nbt);
        if (serverPlayerEntity == null) {
            message.sendToAll();
        } else {
            message.send(serverPlayerEntity);
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putInt(RConstants.REGENS_LEFT, regens());
        compoundNBT.putString(RConstants.CURRENT_STATE, regenState().name());
        compoundNBT.putInt(RConstants.ANIMATION_TICKS, updateTicks());

        if (transitionType == null) {
            transitionType = TransitionTypes.FIERY;
        } else {
            compoundNBT.putString(RConstants.TRANSITION_TYPE, TransitionTypes.getTransitionId(transitionType).toString());
        }

        compoundNBT.putString(RConstants.PREFERENCE, preferredModel().name());
        compoundNBT.putBoolean(RConstants.IS_ALEX, currentlyAlex());
        compoundNBT.putBoolean(RConstants.GLOWING, glowing());
        compoundNBT.putString(RConstants.SOUND_SCHEME, getTimelordSound().name());
        compoundNBT.putString(RConstants.HAND_STATE, handState().name());
        compoundNBT.putBoolean(RConstants.IS_TRAIT_ACTIVE, traitActive);
        compoundNBT.putBoolean("next_" + RConstants.IS_ALEX, isNextSkinTypeAlex());

        if (currentTrait != null) {
            compoundNBT.putString(RConstants.CURRENT_TRAIT, TraitRegistry.TRAITS_REGISTRY.getKey(currentTrait).toString());
        }

        if (nextTrait != null) {
            compoundNBT.putString(RConstants.NEXT_TRAIT, TraitRegistry.TRAITS_REGISTRY.getKey(nextTrait).toString());
        }

        if (isSkinValidForUse()) {
            compoundNBT.putByteArray(RConstants.SKIN, skin());
        }

        if (isNextSkinValid()) {
            compoundNBT.putByteArray("next_" + RConstants.SKIN, nextSkin());
        }

        if (!livingEntity.level().isClientSide) {
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
            setTimelordSound(IRegen.TimelordSound.valueOf(nbt.getString(RConstants.SOUND_SCHEME)));
        }

        if (nbt.contains(RConstants.HAND_STATE)) {
            setHandState(IRegen.Hand.valueOf(nbt.getString(RConstants.HAND_STATE)));
        }
        areHandsGlowing = nbt.getBoolean(RConstants.GLOWING);

        if (nbt.contains(RConstants.PREFERENCE)) {
            setPreferredModel(PlayerUtil.SkinType.valueOf(nbt.getString(RConstants.PREFERENCE)));
        }
        //RegenType
        if (nbt.contains(RConstants.TRANSITION_TYPE)) {
            transitionType = TransitionTypes.TRANSITION_TYPES.get(new ResourceLocation(nbt.getString(RConstants.TRANSITION_TYPE)));
        }

        setCurrentTrait(TraitRegistry.TRAITS_REGISTRY.get(new ResourceLocation(nbt.getString(RConstants.CURRENT_TRAIT))));
        setNextTrait(TraitRegistry.TRAITS_REGISTRY.get(new ResourceLocation(nbt.getString(RConstants.NEXT_TRAIT))));

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
    public void forceRegeneration() {
        if (livingEntity != null) {
            livingEntity.hurt(new DamageSource(livingEntity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RegenDamageTypes.REGEN_DMG_FORCED)), Integer.MAX_VALUE);
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
    public IRegen.TimelordSound getTimelordSound() {
        return timelordSound;
    }

    @Override
    public void setTimelordSound(IRegen.TimelordSound timelordSound) {
        this.timelordSound = timelordSound;
    }

    @Override
    public IRegen.Hand handState() {
        return handState;
    }

    @Override
    public void setHandState(IRegen.Hand handState) {
        this.handState = handState;
    }

    @Override
    public boolean isTraitActive() { return traitActive; }

    @Override
    public void toggleTrait() {
        this.traitActive = !traitActive;
    }

    @Override
    public TraitBase getCurrentTrait() {
        return currentTrait;
    }

    @Override
    public void setCurrentTrait(TraitBase trait) {
        this.currentTrait = trait;
    }

    @Override
    public TraitBase getNextTrait() {
        return nextTrait;
    }

    @Override
    public void setNextTrait(TraitBase trait) {
        this.nextTrait = trait;
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
            ActingForwarder.onHandsStartGlowing(RegenerationData.this);
            syncToClients(null);
        }

        @Override
        public boolean onKilled(DamageSource source) {
            if (source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.CRAMMING)) {
                return false;
            }

            if (source.is(RegenDamageTypes.REGEN_DMG_CRITICAL)) {
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
                    syncToClients(null);
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
                    if (source.is(RegenDamageTypes.REGEN_DMG_FORCED)) {
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
        public boolean onPunchBlock(BlockPos pos, BlockState blockState, Player entity) {

            if (currentState.isGraceful() && glowing()) {

                if (blockState.getBlock() == Blocks.SNOW || blockState.getBlock() == Blocks.SNOW_BLOCK) {
                    entity.level().playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1, 1);
                }
                if (entity instanceof ServerPlayer serverPlayer) {
                    ServerPlayer playerEntity = (ServerPlayer) livingEntity;
                    TriggerManager.CHANGE_REFUSAL.trigger(playerEntity);
                    PlayerUtil.sendMessage(serverPlayer, Component.translatable(RMessages.DELAYED_REGENERATION), true);
                }

                handGlowTimer.cancel();
                scheduleNextHandGlow();
                return true;
            }
            return false;
        }

        private void tick() {
            if (livingEntity.level().isClientSide)
                throw new IllegalStateException("Ticking state manager on the client"); // the state manager shouldn't even exist on the client
            if (currentState == RegenStates.ALIVE)
                throw new IllegalStateException("Ticking dormant state manager (state == ALIVE)"); // would NPE when ticking the transition, but this is a more clear message

            if (currentState.isGraceful()) handGlowTimer.tick();

            ActingForwarder.onRegenTick(RegenerationData.this);

            if (nextTransition != null) {
                nextTransition.tick();
            }

            if (currentState == RegenStates.POST) {
                ActingForwarder.onPerformingPost(RegenerationData.this);
            }
        }

        private void enterGrace() {
            scheduleTransitionInSeconds(RegenStates.Transition.ENTER_CRITICAL, RegenConfig.COMMON.gracePhaseLength.get());
            scheduleHandGlowTrigger();
            currentState = RegenStates.GRACE;
            syncToClients(null);
            ActingForwarder.onEnterGrace(RegenerationData.this);
        }

        private void triggerRegeneration() {
            // We're starting a regeneration!
            currentState = RegenStates.REGENERATING;

            if (RegenConfig.COMMON.sendRegenDeathMessages.get()) {
                if (livingEntity instanceof Player) {
                    MutableComponent text = Component.translatable(RMessages.REGENERATION_DEATH_MSG, livingEntity.getName());
                    if (livingEntity.getLastDamageSource() != null) {
                        text.setStyle(text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, livingEntity.getLastDamageSource().getLocalizedDeathMessage(livingEntity))));
                    }
                    PlayerUtil.globalMessage(text, Objects.requireNonNull(livingEntity.getServer()));
                }
            }

            nextTransition.cancel(); // ... cancel any state shift we had planned
            if (currentState.isGraceful()) handGlowTimer.cancel();
            scheduleTransitionInTicks(RegenStates.Transition.FINISH_REGENERATION, transitionType.getAnimationLength());

            ActingForwarder.onRegenTrigger(RegenerationData.this);
            transitionType.onStartRegeneration(RegenerationData.this);
            syncToClients(null);
        }

        private void enterCriticalPhase() {
            // We're entering critical phase...
            currentState = RegenStates.GRACE_CRIT;
            scheduleTransitionInSeconds(RegenStates.Transition.CRITICAL_DEATH, RegenConfig.COMMON.criticalPhaseLength.get());
            ActingForwarder.onGoCritical(RegenerationData.this);
            if (livingEntity instanceof ServerPlayer playerEntity) {
                TriggerManager.CRITICAL.trigger(playerEntity);
            }
            syncToClients(null);
        }

        private void midSequenceKill(boolean isGrace) {
            currentState = RegenStates.ALIVE;
            nextTransition = null;
            handGlowTimer = null;
            transitionType.onFinishRegeneration(RegenerationData.this);
            Registry<DamageType> damageTypeRegistry = livingEntity.level().registryAccess().registry(Registries.DAMAGE_TYPE).get();
            livingEntity.hurt(isGrace ? new DamageSource(damageTypeRegistry.getHolderOrThrow(RegenDamageTypes.REGEN_DMG_CRITICAL)) : new DamageSource(damageTypeRegistry.getHolderOrThrow(RegenDamageTypes.REGEN_DMG_KILLED)), Integer.MAX_VALUE);
            if (RegenConfig.COMMON.loseRegensOnDeath.get()) {
                extractRegens(regens());
            }
            setCurrentTrait(TraitRegistry.HUMAN.get());
            setSkin(new byte[0]);
            syncToClients(null);
        }

        private void endPost() {
            currentState = RegenStates.ALIVE;
            nextTransition = null;
            if (livingEntity instanceof Player) {
                PlayerUtil.sendMessage(livingEntity, Component.translatable(RMessages.END_OF_PROCESS), true);
            }
            handState = Hand.NOT_CUT;
            syncToClients(null);
        }

        private void finishRegeneration() {
            currentState = RegenStates.POST;
            scheduleTransitionInSeconds(RegenStates.Transition.END_POST, livingEntity.level().random.nextInt(300) + 10);
            handGlowTimer = null;
            transitionType.onFinishRegeneration(RegenerationData.this);
            ActingForwarder.onRegenFinish(RegenerationData.this);
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

                Runnable callback = switch (transition) {
                    case HAND_GLOW_START -> this::scheduleHandGlowTrigger;
                    case HAND_GLOW_TRIGGER -> this::triggerRegeneration;
                    default -> throw new IllegalStateException("Illegal hand glow timer transition");
                };

                handGlowTimer = new RegenScheduledAction(transition, livingEntity, callback, nbt.getLong("handGlowScheduledTicks"));
            }
        }
    }

}
