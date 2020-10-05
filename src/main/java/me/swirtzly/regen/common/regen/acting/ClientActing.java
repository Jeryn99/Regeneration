package me.swirtzly.regen.common.regen.acting;

import me.swirtzly.regen.client.skin.CommonSkin;
import me.swirtzly.regen.client.skin.SkinHandler;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.state.RegenStates;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.SkinMessage;
import me.swirtzly.regen.util.ClientUtil;
import me.swirtzly.regen.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;

import java.io.File;

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
        //TODO - LP - STOP MUSIC PLAYING IN GRACE Minecraft.getInstance().getSoundHandler().stop(null, SoundCategory.MUSIC);
    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        ClientUtil.playSound(cap.getLiving(), RSounds.HAND_GLOW.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 1.0F);
    }

    @Override
    public void onRegenFinish(IRegen cap) {

    }

    @Override
    public void onPerformingPost(IRegen cap) {

    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        if (Minecraft.getInstance().player.getUniqueID().equals(cap.getLiving().getUniqueID())) {

            if (RegenConfig.CLIENT.changeMySkin.get()) {
                if (cap.isNextSkinValid()) {
                    NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(cap.getNextSkin(), cap.isNextSkinTypeAlex()));
                    return;
                }
                File file = CommonSkin.chooseRandomSkin(cap.getLiving().getRNG(), cap.getPreferredModel().isAlex()); //Implement Preferred type
                boolean isAlex = file.getAbsolutePath().contains(CommonSkin.SKIN_DIRECTORY_ALEX.getAbsolutePath());
                NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(RegenUtil.fileToBytes(file), isAlex));
            } else {
                SkinHandler.sendResetMessage();
            }
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
