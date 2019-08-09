package me.swirtzly.regeneration.handlers;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.asm.RegenClientHooks;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

class ActingClientHandler implements IActingHandler {

    public static final IActingHandler INSTANCE = new ActingClientHandler();


    private ActingClientHandler() {
    }

    @Override
    public void onRegenTick(IRegeneration cap) {
        // never forwarded as per the documentation
    }

    @Override
    public void onEnterGrace(IRegeneration cap) {
        ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HEART_BEAT.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().isGraceful(), 0.2F);
        ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.GRACE_HUM.getRegistryName(), SoundCategory.AMBIENT, true, () -> cap.getState() != PlayerUtil.RegenState.GRACE, 1.5F);
        RegenClientHooks.handleShader();
    }

    @Override
    public void onHandsStartGlowing(IRegeneration cap) {
        ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 1.0F);
        RegenClientHooks.handleShader();
    }

    @Override
    public void onRegenFinish(IRegeneration cap) {
        ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerated"), new TextComponentTranslation("regeneration.toast.regenerations_left", cap.getRegenerationsLeft()));

        if (RegenConfig.changeHand && cap.getPlayer().getUniqueID() == Minecraft.getMinecraft().player.getUniqueID()) {
            Minecraft.getMinecraft().gameSettings.mainHand = RegenUtil.randomEnum(EnumHandSide.class);
            Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
        }
        RegenClientHooks.handleShader();
    }

    @Override
    public void onStartPost(IRegeneration cap) {
        RegenClientHooks.handleShader();
    }

    @Override
    public void onProcessDone(IRegeneration cap) {
        RegenClientHooks.handleShader();
    }

    @Override
    public void onRegenTrigger(IRegeneration cap) {
        if (Minecraft.getMinecraft().player.getUniqueID().equals(cap.getPlayer().getUniqueID())) {
            SkinChangingHandler.sendSkinUpdate(cap.getPlayer().world.rand, cap.getPlayer());
        }
        RegenClientHooks.handleShader();
    }

    @Override
    public void onGoCritical(IRegeneration cap) {
        ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.enter_critical"), new TextComponentTranslation("regeneration.toast.enter_critical.sub", RegenConfig.grace.criticalPhaseLength / 60));
        ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.CRITICAL_STAGE.getRegistryName(), SoundCategory.PLAYERS, true, () -> cap.getState() != PlayerUtil.RegenState.GRACE_CRIT, 1.0F);
        RegenClientHooks.handleShader();
    }


}
