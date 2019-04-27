package me.suff.regeneration.client;

import me.suff.regeneration.client.events.AnimationEvent;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;

public class RegenClientHooks {
	
	public static void renderBipedPre(ModelBiped model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if (entity == null)
			return;
		System.out.println("sdfsdfsdfsdfsd");
		AnimationEvent.SetRotationAngles ev = new AnimationEvent.SetRotationAngles(entity, model, f, f1, f2, f3, f4, f5, AnimationEvent.ModelSetRotationAnglesEventType.PRE);
		MinecraftForge.EVENT_BUS.post(ev);
		if (!ev.isCanceled()) {
			model.setRotationAngles(ev.limbSwing, ev.limbSwingAmount, ev.partialTicks, ev.ageInTicks, ev.netHeadYaw, ev.headPitch, ev.getEntity());
		}
	}
	
	public static void renderBipedPost(ModelBiped model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if (entity == null)
			return;
		AnimationEvent.SetRotationAngles ev = new AnimationEvent.SetRotationAngles(entity, model, f, f1, f2, f3, f4, f5, AnimationEvent.ModelSetRotationAnglesEventType.POST);
		MinecraftForge.EVENT_BUS.post(ev);
		System.out.println("sdfsdfsdfsdfsd");
	}

}
