package lucraft.mods.timelords.client.render;

import java.io.File;
import lucraft.mods.timelords.TimelordsConfig;
import lucraft.mods.timelords.entity.TimelordPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TLRenderer {
  public static Minecraft mc = Minecraft.getMinecraft();
  
  public static final ResourceLocation TEX = new ResourceLocation("timelords", "textures/gui/widgets.png");
  
  @SubscribeEvent
  public void onRenderGameOverlayPre(RenderGameOverlayEvent.Pre e) {
    TimelordPlayerData data = TimelordPlayerData.get((EntityPlayer)mc.thePlayer);
    if (e.type == RenderGameOverlayEvent.ElementType.HEALTH || e.type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT || e.type == RenderGameOverlayEvent.ElementType.FOOD || e.type == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, -10.0F, 0.0F);
    } 
    if (e.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
      GlStateManager.pushMatrix();
      mc.renderEngine.bindTexture(TEX);
      float regensProgress = MathHelper.clamp_float(data.getRegenerations() / TimelordsConfig.defaultRegenerations + (data.isRegenerating() ? (1.0F / TimelordsConfig.defaultRegenerations * data.getRegenerationProgress()) : 0.0F), 0.0F, 1.0F);
      mc.ingameGUI.drawTexturedModalRect(e.resolution.getScaledWidth() / 2 - 91, e.resolution.getScaledHeight() - 30, 0, 0, 182, 5);
      mc.ingameGUI.drawTexturedModalRect(e.resolution.getScaledWidth() / 2 - 91, e.resolution.getScaledHeight() - 30, 0, 5, (int)(182.0F * regensProgress), 5);
      if (data.getSkinFile() != null)
        drawString(data.getSkinFile().getResourceLocation().toString(), 0, 0); 
      String text = "" + data.getRegenerations();
      int length = mc.fontRendererObj.getStringWidth(text);
      drawStringWithOutline(text, e.resolution.getScaledWidth() / 2 - length / 2, e.resolution.getScaledHeight() - 32, data.isRegenerating() ? 16738589 : 16761115, 0);
      GlStateManager.popMatrix();
    } 
  }
  
  @SubscribeEvent
  public void onRenderGameOverlayPost(RenderGameOverlayEvent.Post e) {
    TimelordPlayerData data = TimelordPlayerData.get((EntityPlayer)mc.thePlayer);
    if (e.type == RenderGameOverlayEvent.ElementType.HEALTH || e.type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT || e.type == RenderGameOverlayEvent.ElementType.FOOD || e.type == RenderGameOverlayEvent.ElementType.EXPERIENCE)
      GlStateManager.popMatrix(); 
  }
  
  public static void drawString(String string, int posX, int posY) {
    mc.fontRendererObj.drawStringWithShadow(string, posX, posY, 16711422);
  }
  
  public static void drawStringWithOutline(String string, int posX, int posY, int fontColor, int outlineColor) {
    mc.fontRendererObj.drawString(string, posX + 1, posY, outlineColor);
    mc.fontRendererObj.drawString(string, posX - 1, posY, outlineColor);
    mc.fontRendererObj.drawString(string, posX, posY + 1, outlineColor);
    mc.fontRendererObj.drawString(string, posX, posY - 1, outlineColor);
    mc.fontRendererObj.drawString(string, posX, posY, fontColor);
  }
  
  public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String url) {
    TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
    Object object = texturemanager.getTexture(resourceLocationIn);
    if (object == null) {
      object = new ThreadDownloadImageData((File)null, url, DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID("")), (IImageBuffer)new ImageBufferDownload());
      texturemanager.loadTexture(resourceLocationIn, (ITextureObject)object);
    } 
    return (ThreadDownloadImageData)object;
  }
  
  public static void renderFire(Minecraft mc, String texture) {
    mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer buffer = tessellator.getWorldRenderer();
    TextureAtlasSprite sprite = mc.getTextureMapBlocks().getTextureExtry(texture);
    (mc.getRenderManager()).renderEngine.bindTexture(TextureMap.locationBlocksTexture);
    GlStateManager.pushMatrix();
    buffer.startDrawingQuads();
    buffer.addVertexWithUV(0.0D, 0.0D, 0.0D, sprite.getMaxU(), sprite.getMinV());
    buffer.addVertexWithUV(1.0D, 0.0D, 0.0D, sprite.getMinU(), sprite.getMinV());
    buffer.addVertexWithUV(1.0D, 1.0D, 0.0D, sprite.getMinU(), sprite.getMaxV());
    buffer.addVertexWithUV(0.0D, 1.0D, 0.0D, sprite.getMaxU(), sprite.getMaxV());
    buffer.addVertexWithUV(1.0D, 0.0D, 1.0D, sprite.getMaxU(), sprite.getMinV());
    buffer.addVertexWithUV(1.0D, 0.0D, 0.0D, sprite.getMinU(), sprite.getMinV());
    buffer.addVertexWithUV(1.0D, 1.0D, 0.0D, sprite.getMinU(), sprite.getMaxV());
    buffer.addVertexWithUV(1.0D, 1.0D, 1.0D, sprite.getMaxU(), sprite.getMaxV());
    buffer.addVertexWithUV(0.0D, 0.0D, 1.0D, sprite.getMaxU(), sprite.getMinV());
    buffer.addVertexWithUV(1.0D, 0.0D, 1.0D, sprite.getMinU(), sprite.getMinV());
    buffer.addVertexWithUV(1.0D, 1.0D, 1.0D, sprite.getMinU(), sprite.getMaxV());
    buffer.addVertexWithUV(0.0D, 1.0D, 1.0D, sprite.getMaxU(), sprite.getMaxV());
    buffer.addVertexWithUV(0.0D, 0.0D, 1.0D, sprite.getMaxU(), sprite.getMinV());
    buffer.addVertexWithUV(0.0D, 0.0D, 0.0D, sprite.getMinU(), sprite.getMinV());
    buffer.addVertexWithUV(0.0D, 1.0D, 0.0D, sprite.getMinU(), sprite.getMaxV());
    buffer.addVertexWithUV(0.0D, 1.0D, 1.0D, sprite.getMaxU(), sprite.getMaxV());
    tessellator.draw();
    GlStateManager.popMatrix();
  }
  
  public static float lastBrightnessX = OpenGlHelper.lastBrightnessX;
  
  public static float lastBrightnessY = OpenGlHelper.lastBrightnessY;
  
  public static void setLightmapTextureCoords(float x, float y) {
    lastBrightnessX = OpenGlHelper.lastBrightnessX;
    lastBrightnessY = OpenGlHelper.lastBrightnessY;
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, x, y);
  }
  
  public static void restoreLightmapTextureCoords() {
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
  }
}
