package mc.craig.software.regen.mixin;

import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static mc.craig.software.regen.client.screen.overlay.RegenerationOverlay.CUSTOM_ICONS;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow
    @Final
    private static ResourceLocation GUI_ICONS_LOCATION;

    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * Cached state so we don't "flash" back to vanilla hearts when regeneration data
     * hasn't synced or is temporarily missing (e.g., when switching gamemodes).
     */
    @Unique
    private static boolean forceCustomHearts = false;

    @Redirect(
            method = "renderHeart",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"
            )
    )
    private void redirectRenderHeart(
            GuiGraphics guiGraphics,
            ResourceLocation atlasLocation,
            int x,
            int y,
            int u,
            int v,
            int width,
            int height
    ) {
        // Try to fetch regeneration data for the player
        RegenerationData.get(this.minecraft.player).ifPresent(regenerationData -> {
            // Update cached state if valid data exists
            forceCustomHearts = regenerationData.regens() > 0;
        });

        // Decide which texture atlas to use (fallback to cached state if no data present this tick)
        ResourceLocation iconToRender = forceCustomHearts ? CUSTOM_ICONS : GUI_ICONS_LOCATION;

        // Render the heart icon
        guiGraphics.blit(iconToRender, x, y, u, v, width, height, 256, 256);
    }
}
