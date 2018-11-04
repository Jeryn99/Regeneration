package me.fril.regeneration.common.types;

import java.awt.Color;
import java.util.Random;

import me.fril.regeneration.client.layers.LayerRegeneration;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.LimbManipulationUtil;
import me.fril.regeneration.util.PlayerUtil;
import me.fril.regeneration.util.RegenConfig;
import me.fril.regeneration.util.RegenObjects;
import me.fril.regeneration.util.RenderUtil;
import net.minecraft.block.BlockFire;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class TypeFiery implements IRegenType {
	
	@Override
	public void onUpdateMidRegen(EntityPlayer player) {
		player.extinguish();
		
		Random rand = player.world.rand;
		player.rotationPitch += (rand.nextInt(10) - 5) * 0.2;
		player.rotationYaw += (rand.nextInt(10) - 5) * 0.2;
		
		if (player.world.isRemote)
			return;
		
		if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire)
			player.world.setBlockToAir(player.getPosition());
		
		IRegeneration capa = CapabilityRegeneration.getForPlayer(player);
		if (capa.getTicksRegenerating() > 150 && capa.getTicksRegenerating() < 152) {
			if (!player.world.isRemote) {
				PlayerUtil.damagePlayerArmor((EntityPlayerMP) player, player.world.rand.nextInt(3));
			}
		}
		
		double x = player.posX + player.getRNG().nextGaussian() * 2;
		double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
		double z = player.posZ + player.getRNG().nextGaussian() * 2;
		player.world.newExplosion(player, x, y, z, 1, RegenConfig.fieryRegen, false);
		
		for (BlockPos bs : BlockPos.getAllInBox(player.getPosition().north().west(), player.getPosition().south().east())) {
			if (player.world.getBlockState(bs).getBlock() instanceof BlockFire) {
				player.world.setBlockToAir(bs);
			}
		}
	}
	
	@Override
		public void onFinishRegeneration(EntityPlayer player) {
			player.rotationPitch = 0;
		}
	
	
	@Override
	public void onRenderPlayerPre(RenderPlayerEvent.Pre ev) {
		int arm_shake = ev.getEntityPlayer().getRNG().nextInt(7);
		LimbManipulationUtil.getLimbManipulator(ev.getRenderer(), LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75 + arm_shake);
		LimbManipulationUtil.getLimbManipulator(ev.getRenderer(), LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75 + arm_shake);
		LimbManipulationUtil.getLimbManipulator(ev.getRenderer(), LimbManipulationUtil.Limb.HEAD).setAngles(-50, 0, 0);
		LimbManipulationUtil.getLimbManipulator(ev.getRenderer(), LimbManipulationUtil.Limb.LEFT_LEG).setAngles(0, 0, -10);
		LimbManipulationUtil.getLimbManipulator(ev.getRenderer(), LimbManipulationUtil.Limb.RIGHT_LEG).setAngles(0, 0, 10);
	}

	@Override
	public void onRenderLayer(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ModelBiped model = (ModelBiped) renderLivingBase.getMainModel();
		
		// State manager changes
		GlStateManager.pushAttrib();
		GlStateManager.disableTexture2D();
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		GlStateManager.depthMask(false);
		RenderUtil.setLightmapTextureCoords(65, 65);
		
		NBTTagCompound style = capability.getStyle();
		Color primaryColor = new Color(style.getFloat("PrimaryRed"), style.getFloat("PrimaryGreen"), style.getFloat("PrimaryBlue"));
		Color secondaryColor = new Color(style.getFloat("SecondaryRed"), style.getFloat("SecondaryGreen"), style.getFloat("SecondaryBlue"));
		
		/*float primaryScale = capability.getTicksRegenerating() * 40f; TODO reimplement the startup animation
		float secondaryScale = capability.getTicksRegenerating() * 70f;*/
		
		float primaryScale = 5;
		float secondaryScale = 8;
		
		// Render right cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.RIGHT);
		GlStateManager.translate(0f, -0.2f, 0f);
		renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
		renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Render left cone
		GlStateManager.pushMatrix();
		model.postRenderArm(0.0625F, EnumHandSide.LEFT);
		GlStateManager.translate(0f, -0.2f, 0f);
		renderCone(entityPlayer, primaryScale, primaryScale, primaryColor);
		renderCone(entityPlayer, secondaryScale, secondaryScale * 1.5f, secondaryColor);
		GlStateManager.popMatrix();
		
		// Render head cone
		GlStateManager.pushMatrix();
		GlStateManager.translate(0f, 0.3f, 0f);
		GlStateManager.rotate(180, 1.0f, 0.0f, 0.0f);
		renderCone(entityPlayer, primaryScale / 2F, primaryScale * .75F, primaryColor);
		renderCone(entityPlayer, secondaryScale / 2F, secondaryScale / 2F, secondaryColor);
		GlStateManager.popMatrix();
		
		// Check which slightly larger model to use
		ModelPlayer playerModel = ((AbstractClientPlayer) entityPlayer).getSkinType().equals("slim") ? LayerRegeneration.playerModelAlex : LayerRegeneration.playerModelSteve;
		
		// Define which parts are glowing
		playerModel.bipedBody.isHidden = playerModel.bipedLeftLeg.isHidden = playerModel.bipedRightLeg.isHidden = playerModel.bipedBodyWear.isHidden = playerModel.bipedHeadwear.isHidden = playerModel.bipedLeftLegwear.isHidden = playerModel.bipedRightLegwear.isHidden = false;
		
		// Copy model attributes from the real player model
		playerModel.setModelAttributes(model);
		
		// Render glowing overlay
		GlStateManager.color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 1);
		playerModel.render(entityPlayer, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		// Undo state manager changes
		RenderUtil.restoreLightMap();
		GlStateManager.depthMask(true);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.color(255, 255, 255, 255);
		GlStateManager.enableTexture2D();
		GlStateManager.popAttrib();
	}
	
	private void renderCone(EntityPlayer entityPlayer, float scale, float scale2, Color color) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		for (int i = 0; i < 8; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(entityPlayer.ticksExisted * 4 + i * 45, 0.0F, 1.0F, 0.0F);
			GlStateManager.scale(1.0f, 1.0f, 0.65f);
			vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
			vertexBuffer.pos(0.0D, 0.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
			vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
			vertexBuffer.pos(0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
			vertexBuffer.pos(0.0D, scale2, 1.0F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
			vertexBuffer.pos(-0.266D * scale, scale, -0.5F * scale).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}
	
	
	
	
	@Override
	public String getName() {
		return "FIERY";
	}
	
	@Override
	public SoundEvent getSound() {
		return RegenObjects.Sounds.REGENERATION;
	}
	
	@Override
	public boolean blockMovement() {
		return true;
	}
	
	@Override
	public boolean isLaying() {
		return false;
	}
	
}
