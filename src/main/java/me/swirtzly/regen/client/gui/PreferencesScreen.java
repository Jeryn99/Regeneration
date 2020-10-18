package me.swirtzly.regen.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.transitions.TransitionTypes;
import me.swirtzly.regen.common.traits.Traits;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.TypeMessage;
import me.swirtzly.regen.util.PlayerUtil;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class PreferencesScreen extends ContainerScreen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(RConstants.MODID, "textures/gui/pref_back.png");
    private static TransitionTypes SELECTED_TYPE = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).getTransitionType();
    private static PlayerUtil.SkinType CHOICES = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).getPreferredModel();
    private float ROTATION = 0;

    public PreferencesScreen() {
        super(new BlankContainer(), null, new TranslationTextComponent("Regeneration"));
        xSize = 256;
        ySize = 173;
    }

    @Override
    public void init() {
        super.init();

        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;
        final int btnW = 66, btnH = 18;
        ROTATION = 0;

        Button btnClose = new Button(width / 2 - 109, cy + 145, 71, btnH, new TranslationTextComponent("regen.gui.close"), onPress -> Minecraft.getInstance().displayGuiScreen(null));
        Button btnRegenType = new Button(width / 2 + 50 - 66, cy + 125, btnW * 2, btnH, new TranslationTextComponent("regentype." + SELECTED_TYPE.getRegistryName()), new Button.IPressable() {
            @Override
            public void onPress(Button button) {
                int pos = TransitionTypes.getPosition(SELECTED_TYPE) + 1;

                if (pos < 0 || pos >= TransitionTypes.TYPES.length) {
                    pos = 0;
                }
                SELECTED_TYPE = TransitionTypes.TYPES[pos];
                button.setMessage(new TranslationTextComponent("regen.gui.regen_type", SELECTED_TYPE.get().getTranslation()));
                NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new TypeMessage(SELECTED_TYPE.get()));
            }
        });

        Button btnSkinType = new Button(width / 2 + 50 - 66, cy + 85, btnW * 2, btnH, new TranslationTextComponent("regen.gui.skintype", new TranslationTextComponent("regeneration.skin_type." + CHOICES.name().toLowerCase())), button -> {
            if (CHOICES.next() != null) {
                CHOICES = CHOICES.next();
            } else {
                CHOICES = PlayerUtil.SkinType.ALEX;
            }
            button.setMessage(new TranslationTextComponent("regen.gui.skintype", new TranslationTextComponent("regeneration.skin_type." + CHOICES.name().toLowerCase())));
            PlayerUtil.updateModel(CHOICES);
        });
        btnRegenType.setMessage(new TranslationTextComponent("regen.gui.regen_type", SELECTED_TYPE.get().getTranslation()));

        Button btnColor = new Button(width / 2 + 50 - 66, cy + 105, btnW * 2, btnH, new TranslationTextComponent("regen.gui.color_gui"), button -> Minecraft.getInstance().displayGuiScreen(new ColorScreen()));
        Button btnSkinChoice = new Button(width / 2 + 50 - 66, cy + 145, btnW * 2, btnH, new TranslationTextComponent("regen.gui.skin_choice"), p_onPress_1_ -> {
            Minecraft.getInstance().displayGuiScreen(new IncarnationScreen());
        });

        addButton(btnRegenType);
        addButton(btnSkinChoice);
        addButton(btnClose);
        addButton(btnColor);
        addButton(btnSkinType);

        SELECTED_TYPE = RegenCap.get(Minecraft.getInstance().player).orElseGet(null).getTransitionType();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        Minecraft.getInstance().getTextureManager().bindTexture(BACKGROUND);
        IRegen data = RegenCap.get(Minecraft.getInstance().player).orElseGet(null);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
        int cx = (width - xSize) / 2;
        int cy = (height - ySize) / 2;

        GlStateManager.pushMatrix();
        InventoryScreen.drawEntityOnScreen(width / 2 - 75, height / 2 + 45, 55, (float) (guiLeft + 51) - x, (float) (guiTop + 75 - 50) - y, Minecraft.getInstance().player);
        GlStateManager.popMatrix();

        drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer, new TranslationTextComponent("regen.gui.preferences").getString(), width / 2, height / 2 - 80, Color.WHITE.getRGB());

        String str = "Banana Phone";
        int length = minecraft.fontRenderer.getStringWidth(str);

        if (RegenConfig.COMMON.infiniteRegeneration.get())
            str = new TranslationTextComponent("regen.gui.infinite_regenerations").getString();
        else
            str = new TranslationTextComponent("regen.gui.remaining_regens.status", data.getRegens()).getString();

        length = minecraft.fontRenderer.getStringWidth(str);
        font.drawStringWithShadow(matrixStack, str, cx + 170 - length / 2, cy + 21, Color.WHITE.getRGB());

        TranslationTextComponent traitLang = data.getTrait().getTranslation();
        font.drawStringWithShadow(matrixStack, traitLang.getString(), cx + 170 - length / 2, cy + 40, Color.WHITE.getRGB());

        TranslationTextComponent traitLangDesc = data.getTrait().getDescription();
        font.drawStringWithShadow(matrixStack, traitLangDesc.getString(), cx + 170 - length / 2, cy + 50, Color.WHITE.getRGB());

    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float) this.titleX, (float) this.titleY, 4210752);
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