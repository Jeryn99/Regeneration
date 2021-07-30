package me.suff.mc.regen.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TranslatableComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class SkinDownloadScreen extends AbstractContainerScreen {

    private static final ArrayList<SkinPack> PACKS = new ArrayList();

    public SkinDownloadScreen() {
        super(new BlankContainer(), Objects.requireNonNull(Minecraft.getInstance().player).inventory, new TranslatableComponent("Next Incarnation"));
        PACKS.clear();
        PACKS.addAll(SkinPack.getAll());
    }

    @Override
    protected void renderBg(PoseStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {

        SkinPack selectedPack = getPack("doctor_who:doctors");

        if (selectedPack != null) {
            drawCenteredString(p_230450_1_, Minecraft.getInstance().font, new TranslatableComponent(selectedPack.getName()).getString(), width / 2 + 60, height / 2 + 30, Color.WHITE.getRGB());

            // if (selectedPack.hasThumbnail()) {
            this.minecraft.getTextureManager().bind(selectedPack.getThumbnail());
            blit(p_230450_1_, leftPos, topPos, 0, 0, imageWidth, imageHeight);
            //}  // this.minecraft.getTextureManager().bindTexture(selectedPack.getTexture());

        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private SkinPack getPack(String s) {
        for (SkinPack pack : PACKS) {
            if (pack.getName().equals("doctor_who/doctors")) {
                return pack;
            }
        }
        return null;
    }
}
