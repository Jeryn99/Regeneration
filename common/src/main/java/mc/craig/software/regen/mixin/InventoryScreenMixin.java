package mc.craig.software.regen.mixin;

import mc.craig.software.regen.client.screen.PreferencesScreen;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class InventoryScreenMixin {

    @Shadow
    protected abstract <T extends GuiEventListener & Widget & NarratableEntry> T addRenderableWidget(T widget);

    private static final ResourceLocation PREFERENCES_BUTTON_LOCATION = new ResourceLocation(RConstants.MODID, "textures/gui/preferences_button.png");


    @Shadow
    public int width;

    @Shadow
    public int height;

    @Inject(at = @At("HEAD"), method = "init()V", cancellable = true)
    public void init(CallbackInfo ci) {

        Screen screen = (Screen) (Object) this;

        if (screen instanceof InventoryScreen inventoryScreen) {

            boolean recipeVisible = inventoryScreen.getRecipeBookComponent().isActive();
            this.addRenderableWidget(new ImageButton(width / 2 + 57, height / 2 - 22, 20, 18, 0, 0, 19, PREFERENCES_BUTTON_LOCATION, (button) -> {
                Minecraft.getInstance().setScreen(new PreferencesScreen());
            }));

        }
    }
}
