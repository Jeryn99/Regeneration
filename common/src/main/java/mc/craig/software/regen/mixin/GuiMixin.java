package mc.craig.software.regen.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.common.item.GunItem;
import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mc.craig.software.regen.client.screen.overlay.RegenerationOverlay.CUSTOM_ICONS;

@Mixin(Gui.class)
public class GuiMixin {


    @Inject(at = @At("HEAD"), method = "renderHearts", cancellable = true)
    private void renderHearts(GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight, CallbackInfo ci) {
        RegenerationData.get(player).ifPresent(regenerationData -> {
            if(regenerationData.regens() > 0){
                RenderSystem.setShaderTexture(0, CUSTOM_ICONS);
            }
        });
    }

    @Inject(at = @At("TAIL"), method = "renderHearts", cancellable = true)
    private void renderHeartsTail(GuiGraphics guiGraphics, Player player, int x, int y, int height, int i, float f, int j, int k, int l, boolean bl, CallbackInfo ci) {
        RegenerationData.get(player).ifPresent(regenerationData -> {
            if(regenerationData.regens() > 0){
                RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/icons.png"));
            }
        });
    }

    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    private void renderCrosshair(GuiGraphics guiGraphics, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player.getMainHandItem().getItem() instanceof GunItem && player.getUseItemRemainingTicks() > 0) {
            RenderSystem.setShaderTexture(0, CUSTOM_ICONS);
        }
    }

    @Inject(at = @At("TAIL"), method = "renderCrosshair", cancellable = true)
    private void renderCrosshairTail(GuiGraphics guiGraphics, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player.getMainHandItem().getItem() instanceof GunItem && player.getUseItemRemainingTicks() > 0) {
            RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/icons.png"));
        }
    }

}
