package me.swirtzly.regeneration.client.animation;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.item.ItemFobWatch;
import me.swirtzly.regeneration.util.ClientUtil;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class AnimationHandler {
	
	public static boolean animate(AnimationContext animationContext) {
		EntityPlayer player = animationContext.getEntityPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		ItemStack offStack = player.getHeldItemOffhand();
		ModelBiped modelBiped = animationContext.getModelBiped();
		
		//==============FOB WATCH & JAR START==============
		boolean isOpen;
		
		//MAINHAND
		if (stack.getItem() instanceof ItemFobWatch) {
			isOpen = ItemFobWatch.getOpen(stack) == 1;
			if (isOpen) {
				makeZombieArms(modelBiped);
				return copyAndReturn(modelBiped, true);
			}
		}
		
		//OFFHAND
		if (offStack.getItem() instanceof ItemFobWatch) {
			isOpen = ItemFobWatch.getOpen(stack) == 1;
			if (isOpen) {
				makeZombieArms(modelBiped);
				return copyAndReturn(modelBiped, true);
			}
		}
		//==============FOB WATCH END==============
		
		//JAR SYNCING
		if (CapabilityRegeneration.getForPlayer(player).isSyncingToJar()) {
			makeZombieArms(modelBiped);
			modelBiped.bipedHead.rotateAngleX = (float) Math.toRadians(45);
			return copyAndReturn(modelBiped, true);
		}
		
		return copyAndReturn(modelBiped, false);
	}
	
	public static boolean copyAndReturn(ModelBiped modelBiped, boolean cancel) {
		if (modelBiped instanceof ModelPlayer) {
			ModelPlayer playerModel = (ModelPlayer) modelBiped;
			ClientUtil.copyAnglesToWear(playerModel);
		}
		return cancel;
	}
	
	public static void makeZombieArms(ModelBiped modelBiped) {
		modelBiped.bipedRightArm.rotateAngleY = -0.1F + modelBiped.bipedHead.rotateAngleY - 0.4F;
		modelBiped.bipedLeftArm.rotateAngleY = 0.1F + modelBiped.bipedHead.rotateAngleY;
		modelBiped.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
		modelBiped.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + modelBiped.bipedHead.rotateAngleX;
	}
	
}
