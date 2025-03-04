package mc.craig.software.regen.mixin;

import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static mc.craig.software.regen.client.screen.overlay.RegenerationOverlay.CUSTOM_ICONS;

@Mixin(Gui.class)
public class GuiMixin {
 
}
