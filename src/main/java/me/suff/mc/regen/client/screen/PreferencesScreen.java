package me.suff.mc.regen.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.transitions.TransitionType;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.ChangeSoundScheme;
import me.suff.mc.regen.network.messages.TypeMessage;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RConstants;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class PreferencesScreen extends ContainerScreen {

    private static final ResourceLocation screenBackground = new ResourceLocation(RConstants.MODID, "textures/gui/preferences.png");
    private static TransitionType transitionType = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).transitionType();
    private static IRegen.TimelordSound soundScheme = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).getTimelordSound();
    private static PlayerUtil.SkinType skinType = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).preferredModel();

    public PreferencesScreen() {
        super(new BlankContainer(), null, new TranslationTextComponent("Regeneration"));
        imageWidth = 256;
        imageHeight = 173;
    }

    @Override
    public void init() {
        super.init();
        TabRegistry.updateTabValues(leftPos + 2, topPos, RegenPrefTab.class);
        for (AbstractTab button : TabRegistry.getTabList()) {
            addButton(button);
        }

        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        final int btnW = 66, btnH = 20;

        Button btnClose = new Button(width / 2 - 109, cy + 145, 71, btnH, new TranslationTextComponent("regen.gui.close"), onPress -> Minecraft.getInstance().setScreen(null));

        Button btnScheme = new Button(width / 2 + 50 - 66, cy + 60, btnW * 2, btnH, new TranslationTextComponent("regen.gui.sound_scheme." + soundScheme.name().toLowerCase()), button -> {

            IRegen.TimelordSound newOne;
            IRegen.TimelordSound[] values = soundScheme.getAllValues();

            //what is this
            if (soundScheme.ordinal() == values[values.length - 1].ordinal()) {
                newOne = soundScheme = IRegen.TimelordSound.values()[0];
            } else {
                newOne = soundScheme = soundScheme.next();
            }
            button.setMessage(new TranslationTextComponent("regen.gui.sound_scheme." + newOne.name().toLowerCase()));
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ChangeSoundScheme(newOne));
        });


        Button btnRegenType = new Button(width / 2 + 50 + 2, cy + 81, btnW - 2, btnH, transitionType.getTranslation(), button -> {
            int pos = TransitionTypes.getPosition(transitionType) + 1;

            if (pos < 0 || pos >= TransitionTypes.TYPES.length) {
                pos = 0;
            }
            transitionType = TransitionTypes.TYPES[pos];
            button.setMessage(transitionType.getTranslation());
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new TypeMessage(transitionType));
        });

        Button btnSkinType = new Button(width / 2 + 50 - 66, cy + 81, btnW, btnH, new TranslationTextComponent("regeneration.skin_type." + skinType.name().toLowerCase()), button -> {
            if (skinType.next() != null) {
                skinType = skinType.next();
            } else {
                skinType = PlayerUtil.SkinType.ALEX;
            }
            button.setMessage(new TranslationTextComponent("regeneration.skin_type." + skinType.name().toLowerCase()));
            PlayerUtil.updateModel(skinType);
        });
        btnRegenType.setMessage(transitionType.getTranslation());

        Button btnColor = new Button(width / 2 + 50 - 66, cy + 103, btnW, btnH, new TranslationTextComponent("regen.gui.color_gui"), button -> Minecraft.getInstance().setScreen(new ColorScreen()));

        Button btnSkinChoice = new Button(width / 2 + 50 + 2, cy + 103, btnW - 2, btnH, new TranslationTextComponent("regen.gui.skin_choice"), p_onPress_1_ -> {
            Minecraft.getInstance().setScreen(new IncarnationScreen());
        });

        addButton(btnRegenType);
        addButton(btnSkinChoice);
        addButton(btnClose);
        addButton(btnColor);
        addButton(btnSkinType);
        addButton(btnScheme);

        transitionType = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).transitionType();
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        Minecraft.getInstance().getTextureManager().bind(screenBackground);
        IRegen data = RegenCap.get(Minecraft.getInstance().player).orElseGet(null);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        InventoryScreen.renderEntityInInventory(width / 2 - 75, height / 2 + 45, 55, (float) (leftPos + 51) - x, (float) (topPos + 75 - 50) - y, Minecraft.getInstance().player);
        String str = new TranslationTextComponent("regen.gui.remaining_regens.status", data.regens()).getString();
        font.drawShadow(matrixStack, str, width / 2 + 50 - 66, cy + 21, Color.WHITE.getRGB());
        RegenCap.get(Minecraft.getInstance().player).ifPresent(iRegen -> {
            int color = iRegen.traitActive() ? Color.GREEN.getRGB() : Color.RED.getRGB();
            TranslationTextComponent traitLang = data.trait().translation();
            font.drawShadow(matrixStack, traitLang.getString(), width / 2 + 50 - 66, cy + 35, color);
            TranslationTextComponent traitLangDesc = data.trait().description();
            font.drawShadow(matrixStack, traitLangDesc.getString(), width / 2 + 50 - 66, cy + 45, color);
        });

    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

}