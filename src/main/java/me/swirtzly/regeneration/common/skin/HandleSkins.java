package me.swirtzly.regeneration.common.skin;

import me.swirtzly.regeneration.Regeneration;
import net.minecraftforge.fml.common.Mod;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;

import static me.swirtzly.regeneration.util.common.FileUtil.getJsonFromURL;
import static me.swirtzly.regeneration.util.common.RegenUtil.NO_SKIN;

/**
 * Created by Swirtzly
 * on 18/05/2020 @ 10:24
 */
@Mod.EventBusSubscriber
public class HandleSkins {

    public static ArrayList<String> SKINS = new ArrayList<>();

    public static String imageToPixelData(File file) {
        String encodedfile = NO_SKIN;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedfile;
    }

    public static void downloadSkins() {
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
    }

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

}
