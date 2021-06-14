package me.suff.mc.regen.client.skin;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.DownloadSkinsThread;
import me.suff.mc.regen.util.MineSkin;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CommonSkin {

    public static final File SKIN_DIRECTORY = new File(RegenConfig.COMMON.skinDir.get() + "/Regeneration Data/skins/");
    public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
    public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
    public static final File SKIN_DIRECTORY_MALE_TIMELORD = new File(SKIN_DIRECTORY, "/timelord/male");
    public static final File SKIN_DIRECTORY_FEMALE_TIMELORD = new File(SKIN_DIRECTORY, "/timelord/female");
    public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/namemc");
    public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/namemc");

    public static ResourceLocation fileTotexture(File file) {
        NativeImage nativeImage = null;
        try {
            nativeImage = NativeImage.read(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SkinHandler.loadImage(nativeImage);
    }

    //Choose a random PNG from a folder
    public static File chooseRandomSkin(Random rand, boolean isAlex, boolean isTimelord) {
        File skins = isTimelord ? (isAlex ? SKIN_DIRECTORY_FEMALE_TIMELORD : SKIN_DIRECTORY_MALE_TIMELORD) : (isAlex ? SKIN_DIRECTORY_ALEX : SKIN_DIRECTORY_STEVE);

        if (!skins.exists()) {
            try {
                CommonSkin.folderSetup();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png"));

        if (folderFiles.isEmpty()) {
            DownloadSkinsThread.setup(FMLEnvironment.dist == Dist.CLIENT);
            return null;
        }

        return (File) folderFiles.toArray()[rand.nextInt(folderFiles.size())];
    }


    public static void folderSetup() throws IOException {
        File[] folders = new File[]{SKIN_DIRECTORY, SKIN_DIRECTORY_ALEX, SKIN_DIRECTORY_FEMALE_TIMELORD, SKIN_DIRECTORY_MALE_TIMELORD, SKIN_DIRECTORY_STEVE};
        for (File folder : folders) {
            if (!folder.exists()) {
                FileUtils.forceMkdir(folder);
            }
        }
    }

    /**
     * @param url      - URL to download image from
     * @param filename - Filename of the image [SHOULD NOT CONTAIN FILE EXTENSION, PNG IS SUFFIXED FOR YOU]
     * @throws IOException
     */
    public static void downloadSkins(URL url, String filename, File alexDir, File steveDir) throws IOException {
        URLConnection uc = url.openConnection();
        uc.connect();
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        BufferedImage img = ImageIO.read(uc.getInputStream());
        File file = isAlexSkin(img) ? alexDir : steveDir;
        if (!file.exists()) {
            file.mkdirs();
        }

        if (!steveDir.exists()) {
            steveDir.mkdirs();
        }

        if (!alexDir.exists()) {
            alexDir.mkdirs();
        }

        Regeneration.LOG.info("URL: {} || Name: {} || Path: {}", url.toString(), filename, file.getPath());
        ImageIO.write(img, "png", new File(file, filename + ".png"));
    }

    public static void downloadSkinsSpecific(URL url, String filename, File specific) throws IOException {
        URLConnection uc = url.openConnection();
        uc.connect();
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        BufferedImage img = ImageIO.read(uc.getInputStream());
        //  img= toBlackAndWhite(img);
        File file = specific;
        if (!file.exists()) {
            file.mkdirs();
        }

        Regeneration.LOG.info("URL: {} || Name: {} || Path: {}", url.toString(), filename, file.getPath());
        ImageIO.write(img, "png", new File(file, filename + ".png"));
    }

    public static BufferedImage toBlackAndWhite(BufferedImage img) {
        BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return op.filter(img, gray);
    }

    public static boolean skinpacks() {
        if (DownloadSkinsThread.forceStop) return false;
        if (!RegenConfig.CLIENT.downloadInteralSkins.get() || !RegenUtil.doesHaveInternet()) return false;

        File drWhoDir = new File(SKIN_DIRECTORY_ALEX + "/doctor_who");

        long attr = drWhoDir.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(drWhoDir.list()).length == 0) {
            Regeneration.LOG.info("Re-Downloading Internal Skins");

            String packsUrl = "https://raw.githubusercontent.com/WhoCraft/Regeneration/skins/index.json";
            String[] links = Regeneration.GSON.fromJson(RegenUtil.getJsonFromURL(packsUrl), String[].class);
            for (String link : links) {
                try {
                    unzipSkinPack(link);
                    return true;
                } catch (IOException exception) {
                    exception.printStackTrace();
                    DownloadSkinsThread.forceStop = true;
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isAlexSkin(BufferedImage image) {
        for (int i = 0; i < 8; i++) {
            if (!hasAlpha(54, i + 20, image) || !hasAlpha(55, i + 20, image)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasAlpha(int x, int y, BufferedImage image) {
        int pixel = image.getRGB(x, y);
        return pixel >> 24 == 0x00 || ((pixel & 0x00FFFFFF) == 0);
    }

    public static void unzipSkinPack(String url) throws IOException {
        if (DownloadSkinsThread.forceStop) return;
        File tempZip = new File(SKIN_DIRECTORY + "/temp/" + System.currentTimeMillis() + ".zip");
        Regeneration.LOG.info("Downloading " + url + " to " + tempZip.getAbsolutePath());
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
                    Regeneration.LOG.info(entry.getName());
                    File temp = uncompressedFilePath.toFile();
                    if (temp.exists()) {
                        temp.delete();
                    }
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


    public static List<File> listAllSkins(PlayerUtil.SkinType choices) {
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
        Collection<File> folderFiles = FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png") || file.getName().contains("timelord_male") || file.getName().contains("timelord_female"));
        return new ArrayList<>(folderFiles);
    }

    public static void trending() throws IOException {
        if (DownloadSkinsThread.forceStop) return;
        if (!RegenConfig.CLIENT.downloadTrendingSkins.get() || !RegenUtil.doesHaveInternet()) return;
        File trendingDir = TRENDING_ALEX;
        if (!trendingDir.exists()) {
            if (trendingDir.mkdirs()) {
                Regeneration.LOG.info("Creating Directory: " + trendingDir);
                Regeneration.LOG.info("Creating Directory: " + TRENDING_ALEX);
                Regeneration.LOG.info("Creating Directory: " + TRENDING_STEVE);
            }
        }
        long attr = trendingDir.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(trendingDir.list()).length == 0) {
            FileUtils.cleanDirectory(trendingDir);
            Regeneration.LOG.warn("Refreshing Trending skins");
            for (String skin : MineSkin.getSkinsFromPage(87)) {
                String cleanName = String.valueOf(System.currentTimeMillis());
                downloadSkins(new URL(skin), "trending_" + cleanName, TRENDING_ALEX, TRENDING_STEVE);
            }
        }
    }

    public static void timelord() throws IOException {
        long attr = SKIN_DIRECTORY_MALE_TIMELORD.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(SKIN_DIRECTORY_MALE_TIMELORD.list()).length == 0) {
            FileUtils.cleanDirectory(SKIN_DIRECTORY_FEMALE_TIMELORD);
            FileUtils.cleanDirectory(SKIN_DIRECTORY_MALE_TIMELORD);
            Regeneration.LOG.warn("Refreshing Timelord skins");

            String[] genders = new String[]{"male", "female"};
            for (String gender : genders) {
                for (String skin : MineSkin.getSkinsFromPage(87)) {
                    String cleanName = skin.replaceAll("https://namemc.com/texture/", "").replaceAll(".png", "");
                    downloadSkinsSpecific(new URL(skin), "timelord_" + gender + "_" + cleanName, gender.equals("male") ? SKIN_DIRECTORY_MALE_TIMELORD : SKIN_DIRECTORY_FEMALE_TIMELORD);
                }
            }
        }
    }
}
