package me.suff.regeneration.client;

import me.suff.regeneration.client.gui.GuiCustomizer;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.client.skinhandling.SkinInfo;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.handlers.RegenObjects;
import me.suff.regeneration.network.MessageTriggerRegeneration;
import me.suff.regeneration.network.NetworkHandler;
import me.suff.regeneration.util.ClientUtil;
import me.suff.regeneration.util.EnumCompatModids;
import me.suff.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;
import java.util.UUID;

import static me.suff.regeneration.client.RegenKeyBinds.REGEN_NOW;
import static me.suff.regeneration.util.RegenState.*;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class ClientEventHandler {
	
	@SubscribeEvent
	public void onGui(InputUpdateEvent tickEvent) {
		if (EnumCompatModids.LCCORE.isLoaded()) return;
		Minecraft minecraft = Minecraft.getInstance();
		
		if (minecraft.currentScreen == null && minecraft.player != null) {
			ClientUtil.keyBind = RegenKeyBinds.getRegenerateNowDisplayName();
		}
		
		CapabilityRegeneration.getForPlayer(minecraft.player).ifPresent((data) -> {
			if (REGEN_NOW.isPressed() && data.getState().isGraceful()) {
				NetworkHandler.INSTANCE.sendToServer(new MessageTriggerRegeneration());
			}
			
			if(RegenKeyBinds.REGEN_CUSTOMISE.isPressed()){
				Minecraft.getInstance().displayGuiScreen(null);
				Minecraft.getInstance().displayGuiScreen(new GuiCustomizer());
			}
		});
		
	}
	
	
	@SubscribeEvent
	public void onSortofWorldJoin(LivingEvent.LivingUpdateEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer) || Minecraft.getInstance().player == null)
			return;
		
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (player.ticksExisted == 50) {
			
			UUID clientUUID = Minecraft.getInstance().player.getUniqueID();
			CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
				
				if (cap.areHandsGlowing()) {
					ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 0.5F);
				}
				
				if (cap.getState().equals(REGENERATING)) {
					ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.REGENERATION.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().equals(REGENERATING), 1.0F);
				}
				
				if (cap.getState().isGraceful() && clientUUID == player.getUniqueID()) {
					ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.CRITICAL_STAGE.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().equals(GRACE_CRIT), 1F);
					ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HEART_BEAT.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().isGraceful(), 0.2F);
					ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.GRACE_HUM.getRegistryName(), SoundCategory.AMBIENT, true, () -> cap.getState() != GRACE, 1.5F);
				}
			});
		}
	}
	
	@SubscribeEvent
	public void onRenderHand(RenderHandEvent e) {
		Minecraft mc = Minecraft.getInstance();
		EntityPlayerSP player = Minecraft.getInstance().player;
		
		SkinInfo skin = SkinChangingHandler.PLAYER_SKINS.get(player.getUniqueID());
		
		if (skin != null) {
			SkinChangingHandler.setPlayerSkin(player, skin.getSkinTextureLocation());
		}
		
		float factor = 0.2F;
		if (player.getHeldItemMainhand().getItem() != Items.AIR || mc.gameSettings.thirdPersonView > 0)
			return;
		
		CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
			if (!cap.areHandsGlowing())
				return;
			
			GlStateManager.pushMatrix();
			
			float leftHandedFactor = mc.gameSettings.mainHand.equals(EnumHandSide.RIGHT) ? 1 : -1;
			GlStateManager.translatef(0.33F * leftHandedFactor, -0.23F, -0.5F); // move in place
			GlStateManager.translatef(-.8F * player.swingProgress * leftHandedFactor, -.8F * player.swingProgress, -.4F * player.swingProgress); // compensate for 'punching' motion
			GlStateManager.translatef(-(player.renderArmYaw - player.prevRenderArmYaw) / 400F, (player.renderArmPitch - player.prevRenderArmPitch) / 500F, 0); // compensate for 'swinging' motion
			
			RenderUtil.setupRenderLightning();
			GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
			for (int i = 0; i < 15; i++) {
				GlStateManager.rotatef((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
				Vec3d primaryColor = cap.getPrimaryColor();
				
				Random rand = player.world.rand;
				RenderUtil.drawGlowingLine(new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, primaryColor, 0);
			}
			RenderUtil.finishRenderLightning();
			
			GlStateManager.popMatrix();
		});
	}
	
	@SuppressWarnings("incomplete-switch")
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
			return;
		
		CapabilityRegeneration.getForPlayer(Minecraft.getInstance().player).ifPresent((cap) -> {
			String warning = null;
			
			switch (cap.getState()) {
				case GRACE:
					RenderUtil.renderVignette(cap.getPrimaryColor(), 0.3F, cap.getState());
					warning = new TextComponentTranslation("regeneration.messages.warning.grace", ClientUtil.keyBind).getUnformattedComponentText();
					break;
				
				case GRACE_CRIT:
					RenderUtil.renderVignette(new Vec3d(1, 0, 0), 0.5F, cap.getState());
					warning = new TextComponentTranslation("regeneration.messages.warning.grace_critical", ClientUtil.keyBind).getUnformattedComponentText();
					break;
				
				case REGENERATING:
					RenderUtil.renderVignette(cap.getSecondaryColor(), 0.5F, cap.getState());
					break;
			}
			
			if (warning != null)
				Minecraft.getInstance().fontRenderer.drawString(warning, Minecraft.getInstance().mainWindow.getScaledWidth() / 2 - Minecraft.getInstance().fontRenderer.getStringWidth(warning) / 2, 4, 0xffffffff);
		});
	}
	
	@SubscribeEvent
	public void onPlaySound(PlaySoundEvent e) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.world == null)
			return;
		
		if (e.getName().equals("entity.generic.explode")) {
			ISound sound = SimpleSound.getRecord(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 0.2F);
			mc.world.playerEntities.forEach(player -> CapabilityRegeneration.getForPlayer(player).ifPresent((cap) -> {
				if (mc.player != player && mc.player.getDistance(player) < 40) {
					if (cap.getState().equals(REGENERATING)) {
						e.setResultSound(sound);
					}
				}
			}));
			
			CapabilityRegeneration.getForPlayer(Minecraft.getInstance().player).ifPresent((cap) -> {
				if (cap.getState() == REGENERATING) {
					e.setResultSound(sound);
				}
			});
		}
	}
	
	
	@SubscribeEvent
	public void onSetupFogDensity(EntityViewRenderEvent.RenderFogEvent.FogDensity event) {
		Minecraft.getInstance().player.getCapability(CapabilityRegeneration.CAPABILITY).ifPresent(
				(data) -> {
					if (data.getState() == GRACE_CRIT) {
						GlStateManager.fogMode(GlStateManager.FogMode.EXP);
						event.setCanceled(true);
						float amount = MathHelper.cos(data.getPlayer().ticksExisted * 0.06F) * -0.09F;
						event.setDensity(amount);
					}
				}
		);
	}
	
	
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onClientChatRecieved(ClientChatReceivedEvent e) {
		EntityPlayerSP player = Minecraft.getInstance().player;
		if (e.getType() != ChatType.CHAT) return;
		CapabilityRegeneration.getForPlayer(Minecraft.getInstance().player).ifPresent(
				(data) -> {
					if (data.getState() != POST) return;
					if (player.world.rand.nextBoolean()) {
						String message = e.getMessage().getUnformattedComponentText();
						TextComponentString newMessage = new TextComponentString("");
						String[] words = message.split(" ");
						for (String word : words) {
							if (word.equals(words[0])) {
								TextComponentString name = new TextComponentString(word + " ");
								newMessage.appendSibling(name);
								continue;
							}
							if (player.world.rand.nextBoolean()) {
								TextComponentString txtComp = new TextComponentString(getColoredText("&k" + word + "&r "));
								txtComp.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(word)));
								newMessage.appendSibling(txtComp);
							} else {
								TextComponentString txtComp = new TextComponentString(word + " ");
								newMessage.appendSibling(txtComp);
							}
						}
						e.setMessage(newMessage);
					}
				});
	}
	
	public String getColoredText(String msg) {
		return msg.replaceAll("&", String.valueOf('\u00a7'));
	}
	
	@SubscribeEvent
	public void keyInput(InputUpdateEvent e) {
		if (Minecraft.getInstance().player == null)
			return;
		
		CapabilityRegeneration.getForPlayer(Minecraft.getInstance().player).ifPresent(
				(cap) -> {
					if (cap.getState() == REGENERATING) { // locking user
						MovementInput moveType = e.getMovementInput();
						moveType.rightKeyDown = false;
						moveType.leftKeyDown = false;
						moveType.backKeyDown = false;
						moveType.jump = false;
						moveType.moveForward = 0.0F;
						moveType.sneak = false;
						moveType.moveStrafe = 0.0F;
					}
				});
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			SkinChangingHandler.PLAYER_SKINS.remove(player.getUniqueID());
			if (player.getUniqueID().equals(Minecraft.getInstance().player.getUniqueID())) {
				ClientUtil.sendSkinResetPacket();
			}
		}
	}
	
	//TODO find out when the client logs out
	//@SubscribeEvent
	//public void onClientLeaveServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
	//	SkinChangingHandler.PLAYER_SKINS.clear();
	//	SkinChangingHandler.TYPE_BACKUPS.clear();
	//}
	
	
}
