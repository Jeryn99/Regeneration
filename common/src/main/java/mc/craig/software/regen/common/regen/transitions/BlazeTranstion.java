package mc.craig.software.regen.common.regen.transitions;

import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.network.messages.POVMessage;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/* Created by Craig on 31/01/2021 */
public class BlazeTranstion extends TransitionType {
    @Override
    public int getAnimationLength() {
        return 150;
    }

    @Override
    public void onUpdateMidRegen(IRegen cap) {
        LivingEntity entity = cap.getLiving();
        if (!cap.getLiving().level().isClientSide) {
            if (cap.getLiving() instanceof ServerPlayer serverPlayer) {
                new POVMessage(RConstants.THIRD_PERSON_FRONT).send(serverPlayer);
            }
        }
    }

    @Override
    public void onFinishRegeneration(IRegen capability) {
        if (capability.getLiving() instanceof ServerPlayer serverPlayer) {
            new POVMessage(RConstants.FIRST_PERSON).send(serverPlayer);
        }
        capability.setUpdateTicks(0);
        capability.syncToClients(null);
    }

    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_0.get(), RSounds.REGENERATION_1.get(), RSounds.REGENERATION_2.get(), RSounds.REGENERATION_3.get(), RSounds.REGENERATION_4.get(), RSounds.REGENERATION_5.get(), RSounds.REGENERATION_6.get(), RSounds.REGENERATION_7.get()};
    }

    @Override
    public Vec3 getDefaultPrimaryColor() {
        return new Vec3(0.93F, 0.61F, 0F);
    }

    @Override
    public Vec3 getDefaultSecondaryColor() {
        return new Vec3(1F, 0.5F, 0.18F);
    }
}
