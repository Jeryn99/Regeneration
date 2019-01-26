package me.fril.regeneration.client;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.client.skinhandling.SkinChangingHandler;
import me.fril.regeneration.client.skinhandling.SkinInfo;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.util.ClientUtil;
import me.fril.regeneration.util.EnumCompatModids;
import me.fril.regeneration.util.RegenState;
import me.fril.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static me.fril.regeneration.client.skinhandling.SkinChangingHandler.PLAYER_SKINS;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RegenerationMod.MODID)
public class ClientEventHandler {
	
	private static final ResourceLocation VIGNETTE_TEX_PATH = new ResourceLocation(RegenerationMod.MODID, "textures/misc/vignette.png");
	
	@SubscribeEvent
	public static void onGui(InputUpdateEvent tickEvent) {
		if (EnumCompatModids.LCCORE.isLoaded()) return;
		Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.currentScreen == null && minecraft.player != null) {
			ClientUtil.keyBind = RegenKeyBinds.getRegenerateNowDisplayName();
		}
	}
	
	@SubscribeEvent
	public static void onSortofWorldJoin(LivingEvent.LivingUpdateEvent e) {
		if (!(e.getEntity() instanceof EntityPlayer) || Minecraft.getMinecraft().player == null)
			return;
		
		EntityPlayer player = (EntityPlayer) e.getEntity();
		if (player.ticksExisted == 50) {
			
			UUID clientUUID = Minecraft.getMinecraft().player.getUniqueID();
			IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
			
			if (cap.areHandsGlowing()) {
				ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing(), 0.5F);
			}
			
			if (cap.getState().equals(RegenState.REGENERATING)) {
				ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.REGENERATION.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().equals(RegenState.REGENERATING), 1.0F);
			}
			
			if (cap.getState().isGraceful() && clientUUID == player.getUniqueID()) {
				ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.CRITICAL_STAGE.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().equals(RegenState.GRACE_CRIT), 1F);
				ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.HEART_BEAT.getRegistryName(), SoundCategory.PLAYERS, true, () -> !cap.getState().isGraceful(), 0.2F);
				ClientUtil.playSound(cap.getPlayer(), RegenObjects.Sounds.G_HUM.getRegistryName(), SoundCategory.AMBIENT, true, () -> cap.getState() != RegenState.GRACE, 1.5F);
			}
		}
	}
	
	@SubscribeEvent
	public static void onRenderHand(RenderHandEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		
		SkinInfo skin = PLAYER_SKINS.get(player.getUniqueID());
		
		if (skin != null) {
			SkinChangingHandler.setPlayerTexture(player, skin.getTextureLocation());
		}
		
		float factor = 0.2F;
		if (player.getHeldItemMainhand().getItem() != Items.AIR || mc.gameSettings.thirdPersonView > 0)
			return;
		
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		if (!cap.areHandsGlowing())
			return;
		
		GlStateManager.pushMatrix();
		
		float leftHandedFactor = mc.gameSettings.mainHand.equals(EnumHandSide.RIGHT) ? 1 : -1;
		GlStateManager.translate(0.33F * leftHandedFactor, -0.23F, -0.5F); // move in place
		GlStateManager.translate(-.8F * player.swingProgress * leftHandedFactor, -.8F * player.swingProgress, -.4F * player.swingProgress); // compensate for 'punching' motion
		GlStateManager.translate(-(player.renderArmYaw - player.prevRenderArmYaw) / 400F, (player.renderArmPitch - player.prevRenderArmPitch) / 500F, 0); // compensate for 'swinging' motion
		
		RenderUtil.setupRenderLightning();
		GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
		for (int i = 0; i < 15; i++) {
			GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
			Vec3d primaryColor = cap.getPrimaryColor();
			
			Random rand = player.world.rand;
			RenderUtil.drawGlowingLine(new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), new Vec3d((-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor, (-factor / 2F) + rand.nextFloat() * factor), 0.1F, primaryColor, 0);
		}
		RenderUtil.finishRenderLightning();
		
		GlStateManager.popMatrix();
	}
	
	@SuppressWarnings("incomplete-switch")
	@SubscribeEvent
	public static void onRenderGui(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL)
			return;
		
		IRegeneration cap = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player);
		String warning = null;
		
		switch (cap.getState()) {
			case GRACE:
				renderVignette(cap.getPrimaryColor(), 0.3F, cap.getState());
				warning = new TextComponentTranslation("regeneration.messages.warning.grace", ClientUtil.keyBind).getUnformattedText();
				break;
			
			case GRACE_CRIT:
				renderVignette(new Vec3d(1, 0, 0), 0.5F, cap.getState());
				warning = new TextComponentTranslation("regeneration.messages.warning.grace_critical", ClientUtil.keyBind).getUnformattedText();
				break;
			
			case REGENERATING:
				renderVignette(cap.getSecondaryColor(), 0.5F, cap.getState());
				break;
		}
		
		if (warning != null)// && !Loader.isModLoaded("lucraftcore"))
			Minecraft.getMinecraft().fontRenderer.drawString(warning, new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(warning) / 2, 4, 0xffffffff);
	}
	
	@SubscribeEvent
	public static void onPlaySound(PlaySoundEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.player == null || mc.world == null)
			return;
		
		if (e.getName().equals("entity.generic.explode")) {
			ISound sound = PositionedSoundRecord.getRecord(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 0.2F);
			mc.world.playerEntities.forEach(player -> {
				if (mc.player != player && mc.player.getDistance(player) < 40) {
					if (CapabilityRegeneration.getForPlayer(player).getState().equals(RegenState.REGENERATING)) {
						e.setResultSound(sound);
					}
				}
			});
			
			if (CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getState() == RegenState.REGENERATING) {
				e.setResultSound(sound);
			}
		}
		
	}
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onClientChatRecieved(ClientChatReceivedEvent e) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (e.getType() != ChatType.CHAT) return;
		if (CapabilityRegeneration.getForPlayer(player).getState() != RegenState.POST) return;
		
		String message = e.getMessage().getUnformattedText();
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
	
	public static String getColoredText(String msg) {
		return msg.replaceAll("&", String.valueOf('\u00a7'));
	}
	
	
	private static void renderVignette(Vec3d color, float a, RegenState state) {
		GlStateManager.color((float) color.x, (float) color.y, (float) color.z, a);
		GlStateManager.disableAlpha();
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Minecraft.getMinecraft().getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		
		ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
		int z = -89; // below the HUD
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(0, scaledRes.getScaledHeight(), z).tex(0, 1).endVertex();
		bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), z).tex(1.0D, 1.0D).endVertex();
		bufferbuilder.pos(scaledRes.getScaledWidth(), 0, z).tex(1, 0).endVertex();
		bufferbuilder.pos(0, 0, z).tex(0, 0).endVertex();
		tessellator.draw();
		
		GlStateManager.depthMask(true);
		GlStateManager.enableAlpha();
		GlStateManager.color(1, 1, 1, 1);
	}
	
	
	@SubscribeEvent
	public static void keyInput(InputUpdateEvent e) {
		if (Minecraft.getMinecraft().player == null)
			return;
		
		IRegeneration cap = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player);
		if (cap.getState() == RegenState.REGENERATING) { // locking user
			MovementInput moveType = e.getMovementInput();
			moveType.rightKeyDown = false;
			moveType.leftKeyDown = false;
			moveType.backKeyDown = false;
			moveType.jump = false;
			moveType.moveForward = 0.0F;
			moveType.sneak = false;
			moveType.moveStrafe = 0.0F;
		}
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent ev) {
		RegenObjects.ITEMS.forEach(RenderUtil::setItemRender);
		RegenObjects.ITEMS = new ArrayList<>();
	}
	
	@SubscribeEvent
	public static void onDeath(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			PLAYER_SKINS.remove(player.getUniqueID());
			
			if (player.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID())) { //SUB this crashed once while I tested but I don't know why. I'm 70% sure I didn't even die
				ClientUtil.sendSkinResetPacket();
			}
		}
	}
	
	@SubscribeEvent
	public static void onClientLeaveServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent e) {
		PLAYER_SKINS.clear();
	}
	
}
