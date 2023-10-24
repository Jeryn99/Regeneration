package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.network.messages.POVMessage;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EnderDragonTransition extends TransitionType {
    @Override
    public int getAnimationLength() {
        return 300;
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {

        if (!cap.getLiving().level().isClientSide) {
            if (cap.getLiving() instanceof ServerPlayer serverPlayer) {
                new POVMessage(RConstants.THIRD_PERSON_FRONT).send(serverPlayer);
            }
        }

        if (cap.getLiving() instanceof Player serverPlayerEntity) {
            serverPlayerEntity.getAbilities().mayfly = RegenConfig.COMMON.allowUpwardsMotion.get();
            serverPlayerEntity.getAbilities().flying = RegenConfig.COMMON.allowUpwardsMotion.get();
        }
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer serverPlayerEntity) {
            serverPlayerEntity.getAbilities().mayfly = serverPlayerEntity.isCreative();
            serverPlayerEntity.getAbilities().flying = false;

            if (cap.getLiving() instanceof ServerPlayer serverPlayer) {
                new POVMessage(RConstants.FIRST_PERSON).send(serverPlayer);
            }
        }

    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{SoundEvents.ENDER_DRAGON_DEATH};
    }

    @Override
    public Vec3 getDefaultPrimaryColor() {
        return Vec3.ZERO;
    }

    @Override
    public Vec3 getDefaultSecondaryColor() {
        return Vec3.ZERO;
    }

}
