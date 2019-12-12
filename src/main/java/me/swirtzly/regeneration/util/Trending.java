package me.swirtzly.regeneration.util;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

import static me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler.SKIN_DIRECTORY_ALEX;
import static me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler.SKIN_DIRECTORY_STEVE;
import static me.swirtzly.regeneration.util.FileUtil.getJsonFromURL;
import static me.swirtzly.regeneration.util.FileUtil.unzipSkinPack;


public class Trending {

    public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/namemc");
    public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/namemc");

    public static File USER_ALEX = new File(SKIN_DIRECTORY_ALEX + "/the_past");
    public static File USER_STEVE = new File(SKIN_DIRECTORY_STEVE + "/the_past");

    public static void downloadPreviousSkins() {
        if (!RegenConfig.skins.downloadPastSkins) return;
        RegenerationMod.LOG.warn("Refreshing users past skins");

        if (!USER_ALEX.exists()) {
            USER_ALEX.mkdirs();
        }

        long attr = USER_ALEX.lastModified();

        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(USER_ALEX.list()).length == 0) {
            for (int i = 0; i < 5; i++) {
                try {
                    String url = "https://namemc.com/minecraft-skins/profile/" + Minecraft.getMinecraft().getSession().getPlayerID() + "?page=" + i;
                    for (String skin : getSkins(url)) {
                        FileUtil.downloadSkins(new URL(skin), "namemmc_" + System.currentTimeMillis(), USER_ALEX, USER_STEVE);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
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


    public static void downloadTrendingSkins() throws IOException {
        if (!RegenConfig.skins.downloadTrendingSkins) return;
        File trendingDir = TRENDING_ALEX;
        if (!trendingDir.exists()) {
            trendingDir.mkdirs();
        }
        long attr = trendingDir.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(trendingDir.list()).length == 0) {
            FileUtils.deleteDirectory(trendingDir);
            RegenerationMod.LOG.warn("Refreshing Trending skins");
            for (String skin : getSkins("https://namemc.com/minecraft-skins")) {
                FileUtil.downloadSkins(new URL(skin), "namemc_" + System.currentTimeMillis(), TRENDING_ALEX, TRENDING_STEVE);
            }
        }
    }

    public static void handleDownloads() throws IOException {
        if (!RegenConfig.skins.downloadInternalSkins) return;
        String PACKS_URL = "https://raw.githubusercontent.com/Swirtzly/Regeneration/skins/index.json";
        String[] links = RegenerationMod.GSON.fromJson(getJsonFromURL(PACKS_URL), String[].class);
        for (String link : links) {
            unzipSkinPack(link);
        }
    }

}