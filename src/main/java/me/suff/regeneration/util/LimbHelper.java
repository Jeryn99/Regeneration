package me.suff.regeneration.util;

import net.minecraft.client.renderer.entity.RenderPlayer;

public class LimbHelper {
	
	//Head
	public static void rotateHead(RenderPlayer render, float x, float y, float z) {
		LimbManipulationUtil.getLimbManipulator(render, LimbManipulationUtil.Limb.HEAD).setAngles(x, y, z);
	}
	
	//Arms
	public static void rotateLeftArm(RenderPlayer render, float x, float y, float z) {
		LimbManipulationUtil.getLimbManipulator(render, LimbManipulationUtil.Limb.LEFT_ARM).setAngles(x, y, z);
	}
	
	public static void rotateRightArm(RenderPlayer render, float x, float y, float z) {
		LimbManipulationUtil.getLimbManipulator(render, LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(x, y, z);
	}
	
	//Legs
	public static void rotateLeftLeg(RenderPlayer render, float x, float y, float z) {
		LimbManipulationUtil.getLimbManipulator(render, LimbManipulationUtil.Limb.LEFT_LEG).setAngles(x, y, z);
	}
	
	public static void rotateRightLeg(RenderPlayer render, float x, float y, float z) {
		LimbManipulationUtil.getLimbManipulator(render, LimbManipulationUtil.Limb.RIGHT_LEG).setAngles(x, y, z);
	}
	
	//Body
	public static void rotateBody(RenderPlayer render, float x, float y, float z) {
		LimbManipulationUtil.getLimbManipulator(render, LimbManipulationUtil.Limb.BODY).setAngles(x, y, z);
	}
}
