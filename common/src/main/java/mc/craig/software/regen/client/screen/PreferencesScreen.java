package mc.craig.software.regen.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mc.craig.software.regen.client.screen.overlay.RegenerationOverlay;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.transitions.TransitionType;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.network.messages.ChangeSoundScheme;
import mc.craig.software.regen.network.messages.TypeMessage;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.awt.*;
import java.util.function.Function;

public class PreferencesScreen extends Screen {

    private static final ResourceLocation screenBackground = new ResourceLocation(RConstants.MODID, "textures/gui/preferences.png");
    private static PlayerUtil.SkinType skinType = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).preferredModel();
    private static TransitionType transitionType = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).transitionType();
    private static IRegen.TimelordSound soundScheme = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).getTimelordSound();

    protected int imageWidth, imageHeight = 0;
    private int leftPos, topPos;

    public PreferencesScreen() {
        super(Component.literal("Regeneration"));
        imageWidth = 256;
        imageHeight = 173;
    }

    @Override
    public void init() {
        super.init();

        skinType = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).preferredModel();

        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        final int btnW = 66, btnH = 20;

        this.addRenderableWidget(new ImageButton(4, 4, 20, 18, 0, 0, 19, ColorScreen.PREFERENCES_BUTTON_LOCATION, (button) -> {
            Minecraft.getInstance().setScreen(null);
        }));

        Button btnClose = Button.builder(Component.translatable("gui.regen.close"), onPress -> Minecraft.getInstance().setScreen(null))
                .bounds(width / 2 - 109, cy + 145, 71, btnH).build();

        Button btnScheme = Button.builder(Component.translatable("gui.regen.sound_scheme." + soundScheme.name().toLowerCase()), button -> {
            IRegen.TimelordSound newOne;
            IRegen.TimelordSound[] values = soundScheme.getAllValues();

            //what is this
            if (soundScheme.ordinal() == values[values.length - 1].ordinal()) {
                newOne = soundScheme = IRegen.TimelordSound.values()[0];
            } else {
                newOne = soundScheme = soundScheme.next();
            }
            button.setMessage(Component.translatable("gui.regen.sound_scheme." + newOne.name().toLowerCase()));
            new ChangeSoundScheme(newOne).send();
        }).bounds(width / 2 + 50 - 66, cy + 60, btnW * 2, btnH).build();


        Button btnRegenType = Button.builder(transitionType.getTranslation(), button -> {
            int pos = TransitionTypes.getPosition(transitionType) + 1;

            if (pos < 0 || pos >= TransitionTypes.TYPES.length) {
                pos = 0;
            }
            transitionType = TransitionTypes.TYPES[pos];
            button.setMessage(transitionType.getTranslation());
            new TypeMessage(transitionType).send();
        }).bounds(width / 2 + 50 + 2, cy + 81, btnW - 2, btnH).build();

        this.addRenderableWidget(CycleButton.builder((Function<PlayerUtil.SkinType, Component>) skinType -> Component.translatable("regeneration.skin_type." + skinType.name().toLowerCase())).withValues(PlayerUtil.SkinType.values()).withInitialValue(skinType).create(width / 2 + 50 - 66, cy + 81, btnW, btnH, Component.nullToEmpty("Model"), (skinTypeCycleButton, skinType) -> {
            PreferencesScreen.skinType = skinType;
            PlayerUtil.updateModel(skinType);
        }));

        btnRegenType.setMessage(transitionType.getTranslation());

        Button btnColor = Button.builder(Component.translatable("gui.regen.color_gui"), button -> Minecraft.getInstance().setScreen(new ColorScreen()))
                .bounds(width / 2 + 50 - 66, cy + 103, btnW, btnH).build();

        Button btnSkinChoice = Button.builder(Component.translatable("gui.regen.skin_choice"), p_onPress_1_ -> Minecraft.getInstance().setScreen(new IncarnationScreen())).bounds(width / 2 + 50 + 2, cy + 103, btnW - 2, btnH).build();

        addRenderableWidget(btnRegenType);
        addRenderableWidget(btnSkinChoice);
        addRenderableWidget(btnClose);
        addRenderableWidget(btnColor);
        addRenderableWidget(btnScheme);

        transitionType = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null).transitionType();
    }

    private static final int deactivatedColor = Color.RED.getRGB();
    private static final int activatedColor = Color.GREEN.getRGB();

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);

        RenderSystem.setShaderTexture(0, screenBackground);
        IRegen data = RegenerationData.get(Minecraft.getInstance().player).orElseGet(null);
        guiGraphics.blit(screenBackground, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, width / 2 - 75, height / 2 + 45, 55, (float) (leftPos + 51) - mouseX, (float) (topPos + 75 - 50) - mouseY, Minecraft.getInstance().player);
        String str = Component.translatable("gui.regen.remaining_regens.status", data.regens()).getString();
        guiGraphics.drawString(font, str, width / 2 + 50 - 66, cy + 21, Color.WHITE.getRGB());

        RenderSystem.setShaderTexture(0, RegenerationOverlay.CUSTOM_ICONS);

        for (int i = 0; i < data.regens(); i++) {
            guiGraphics.blit(RegenerationOverlay.CUSTOM_ICONS, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + (i * 10) - 10, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 45, 34, 0, 9, 9, 256, 256);
            guiGraphics.blit(RegenerationOverlay.CUSTOM_ICONS, Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + (i * 10) - 10, Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 - 45, 52, 0, 9, 9, 256, 256);
        }


        boolean isTraitActivated = data.isTraitActive();

        int color = isTraitActivated ? activatedColor : deactivatedColor;

        Component traitLang = data.getCurrentTrait().getTitle();
        IncarnationScreen.renderWidthScaledText(traitLang.getString(), guiGraphics, font, width / 2 + 120 - 70, cy + 135, color, 100);
        Component traitLangDesc = data.getCurrentTrait().getDescription();
        IncarnationScreen.renderWidthScaledText(traitLangDesc.getString(), guiGraphics, font, width / 2 + 120 - 70, cy + 145, color, 100);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

}