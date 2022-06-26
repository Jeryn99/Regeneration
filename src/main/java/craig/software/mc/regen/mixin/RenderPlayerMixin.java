package craig.software.mc.regen.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import craig.software.mc.regen.client.skin.SkinHandler;
import craig.software.mc.regen.common.regen.RegenCap;
import craig.software.mc.regen.common.regen.state.RegenStates;
import craig.software.mc.regen.util.PlayerUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* Created by Craig on 09/03/2021 */
@Mixin(PlayerRenderer.class)
public class RenderPlayerMixin {

    @Inject(at = @At("TAIL"), method = "setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")
    protected void setupRotations(AbstractClientPlayer entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {

        RegenCap.get(entityLiving).ifPresent(iRegen -> {
            if (PlayerUtil.isPlayerAboveZeroGrid(entityLiving) && iRegen.regenState() == RegenStates.POST) {
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(entityLiving.yBodyRot));
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(90));
                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(270.0F));
                float offset = Mth.cos(entityLiving.tickCount * 0.1F) * -0.09F;
                matrixStackIn.translate(0, -1, 0);
                matrixStackIn.translate(0, 0, -1);
                matrixStackIn.translate(0, 0, -offset);
            }
        });
    }


    @Inject(at = @At("TAIL"), cancellable = true, method = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;")
    protected void getTextureLocation(AbstractClientPlayer player, CallbackInfoReturnable<ResourceLocation> ci) {
        if (SkinHandler.PLAYER_SKINS.containsKey(player.getUUID())) {
            ResourceLocation skin = SkinHandler.PLAYER_SKINS.get(player.getUUID());
            ci.setReturnValue(skin);
        }
    }


}
