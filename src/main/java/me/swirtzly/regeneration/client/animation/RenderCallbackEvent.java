package me.swirtzly.regeneration.client.animation;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;

public class RenderCallbackEvent extends LivingEvent {

    protected RenderLivingBase<EntityLivingBase> renderer;

    public RenderCallbackEvent(EntityLivingBase entity, RenderLivingBase<EntityLivingBase> renderer) {
        super(entity);
        this.renderer = renderer;
    }

    public RenderLivingBase<EntityLivingBase> getRenderer() {
        return renderer;
    }

}
