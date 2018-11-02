package me.fril.regeneration.common.states;

import me.fril.regeneration.common.init.RObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class TypeLayFade implements IRegenType {
	@Override
	public String getName() {
		return "LAYFADE";
	}
	
	// TODO Yeah I know, some magic numbers in this class. They were copy pasted from a System out.
	// I'll look further into them soon
	@Override
	public void onUpdateInitial(EntityPlayer player) {
		setPlayerRotation(player);
	}
	
	@Override
	public void onUpdateMidRegen(EntityPlayer player) {
		setPlayerRotation(player);
	}
	
	@Override
	public void onFinish(EntityPlayer player) {
		setPlayerRotation(player);
	}
	
	private void setPlayerRotation(EntityPlayer player) {
		player.rotationPitch = -83.550026F;
		player.rotationYaw = -0.54983205F;
	}
	
	@Override
	public SoundEvent getSound() {
		return RObjects.Sounds.REGENERATION;
	}
	
	@Override
	public boolean blockMovement() {
		return true;
	}
	
	@Override
	public boolean isLaying() {
		return true;
	}
}
