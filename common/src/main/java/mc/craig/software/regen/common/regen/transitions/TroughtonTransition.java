package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.network.messages.POVMessage;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class TroughtonTransition extends TransitionType {

    @Override
    public int getAnimationLength() {
        return 330;
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer serverPlayer) {
            new POVMessage(RConstants.FIRST_PERSON).send(serverPlayer);
        }
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity living = cap.getLiving();
    }

    @Override
    public void onFinishRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer serverPlayer) {
            new POVMessage(RConstants.THIRD_PERSON_FRONT).send(serverPlayer);
        }
        cap.setUpdateTicks(0);
        cap.syncToClients(null);
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_TROUGHTON.get()};
    }

    @Override
    public Vec3 getDefaultPrimaryColor() {
        return new Vec3(0.5, 0.5, 0.5);
    }

    @Override
    public Vec3 getDefaultSecondaryColor() {
        return new Vec3(0.5, 0.5, 0.5);
    }

}
