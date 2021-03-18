package me.suff.mc.regen.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.client.gui.IncarnationScreen;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.config.RegenConfig;
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

    private static final ResourceLocation BACKGROUND = new ResourceLocation(RConstants.MODID, "textures/gui/pref_back.png");
    private static TransitionTypes SELECTED_TYPE = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).transitionType();
    private static IRegen.TimelordSound SOUND_SCHEME = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).getTimelordSound();
    private static PlayerUtil.SkinType CHOICES = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).preferredModel();
    private float ROTATION = 0;

    public PreferencesScreen() {
        super(new BlankContainer(), null, new TranslationTextComponent("Regeneration"));
        imageWidth = 256;
        imageHeight = 173;
    }

    @Override
    public void init() {
        super.init();
        TabRegistry.updateTabValues(leftPos + 2, topPos, RegenPrefTab.class);
        for (AbstractTab button : TabRegistry.tabList) {
            addButton(button);
        }

        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;
        final int btnW = 66, btnH = 18;
        ROTATION = 0;

        Button btnClose = new Button(width / 2 - 109, cy + 145, 71, btnH, new TranslationTextComponent("regen.gui.close"), onPress -> Minecraft.getInstance().setScreen(null));

        Button btnScheme = new Button(width / 2 + 50 - 66, cy + 65, btnW * 2, btnH, new TranslationTextComponent("regen.gui.sound_scheme." + SOUND_SCHEME.name().toLowerCase()), button -> {
            IRegen.TimelordSound newOne = SOUND_SCHEME == IRegen.TimelordSound.DRUM ? IRegen.TimelordSound.HUM : IRegen.TimelordSound.DRUM;
            SOUND_SCHEME = newOne;
            button.setMessage(new TranslationTextComponent("regen.gui.sound_scheme." + newOne.name().toLowerCase()));
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ChangeSoundScheme(newOne));
        });


        Button btnRegenType = new Button(width / 2 + 50 - 66, cy + 105, btnW * 2, btnH, SELECTED_TYPE.get().getTranslation(), button -> {
            int pos = TransitionTypes.getPosition(SELECTED_TYPE) + 1;

            if (pos < 0 || pos >= TransitionTypes.TYPES.length) {
                pos = 0;
            }
            SELECTED_TYPE = TransitionTypes.TYPES[pos];
            button.setMessage(SELECTED_TYPE.get().getTranslation());
            NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new TypeMessage(SELECTED_TYPE.get()));
        });

        Button btnSkinType = new Button(width / 2 + 50 - 66, cy + 85, btnW * 2, btnH, new TranslationTextComponent("regeneration.skin_type." + CHOICES.name().toLowerCase()), button -> {
            if (CHOICES.next() != null) {
                CHOICES = CHOICES.next();
            } else {
                CHOICES = PlayerUtil.SkinType.ALEX;
            }
            button.setMessage(new TranslationTextComponent("regeneration.skin_type." + CHOICES.name().toLowerCase()));
            PlayerUtil.updateModel(CHOICES);
        });
        btnRegenType.setMessage(SELECTED_TYPE.get().getTranslation());

        Button btnColor = new Button(width / 2 + 50 - 66, cy + 125, btnW * 2, btnH, new TranslationTextComponent("regen.gui.color_gui"), button -> Minecraft.getInstance().setScreen(new ColorScreen()));

        Button btnSkinChoice = new Button(width / 2 + 50 - 66, cy + 145, btnW * 2, btnH, new TranslationTextComponent("regen.gui.skin_choice"), p_onPress_1_ -> {
            Minecraft.getInstance().setScreen(new IncarnationScreen());
        });

        addButton(btnRegenType);
        addButton(btnSkinChoice);
        addButton(btnClose);
        addButton(btnColor);
        addButton(btnSkinType);
        addButton(btnScheme);

        SELECTED_TYPE = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).transitionType();
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        Minecraft.getInstance().getTextureManager().bind(BACKGROUND);
        IRegen data = RegenCap.get(Minecraft.getInstance().player).orElseGet(null);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int cx = (width - imageWidth) / 2;
        int cy = (height - imageHeight) / 2;

        InventoryScreen.renderEntityInInventory(width / 2 - 75, height / 2 + 45, 55, (float) (leftPos + 51) - x, (float) (topPos + 75 - 50) - y, Minecraft.getInstance().player);

        String str = "Banana Phone";
        int length = minecraft.font.width(str);

        if (RegenConfig.COMMON.infiniteRegeneration.get())
            str = new TranslationTextComponent("regen.gui.infinite_regenerations").getString();
        else
            str = new TranslationTextComponent("regen.gui.remaining_regens.status", data.regens()).getString();

        length = minecraft.font.width(str);
        font.drawShadow(matrixStack, str, width / 2 + 50 - 66, cy + 21, Color.WHITE.getRGB());

        TranslationTextComponent traitLang = data.trait().getTranslation();
        font.drawShadow(matrixStack, traitLang.getString(), width / 2 + 50 - 66, cy + 40, Color.WHITE.getRGB());

        TranslationTextComponent traitLangDesc = data.trait().getDescription();
        font.drawShadow(matrixStack, traitLangDesc.getString(), width / 2 + 50 - 66, cy + 50, Color.WHITE.getRGB());

    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.title.getString(), (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        ROTATION++;
        if (ROTATION > 360) {
            ROTATION = 0;
        }
    }

}