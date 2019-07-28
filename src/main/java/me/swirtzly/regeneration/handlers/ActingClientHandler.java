package me.swirtzly.regeneration.handlers;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.PlayerUtil;
import me.swirtzly.regeneration.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

import static me.swirtzly.regeneration.client.ClientEventHandler.SHADERS_TEXTURES;

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
		handleShader(cap);
	}
	
	@Override
	public void onHandsStartGlowing(IRegeneration cap) {
		ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 1.0F);
	}
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerated"), new TextComponentTranslation("regeneration.toast.regenerations_left", cap.getRegenerationsLeft()));
		
		if (RegenConfig.changeHand && cap.getPlayer().getUniqueID() == Minecraft.getMinecraft().player.getUniqueID()) {
			Minecraft.getMinecraft().gameSettings.mainHand = RegenUtil.randomEnum(EnumHandSide.class);
			Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
		}

		
	}
	
	@Override
	public void onPerformingPost(IRegeneration cap) {
		handleShader(cap);
	}
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		if (Minecraft.getMinecraft().player.getUniqueID().equals(cap.getPlayer().getUniqueID())) {
			SkinChangingHandler.sendSkinUpdate(cap.getPlayer().world.rand, cap.getPlayer());
		}
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.enter_critical"), new TextComponentTranslation("regeneration.toast.enter_critical.sub", RegenConfig.grace.criticalPhaseLength / 60));
		ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.CRITICAL_STAGE.getRegistryName(), SoundCategory.PLAYERS, true, () -> cap.getState() != PlayerUtil.RegenState.GRACE_CRIT, 1.0F);
	}


	public static void handleShader(IRegeneration cap) {
		if(true) return; //Temp disabled
		EntityRenderer entityRender = Minecraft.getMinecraft().entityRenderer;
		switch (cap.getState()) {
			case POST:
				if(cap.getPlayer().ticksExisted % 600 == 0 || cap.getPlayer().hurtTime == 1) {
					entityRender.loadShader(SHADERS_TEXTURES[cap.getPlayer().world.rand.nextInt(SHADERS_TEXTURES.length)]);
				}
				break;

			case GRACE:
			//	entityRender.loadShader(SHADERS_TEXTURES[5]);
			//	entityRender.loadShader(SHADERS_TEXTURES[16]);
				break;

			default:
				entityRender.stopUseShader();
				break;
		}
	}

}
