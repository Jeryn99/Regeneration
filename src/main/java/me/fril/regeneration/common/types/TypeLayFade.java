package me.fril.regeneration.common.types;

import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class TypeLayFade implements IRegenType {
	
	@Override
	public void onStartRegeneration(EntityPlayer player, IRegeneration capability) {
		setPlayerRotation(player);
	}
	
	@Override
	public void onUpdateMidRegen(EntityPlayer player, IRegeneration capability) {
		setPlayerRotation(player);
	}
	
	@Override
	public void onFinishRegeneration(EntityPlayer player, IRegeneration capability) {
		setPlayerRotation(player);
	}
	
	//TODO Yeah I know, some magic numbers in this class. They were copy pasted from a System out. I'll look further into them soon
	private void setPlayerRotation(EntityPlayer player) {
		player.rotationPitch = -83.550026F;
		player.rotationYaw = -0.54983205F;
	}
	
	
	@Override
	public String getName() {
		return "LAYFADE";
	}
	
}
