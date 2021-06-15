package me.suff.mc.regen.common.skin;

import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.util.common.MineSkin;
import me.suff.mc.regen.util.common.RegenUtil;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Random;

import static me.suff.mc.regen.util.common.RegenUtil.NO_SKIN;

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


    public static ArrayList<String> SKINS = new ArrayList<>();
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

        if (TIMELORDS_OLD.exists()) {
            FileUtils.deleteDirectory(TIMELORDS_OLD);
        }

        if (!TIMELORDS.exists()) {
            TIMELORDS.mkdirs();
        }

        FileUtils.cleanDirectory(TIMELORDS);
        Regeneration.LOG.info("Refreshing Skins for Timelords");
        SKINS.clear();

        int randomPage = RegenUtil.RAND.nextInt(3000);
        for (int i = 3; i > 0; i--) {
            SKINS.addAll(MineSkin.getSkinsFromPage(randomPage + i));
        }

        for (String skin : SKINS) {
            String cleanName = String.valueOf(System.currentTimeMillis());
            File file = new File(TIMELORDS + "/" + cleanName + ".png");
            URL url = new URL(skin);
            URLConnection openConnection = url.openConnection();
            openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            openConnection.connect();
            BufferedImage img = ImageIO.read(openConnection.getInputStream());
            ImageIO.write(img, "png", file);
        }
    }


    public static File chooseRandomTimelordSkin(Random rand) throws IOException {
        Collection<File> folderFiles = FileUtils.listFiles(TIMELORDS, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        if (folderFiles.isEmpty()) {
            folderFiles = FileUtils.listFiles(TIMELORDS, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        }

        //Check again
        if (folderFiles.isEmpty()) {
            downloadTimelordSkins();
            return null;
        }

        return (File) folderFiles.toArray()[rand.nextInt(folderFiles.size())];
    }

    public static File chooseRandomSkin(Random rand, boolean isAlex) {
        File skins = isAlex ? SKIN_DIRECTORY_ALEX : SKIN_DIRECTORY_STEVE;
        Collection<File> folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        if (folderFiles.isEmpty()) {
            folderFiles = FileUtils.listFiles(skins, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        }
        return (File) folderFiles.toArray()[rand.nextInt(folderFiles.size())];
    }

}
