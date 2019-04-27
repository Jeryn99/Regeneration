package me.suff.regeneration.client.events;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

public class AnimationEvent extends LivingEvent {
	
	protected RenderLivingBase<EntityLivingBase> renderer;
	
	public AnimationEvent(EntityLivingBase entity, RenderLivingBase<EntityLivingBase> renderer) {
		super(entity);
		this.renderer = renderer;
	}
	
	public RenderLivingBase<EntityLivingBase> getRenderer() {
		return renderer;
	}
	
	@Cancelable
	public static class SetRotationAngles extends EntityEvent {
		
		public final ModelSetRotationAnglesEventType type;
		public ModelBiped model;
		public float limbSwing;
		public float limbSwingAmount;
		public float partialTicks;
		public float ageInTicks;
		public float netHeadYaw;
		public float headPitch;
		
		public SetRotationAngles(Entity entity, ModelBiped model, float f, float f1, float f2, float f3, float f4, float f5, ModelSetRotationAnglesEventType type) {
			super(entity);
			this.model = model;
			this.limbSwing = f;
			this.limbSwingAmount = f1;
			this.partialTicks = f2;
			this.ageInTicks = f3;
			this.netHeadYaw = f4;
			this.headPitch = f5;
			this.type = type;
		}
		
	}
	
	public enum ModelSetRotationAnglesEventType {
		PRE, POST;
	}
	
}