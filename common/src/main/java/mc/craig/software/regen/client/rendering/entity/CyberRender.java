package mc.craig.software.regen.client.rendering.entity;

import mc.craig.software.regen.client.rendering.layers.CyberGlowLayer;
import mc.craig.software.regen.client.rendering.layers.HandLayer;
import mc.craig.software.regen.client.rendering.layers.RenderRegenLayer;
import mc.craig.software.regen.client.rendering.model.CybermanModel;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CyberRender extends LivingEntityRenderer<Cyberman, CybermanModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RConstants.MODID, "textures/entity/cyberman.png");


    public CyberRender(EntityRendererProvider.Context context) {
        super(context, new CybermanModel(Minecraft.getInstance().getEntityModels().bakeLayer(RModels.CYBERMAN)), 1);
        addLayer(new RenderRegenLayer(this));
        addLayer(new HandLayer(this));
        addLayer(new CyberGlowLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Cyberman cyberman) {
        return TEXTURE;
    }
}
