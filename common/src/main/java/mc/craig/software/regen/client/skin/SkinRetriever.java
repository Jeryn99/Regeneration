package mc.craig.software.regen.client.skin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.NativeImage;
import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenUtil;
import mc.craig.software.regen.util.SkinApi;
import mc.craig.software.regen.util.TextureFixer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class SkinRetriever {

    public static final File SKINS_DIR = new File("./regen_data/skins");
    public static final File SKINS_DIR_SLIM = new File(SKINS_DIR, "slim");
    public static final File SKINS_DIR_SLIM_TRENDING = new File(SKINS_DIR_SLIM, "web");
    public static final File SKINS_DIR_DEFAULT = new File(SKINS_DIR, "default");
    public static final File SKINS_DIR_DEFAULT_TRENDING = new File(SKINS_DIR_DEFAULT, "web");

    private static final ExecutorService DOWNLOAD_POOL = Executors.newFixedThreadPool(10);

    // --------------------- FOLDER SETUP ---------------------
    public static void folderSetup() {
        createFolder(SKINS_DIR, SKINS_DIR_DEFAULT, SKINS_DIR_SLIM, SKINS_DIR_DEFAULT_TRENDING, SKINS_DIR_SLIM_TRENDING);
    }

    public static void createFolder(File... folders) {
        for (File folder : folders) {
            if (folder.exists()) continue;
            if (folder.mkdirs()) {
                Regeneration.LOGGER.info("Setup missing Regeneration Folder: {}", folder);
            }
        }
    }

    // --------------------- SKIN TYPE CHECKS ---------------------
    public static boolean isAlexSkin(BufferedImage image) {
        for (int i = 0; i < 8; i++) {
            if (!hasAlpha(54, i + 20, image) || !hasAlpha(55, i + 20, image)) return false;
        }
        return true;
    }

    public static boolean hasAlpha(int x, int y, BufferedImage image) {
        int pixel = image.getRGB(x, y);
        return pixel >> 24 == 0x00 || ((pixel & 0x00FFFFFF) == 0);
    }

    // --------------------- DOWNLOAD HELPERS ---------------------
    private static void downloadSkinFromURL(URL url, File alexDir, File steveDir) throws IOException {
        BufferedImage img = ImageIO.read(url);
        File targetDir = isAlexSkin(img) ? alexDir : steveDir;
        createFolder(targetDir);

        String filename = extractSkinName(url);
        File targetFile = new File(targetDir, filename + ".png");
        if (targetFile.exists()) return;

        ImageIO.write(img, "png", targetFile);
        Regeneration.LOGGER.info("Downloaded {} -> {}", url, targetFile.getPath());
    }

    private static String extractSkinName(URL url) {
        String fileName = Paths.get(url.getPath()).getFileName().toString();
        return fileName.replaceAll("\\W+", "_");
    }

    private static void downloadSkinsAsync(List<URL> urls, File alexDir, File steveDir) {
        List<CompletableFuture<Void>> futures = urls.stream()
                .map(url -> CompletableFuture.runAsync(() -> {
                    try {
                        downloadSkinFromURL(url, alexDir, steveDir);
                    } catch (IOException e) {
                        Regeneration.LOGGER.error("Failed to download skin from {}", url, e);
                    }
                }, DOWNLOAD_POOL))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    // --------------------- REMOTE & INTERNAL SKINS ---------------------
    public static void remoteSkins() throws IOException {
        FileUtils.cleanDirectory(SKINS_DIR_SLIM_TRENDING);
        FileUtils.cleanDirectory(SKINS_DIR_DEFAULT_TRENDING);
        Regeneration.LOGGER.warn("Downloading new Trending skins");

        List<URL> urls = new ArrayList<>();
        for (JsonElement skin : SkinApi.interalApiSkins()) {
            String link = skin.getAsJsonObject().get("link").getAsString();
            urls.add(new URL(link));
        }

        downloadSkinsAsync(urls, SKINS_DIR_SLIM_TRENDING, SKINS_DIR_DEFAULT_TRENDING);
    }

    public static void internalSkins() throws IOException {
        Regeneration.LOGGER.warn("Re-downloading internal skins");
        String packsUrl = "https://api.jeryn.dev/mc/skins/random";
        JsonElement links = SkinApi.getApiData(packsUrl);
        if (!links.isJsonArray()) return;

        List<URL> urls = new ArrayList<>();
        for (JsonElement e : links.getAsJsonArray()) {
            JsonObject obj = e.getAsJsonObject();
            System.out.println(obj);
            String downloadLink = obj.get("url").getAsString();
            String destination = obj.get("destination").getAsString();
            File skinPackDir = new File(SKINS_DIR, destination.replaceAll("alex", "slim").replaceAll("steve", "default"));
            createFolder(skinPackDir);

            urls.add(new URL(downloadLink));
        }

        downloadSkinsAsync(urls, SKINS_DIR_SLIM, SKINS_DIR_DEFAULT);
    }

    // --------------------- CACHE ---------------------
    public static void writeTime() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("update_interval", new JsonPrimitive(System.currentTimeMillis()));
        try (FileWriter writer = new FileWriter(new File(SKINS_DIR, "cache_tracker.json"))) {
            RegenUtil.GSON.toJson(jsonObject, writer);
            writer.flush();
        }
    }

    public static boolean shouldUpdateSkins() throws FileNotFoundException {
        File cacheFile = new File(SKINS_DIR, "cache_tracker.json");
        if (!cacheFile.exists()) {
            Regeneration.LOGGER.info("No skins downloaded yet! First time setup.");
            return true;
        }

        BufferedReader br = new BufferedReader(new FileReader(cacheFile));
        JsonObject json;
        try {
            json = GsonHelper.parse(br);
        } catch (JsonSyntaxException e) {
            Regeneration.LOGGER.error("Failed to parse cache JSON! Skins will not update.");
            return false;
        }

        if (!json.has("update_interval")) return true;

        long last = json.getAsJsonPrimitive("update_interval").getAsLong();
        long hours = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - last);
        boolean should = hours > 24;
        Regeneration.LOGGER.info("It has been {} hours since last skin update! {}", hours, should ? "Updating skins..." : "No update needed.");
        return should;
    }

    // --------------------- DOWNLOAD FLOW ---------------------
    public static void doDownloads(boolean isClient) throws IOException {
        folderSetup();
        writeTime();
        //   remoteSkins();
        //   internalSkins();
    }

    // --------------------- SKIN SELECTION ---------------------

    public static File chooseRandomSkin(RandomSource random, boolean isAlex, int regenCount) {
        File dir = isAlex ? SKINS_DIR_SLIM : SKINS_DIR_DEFAULT;
        if (!dir.exists()) folderSetup();
        return chooseSkinForRegen(dir, regenCount, random);
    }

    /**
     * Choose a skin for a player based on their regen count.
     * If a skin contains the number, it is used. Otherwise, fallback to random.
     */
    public static File chooseSkinForRegen(File dir, int regenCount, RandomSource rand) {

        int currentRegen = RegenConfig.CLIENT.AAAAAAAAA.get() - regenCount;
        if (regenCount == 13) {
            currentRegen = -1;
        }


        Regeneration.LOGGER.info("We think you are on your {} regeneration!", currentRegen);

        // List all PNG files recursively
        Collection<File> allFiles = FileUtils.listFiles(dir, new String[]{"png"}, true);
        File[] skins = allFiles.toArray(new File[0]);

        if (skins.length == 0) {
            Regeneration.LOGGER.warn("No skins found in folder: {}", dir);
            return null;
        }

        Regeneration.LOGGER.info("Found {} skins in folder {}", skins.length, dir);

        // Prepare regex to match the exact currentRegen number (e.g., 02)
        String regex = "(^|[^0-9])" + String.format("%02d", currentRegen) + "([^0-9]|$)";
        Pattern pattern = Pattern.compile(regex);

        // Collect all skins that match the current regen number exactly
        List<File> matchingSkins = new ArrayList<>();
        for (File skin : skins) {
            String nameWithoutExt = skin.getName().replaceFirst("\\.png$", "");
            if (pattern.matcher(nameWithoutExt).find()) {
                matchingSkins.add(skin);
            }
        }

        // If we found multiple, choose one randomly
        if (!matchingSkins.isEmpty()) {
            File chosen = matchingSkins.get(rand.nextInt(matchingSkins.size()));
            Regeneration.LOGGER.info("Found {} matching skins for regen {}. Chosen: {}",
                    matchingSkins.size(), currentRegen, chosen.getName());
            return chosen;
        }

        // fallback: random skin
        File randomSkin = skins[rand.nextInt(skins.length)];
        Regeneration.LOGGER.info("No matching skin for regen {}. Choosing random: {}", currentRegen, randomSkin.getName());
        return randomSkin;
    }


    // --------------------- TEXTURE CONVERSION ---------------------
    public static ResourceLocation fileToTexture(File file) {
        NativeImage nativeImage = null;
        try {
            nativeImage = TextureFixer.processLegacySkin(NativeImage.read(new FileInputStream(file)), file.toString());
        } catch (IOException e) {
            Regeneration.LOGGER.error("Failed to convert file to texture: {}", e.getMessage());
        }
        return VisualManipulator.loadImage(nativeImage);
    }

    // --------------------- LIST SKINS ---------------------
    public static List<File> listAllSkins(PlayerUtil.SkinType currentSkinType) {
        File DIR = switch (currentSkinType) {
            case EITHER -> SKINS_DIR;
            case ALEX -> SKINS_DIR_SLIM;
            case STEVE -> SKINS_DIR_DEFAULT;
        };

        if (!DIR.exists()) return Collections.emptyList();

        Collection<File> folderFiles = FileUtils.listFiles(DIR, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png")
                || file.getName().contains("timelord_male")
                || file.getName().contains("timelord_female"));

        return new ArrayList<>(folderFiles);
    }
}
