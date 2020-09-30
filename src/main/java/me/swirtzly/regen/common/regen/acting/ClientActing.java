package me.swirtzly.regen.common.regen.acting;

import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;

class ClientActing implements Acting {

    public static final Acting INSTANCE = new ClientActing();

    private ClientActing() {
    }

    @Override
    public void onRegenTick(IRegen cap) {
        // never forwarded as per the documentation
    }

    @Override
    public void onEnterGrace(IRegen cap) {
        ClientUtil.playSound(cap.getLiving(), RSounds.HEART_BEAT.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getCurrentState().isGraceful(), 0.2F);
        ClientUtil.playSound(cap.getLiving(), RSounds.GRACE_HUM.get().getRegistryName(), SoundCategory.AMBIENT, true, () -> cap.getCurrentState() != RegenStates.GRACE, 1.5F);
    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        ClientUtil.playSound(cap.getLiving(), RSounds.HAND_GLOW.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 1.0F);
    }

    @Override
    public void onRegenFinish(IRegen cap) {
        if (RegenConfig.CLIENT.changeHand.get() && cap.getLiving().getUniqueID() == Minecraft.getInstance().player.getUniqueID()) {
           //TODO ClientUtil.createToast(new TranslationTextComponent("regeneration.toast.regenerated"), new TranslationTextComponent("regeneration.toast.regenerations_left", cap.getRegenerationsLeft()));
        }

    }

    @Override
    public void onPerformingPost(IRegen cap) {

    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        if (Minecraft.getInstance().player.getUniqueID().equals(cap.getLiving().getUniqueID())) {
            //TODO  SkinManipulation.sendSkinUpdate(cap.getLiving().world.rand, (PlayerEntity) cap.getLiving());
        }
    }

    @Override
    public void onGoCritical(IRegen cap) {
        if (Minecraft.getInstance().player.getUniqueID().equals(cap.getLiving().getUniqueID())) {
            //TODO  ClientUtil.createToast(new TranslationTextComponent("regeneration.toast.enter_critical"), new TranslationTextComponent("regeneration.toast.enter_critical.sub", RegenConfig.COMMON.criticalPhaseLength.get() / 60));
            ClientUtil.playSound(cap.getLiving(), RSounds.CRITICAL_STAGE.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> cap.getCurrentState() != RegenStates.GRACE_CRIT, 1.0F);
        }
    }

}
