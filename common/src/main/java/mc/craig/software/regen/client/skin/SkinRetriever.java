package mc.craig.software.regen.client.skin;

import com.google.gson.*;
import com.mojang.blaze3d.platform.NativeImage;
import mc.craig.software.regen.Regeneration;
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
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SkinRetriever {

    public static final File SKINS_DIR = new File("./regen_data/skins");
    public static final File SKINS_DIR_SLIM = new File(SKINS_DIR, "slim");
    public static final File SKINS_DIR_SLIM_TRENDING = new File(SKINS_DIR_SLIM, "web");
    public static final File SKINS_DIR_DEFAULT = new File(SKINS_DIR, "default");
    public static final File SKINS_DIR_DEFAULT_TRENDING = new File(SKINS_DIR_DEFAULT, "web");

    /**
     * Sets up the necessary folders for storing skins.
     */
    public static void folderSetup() {
        createFolder(SKINS_DIR);
        createFolder(SKINS_DIR_DEFAULT, SKINS_DIR_SLIM, SKINS_DIR_DEFAULT_TRENDING, SKINS_DIR_SLIM_TRENDING);
    }



    /**
     * Determines whether the given image represents an Alex model skin.
     *
     * @param image the image to check
     * @return true if the image represents an Alex model skin, false otherwise
     */
    public static boolean isAlexSkin(BufferedImage image) {
        // Check if the image has a transparent 2x8 or 8x2 pixel area on the arms
        for (int i = 0; i < 8; i++) {
            if (!hasAlpha(54, i + 20, image) || !hasAlpha(55, i + 20, image)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether the pixel at the specified coordinates in the given image has an alpha value.
     *
     * @param x     the x coordinate of the pixel
     * @param y     the y coordinate of the pixel
     * @param image the image containing the pixel
     * @return true if the pixel has an alpha value, false otherwise
     */
    public static boolean hasAlpha(int x, int y, BufferedImage image) {
        int pixel = image.getRGB(x, y);
        return pixel >> 24 == 0x00 || ((pixel & 0x00FFFFFF) == 0);
    }


    /**
     * Downloads a skin pack from the given URL and saves it to the specified directory.
     *
     * @param url      the URL of the skin pack to download
     * @param filename the filename to use when saving the skin pack
     * @param specific the directory to save the skin pack to
     */
    public static void downloadSkinToDirectory(URL url, String filename, File specific) {
        // Open a connection to the given URL
        URLConnection uc = null;
        try {
            uc = url.openConnection();
            uc.connect();
            uc = url.openConnection();

            // Set the connection timeout to an infinite value to prevent timeouts
            uc.setConnectTimeout(0);

            // Set the User-Agent header to identify the client
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");

            // Read the skin pack image from the connection and save it to the specified directory
            BufferedImage img = ImageIO.read(uc.getInputStream());
            if (!specific.exists()) {
                specific.mkdirs();
            }

            // Log the URL, filename, and destination path of the skin pack
            Regeneration.LOGGER.info("URL: {} || Name: {} || Path: {}", url, filename, specific);

            // Save the skin pack to the specified directory
            ImageIO.write(img, "png", new File(specific, filename + ".png"));
        } catch (IOException e) {
            // Log an error message and print the stack trace if an error occurred
            Regeneration.LOGGER.error("Failed to Download: " + url);
            e.printStackTrace();
        }
    }

    /**
     * Downloads skin packs from the internal API and saves them to the specified directories.
     *
     * @throws IOException if there is an error while downloading or saving the skin packs
     */
    public static void remoteSkins() throws IOException {
        // Delete any existing files in the skin directories
        FileUtils.cleanDirectory(SKINS_DIR_SLIM_TRENDING);
        FileUtils.cleanDirectory(SKINS_DIR_DEFAULT_TRENDING);

        // Log a warning message indicating that new Trending skins are being downloaded
        Regeneration.LOGGER.warn("Downloading new Trending skins");

        // Iterate over the skins returned by the internal API
        for (JsonElement skin : SkinApi.interalApiSkins()) {
            // Get the link and ID of the current skin
            String link = skin.getAsJsonObject().get("link").getAsString();
            String id = skin.getAsJsonObject().get("name").getAsString();
            // Download the skin from the given link and save it to the specified directories
            downloadSkins(new URL(link), "web_" + id, SKINS_DIR_SLIM_TRENDING, SKINS_DIR_DEFAULT_TRENDING);
        }
    }

    /**
     * Downloads a skin from the given URL and saves it to the specified directory.
     *
     * @param url      the URL of the skin to download
     * @param filename the name to give the downloaded skin file
     * @param alexDir  the directory to save Alex skins to
     * @param steveDir the directory to save Steve skins to
     * @throws IOException if there is an error while downloading or saving the skin
     */
    public static void downloadSkins(URL url, String filename, File alexDir, File steveDir) throws IOException {
        // Open a connection to the URL
        URLConnection uc = url.openConnection();

        // Set the User-Agent request property to simulate a web browser
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");

        // Read the image data from the URL
        BufferedImage img = ImageIO.read(uc.getInputStream());

        // Determine the correct directory to save the image to based on its type (Alex or Steve)
        File file = isAlexSkin(img) ? alexDir : steveDir;

        // Create the directories if they don't already exist
        createFolder(file, steveDir, alexDir);

        // Log the URL, filename, and destination of the skin
        Regeneration.LOGGER.info("URL: {} || Name: {} || Path: {}", url, filename, file.getPath());

        // Save the image to the specified directory
        ImageIO.write(img, "png", new File(file, filename + ".png"));
    }

    /**
     * Downloads skin packs from the internal API.
     *
     * @throws IOException if there is an error while downloading the skin packs
     */
    public static void internalSkins() throws IOException {
        //if(!RegenConfig.CLIENT.downloadInteralSkins.get()) return;
        Regeneration.LOGGER.warn("Re-downloading internal skins");
        String packsUrl = "https://mc-api.craig.software/skins";
        JsonElement links = SkinApi.getApiData(packsUrl);

        // Check if the links element is a JSON object
        if (links.isJsonObject()) {
            JsonArray data = links.getAsJsonObject().getAsJsonArray("data");
            for (JsonElement jsonElement : data) {
                JsonObject currentSkin = jsonElement.getAsJsonObject();

                // Get the name, download link, and destination of the current skin
                String packName = currentSkin.get("name").getAsString();
                String downloadLink = currentSkin.get("url").getAsString();
                String destination = currentSkin.get("destination").getAsString();

                // Create the skin pack directory
                File skinPackDir = new File(SKINS_DIR + "/" + destination.replaceAll("alex", "slim").replaceAll("steve", "default"));
                createFolder(skinPackDir);

                // Download the skin pack to the specified directory
                downloadSkinToDirectory(new URL(downloadLink), packName, skinPackDir);
            }
        }
    }

    /**
     * Writes the current time to the cache tracker file.
     *
     * @throws IOException if an error occurs while writing to the cache tracker file
     */
    public static void writeTime() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("update_interval", new JsonPrimitive(System.currentTimeMillis()));

        try (FileWriter writer = new FileWriter(new File(SKINS_DIR, "cache_tracker.json"))) {
            RegenUtil.GSON.toJson(jsonObject, writer);
            writer.flush();
        }
    }

    /**
     * Downloads and updates the necessary skins.
     *
     * @throws IOException if an error occurs while downloading or updating the skins
     */
    public static void doDownloads(boolean isClient) throws IOException {
        folderSetup();
        writeTime();
        remoteSkins();
        internalSkins();
    }

    public static boolean shouldUpdateSkins() throws FileNotFoundException {
        // Check if the "cache_tracker.json" file exists
        File cacheFile = new File(SKINS_DIR, "cache_tracker.json");
        if (!cacheFile.exists()) {
            Regeneration.LOGGER.info("Looks like no skins have been downloaded! Commencing first time set up!");
            return true;
        }

        // Read and parse the JSON file
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile)));
        JsonObject json;
        try {
            json = GsonHelper.parse(br);
        } catch (JsonSyntaxException e) {
            Regeneration.LOGGER.error("Failed to parse the JSON file! Skins will not be updated.");
            return false;
        }

        if (!json.has("update_interval")) {
            return true;
        }

        // Get the update interval from the JSON file
        int updateInterval = json.getAsJsonPrimitive("update_interval").getAsInt();

        // Get the time in minutes since the last update
        long timeSinceDownloaded = json.getAsJsonPrimitive("update_interval").getAsLong();
        long hoursSince = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - timeSinceDownloaded);

        // Check if it's time to update the skins
        boolean shouldDownload = hoursSince > 24;
        Regeneration.LOGGER.info("It has been {} hours since last skin update! {}", hoursSince, shouldDownload ? "A Skin update will commence!" : "A Skin update will not commence just now!");
        return shouldDownload;
    }

    /**
     * Creates the given folders if they do not currently exist.
     *
     * @param folders the folders to create
     */
    public static void createFolder(File... folders) {
        for (File folder : folders) {
            if (folder.exists()) continue;
            if (folder.mkdirs()) {
                Regeneration.LOGGER.info("Setup missing Regeneration Folder: {}", folder);
            }
        }
    }

    /**
     * Chooses a random skin from the given skin directory.
     *
     * @param random     the random source to use for choosing the skin
     * @param isAlex     whether to choose an Alex skin
     * @return a file representing the chosen skin
     */
    public static File chooseRandomSkin(RandomSource random, boolean isAlex) {
        File skins = isAlex ? SKINS_DIR_SLIM : SKINS_DIR_DEFAULT;

        if (!skins.exists()) {
            folderSetup();
        }

        Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png"));

        if (folderFiles.size() == 0) {
            return null;
        }

        File[] files = folderFiles.toArray(new File[0]);
        return files[random.nextInt(files.length)];
    }

    /**
     * Converts a file to a texture resource location.
     *
     * @param file the file to convert
     * @return the texture resource location
     */
    public static ResourceLocation fileToTexture(File file) {
        NativeImage nativeImage = null;
        try {

            nativeImage = TextureFixer.processLegacySkin(NativeImage.read(new FileInputStream(file)), file.toString());
        } catch (IOException e) {
            Regeneration.LOGGER.error("Failed to convert file to texture: {}", e.getMessage());
        }
        return VisualManipulator.loadImage(nativeImage);
    }

    /**
     * Lists all the available skins for the given skin type.
     *
     * @param currentSkinType the type of skins to list
     * @return a list of files representing the available skins
     */
    public static List<File> listAllSkins(PlayerUtil.SkinType currentSkinType) {
        File DIR = switch (currentSkinType) {
            case EITHER -> SKINS_DIR;
            case ALEX -> SKINS_DIR_SLIM;
            case STEVE -> SKINS_DIR_DEFAULT;
        };

        if (!DIR.exists()) {
            return Collections.emptyList();
        }

        Collection<File> folderFiles = FileUtils.listFiles(DIR, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png") || file.getName().contains("timelord_male") || file.getName().contains("timelord_female"));
        return new ArrayList<>(folderFiles);
    }
}
