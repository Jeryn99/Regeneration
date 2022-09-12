package mc.craig.software.regen.mixin;

import mc.craig.software.regen.client.screen.ColorScreen;
import mc.craig.software.regen.client.screen.PreferencesScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class InventoryScreenMixin {

    @Shadow
    protected abstract <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T widget);

    @Inject(at = @At("HEAD"), method = "init()V", cancellable = true)
    public void init(CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;
        if (screen instanceof InventoryScreen) {
            this.addRenderableWidget(new ImageButton(4, 4, 19, 18, 0, 0, 19, ColorScreen.PREFERENCES_BUTTON_LOCATION, (button) -> {
                Minecraft.getInstance().setScreen(new PreferencesScreen());
            }));
        }
    }
}
