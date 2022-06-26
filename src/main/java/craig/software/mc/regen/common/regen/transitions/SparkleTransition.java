package craig.software.mc.regen.common.regen.transitions;

import craig.software.mc.regen.common.objects.RSounds;
import craig.software.mc.regen.common.regen.IRegen;
import craig.software.mc.regen.network.NetworkDispatcher;
import craig.software.mc.regen.network.messages.POVMessage;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class SparkleTransition extends TransitionType {

    @Override
    public int getAnimationLength() {
        return 230;
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

    @Override
    public void onFinishRegeneration(IRegen cap) {
        LivingEntity living = cap.getLiving();
        if (living instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.FIRST_PERSON));
        }
    }

    @Override
    public void onStartRegeneration(IRegen cap) {
        if (cap.getLiving() instanceof ServerPlayer) {
            NetworkDispatcher.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) cap.getLiving()), new POVMessage(RConstants.THIRD_PERSON_FRONT));
        }
    }

}
