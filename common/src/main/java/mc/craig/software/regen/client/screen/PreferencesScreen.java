package mc.craig.software.regen.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.network.messages.ChangeSoundScheme;
import mc.craig.software.regen.network.messages.TypeMessage;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.function.Function;

public class PreferencesScreen extends AbstractContainerScreen {

    private static final ResourceLocation screenBackground = new ResourceLocation(RConstants.MODID, "textures/gui/preferences.png");
    private static TransitionType transitionType = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).transitionType();
    private static IRegen.TimelordSound soundScheme = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).getTimelordSound();
    private static PlayerUtil.SkinType skinType = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).preferredModel();

    public PreferencesScreen() {
        super(new BlankContainer(), Minecraft.getInstance().player.getInventory(), Component.literal("Regeneration"));
        imageWidth = 256;
        imageHeight = 173;
    }

    @Override
    public void init() {
        super.init();

        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        final int btnW = 66, btnH = 20;

        Button btnClose = new Button(width / 2 - 109, cy + 145, 71, btnH, Component.translatable("regen.gui.close"), onPress -> Minecraft.getInstance().setScreen(null));

        Button btnScheme = new Button(width / 2 + 50 - 66, cy + 60, btnW * 2, btnH, Component.translatable("regen.gui.sound_scheme." + soundScheme.name().toLowerCase()), button -> {

            IRegen.TimelordSound newOne;
            IRegen.TimelordSound[] values = soundScheme.getAllValues();

            //what is this
            if (soundScheme.ordinal() == values[values.length - 1].ordinal()) {
                newOne = soundScheme = IRegen.TimelordSound.values()[0];
            } else {
                newOne = soundScheme = soundScheme.next();
            }
            button.setMessage(Component.translatable("regen.gui.sound_scheme." + newOne.name().toLowerCase()));
            new ChangeSoundScheme(newOne).send();
        });


        Button btnRegenType = new Button(width / 2 + 50 + 2, cy + 81, btnW - 2, btnH, transitionType.getTranslation(), button -> {
            int pos = TransitionTypes.getPosition(transitionType) + 1;

            if (pos < 0 || pos >= TransitionTypes.TYPES.length) {
                pos = 0;
            }
            transitionType = TransitionTypes.TYPES[pos];
            button.setMessage(transitionType.getTranslation());
            new TypeMessage(transitionType).send();
        });

        this.addRenderableWidget(CycleButton.builder((Function<PlayerUtil.SkinType, Component>) skinType -> Component.translatable("regeneration.skin_type." + skinType.name().toLowerCase())).withValues(PlayerUtil.SkinType.values()).withInitialValue(skinType).create(width / 2 + 50 - 66, cy + 81, btnW, btnH,  Component.nullToEmpty(""), (skinTypeCycleButton, skinType) -> {
            PlayerUtil.updateModel(skinType);
        }));

        btnRegenType.setMessage(transitionType.getTranslation());

        Button btnColor = new Button(width / 2 + 50 - 66, cy + 103, btnW, btnH, Component.translatable("regen.gui.color_gui"), button -> Minecraft.getInstance().setScreen(new ColorScreen()));

        Button btnSkinChoice = new Button(width / 2 + 50 + 2, cy + 103, btnW - 2, btnH, Component.translatable("regen.gui.skin_choice"), p_onPress_1_ -> Minecraft.getInstance().setScreen(new IncarnationScreen()));

        addRenderableWidget(btnRegenType);
        addRenderableWidget(btnSkinChoice);
        addRenderableWidget(btnClose);
        addRenderableWidget(btnColor);
        addRenderableWidget(btnScheme);

        transitionType = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).transitionType();
    }

    @Override
    protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderTexture(0, screenBackground);
        IRegen data = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        InventoryScreen.renderEntityInInventory(width / 2 - 75, height / 2 + 45, 55, (float) (leftPos + 51) - x, (float) (topPos + 75 - 50) - y, Minecraft.getInstance().player);
        String str = Component.translatable("regen.gui.remaining_regens.status", data.regens()).getString();
        font.drawShadow(matrixStack, str, width / 2 + 50 - 66, cy + 21, Color.WHITE.getRGB());
    }

    @Override
    protected void renderLabels(@NotNull PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

}