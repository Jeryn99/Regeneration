package me.suff.mc.regen.util;

import me.suff.mc.regen.RegenerationMod;
import me.suff.mc.regen.client.image.ImageDownloadAlt;
import me.suff.mc.regen.client.skinhandling.SkinChangingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static me.suff.mc.regen.client.skinhandling.SkinChangingHandler.*;

public class FileUtil {

    private static final String[] extensions = {"png"};

    /**
     * Creates skin folders Proceeds to download skins to the folders if they are empty If the download doesn't happen, NPEs will occur later on
     */
    public static void createDefaultFolders() throws IOException {

        if (!SKIN_DIRECTORY.exists()) {
            FileUtils.forceMkdir(SKIN_DIRECTORY);
        }

        if (!SKIN_DIRECTORY_ALEX.exists()) {
            FileUtils.forceMkdir(SKIN_DIRECTORY_ALEX);
        }

        if (!SKIN_DIRECTORY_STEVE.exists()) {
            FileUtils.forceMkdir(SKIN_DIRECTORY_STEVE);
        }
    }

    /**
     * @param url      - URL to download image from
     * @param filename - Filename of the image [SHOULD NOT CONTAIN FILE EXTENSION, PNG IS SUFFIXED FOR YOU]
     * @throws IOException
     */
    public static void downloadAsPng(URL url, String filename, File alexDir, File steveDir) throws IOException {
        URLConnection uc = url.openConnection();
        uc.connect();
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        SkinChangingHandler.SKIN_LOG.info("Downloading Skin from: {}", url.toString());
        BufferedImage img = ImageIO.read(uc.getInputStream());
        img = ClientUtil.ImageFixer.convertSkinTo64x64(img);

        File file = ImageDownloadAlt.isAlexSkin(img) ? alexDir : steveDir;

        if (!file.exists()) {
            file.mkdirs();
        }

        if (!steveDir.exists()) {
            steveDir.mkdirs();
        }

        if (!alexDir.exists()) {
            alexDir.mkdirs();
        }
        ImageIO.write(img, "png", new File(file, filename + ".png"));
    }

    public static void doSetupOnThread() {
        AtomicBoolean notDownloaded = new AtomicBoolean(true);
        new Thread(() -> {
            while (notDownloaded.get()) {
                try {
                    createDefaultFolders();
                    Trending.trending();
                    Trending.trending();
                    notDownloaded.set(false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, RegenerationMod.NAME + " Download Daemon").start();
    }

    public static void unzipSkinPack(String url) throws IOException {
        File tempZip = new File(SKIN_DIRECTORY + "/temp/" + System.currentTimeMillis() + ".zip");
        RegenerationMod.LOG.info("Downloading " + url + " to " + tempZip.getAbsolutePath());
        FileUtils.copyURLToFile(new URL(url), tempZip);
        try (ZipFile file = new ZipFile(tempZip)) {
            FileSystem fileSystem = FileSystems.getDefault();
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    Files.createDirectories(fileSystem.getPath(SKIN_DIRECTORY + File.separator + entry.getName()));
                } else {
                    InputStream is = file.getInputStream(entry);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    String uncompressedFileName = SKIN_DIRECTORY + File.separator + entry.getName();
                    Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
                    RegenerationMod.LOG.info("Extracting file: " + uncompressedFilePath);
                    Files.createFile(uncompressedFilePath);
                    FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
                    while (bis.available() > 0) {
                        fileOutput.write(bis.read());
                    }
                    fileOutput.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tempZip.exists()) {
            FileUtils.forceDelete(tempZip.getParentFile());
        }
    }


    public static List<File> listAllSkins(EnumChoices choices) {
        List<File> resultList = new ArrayList<>();
        File directory = null;

        switch (choices) {
            case EITHER:
                directory = SKIN_DIRECTORY;
                break;
            case ALEX:
                directory = SKIN_DIRECTORY_ALEX;
                break;
            case STEVE:
                directory = SKIN_DIRECTORY_STEVE;
                break;
        }

        Collection files = org.apache.commons.io.FileUtils.listFiles(directory, extensions, true);

        for (Object file : files) {
            resultList.add((File) file);
        }

        Collections.sort(resultList);

        return resultList;
    }

    public static List<File> similarWords(String word, List<File> allWords) {
        List<File> similarWordList = new ArrayList<>();

        for (File currentWord : allWords) {
            if (currentWord.getName().contains(word)) {
                similarWordList.add(currentWord);
            }
        }
        return similarWordList;
    }

    public interface IEnum<E extends Enum<E>> {

        int ordinal();

        default E next() {
            E[] ies = this.getAllValues();
            return this.ordinal() != ies.length - 1 ? ies[this.ordinal() + 1] : null;
        }

        default E previous() {
            return this.ordinal() != 0 ? this.getAllValues()[this.ordinal() - 1] : null;
        }

        @SuppressWarnings("unchecked")
        default E[] getAllValues() {
            IEnum[] ies = this.getClass().getEnumConstants();
            return (E[]) ies;
        }
    }

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String url) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        Object object = texturemanager.getTexture(resourceLocationIn);
        if (object == null) {
            object = new ThreadDownloadImageData(null, url, DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID("")), new ImageBufferDownload());
            texturemanager.loadTexture(resourceLocationIn, (ITextureObject) object);
        }
        return (ThreadDownloadImageData) object;
    }
}
