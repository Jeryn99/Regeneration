package me.swirtzly.regen.client.skin;

import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.util.PlayerUtil;
import me.swirtzly.regen.util.RConstants;
import me.swirtzly.regen.util.RegenUtil;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static me.swirtzly.regen.util.RegenUtil.getJsonFromURL;

public class CommonSkin {

    public static final File SKIN_DIRECTORY = new File(RegenConfig.COMMON.skinDir.get() + "/Regeneration Data/skins/");
    public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
    public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");

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
    public static File chooseRandomSkin(Random rand, boolean isAlex) {
        File skins = isAlex ? SKIN_DIRECTORY_ALEX : SKIN_DIRECTORY_STEVE;
        Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        folderFiles.removeIf(file -> !file.getName().endsWith(".png") || !isActuallyAImage(file));
        return (File) folderFiles.toArray()[rand.nextInt(folderFiles.size())];
    }

    public static boolean isActuallyAImage(File file) {
        String mimetype = null;
        try {
            mimetype = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mimetype != null && mimetype.split("/")[0].equals("image");
    }


    //Get a list of skins from namemc url
    public static ArrayList<String> getSkins(String downloadUrl) throws IOException {
        ArrayList<String> skins = new ArrayList<>();
        BufferedReader br = null;

        try {
            URL url = new URL(downloadUrl);
            URLConnection uc = url.openConnection();
            uc.connect();
            uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
            br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("<a href=\"/skin/")) {
                    String downloadLine = line.replaceAll("<a href=\"/skin/", "").replaceAll("\">", "").replaceAll("        ", "");
                    skins.add("https://namemc.com/texture/" + downloadLine + ".png");
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return skins;
    }

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
    public static void downloadSkins(URL url, String filename, File alexDir, File steveDir) throws IOException {

        URLConnection uc = url.openConnection();
        uc.connect();
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
        Regeneration.LOG.warn("Downloading Skin from: {}", url.toString());
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

        Regeneration.LOG.warn("Saving Skin to: {}", file.getPath());

        ImageIO.write(img, "png", new File(file, filename + ".png"));
    }


    public static void handleDownloads() throws IOException {
        if (!RegenConfig.SKIN.downloadInteralSkins.get() || !RegenUtil.doesHaveInternet()) return;

        File drWhoDir = new File(SKIN_DIRECTORY_ALEX + "/doctor_who");

        long attr = drWhoDir.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(drWhoDir.list()).length == 0) {
            Regeneration.LOG.info("Re-Downloading Internal Skins");
            FileUtils.cleanDirectory(drWhoDir);
            String PACKS_URL = "https://raw.githubusercontent.com/Swirtzly/Regeneration/skins/index.json";
            String[] links = Regeneration.GSON.fromJson(getJsonFromURL(PACKS_URL), String[].class);
            for (String link : links) {
                unzipSkinPack(link);
            }
        }
    }

    public static boolean isAlexSkin(BufferedImage image) {
        return hasAlpha(55, 20, image) && hasAlpha(55, 21, image) && hasAlpha(55, 22, image) && hasAlpha(55, 23, image) && hasAlpha(55, 24, image) && hasAlpha(55, 25, image) && hasAlpha(55, 26, image) && hasAlpha(55, 27, image) && hasAlpha(55, 28, image) && hasAlpha(55, 29, image) && hasAlpha(55, 30, image) && hasAlpha(55, 31, image) && hasAlpha(54, 20, image) && hasAlpha(54, 21, image) && hasAlpha(54, 22, image) && hasAlpha(54, 23, image) && hasAlpha(54, 24, image) && hasAlpha(54, 25, image) && hasAlpha(54, 26, image) && hasAlpha(54, 27, image) && hasAlpha(54, 28, image) && hasAlpha(54, 29, image) && hasAlpha(54, 30, image) && hasAlpha(54, 31, image) || hasAlpha(46, 52, image) && hasAlpha(46, 53, image) && hasAlpha(46, 54, image) && hasAlpha(46, 54, image) && hasAlpha(46, 55, image) && hasAlpha(46, 56, image) && hasAlpha(46, 57, image) && hasAlpha(46, 58, image) && hasAlpha(46, 59, image) && hasAlpha(46, 60, image) && hasAlpha(46, 61, image) && hasAlpha(46, 63, image) && hasAlpha(46, 53, image);
    }

    public static boolean hasAlpha(int x, int y, BufferedImage image) {
        int pixel = image.getRGB(x, y);
        return pixel >> 24 == 0x00 || ((pixel & 0x00FFFFFF) == 0);
    }

    public static void doSetupOnThread() {
        AtomicBoolean notDownloaded = new AtomicBoolean(true);
        new Thread(() -> {
            while (notDownloaded.get()) {
                try {
                    createDefaultFolders();
                    handleDownloads();
                    ClientSkin.downloadTrendingSkins();
                    ClientSkin.downloadPreviousSkins();
                    notDownloaded.set(false);
                } catch (Exception e) {
                    Regeneration.LOG.error("Regeneration Mod: Failed to download skins! Check your internet connection and ensure you are playing in online mode!");
                    Regeneration.LOG.error(e.getMessage());
                }
            }
        }, RConstants.MODID + " Download Daemon").start();
    }

    public static void unzipSkinPack(String url) throws IOException {
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
                    Regeneration.LOG.info("Extracting file: " + uncompressedFilePath);
                    File temp = uncompressedFilePath.toFile();
                    if (temp.exists()) {
                        Regeneration.LOG.info("Recreating: " + uncompressedFilePath);
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
        try {
            Files.find(Paths.get(directory.toString()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile()).forEach((file) -> resultList.add(file.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
