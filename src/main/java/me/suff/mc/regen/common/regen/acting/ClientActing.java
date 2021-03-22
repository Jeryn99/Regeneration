package me.suff.mc.regen.common.regen.acting;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.client.skin.CommonSkin;
import me.suff.mc.regen.client.skin.SkinHandler;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.SkinMessage;
import me.suff.mc.regen.util.ClientUtil;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TranslationTextComponent;

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
        if (cap.getLiving().getUUID().equals(Minecraft.getInstance().player.getUUID())) {
            SoundEvent ambientSound = cap.getTimelordSound() == IRegen.TimelordSound.DRUM ? RSounds.DRUM_BEAT.get() : RSounds.GRACE_HUM.get();
            ClientUtil.playSound(cap.getLiving(), RSounds.HEART_BEAT.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.regenState().isGraceful(), 0.2F);
            ClientUtil.playSound(cap.getLiving(), ambientSound.getRegistryName(), SoundCategory.AMBIENT, true, () -> cap.regenState() != RegenStates.GRACE, 1.5F);
        }
        //TODO - LP - STOP MUSIC PLAYING IN GRACE Minecraft.getInstance().getSoundHandler().stop(null, SoundCategory.MUSIC);
    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        if (cap.getLiving().getType() == EntityType.PLAYER) {
            ClientUtil.playSound(cap.getLiving(), RSounds.HAND_GLOW.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.glowing(), 1.0F);
        }
    }

    @Override
    public void onRegenFinish(IRegen cap) {

    }

    @Override
    public void onPerformingPost(IRegen cap) {

    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        if (Minecraft.getInstance().player.getUUID().equals(cap.getLiving().getUUID())) {

            if (RegenConfig.CLIENT.changeMySkin.get()) {
                if (cap.isNextSkinValid()) {
                    NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(cap.nextSkin(), cap.isNextSkinTypeAlex()));
                    return;
                }
                Minecraft.getInstance().submitAsync(() -> {
                    File file = CommonSkin.chooseRandomSkin(cap.getLiving().getRandom(), cap.preferredModel().isAlex(), false);
                    boolean isAlex = file.getAbsolutePath().contains(CommonSkin.SKIN_DIRECTORY_ALEX.getAbsolutePath());
                    Regeneration.LOG.info("Choosen Skin: " + file);
                    NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(RegenUtil.fileToBytes(file), isAlex));
                });
            } else {
                SkinHandler.sendResetMessage();
            }
        }
    }

    @Override
    public void onGoCritical(IRegen cap) {
        if (Minecraft.getInstance().player.getUUID().equals(cap.getLiving().getUUID())) {
            if (cap.getLiving().getType() == EntityType.PLAYER) {
                ClientUtil.createToast(new TranslationTextComponent("regen.toast.enter_critical"), new TranslationTextComponent("regen.toast.enter_critical.sub", RegenConfig.COMMON.criticalPhaseLength.get() / 60));
                ClientUtil.playSound(cap.getLiving(), RSounds.CRITICAL_STAGE.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> cap.regenState() != RegenStates.GRACE_CRIT, 1.0F);
            }
        }
    }

}
