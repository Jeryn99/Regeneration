package me.suff.mc.regen.client.rendering.layers;

import me.suff.mc.regen.common.item.ItemArchInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Swirtzly
 * on 15/03/2020 @ 10:15
 */
public class RenderArchItem implements LayerRenderer<EntityPlayer> {

    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderPlayer playerRenderer;
    private ModelPlayer chargedModel = new ModelPlayer(1.0f, true);

    public RenderArchItem(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    @Override
    public void doRenderLayer(EntityPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof ItemArchInterface) {
            ItemStack archStack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

            //Item Render
            //  GlStateManager.pushMatrix();
            //  playerRenderer.getMainModel().bipedHead.postRender(0.06125f);
            //  GlStateManager.translate(0, -0.3, -0.4);
            //  GlStateManager.scale(0.3, 0.3, 0.3);
            //   GlStateManager.rotate(180, 1, 0, 0);
            //   GlStateManager.rotate(180, 0, 1, 0);
            //   Minecraft.getMinecraft().getRenderItem().renderItem(itemStored, ItemCameraTransforms.TransformType.FIXED);
            //  GlStateManager.popMatrix();

            if (entitylivingbaseIn.world.isBlockPowered(entitylivingbaseIn.getPosition())) {
                boolean flag = entitylivingbaseIn.isInvisible();
                GlStateManager.depthMask(!flag);
                this.playerRenderer.bindTexture(LIGHTNING_TEXTURE);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float f = (float) entitylivingbaseIn.ticksExisted + partialTicks;
                GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
                GlStateManager.matrixMode(5888);
                GlStateManager.enableBlend();
                GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                this.chargedModel.setModelAttributes(this.playerRenderer.getMainModel());
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                this.chargedModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(flag);
            }
        }


    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
