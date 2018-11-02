package me.fril.regeneration.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import me.fril.regeneration.Regeneration;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.states.RegenTypes;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.network.packets.MessageEnterGrace;
import me.fril.regeneration.util.LimbManipulationUtil;
import me.fril.regeneration.util.RegenObjects;
import me.fril.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Regeneration.MODID)
public class ClientHandler {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderHand(RenderHandEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		Random rand = player.world.rand;
		float f = 0.2F;
		
		if (player.getHeldItemMainhand().getItem() != Items.AIR)
			return;
		
		IRegeneration regenInfo = CapabilityRegeneration.get(player);
		
		if (regenInfo.isGlowing()) {
			
			if (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0)
				return;
			
			GlStateManager.pushMatrix();
			
			if (mc.gameSettings.mainHand.equals(EnumHandSide.RIGHT)) {
				GlStateManager.translate(-0.6F, -0.18F, -0.5F);
				GlStateManager.translate(1, -0.1, 0);
			} else {
				GlStateManager.translate(-0.3F, -0.18F, -0.5F);
			}
			
			RenderUtil.setupRenderLightning();
			GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
			for (int i = 0; i < 15; i++) {
				GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
				Color primaryColor = regenInfo.getPrimaryColor();
				RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
			}
			RenderUtil.finishRenderLightning();
			
			GlStateManager.popMatrix();
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onUpdate(LivingEvent.LivingUpdateEvent e) {
		if (!(e.getEntityLiving() instanceof EntityPlayerSP))
			return;
		
		EntityPlayer player = (EntityPlayer) e.getEntityLiving();
		IRegeneration regeneration = CapabilityRegeneration.get(player);
		if (Minecraft.getMinecraft().player == null)
			return;
		
		if (regeneration.isRegenerating() && !regeneration.isInGracePeriod() && // player is actually regenerating or choosing
				regeneration.getSolaceTicks() > 0 &&
				Minecraft.getMinecraft().player.getEntityId() == player.getEntityId()) {
			Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
		}
	}
	
	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent e) {
		if (Minecraft.getMinecraft().world == null)
			return;
		
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player == null)
			return;
		
		// apparently there's no check if we're actually in a situation where we have to choose between grace/immediate regen, that happens on the server side (packet handler)
		if (RKeyBinds.GRACE.isPressed()) {
			NetworkHandler.INSTANCE.sendToServer(new MessageEnterGrace(true));
			
			IRegeneration cap = CapabilityRegeneration.get(player);
			if (cap.getSolaceTicks() > 0 && cap.getTicksRegenerating() == 0) { // player has chosen to enter grace period, set the perspective back to 0
				Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
			}
		}
		
		if (RKeyBinds.JUSTDOIT.isPressed()) {
			NetworkHandler.INSTANCE.sendToServer(new MessageEnterGrace(false));
		}
	}
	
	@SubscribeEvent
	public static void cameraUpdate(EntityViewRenderEvent.FOVModifier e) {
		
		if (Minecraft.getMinecraft().player != null) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			IRegeneration handler = CapabilityRegeneration.get(player);
			
			if (handler.getTicksRegenerating() >= 1 && handler.getType() == RegenTypes.LAYDOWN || handler.getSolaceTicks() > 0 && handler.getSolaceTicks() < 200 && !handler.isInGracePeriod()) {
				e.setFOV(30);
			}
		}
	}
	
	@SubscribeEvent
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre e) {
		EntityPlayer player = e.getEntityPlayer();
		
		IRegeneration handler = CapabilityRegeneration.get(player);
		
		if (handler != null && handler.isRegenerating() && handler.getSolaceTicks() >= 200 && !handler.isInGracePeriod()) {
			
			// Fiery Regen T-Posing
			if (handler.getType() == RegenTypes.FIERY) {
				int arm_shake = e.getEntityPlayer().getRNG().nextInt(7);
				LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75 + arm_shake);
				LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75 + arm_shake);
				LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.HEAD).setAngles(-50, 0, 0);
				LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.LEFT_LEG).setAngles(0, 0, -10);
				LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.RIGHT_LEG).setAngles(0, 0, 10);
			}
			
			// if (handler.getType().getType().isLaying()) {
			// RenderUtil.renderPlayerLaying(e, player);
			// }
		}
	}
	
	@SubscribeEvent
	public static void keyInput(InputUpdateEvent e) {
		if (Minecraft.getMinecraft().player == null)
			return;
		
		IRegeneration capability = CapabilityRegeneration.get(Minecraft.getMinecraft().player);
		
		if (capability.isRegenerating() && !capability.isInGracePeriod() && capability.getType().blockMovement()) {
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
		for (Item item : RegenObjects.ITEMS) {
			RenderUtil.setItemRender(item);
		}
		RegenObjects.ITEMS = new ArrayList<>();
	}
	
}
