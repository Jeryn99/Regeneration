package mc.craig.software.regen.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.common.item.GunItem;
import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mc.craig.software.regen.client.screen.overlay.RegenerationOverlay.CUSTOM_ICONS;

@Mixin(Gui.class)
public class GuiMixin {


    @Inject(at = @At("HEAD"), method = "renderHearts(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V", cancellable = true)
    private void renderHearts(PoseStack poseStack, Player player, int x, int y, int height, int i, float f, int j, int k, int l, boolean bl, CallbackInfo ci) {
        RegenerationData.get(player).ifPresent(regenerationData -> {
            if(regenerationData.canRegenerate()){
                RenderSystem.setShaderTexture(0, CUSTOM_ICONS);
            }
        });
    }

    @Inject(at = @At("TAIL"), method = "renderHearts(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V", cancellable = true)
    private void renderHeartsTail(PoseStack poseStack, Player player, int x, int y, int height, int i, float f, int j, int k, int l, boolean bl, CallbackInfo ci) {
        RegenerationData.get(player).ifPresent(regenerationData -> {
            if(regenerationData.canRegenerate()){
                RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
            }
        });
    }

    @Inject(at = @At("HEAD"), method = "renderCrosshair(Lcom/mojang/blaze3d/vertex/PoseStack;)V", cancellable = true)
    private void renderCrosshair(PoseStack poseStack, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player.getMainHandItem().getItem() instanceof GunItem && player.getUseItemRemainingTicks() > 0) {
            RenderSystem.setShaderTexture(0, CUSTOM_ICONS);
        }
    }

    @Inject(at = @At("TAIL"), method = "renderCrosshair(Lcom/mojang/blaze3d/vertex/PoseStack;)V", cancellable = true)
    private void renderCrosshairTail(PoseStack poseStack, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player.getMainHandItem().getItem() instanceof GunItem && player.getUseItemRemainingTicks() > 0) {
            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
        }
    }

}
