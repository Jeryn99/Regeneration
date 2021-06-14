package me.swirtzly.regeneration.common.skin;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.util.common.FileUtil;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Random;

import static me.swirtzly.regeneration.util.common.FileUtil.getJsonFromURL;
import static me.swirtzly.regeneration.util.common.RegenUtil.NO_SKIN;

/**
 * Created by Swirtzly
 * on 18/05/2020 @ 10:24
 */
@Mod.EventBusSubscriber
public class HandleSkins {

    public static final File SKIN_DIRECTORY = new File(RegenConfig.COMMON.skinDir.get() + "/Regeneration Data/skins/");
    public static final File BASE_DIR = new File(RegenConfig.COMMON.skinDir.get() + "/Regeneration Data/");
    public static final File SKIN_DIRECTORY_STEVE = new File(SKIN_DIRECTORY, "/steve");
    public static final File SKIN_DIRECTORY_ALEX = new File(SKIN_DIRECTORY, "/alex");
    public static final File TIMELORDS_OLD = new File(SKIN_DIRECTORY, "/timelords");


    public static ArrayList< String > SKINS = new ArrayList<>();
    public static File TIMELORDS = new File(BASE_DIR + "/timelords");

    public static String imageToPixelData(File file) {
        String encodedfile = NO_SKIN;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getMimeEncoder().encodeToString(bytes);
        } catch (IOException e) {
            Regeneration.LOG.error("Error creating image for: " + encodedfile);
            e.printStackTrace();
        }
        return encodedfile;
    }

    public static void downloadTimelordSkins() throws IOException {

        if(TIMELORDS_OLD.exists()){
            FileUtils.deleteDirectory(TIMELORDS_OLD);
        }

        if (!TIMELORDS.exists()) {
            TIMELORDS.mkdirs();
        }

        FileUtils.cleanDirectory(TIMELORDS);
        Regeneration.LOG.info("Refreshing Skins for Timelords");
        SKINS.clear();
        try {
            String[] uuids = Regeneration.GSON.fromJson(getJsonFromURL("https://raw.githubusercontent.com/Swirtzly/Regeneration/skins/donators.json"), String[].class);

            for (String uuid : uuids) {
                SKINS.addAll(getSkins("https://namemc.com/minecraft-skins/profile/" + uuid));
            }

            SKINS.addAll(getSkins("https://namemc.com/minecraft-skins"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (String skin : SKINS) {
            String cleanName = skin.replaceAll("https://namemc.com/texture/", "").replaceAll(".png", "");
            File file = new File(TIMELORDS + "/" + cleanName + ".png");
            URL url = new URL(skin);
            URLConnection openConnection = url.openConnection();
            openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            openConnection.connect();
            BufferedImage img = ImageIO.read(openConnection.getInputStream());
            ImageIO.write(img, "png", file);
        }
    }

    public static ArrayList< String > getSkins(String downloadUrl) throws IOException {
        ArrayList< String > skins = new ArrayList<>();
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

    public static File chooseRandomTimelordSkin(Random rand) throws IOException {
        Collection< File > folderFiles = FileUtils.listFiles(TIMELORDS, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        if (folderFiles.isEmpty()) {
            folderFiles = FileUtils.listFiles(TIMELORDS, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        }

        //Check again
        if(folderFiles.isEmpty()){
            downloadTimelordSkins();
            return null;
        }

        return (File) folderFiles.toArray()[rand.nextInt(folderFiles.size())];
    }

    public static File chooseRandomSkin(Random rand, boolean isAlex) {
        File skins = isAlex ? SKIN_DIRECTORY_ALEX : SKIN_DIRECTORY_STEVE;
        Collection< File > folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        if (folderFiles.isEmpty()) {
            folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        }
        return (File) folderFiles.toArray()[rand.nextInt(folderFiles.size())];
    }

}
