package me.craig.software.regen.client.skin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.craig.software.regen.Regeneration;
import me.craig.software.regen.config.RegenConfig;
import me.craig.software.regen.util.DownloadSkinsThread;
import me.craig.software.regen.util.MineSkin;
import me.craig.software.regen.util.PlayerUtil;
import me.craig.software.regen.util.RegenUtil;
import net.minecraft.client.renderer.texture.DownloadingTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.*;

public class CommonSkin {

    public static final File SKIN_DIRECTORY = new File(RegenConfig.COMMON.skinDir.get() + "/regeneration_skins/skins/");
    public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
    public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/mineskin");
    public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
    public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/mineskin");
    public static final File SKIN_DIRECTORY_MALE_TIMELORD = new File(SKIN_DIRECTORY, "/timelord/male");
    public static final File SKIN_DIRECTORY_FEMALE_TIMELORD = new File(SKIN_DIRECTORY, "/timelord/female");

    public static ResourceLocation fileTotexture(File file) {
        NativeImage nativeImage = null;
        try {
            nativeImage = DownloadingTexture.processLegacySkin(NativeImage.read(Files.newInputStream(file.toPath())));
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
            DownloadSkinsThread.setup();
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
    public static void downloadSkins(URL url, String filename, File alexDir, File steveDir) {

        URLConnection uc = null;
        try {
            uc = url.openConnection();
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
        } catch (IOException e) {
            Regeneration.LOG.error("Issue while downloading skin {}, error: {}", url.toString(), e);
        }
    }

    public static void downloadSkinsSpecific(URL url, String filename, File specific) {
        Regeneration.LOG.info("URL: {} || Name: {} || Path: {}", url.toString(), filename, specific.getPath());

        URLConnection uc = null;
        try {
            uc = url.openConnection();
            uc.connect();
            uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
            BufferedImage img = ImageIO.read(uc.getInputStream());
            if (!specific.exists()) {
                specific.mkdirs();
            }

            ImageIO.write(img, "png", new File(specific, filename + ".png"));
        } catch (IOException e) {
            Regeneration.LOG.error("Issue while downloading skin {}, error: {}", url.toString(), e);
        }
    }

    public static void skinpacks() throws IOException {
        if (!RegenConfig.CLIENT.downloadInteralSkins.get() || !RegenUtil.doesHaveInternet()) return;
        String packsUrl = "https://mc-api.craig.software/skins";
        JsonObject links = MineSkin.getApiResponse(new URL(packsUrl));

        for (int skins = links.getAsJsonArray("data").size() - 1; skins >= 0; skins--) {
            JsonArray data = links.getAsJsonArray("data");
            JsonObject currentSkin = data.get(skins).getAsJsonObject();
            String packName = currentSkin.get("name").getAsString();
            String downloadLink = currentSkin.get("url").getAsString();
            String destination = currentSkin.get("destination").getAsString();


            File skinPackDir = new File(SKIN_DIRECTORY + "/" + destination);
            if (skinPackDir.exists()) {
                skinPackDir.mkdirs();
            }
            downloadSkinsSpecific(new URL(downloadLink), packName, skinPackDir);

        }
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

        if (!directory.exists()) {
            return new ArrayList<>();
        }

        Collection<File> folderFiles = FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png") || file.getName().contains("timelord_male") || file.getName().contains("timelord_female"));
        return new ArrayList<>(folderFiles);
    }

    public static void trending() throws IOException {
        if (!RegenConfig.CLIENT.downloadTrendingSkins.get() || !RegenUtil.doesHaveInternet() || FMLEnvironment.dist == Dist.DEDICATED_SERVER) return;
        File trendingDir = TRENDING_ALEX;
        createFolder(trendingDir, TRENDING_ALEX, TRENDING_STEVE);
        FileUtils.cleanDirectory(TRENDING_ALEX);
        FileUtils.cleanDirectory(TRENDING_STEVE);
        Regeneration.LOG.warn("Downloading new Trending skins");
        for (JsonElement skin : MineSkin.interalApiSkins()) {
            String link = skin.getAsJsonObject().get("link").getAsString();
            String id = skin.getAsJsonObject().get("_id").getAsJsonObject().get("timestamp").getAsString();
            downloadSkins(new URL(link), "web_" + id, TRENDING_ALEX, TRENDING_STEVE);
        }
    }

    private static void createFolder(File... folder) {
        for (File file : folder) {
            if (file.exists()) continue;
            if (file.mkdirs()) {
                Regeneration.LOG.info("Setup missing Regeneration Folder: {}", file);
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
                for (String skin : MineSkin.searchSkins(gender)) {
                    downloadSkinsSpecific(new URL(skin), "timelord_" + gender + "_" + RandomStringUtils.random(5, true, false), gender.equals("male") ? SKIN_DIRECTORY_MALE_TIMELORD : SKIN_DIRECTORY_FEMALE_TIMELORD);
                }
            }
        }
    }
}
