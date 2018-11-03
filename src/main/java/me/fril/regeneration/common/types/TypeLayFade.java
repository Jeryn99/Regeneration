package me.fril.regeneration.common.types;

import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.RegenObjects;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class TypeLayFade implements IRegenType {
	@Override
	public String getName() {
		return "LAYFADE";
	}
	
	// TODO Yeah I know, some magic numbers in this class. They were copy pasted from a System out. I'll look further into them soon
	@Override
	public void onStartRegeneration(EntityPlayer player) {
		setPlayerRotation(player);
	}
	
	@Override
	public void onUpdateMidRegen(EntityPlayer player) {
		setPlayerRotation(player);
	}
	
	@Override
	public void onFinishRegeneration(EntityPlayer player) {
		setPlayerRotation(player);
	}
	
	private void setPlayerRotation(EntityPlayer player) {
		player.rotationPitch = -83.550026F;
		player.rotationYaw = -0.54983205F;
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
		return true;
	}
	
	
	@Override
	public void onRenderPlayerPre(Pre ev) {
		//STUB Auto-generated method stub
		
	}

	@Override
	public void onRenderLayer(RenderLivingBase<?> renderLivingBase, IRegeneration capability, EntityPlayer entityPlayer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		//STUB Auto-generated method stub
		
	}
}
