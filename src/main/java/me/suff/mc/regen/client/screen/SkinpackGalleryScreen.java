package me.suff.mc.regen.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.suff.mc.regen.client.skin.CommonSkin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class SkinpackGalleryScreen extends Screen {

    private static final TranslationTextComponent TITLE = new TranslationTextComponent("nicephore.gui.screenshots");
    private static final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
    private static final int ROW = 2;
    private static final int COLUMN = 4;
    private static final int IMAGES_TO_DISPLAY = ROW * COLUMN;
    private static File SCREENSHOTS_DIR = CommonSkin.THUMBNAILS;
    private static ArrayList<ResourceLocation> THUMBNAILS = new ArrayList<>();
    private ArrayList<File> allThumbnails;
    private ArrayList<List<File>> pagesOfThumbnails;
    private int index;
    private float aspectRatio;

    public SkinpackGalleryScreen(int index) {
        super(TITLE);
        this.index = index;
    }

    public static <T> Stream<List<T>> batches(List<T> source, int length) {
        if (length <= 0)
            throw new IllegalArgumentException("length = " + length);
        int size = source.size();
        if (size <= 0)
            return Stream.empty();
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }

    public static boolean canBeShow() {
        return SCREENSHOTS_DIR.exists() && SCREENSHOTS_DIR.list().length > 0;
    }

    @Override
    protected void init() {
        super.init();

        allThumbnails = (ArrayList<File>) Arrays.stream(SCREENSHOTS_DIR.listFiles()).sorted(Comparator.comparingLong(File::lastModified).reversed()).collect(Collectors.toList());
        pagesOfThumbnails = (ArrayList<List<File>>) batches(allThumbnails, IMAGES_TO_DISPLAY).collect(Collectors.toList());
        index = getIndex();
        aspectRatio = 1.7777F;

        if (!allThumbnails.isEmpty()) {
            try {
                BufferedImage bimg = ImageIO.read(allThumbnails.get(index));
                final int width = bimg.getWidth();
                final int height = bimg.getHeight();
                bimg.getGraphics().dispose();
                aspectRatio = (float) (width / (double) height);
            } catch (IOException e) {
                e.printStackTrace();
            }

            THUMBNAILS.forEach(textureManager::release);
            THUMBNAILS.clear();

            List<File> filesToLoad = pagesOfThumbnails.get(index);
            if (!filesToLoad.isEmpty()) {
                filesToLoad.forEach(file -> THUMBNAILS.add(CommonSkin.fileTotexture(file)));
            } else {
                closeScreen("nicephore.screenshots.loading.error");
                return;
            }
        }

        this.buttons.clear();

        if (!allThumbnails.isEmpty()) {
            this.addButton(new Button(this.width / 2 + 50, this.height / 2 + 100, 20, 20, new TranslationTextComponent(">"), button -> modIndex(1)));
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        final int centerX = this.width / 2;
        final int imageWidth = (int) (this.width * 1.0 / 5);
        final int imageHeight = (int) (imageWidth / aspectRatio);

        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        final List<File> currentPage = pagesOfThumbnails.get(index);
        if (currentPage.stream().allMatch(File::exists)) {
            for (ResourceLocation TEXTURE : THUMBNAILS) {
                final int imageIndex = THUMBNAILS.indexOf(TEXTURE);
                final String name = currentPage.get(imageIndex).getName();
                final TranslationTextComponent text = new TranslationTextComponent(StringUtils.capitalize(name).replaceAll(".png", ""));

                Minecraft.getInstance().textureManager.bind(TEXTURE);

                switch (imageIndex) {
                    case 0:
                        blit(matrixStack, centerX - 15 - 2 * imageWidth, 50, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                        this.addButton(new Button(centerX - 15 - 2 * imageWidth, 55 + imageHeight, imageWidth, 20, text, button -> lookNDownload(text, button)));
                        break;
                    case 1:
                        blit(matrixStack, centerX - 5 - imageWidth, 50, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                        this.addButton(new Button(centerX - 5 - imageWidth, 55 + imageHeight, imageWidth, 20, text, button -> lookNDownload(text, button)));
                        break;
                    case 2:
                        blit(matrixStack, centerX + 5, 50, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                        this.addButton(new Button(centerX + 5, 55 + imageHeight, imageWidth, 20, text, button -> lookNDownload(text, button)));
                        break;
                    case 3:
                        blit(matrixStack, centerX + 15 + imageWidth, 50, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                        this.addButton(new Button(centerX + 15 + imageWidth, 55 + imageHeight, imageWidth, 20, text, button -> lookNDownload(text, button)));
                        break;
                    case 4:
                        blit(matrixStack, centerX - 15 - 2 * imageWidth, imageHeight + 80, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                        this.addButton(new Button(centerX - 15 - 2 * imageWidth, 2 * imageHeight + 85, imageWidth, 20, text, button -> lookNDownload(text, button)));
                    case 5:
                        blit(matrixStack, centerX - 5 - imageWidth, imageHeight + 80, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                        this.addButton(new Button(centerX - 5 - imageWidth, 2 * imageHeight + 85, imageWidth, 20, text, button -> lookNDownload(text, button)));
                        break;
                    case 6:
                        blit(matrixStack, centerX + 5, imageHeight + 80, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                        this.addButton(new Button(centerX + 5, 2 * imageHeight + 85, imageWidth, 20, text, button -> lookNDownload(text, button)));
                        break;
                    case 7:
                        blit(matrixStack, centerX + 15 + imageWidth, imageHeight + 80, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);
                        this.addButton(new Button(centerX + 15 + imageWidth, 2 * imageHeight + 85, imageWidth, 20, text, button -> lookNDownload(text, button)));
                        break;
                }
            }
        }
    }

    private void lookNDownload(TranslationTextComponent indexOf, Button button) {
        String name = indexOf.getString();
        for (SkinPack skinPack : SkinPack.getAll()) {
            if (skinPack.location().getPath().equalsIgnoreCase(name)) {
                CommonSkin.downloadOnCommand(skinPack);
                button.visible = false;
            }
        }
    }

    private void modIndex(int value) {
        final int max = pagesOfThumbnails.size();
        if (index + value >= 0 && index + value < max) {
            index += value;
        } else {
            if (index + value < 0) {
                index = max - 1;
            } else {
                index = 0;
            }
        }
        init();
    }

    private int getIndex() {
        if (index >= pagesOfThumbnails.size() || index < 0) {
            index = pagesOfThumbnails.size() - 1;
        }
        return index;
    }

    private void closeScreen(String textComponentId) {
        this.onClose();
    }
}