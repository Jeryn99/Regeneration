package mc.craig.software.regen.client.screen;

import mc.craig.software.regen.client.skin.SkinRetriever;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RErrorScreen extends Screen {
    private final Component message;

    public RErrorScreen(Component component, Component message) {
        super(component);
        this.message = message;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(Button.builder(Component.translatable("button_tooltip.regen.open_folder"), (p_213034_1_) -> {
            Util.getPlatform().openFile(SkinRetriever.SKINS_DIR);
            SkinRetriever.folderSetup();
            Minecraft.getInstance().setScreen(null);
        }).bounds(this.width / 2 - 100, 140, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, -12574688, -11530224);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 90, 16777215);
        guiGraphics.drawCenteredString(this.font, this.message, this.width / 2, 110, 16777215);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }
}
