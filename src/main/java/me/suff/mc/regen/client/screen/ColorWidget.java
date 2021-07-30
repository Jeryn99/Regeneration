package me.suff.mc.regen.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.awt.*;

import static java.lang.Math.*;

//Based on https://github.com/Petersil1998/utilcraft/blob/master/src/main/java/net/petersil98/utilcraft/screen/widget/ColorChooser.java
public class ColorWidget extends AbstractWidget {

    protected final RCheckbox.IInteraction<ColorWidget> onPress;
    private final int radius;
    private final int textHeight;
    private int color;
    private EditBox text = null;


    public ColorWidget(Font font, int x, int y, int width, int textHeight, Component title, int defaultColor, RCheckbox.IInteraction<ColorWidget> iInteraction) {
        super(x, y, width, width, title);
        radius = width / 2;
        color = defaultColor;
        text = new EditBox(font, x, y + width + 10, width - textHeight - 5, textHeight, this.text, title);
        text.setEditable(true);
        this.textHeight = textHeight;
        this.onPress = iInteraction;

        text.setResponder(s -> {
            try {
                Color color = Color.decode(s);
                setColor(color.getRGB());
                updateColor();
            } catch (Exception e) {
            }
        });

        updateColor();
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            for (int x = -radius; x < radius; x++) {
                for (int y = -radius; y < radius; y++) {
                    double distance = sqrt(x * x + y * y);

                    if (distance > radius) {
                        continue;
                    }

                    double angle = atan2(y, x);
                    double degrees = toDegrees(angle);

                    float hue = (float) (degrees < 0 ? 360 + degrees : degrees);
                    float saturation = (float) (distance / radius);

                    fill(matrixStack, x + radius + this.x, y + radius + this.y, x + radius + 1 + this.x, y + radius + 1 + this.y, hsv2rgb(hue, saturation, 1));
                }
            }
        }
        text.render(matrixStack, mouseX, mouseY, partialTicks);
        fill(matrixStack, x + width - textHeight - 1, y + width + 9, x + width, y + width + 10 + textHeight + 1, 0xFF9E9E9E);
        fill(matrixStack, x + width - textHeight, y + width + 10, x + width - 1, y + width + 10 + textHeight, color);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        double distance = sqrt(pow(mouseX - x - radius, 2) + pow(mouseY - y - radius, 2));
        if (distance <= radius) {
            double angle = atan2(mouseY - y - radius, mouseX - x - radius);
            double degrees = toDegrees(angle);

            float hue = (float) (degrees < 0 ? 360 + degrees : degrees);
            float saturation = (float) (distance / radius);
            color = hsv2rgb(hue, saturation, 1);
            updateColor();
        }
        onPress.onPress(this);
    }

    private void updateColor() {
        text.setValue(String.format("#%s", Integer.toHexString(color)).toUpperCase());
    }

    public int hsv2rgb(float hue, float saturation, float value) {
        float chroma = value * saturation;
        float hue1 = hue / 60;
        float x = chroma * (1 - Math.abs((hue1 % 2) - 1));
        float r1 = 0, g1 = 0, b1 = 0;
        if (hue1 >= 0 && hue1 <= 1) {
            r1 = chroma;
            g1 = x;
            b1 = 0;
        } else if (hue1 >= 1 && hue1 <= 2) {
            r1 = x;
            g1 = chroma;
            b1 = 0;
        } else if (hue1 >= 2 && hue1 <= 3) {
            r1 = 0;
            g1 = chroma;
            b1 = x;
        } else if (hue1 >= 3 && hue1 <= 4) {
            r1 = 0;
            g1 = x;
            b1 = chroma;
        } else if (hue1 >= 4 && hue1 <= 5) {
            r1 = x;
            g1 = 0;
            b1 = chroma;
        } else if (hue1 >= 5 && hue1 <= 6) {
            r1 = chroma;
            g1 = 0;
            b1 = x;
        }

        float m = value - chroma;
        float r = r1 + m, g = g1 + m, b = b1 + m;

        return (0xff << 24) | (((int) (255 * r) & 0xff) << 16) | (((int) (255 * g) & 0xff) << 8) | ((int) (255 * b) & 0xff);
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        updateColor();
    }


    public void tick() {
        text.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.text.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}