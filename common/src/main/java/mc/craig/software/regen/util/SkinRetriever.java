package mc.craig.software.regen.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mc.craig.software.regen.Regeneration;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static mc.craig.software.regen.util.RegenUtil.getApiResponse;

public class SkinRetriever {

    private static final File SKINS_DIRECTORY = new File("./regen_data/skins");
    private static final File SKINS_DIRECTORY_SLIM = new File(SKINS_DIRECTORY, "slim");
    private static final File SKINS_DIRECTORY_DEFAULT = new File(SKINS_DIRECTORY, "default");

    // Setup required folders
    public static void folderSetup(boolean client) throws IOException {
        createFolder(SKINS_DIRECTORY);

        if (client) {
            createFolder(SKINS_DIRECTORY_DEFAULT, SKINS_DIRECTORY_SLIM);
            internalSkins();
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


    public static void internalSkins() throws IOException {
        String packsUrl = "https://mc-api.craig.software/skins";
        JsonObject links = getApiResponse(new URL(packsUrl));

        for (int skins = links.getAsJsonArray("data").size() - 1; skins >= 0; skins--) {
            JsonArray data = links.getAsJsonArray("data");
            JsonObject currentSkin = data.get(skins).getAsJsonObject();
            String packName = currentSkin.get("name").getAsString();
            String downloadLink = currentSkin.get("url").getAsString();
            String destination = currentSkin.get("destination").getAsString();

            File skinPackDir = new File(SKINS_DIRECTORY + "/" + destination.replaceAll("alex", "slim").replaceAll("steve", "default"));
            if (skinPackDir.exists()) {
                skinPackDir.mkdirs();
            }
            downloadSkinsSpecific(new URL(downloadLink), packName, skinPackDir);

        }
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


}
