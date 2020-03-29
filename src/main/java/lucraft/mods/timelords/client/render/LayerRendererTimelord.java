package lucraft.mods.timelords.client.render;

import lucraft.mods.timelords.entity.TimelordPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class LayerRendererTimelord implements LayerRenderer {
  public ModelPlayer modelNormal = new ModelPlayer(0.0F, false);
  
  public ModelPlayer modelAlex = new ModelPlayer(0.0F, true);
  
  public RenderPlayer renderer;
  
  public static Minecraft mc = Minecraft.getMinecraft();
  
  public LayerRendererTimelord(RenderPlayer renderer) {
    this.renderer = renderer;
  }
  
  public void doRenderLayer(EntityLivingBase player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float mcScale) {
    TimelordPlayerData data = TimelordPlayerData.get((EntityPlayer)player);
    if (data.getSkinFile() != null) {
      GlStateManager.pushMatrix();
      GlStateManager.enableBlend();
      GL11.glBlendFunc(770, 771);
      if (data.isRegenerating() && data.getPrevSkinFile() != null) {
        GlStateManager.scale(0.99F, 0.99F, 0.99F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, data.getRegenerationProgress());
        ModelPlayer modelPlayer = data.getPrevSkinFile().hasSmallArms() ? this.modelAlex : this.modelNormal;
        modelPlayer.setInvisible(true);
        modelPlayer.setModelAttributes((ModelBase)this.renderer.getPlayerModel());
        mc.renderEngine.bindTexture(data.getPrevSkinFile().getResourceLocation());
        modelPlayer.render((Entity)player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, mcScale);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F - data.getRegenerationProgress());
        GlStateManager.enableRescaleNormal();
      } 
      ModelPlayer model = data.getSkinFile().hasSmallArms() ? this.modelAlex : this.modelNormal;
      model.setInvisible(true);
      model.setModelAttributes((ModelBase)this.renderer.getPlayerModel());
      player.setInvisible(true);
      mc.renderEngine.bindTexture(data.getSkinFile().getResourceLocation());
      model.render((Entity)player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, mcScale);
      if (data.isRegenerating()) {
        float flameAlpha = (float)Math.sin(data.getRegenerationProgress() * Math.PI);
        GlStateManager.color(1.0F, 1.0F, 0.0F, flameAlpha * 0.4F);
        float armTranslate = data.isRegenerating() ? ((!data.getSkinFile().hasSmallArms() && data.getSkinFile().hasSmallArms() == data.getPrevSkinFile().hasSmallArms()) ? 0.0F : ((data.getSkinFile().hasSmallArms() && data.getSkinFile().hasSmallArms() == data.getPrevSkinFile().hasSmallArms()) ? 1.0F : (!data.getSkinFile().hasSmallArms() ? (1.0F - data.getRegenerationProgress()) : data.getRegenerationProgress()))) : (data.getSkinFile().hasSmallArms() ? true : false);
        float armScale = data.isRegenerating() ? ((!data.getSkinFile().hasSmallArms() && data.getSkinFile().hasSmallArms() == data.getPrevSkinFile().hasSmallArms()) ? 1.0F : ((data.getSkinFile().hasSmallArms() && data.getSkinFile().hasSmallArms() == data.getPrevSkinFile().hasSmallArms()) ? 0.75F : (!data.getSkinFile().hasSmallArms() ? (0.75F + (1.0F - data.getRegenerationProgress()) * 0.25F) : (1.0F - (1.0F - data.getRegenerationProgress()) * 0.25F)))) : (data.getSkinFile().hasSmallArms() ? 0.75F : 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.003921569F);
        GlStateManager.translate(0.0F, player.isSneaking() ? 0.2F : 0.0F, 0.0F);
        float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
        float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
        GlStateManager.rotate(yawOffset, 0.0F, -1.0F, 0.0F);
        GlStateManager.rotate(yaw - 270.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(pitch, 0.0F, 0.0F, 1.0F);
        float f1 = 0.6F;
        TLRenderer.setLightmapTextureCoords(240.0F, 240.0F);
        GlStateManager.scale(f1, f1 * 3.3F, f1);
        GlStateManager.translate(-0.5F, -1.0F, -0.5F);
        TLRenderer.renderFire(mc, "minecraft:blocks/fire_layer_0");
        TLRenderer.renderFire(mc, "minecraft:blocks/fire_layer_1");
        TLRenderer.restoreLightmapTextureCoords();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.003921569F);
        float flameScale = 0.27F;
        if (player.isSneaking())
          GlStateManager.translate(0.0F, 0.2F, 0.0F); 
        (this.renderer.getPlayerModel()).bipedRightArm.postRender(0.0625F);
        GlStateManager.rotate(-(this.renderer.getPlayerModel()).bipedRightArm.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate((float)Math.sin(data.getRegenerationProgress() * Math.PI) * 57.295776F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        TLRenderer.setLightmapTextureCoords(240.0F, 240.0F);
        GlStateManager.scale(flameScale * armScale, flameScale * 3.3F, flameScale);
        GlStateManager.translate(-0.74F + armTranslate * 0.05F, -1.5F, -0.5F);
        TLRenderer.renderFire(mc, "minecraft:blocks/fire_layer_0");
        TLRenderer.renderFire(mc, "minecraft:blocks/fire_layer_1");
        TLRenderer.restoreLightmapTextureCoords();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.003921569F);
        flameScale = 0.27F;
        if (player.isSneaking())
          GlStateManager.translate(0.0F, 0.2F, 0.0F); 
        (this.renderer.getPlayerModel()).bipedLeftArm.postRender(0.0625F);
        GlStateManager.rotate(-(this.renderer.getPlayerModel()).bipedLeftArm.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate((float)Math.sin(data.getRegenerationProgress() * Math.PI) * -57.295776F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        TLRenderer.setLightmapTextureCoords(240.0F, 240.0F);
        GlStateManager.scale(flameScale * armScale, flameScale * 3.3F, flameScale);
        GlStateManager.translate(-0.28F + armTranslate * -0.03F, -1.5F, -0.5F);
        TLRenderer.renderFire(mc, "minecraft:blocks/fire_layer_0");
        TLRenderer.renderFire(mc, "minecraft:blocks/fire_layer_1");
        TLRenderer.restoreLightmapTextureCoords();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
      } 
      GlStateManager.disableBlend();
      GlStateManager.popMatrix();
    } 
  }
  
  public boolean shouldCombineTextures() {
    return false;
  }
}
