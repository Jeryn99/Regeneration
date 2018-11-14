package me.fril.regeneration.client;

import java.util.ArrayList;
import java.util.Random;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.network.MessageTriggerRegeneration;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.RegenState;
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
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = RegenerationMod.MODID)
public class ClientEventHandler {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderHand(RenderHandEvent e) { //TODO yellow vingette to make sure there's always a grace indicator? Or the heart timer?
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		float f = 0.2F;
		
		if (player.getHeldItemMainhand().getItem() != Items.AIR || mc.gameSettings.thirdPersonView > 0)
			return;
		
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		if (!cap.getState().isGraceful())
			return;
		
		
		GlStateManager.pushMatrix();
		
		float leftHandedFactor = mc.gameSettings.mainHand.equals(EnumHandSide.RIGHT) ? 1 : -1;
		GlStateManager.translate(0.33F*leftHandedFactor, -0.23F, -0.5F); //move in place
		GlStateManager.translate(-.8F*player.swingProgress*leftHandedFactor, -.8F*player.swingProgress, -.4F*player.swingProgress); //compensate for 'punching' motion
		GlStateManager.translate(-(player.renderArmYaw - player.prevRenderArmYaw)/400F, (player.renderArmPitch - player.prevRenderArmPitch)/500F, 0); //compensate for 'swinging' motion
		
		RenderUtil.setupRenderLightning();
		GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) / 2F, 0, 1, 0);
		for (int i = 0; i < 15; i++) {
			GlStateManager.rotate((mc.player.ticksExisted + RenderUtil.renderTick) * i / 70F, 1, 1, 0);
			Vec3d primaryColor = cap.getPrimaryColor();
			
			Random rand = player.world.rand;
			RenderUtil.drawGlowingLine(new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), new Vec3d((-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f, (-f / 2F) + rand.nextFloat() * f), 0.1F, primaryColor, 0);
		}
		RenderUtil.finishRenderLightning();
		
		GlStateManager.popMatrix();
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onClientTick(TickEvent.ClientTickEvent e) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player == null || Minecraft.getMinecraft().world == null)
			return;
		
		if (RegenKeyBinds.REGEN_NOW.isPressed() && CapabilityRegeneration.getForPlayer(player).getState().isGraceful()) {
			NetworkHandler.INSTANCE.sendToServer(new MessageTriggerRegeneration(player));
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onRenderPlayerPre(RenderPlayerEvent.Pre e) {
		EntityPlayer player = e.getEntityPlayer();
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		
		if (cap.getState() == RegenState.REGENERATING) {
			cap.onRenderRegeneratingPlayerPre(e);
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void keyInput(InputUpdateEvent e) {
		if (Minecraft.getMinecraft().player == null) return;
		
		IRegeneration cap = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player);
		if (cap.getState() == RegenState.REGENERATING) { //locking user
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
	@SideOnly(Side.CLIENT)
	public static void registerModels(ModelRegistryEvent ev) {
		for (Item item : RegenObjects.ITEMS) {
			RenderUtil.setItemRender(item);
		}
		RegenObjects.ITEMS = new ArrayList<>();
	}
	
}
