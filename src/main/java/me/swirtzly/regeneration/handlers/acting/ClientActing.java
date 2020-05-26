package me.swirtzly.regeneration.handlers.acting;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.client.ClientUtil;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import me.swirtzly.regeneration.util.common.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;

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
        ClientUtil.playSound(cap.getLivingEntity(), RegenObjects.Sounds.HEART_BEAT.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().isGraceful(), 0.2F);
        ClientUtil.playSound(cap.getLivingEntity(), RegenObjects.Sounds.GRACE_HUM.get().getRegistryName(), SoundCategory.AMBIENT, true, () -> cap.getState() != PlayerUtil.RegenState.GRACE, 1.5F);
	}
	
	@Override
    public void onHandsStartGlowing(IRegen cap) {
        ClientUtil.playSound(cap.getLivingEntity(), RegenObjects.Sounds.HAND_GLOW.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 1.0F);
	}
	
	@Override
    public void onRegenFinish(IRegen cap) {
        if (RegenConfig.CLIENT.changeHand.get() && cap.getLivingEntity().getUniqueID() == Minecraft.getInstance().player.getUniqueID()) {
			ClientUtil.createToast(new TranslationTextComponent("regeneration.toast.regenerated"), new TranslationTextComponent("regeneration.toast.regenerations_left", cap.getRegenerationsLeft()));
			Minecraft.getInstance().gameSettings.mainHand = RegenUtil.randomEnum(HandSide.class);
			Minecraft.getInstance().gameSettings.sendSettingsToServer();
		}
		
	}
	
	@Override
    public void onPerformingPost(IRegen cap) {
		
	}
	
	@Override
    public void onRegenTrigger(IRegen cap) {
        if (Minecraft.getInstance().player.getUniqueID().equals(cap.getLivingEntity().getUniqueID())) {
            SkinManipulation.sendSkinUpdate(cap.getLivingEntity().world.rand, (PlayerEntity) cap.getLivingEntity());
		}
	}
	
	@Override
    public void onGoCritical(IRegen cap) {
		if (Minecraft.getInstance().player.getUniqueID().equals(cap.getLivingEntity().getUniqueID())) {
			ClientUtil.createToast(new TranslationTextComponent("regeneration.toast.enter_critical"), new TranslationTextComponent("regeneration.toast.enter_critical.sub", RegenConfig.COMMON.criticalPhaseLength.get() / 60));
			ClientUtil.playSound(cap.getLivingEntity(), RegenObjects.Sounds.CRITICAL_STAGE.get().getRegistryName(), SoundCategory.PLAYERS, true, () -> cap.getState() != PlayerUtil.RegenState.GRACE_CRIT, 1.0F);
		}
	}
	
}
