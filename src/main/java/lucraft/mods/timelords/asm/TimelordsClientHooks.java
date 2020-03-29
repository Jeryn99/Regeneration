package lucraft.mods.timelords.asm;

import lucraft.mods.timelords.entity.TimelordPlayerData;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class TimelordsClientHooks {
  public static void preRenderCallBack(EntityLivingBase entity) {}
  
  public static void renderBipedPre(ModelBiped model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    if (entity instanceof EntityPlayer && model instanceof ModelPlayer) {
      TimelordPlayerData data = TimelordPlayerData.get((EntityPlayer)entity);
      if (data.isRegenerating()) {
        float progress = (float)Math.sin(data.getRegenerationProgress() * Math.PI);
        ModelPlayer modelPlayer = (ModelPlayer)model;
        modelPlayer.bipedRightArm.rotateAngleZ = progress;
        modelPlayer.bipedRightArmwear.rotateAngleZ = progress;
        modelPlayer.bipedLeftArm.rotateAngleZ = -progress;
        modelPlayer.bipedLeftArmwear.rotateAngleZ = -progress;
      } 
    } 
  }
  
  public static void renderBipedPost(ModelBiped model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {}
}
