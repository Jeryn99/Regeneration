package mc.craig.software.regen.mixin;

import mc.craig.software.regen.client.screen.PreferencesScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class InventoryScreenMixin {

    @Shadow
    protected abstract <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T widget);

    @Shadow
    public int width;

    @Shadow
    public int height;

    @Inject(at = @At("HEAD"), method = "init()V", cancellable = true)
    public void init(CallbackInfo ci) {

        Screen screen = (Screen) (Object) this;

        if (screen instanceof InventoryScreen inventoryScreen) {
            this.addRenderableWidget(new Button(width / 2 + 67, height / 6 + 96 - 6, 50, 50, Component.translatable("options.chat.title"), (button) -> {
                Minecraft.getInstance().setScreen(new PreferencesScreen());
            }));
        }
    }
}
