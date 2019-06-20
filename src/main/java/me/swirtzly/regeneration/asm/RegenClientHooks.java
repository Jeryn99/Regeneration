package me.swirtzly.regeneration.asm;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;

public class RegenClientHooks {
	
	public static void handleRotations(ModelBiped model, float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		if (entity == null)
			return;
		AnimationEvent.SetRotationAngles ev = new AnimationEvent.SetRotationAngles(entity, model, f, f1, f2, f3, f4, f5);
		MinecraftForge.EVENT_BUS.post(ev);
	}
	
}
