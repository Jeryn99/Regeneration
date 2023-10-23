package mc.craig.software.regen.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.item.GunItem;
import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mc.craig.software.regen.client.screen.overlay.RegenerationOverlay.CUSTOM_ICONS;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow @Final private static ResourceLocation GUI_ICONS_LOCATION;
    @Shadow @Final private Minecraft minecraft;

    @Redirect(method = "renderHeart", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"))
    private void renderHeart(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight){
        RegenerationData.get(this.minecraft.player).ifPresent(regenerationData -> {
            ResourceLocation icon_to_render = regenerationData.regens() > 0 ? CUSTOM_ICONS : GUI_ICONS_LOCATION;
            instance.blit(icon_to_render, x, y, 0, (float)uOffset, (float)vOffset, uWidth, vHeight, 256, 256);
        });
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"), method = "renderCrosshair")
    private void renderCrosshair(GuiGraphics instance, ResourceLocation atlasLocation, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        LocalPlayer player = Minecraft.getInstance().player;
        ResourceLocation icon_to_render = player.getMainHandItem().getItem() instanceof GunItem && player.getUseItemRemainingTicks() > 0 ? CUSTOM_ICONS : GUI_ICONS_LOCATION;
        instance.blit(icon_to_render, x, y, 0, (float)uOffset, (float)vOffset, uWidth, vHeight, 256, 256);
    }
}
