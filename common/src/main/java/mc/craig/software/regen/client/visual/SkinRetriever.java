package mc.craig.software.regen.client.visual;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.NativeImage;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.client.skin.SkinHandler;
import mc.craig.software.regen.util.MineSkin;
import mc.craig.software.regen.util.Platform;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SkinRetriever {

    public static final File SKINS_DIRECTORY = new File("./regen_data/skins");
    public static final File SKINS_DIRECTORY_SLIM = new File(SKINS_DIRECTORY, "slim");
    public static final File SKINS_DIRECTORY_DEFAULT = new File(SKINS_DIRECTORY, "default");

    public static final File SKINS_DIRECTORY_SLIM_TIMELORD = new File(SKINS_DIRECTORY, "/timelords/slim");
    public static final File SKINS_DIRECTORY_DEFAULT_TIMELORD = new File(SKINS_DIRECTORY, "/timelords/default");

    public static final File SKINS_DIRECTORY_SLIM_TRENDING = new File(SKINS_DIRECTORY_SLIM, "web");
    public static final File SKINS_DIRECTORY_DEFAULT_TRENDING = new File(SKINS_DIRECTORY_DEFAULT, "web");

    // Setup required folders
    public static void folderSetup() {
        createFolder(SKINS_DIRECTORY, SKINS_DIRECTORY_DEFAULT_TIMELORD, SKINS_DIRECTORY_SLIM_TIMELORD);

        if (Platform.isClient()) {
            createFolder(SKINS_DIRECTORY_DEFAULT, SKINS_DIRECTORY_SLIM);
        }
    }


    public static void timelord() throws IOException {
        FileUtils.cleanDirectory(SKINS_DIRECTORY_DEFAULT_TIMELORD);
        FileUtils.cleanDirectory(SKINS_DIRECTORY_SLIM_TIMELORD);
        Regeneration.LOGGER.warn("Refreshing Timelord skins");

        String[] genders = new String[]{"male", "female"};
        for (String gender : genders) {
            for (String skin : MineSkin.searchSkins(gender)) {
                downloadSkinsSpecific(new URL(skin), "timelord_" + gender + "_" + RandomStringUtils.random(5, true, false), gender.equals("male") ? SKINS_DIRECTORY_DEFAULT_TIMELORD : SKINS_DIRECTORY_SLIM_TIMELORD);
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

            Regeneration.LOGGER.info("URL: {} || Name: {} || Path: {}", url, filename, specific.getPath());
            ImageIO.write(img, "png", new File(specific, filename + ".png"));
        } catch (IOException e) {
            Regeneration.LOGGER.error("Failed to Download: " + url);
            e.printStackTrace();
        }
    }

    public static void remoteSkins() throws IOException {
        Regeneration.LOGGER.warn("Downloading new Trending skins");

        int randomPage = RandomSource.create().nextInt(7800);

        for (int i = 3; i > 0; i--) {
            for (String skin : MineSkin.getSkinsFromPage(randomPage + i)) {
                downloadSkins(new URL(skin), "mk_" + RandomStringUtils.random(5, true, false), SKINS_DIRECTORY_SLIM_TRENDING, SKINS_DIRECTORY_DEFAULT_TRENDING);
            }
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
        Regeneration.LOGGER.warn("Re-downloading internal skins");
        String packsUrl = "https://mc-api.craig.software/skins";
        JsonObject links = MineSkin.getApiResponse(new URL(packsUrl));

        for (int skins = links.getAsJsonArray("data").size() - 1; skins >= 0; skins--) {
            JsonArray data = links.getAsJsonArray("data");
            JsonObject currentSkin = data.get(skins).getAsJsonObject();
            String packName = currentSkin.get("name").getAsString();
            String downloadLink = currentSkin.get("url").getAsString();
            String destination = currentSkin.get("destination").getAsString();

            File skinPackDir = new File(SKINS_DIRECTORY + "/" + destination.replaceAll("alex", "slim").replaceAll("steve", "default"));
            createFolder(skinPackDir);
            downloadSkinsSpecific(new URL(downloadLink), packName, skinPackDir);
        }
    }

    public static void writeTime() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("last_downloaded", new JsonPrimitive(System.currentTimeMillis()));

        try (FileWriter writer = new FileWriter(new File(SKINS_DIRECTORY, "cache_tracker.json"))) {
            RegenUtil.GSON.toJson(jsonObject, writer);
            writer.flush();
        }
    }

    public static void doDownloads() throws IOException {
        folderSetup();
        writeTime();
        timelord();
        internalSkins();
        remoteSkins();
    }

    public static boolean shouldUpdateSkins() throws FileNotFoundException {
        File cacheFile = new File(SKINS_DIRECTORY, "cache_tracker.json");
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


    // Turns a File into a Base64 String
    public static String skinToBinary(File file) throws IOException {
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }

    // Turns a File into a byte representation of itself
    public static byte[] fileToBytes(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
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
        File skins = isTimelord ? (isAlex ? SKINS_DIRECTORY_SLIM_TIMELORD : SKINS_DIRECTORY_DEFAULT_TIMELORD) : (isAlex ? SKINS_DIRECTORY_SLIM : SKINS_DIRECTORY_DEFAULT);

        if (!skins.exists()) {
            SkinRetriever.folderSetup();
        }

        Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png"));

        return (File) folderFiles.toArray()[random.nextInt(folderFiles.size())];
    }

    public static ResourceLocation fileTotexture(File file) {
        NativeImage nativeImage = null;
        try {
            nativeImage = NativeImage.read(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SkinHandler.loadImage(nativeImage);
    }

    public static List<File> listAllSkins(PlayerUtil.SkinType currentSkinType) {
        File directory = switch (currentSkinType) {
            case EITHER -> SKINS_DIRECTORY;
            case ALEX -> SKINS_DIRECTORY_SLIM;
            case STEVE -> SKINS_DIRECTORY_DEFAULT;
        };

        if (!directory.exists()) {
            return new ArrayList<>();
        }

        Collection<File> folderFiles = FileUtils.listFiles(directory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png") || file.getName().contains("timelord_male") || file.getName().contains("timelord_female"));
        return new ArrayList<>(folderFiles);
    }
}
