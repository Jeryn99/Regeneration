package mc.craig.software.regen.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.client.skin.SkinRetriever;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
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
        this.addRenderableWidget(new Button(this.width / 2 - 100, 140, 200, 20, Component.translatable("button.tooltip.open_folder"), (p_213034_1_) -> {
            Util.getPlatform().openFile(SkinRetriever.SKINS_DIR);
            SkinRetriever.folderSetup();
            Minecraft.getInstance().setScreen(null);
        }));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.fillGradient(poseStack, 0, 0, this.width, this.height, -12574688, -11530224);
        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 90, 16777215);
        drawCenteredString(poseStack, this.font, this.message, this.width / 2, 110, 16777215);
        super.render(poseStack, mouseX, mouseY, partialTick);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }
}
