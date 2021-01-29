package me.swirtzly.regeneration.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.gui.parts.ContainerBlank;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.common.types.RegenTypes;
import me.swirtzly.regeneration.handlers.ClientHandler;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.network.messages.UpdateTypeMessage;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import micdoodle8.mods.galacticraft.api.client.tabs.RegenPrefTab;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;

public class GuiPreferences extends ContainerScreen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Regeneration.MODID, "textures/gui/pref_back.png");
    private static RegenTypes SELECTED_TYPE = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).getRegenType();
    private static PlayerUtil.EnumChoices CHOICES = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).getPreferredModel();
    private float ROTATION = 0;

    public GuiPreferences() {
        super(new ContainerBlank(), null, new TranslationTextComponent("Regeneration"));
        xSize = 256;
        ySize = 173;
    }

    @Override
    public void init() {
        super.init();
        TabRegistry.updateTabValues(guiLeft + 2, guiTop, RegenPrefTab.class);
        for (AbstractTab button : TabRegistry.tabList) {
            addButton(button);
        }
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;
        final int btnW = 66, btnH = 18;
        ROTATION = 0;


        GuiButtonExt btnClose = new GuiButtonExt(width / 2 - 109, cy + 145, 71, btnH, new TranslationTextComponent("regeneration.gui.close").getFormattedText(), onPress -> Minecraft.getInstance().displayGuiScreen(null));
        GuiButtonExt btnRegenType = new GuiButtonExt(width / 2 + 50 - 66, cy + 125, btnW * 2, btnH, new TranslationTextComponent("regentype." + SELECTED_TYPE.getRegistryName()).getUnformattedComponentText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                int pos = RegenTypes.getPosition(SELECTED_TYPE) + 1;

                if (pos < 0 || pos >= RegenTypes.TYPES.length) {
                    pos = 0;
                }
                SELECTED_TYPE = RegenTypes.TYPES[pos];
                button.setMessage(new TranslationTextComponent("regeneration.gui.regen_type", SELECTED_TYPE.create().getTranslation()).getUnformattedComponentText());
                NetworkDispatcher.sendToServer(new UpdateTypeMessage(SELECTED_TYPE.getRegistryName().toString()));
            }
        });

        GuiButtonExt btnSkinType = new GuiButtonExt(width / 2 + 50 - 66, cy + 85, btnW * 2, btnH, new TranslationTextComponent("regeneration.gui.skintype", new TranslationTextComponent("regeneration.skin_type." + CHOICES.name().toLowerCase())).getUnformattedComponentText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                if (CHOICES.next() != null) {
                    CHOICES = (PlayerUtil.EnumChoices) CHOICES.next();
                } else {
                    CHOICES = PlayerUtil.EnumChoices.ALEX;
                }
                button.setMessage(new TranslationTextComponent("regeneration.gui.skintype", new TranslationTextComponent("regeneration.skin_type." + CHOICES.name().toLowerCase())).getUnformattedComponentText());
                PlayerUtil.updateModel(CHOICES);
            }
        });
        btnRegenType.setMessage(new TranslationTextComponent("regeneration.gui.regen_type", SELECTED_TYPE.create().getTranslation()).getUnformattedComponentText());

        GuiButtonExt btnColor = new GuiButtonExt(width / 2 + 50 - 66, cy + 105, btnW * 2, btnH, new TranslationTextComponent("regeneration.gui.color_gui").getUnformattedComponentText(), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                Minecraft.getInstance().displayGuiScreen(new ColorScreen());
            }
        });
        GuiButtonExt btnOpenFolder = new GuiButtonExt(width / 2 + 50 - 66, cy + 145, btnW * 2, btnH, new TranslationTextComponent("regeneration.gui.skin_choice").getFormattedText(), new Button.IPressable() {
            @Override
            public void onPress(Button p_onPress_1_) {
                Minecraft.getInstance().displayGuiScreen(new SkinChoiceScreen());
            }
        });

        addButton(btnRegenType);
        addButton(btnOpenFolder);
        addButton(btnClose);
        addButton(btnColor);
        addButton(btnSkinType);

        SELECTED_TYPE = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).getRegenType();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        IRegen data = RegenCap.get(Minecraft.getInstance().player).orElseGet(null);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        GlStateManager.pushMatrix();
        InventoryScreen.drawEntityOnScreen(width / 2 - 75, height / 2 + 45, 55, (float) (guiLeft + 51) - mouseX, (float) (guiTop + 75 - 50) - mouseY, Minecraft.getInstance().player);
        GlStateManager.popMatrix();

        drawCenteredString(Minecraft.getInstance().fontRenderer, new TranslationTextComponent("regeneration.gui.preferences").getUnformattedComponentText(), width / 2, height / 2 - 80, Color.WHITE.getRGB());

        String str = "Banana Phone";
        int length = minecraft.fontRenderer.getStringWidth(str);

        if (RegenConfig.COMMON.infiniteRegeneration.get())
            str = new TranslationTextComponent("regeneration.gui.infinite_regenerations").getFormattedText(); // TODO this should be optimized
        else
            str = new TranslationTextComponent("regeneration.gui.remaining_regens.status").getFormattedText() + " " + data.getRegenerationsLeft();

        length = minecraft.fontRenderer.getStringWidth(str);
        font.drawStringWithShadow(str, cx + 170 - length / 2, cy + 21, Color.WHITE.getRGB());

        TranslationTextComponent traitLang = new TranslationTextComponent(TraitManager.getDnaEntry(data.getTrait()).getLangKey());
        font.drawStringWithShadow(traitLang.getUnformattedComponentText(), cx + 170 - length / 2, cy + 40, Color.WHITE.getRGB());

        TranslationTextComponent traitLangDesc = new TranslationTextComponent(TraitManager.getDnaEntry(data.getTrait()).getLocalDesc());
        font.drawStringWithShadow(traitLangDesc.getUnformattedComponentText(), cx + 170 - length / 2, cy + 50, Color.WHITE.getRGB());

    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        ROTATION++;
        if (ROTATION > 360) {
            ROTATION = 0;
        }

    }

}
