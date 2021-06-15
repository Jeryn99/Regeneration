package me.suff.mc.regen.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.client.skin.CommonSkin;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RErrorScreen extends Screen {
    private final ITextComponent message;

    public RErrorScreen(ITextComponent p_i232277_1_, ITextComponent p_i232277_2_) {
        super(p_i232277_1_);
        this.message = p_i232277_2_;
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(new Button(this.width / 2 - 100, 140, 200, 20, new TranslationTextComponent("button.tooltip.open_folder"), (p_213034_1_) -> {
            Util.getPlatform().openFile(CommonSkin.SKIN_DIRECTORY);
        }));
    }

    @Override
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.fillGradient(p_230430_1_, 0, 0, this.width, this.height, -12574688, -11530224);
        drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 90, 16777215);
        drawCenteredString(p_230430_1_, this.font, this.message, this.width / 2, 110, 16777215);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }
}
