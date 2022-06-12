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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;

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
            SoundEvent ambientSound = cap.getTimelordSound().getSound();
            ClientUtil.playSound(cap.getLiving(), RSounds.HEART_BEAT.get().getRegistryName(), SoundSource.PLAYERS, true, () -> !cap.regenState().isGraceful(), 0.2F);
            ClientUtil.playSound(cap.getLiving(), ambientSound.getRegistryName(), SoundSource.AMBIENT, true, () -> cap.regenState() != RegenStates.GRACE, 1.5F);
        }
        //TODO - LP - STOP MUSIC PLAYING IN GRACE Minecraft.getInstance().getSoundHandler().stop(null, SoundCategory.MUSIC);
    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        if (cap.getLiving().getType() == EntityType.PLAYER) {
            ClientUtil.playSound(cap.getLiving(), RSounds.HAND_GLOW.get().getRegistryName(), SoundSource.PLAYERS, true, () -> !cap.glowing(), 1.0F);
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
                    if (!cap.isNextSkinValid()) {
                        File file = CommonSkin.chooseRandomSkin(cap.getLiving().getRandom(), cap.preferredModel().isAlex(), false);
                        boolean isAlex = file.getAbsolutePath().contains("\\skins\\alex");
                        Regeneration.LOG.info("Choosen Skin: " + file);
                        NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new SkinMessage(RegenUtil.fileToBytes(file), isAlex));
                    }
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
                ClientUtil.createToast(Component.translatable("regen.toast.enter_critical"), Component.translatable("regen.toast.enter_critical.sub", RegenConfig.COMMON.criticalPhaseLength.get() / 60));
                ClientUtil.playSound(cap.getLiving(), RSounds.CRITICAL_STAGE.get().getRegistryName(), SoundSource.PLAYERS, true, () -> cap.regenState() != RegenStates.GRACE_CRIT, 1.0F);
            }
        }
    }

}
