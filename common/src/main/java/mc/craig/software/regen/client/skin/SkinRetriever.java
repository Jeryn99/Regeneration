package mc.craig.software.regen.client.skin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.NativeImage;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SkinRetriever {

    public static final File SKINS_DIR = new File("./regen_data/skins");
    public static final File SKINS_DIR_SLIM = new File(SKINS_DIR, "slim");
    public static final File SKINS_DIR_SLIM_TRENDING = new File(SKINS_DIR_SLIM, "web");
    public static final File SKINS_DIR_DEFAULT = new File(SKINS_DIR, "default");
    public static final File SKINS_DIR_DEFAULT_TRENDING = new File(SKINS_DIR_DEFAULT, "web");
    public static final File SKINS_DIR_SLImale_timelord = new File(SKINS_DIR, "/timelords/slim");
    public static final File SKINS_DIR_DEFAULT_TIMELORD = new File(SKINS_DIR, "/timelords/default");

    // Setup required folders
    public static void folderSetup() {
        createFolder(SKINS_DIR, SKINS_DIR_DEFAULT_TIMELORD, SKINS_DIR_SLImale_timelord);

        createFolder();

        if (Platform.isClient()) {
            createFolder(SKINS_DIR_DEFAULT, SKINS_DIR_SLIM, SKINS_DIR_DEFAULT_TRENDING, SKINS_DIR_SLIM_TRENDING);
        }




    }


    public static void timelord() throws IOException {
        FileUtils.cleanDirectory(SKINS_DIR_DEFAULT_TIMELORD);
        FileUtils.cleanDirectory(SKINS_DIR_SLImale_timelord);
        Regeneration.LOGGER.warn("Refreshing Timelord skins");

        String[] genders = new String[]{"male", "female"};
        for (String gender : genders) {
            for (String skin : MineSkin.searchSkins(gender)) {
                downloadSkinsSpecific(new URL(skin), "timelord_" + gender + "_" + RandomStringUtils.random(5, true, false), gender.equals("male") ? SKINS_DIR_DEFAULT_TIMELORD : SKINS_DIR_SLImale_timelord);
            }
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

    public static void downloadSkinsSpecific(URL url, String filename, File specific) {
        URLConnection uc = null;
        try {
            uc = url.openConnection();
            uc.connect();
            uc = url.openConnection();
            uc.setConnectTimeout(0);
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
            BufferedImage img = ImageIO.read(uc.getInputStream());
            if (!specific.exists()) {
                specific.mkdirs();
            }

            Regeneration.LOGGER.info("URL: {} || Name: {} || Path: {}", url, filename, specific);
            ImageIO.write(img, "png", new File(specific, filename + ".png"));
        } catch (IOException e) {
            Regeneration.LOGGER.error("Failed to Download: " + url);
            e.printStackTrace();
        }
    }
    public static void remoteSkins() throws IOException {
        FileUtils.cleanDirectory(SKINS_DIR_SLIM_TRENDING);
        FileUtils.cleanDirectory(SKINS_DIR_DEFAULT_TRENDING);
        Regeneration.LOGGER.warn("Downloading new Trending skins");
            for (JsonElement skin : MineSkin.interalApiSkins()) {
                String link = skin.getAsJsonObject().get("link").getAsString();
                String id = skin.getAsJsonObject().get("_id").getAsJsonObject().get("timestamp").getAsString();
                downloadSkins(new URL(link), "web_" + id, SKINS_DIR_SLIM_TRENDING, SKINS_DIR_DEFAULT_TRENDING);
        }
    }

    public static void downloadSkins(URL url, String filename, File alexDir, File steveDir) throws IOException {
        URLConnection uc = url.openConnection();
        uc.connect();
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        BufferedImage img = ImageIO.read(uc.getInputStream());
        File file = isAlexSkin(img) ? alexDir : steveDir;
        createFolder(file, steveDir, alexDir);
        Regeneration.LOGGER.info("URL: {} || Name: {} || Path: {}", url, filename, file.getPath());
        ImageIO.write(img, "png", new File(file, filename + ".png"));
    }

    public static void internalSkins() throws IOException {
        //if(!RegenConfig.CLIENT.downloadInteralSkins.get()) return;
        Regeneration.LOGGER.warn("Re-downloading internal skins");
        String packsUrl = "https://mc-api.craig.software/skins";
        JsonObject links = MineSkin.getApiResponse(new URL(packsUrl));

        for (int skins = links.getAsJsonArray("data").size() - 1; skins >= 0; skins--) {
            JsonArray data = links.getAsJsonArray("data");
            JsonObject currentSkin = data.get(skins).getAsJsonObject();
            String packName = currentSkin.get("name").getAsString();
            String downloadLink = currentSkin.get("url").getAsString();
            String destination = currentSkin.get("destination").getAsString();

            File skinPackDir = new File(SKINS_DIR + "/" + destination.replaceAll("alex", "slim").replaceAll("steve", "default"));
            createFolder(skinPackDir);
            downloadSkinsSpecific(new URL(downloadLink), packName, skinPackDir);
        }
    }

    public static void writeTime() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("last_downloaded", new JsonPrimitive(System.currentTimeMillis()));

        try (FileWriter writer = new FileWriter(new File(SKINS_DIR, "cache_tracker.json"))) {
            RegenUtil.GSON.toJson(jsonObject, writer);
            writer.flush();
        }
    }

    public static void doDownloads() throws IOException {
        folderSetup();
        writeTime();
        remoteSkins();
        timelord();
        internalSkins();
    }

    public static boolean shouldUpdateSkins() throws FileNotFoundException {
        File cacheFile = new File(SKINS_DIR, "cache_tracker.json");
        if (!cacheFile.exists()) {
            Regeneration.LOGGER.info("Looks like no skins have been downloaded! Commencing first time set up!");
            return true;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile)));
        JsonObject json = GsonHelper.parse(br);
        long timeSinceDownloaded = json.getAsJsonPrimitive("last_downloaded").getAsLong();

        long minutesSince = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - timeSinceDownloaded);
        boolean shouldDownload = minutesSince > 1440;
        Regeneration.LOGGER.info("It has been {} minutes since last skin update! {}", minutesSince, shouldDownload ? "A Skin update will commence!" : "A Skin update will not commence just now!");
        return shouldDownload;
    }


    // Helper for creating folders if they do not currently exist
    private static void createFolder(File... folder) {
        for (File file : folder) {
            if (file.exists()) continue;
            if (file.mkdirs()) {
                Regeneration.LOGGER.info("Setup missing Regeneration Folder: {}", file);
            }
        }
    }


    public static File chooseRandomSkin(RandomSource random, boolean isTimelord, boolean isAlex) {
        File skins = isTimelord ? (isAlex ? SKINS_DIR_SLImale_timelord : SKINS_DIR_DEFAULT_TIMELORD) : (isAlex ? SKINS_DIR_SLIM : SKINS_DIR_DEFAULT);

        if (!skins.exists()) {
            SkinRetriever.folderSetup();
        }

        Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png"));

        return (File) folderFiles.toArray()[random.nextInt(folderFiles.size())];
    }

    public static ResourceLocation fileToTexture(File file) {
        NativeImage nativeImage = null;
        try {
            nativeImage = TextureFixer.processLegacySkin(NativeImage.read(new FileInputStream(file)), file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return VisualManipulator.loadImage(nativeImage);
    }

    public static List<File> listAllSkins(PlayerUtil.SkinType currentSkinType) {
        File DIR = switch (currentSkinType) {
            case EITHER -> SKINS_DIR;
            case ALEX -> SKINS_DIR_SLIM;
            case STEVE -> SKINS_DIR_DEFAULT;
        };

        if (!DIR.exists()) {
            return new ArrayList<>();
        }

        Collection<File> folderFiles = FileUtils.listFiles(DIR, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png") || file.getName().contains("timelord_male") || file.getName().contains("timelord_female"));
        return new ArrayList<>(folderFiles);
    }
}
