package me.suff.mc.regen.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.RegenerationMod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

import static me.suff.mc.regen.client.skinhandling.SkinChangingHandler.SKIN_DIRECTORY_ALEX;
import static me.suff.mc.regen.client.skinhandling.SkinChangingHandler.SKIN_DIRECTORY_STEVE;

public class SkinHandler {

    public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/namemc");
    public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/namemc");


    public static void trending() throws IOException {
        if (!RegenConfig.skins.downloadTrendingSkins) return;
        File trendingDir = TRENDING_ALEX;
        if (!trendingDir.exists()) {
            if (trendingDir.mkdirs()) {
                RegenerationMod.LOG.info("Creating Directory: " + trendingDir);
                RegenerationMod.LOG.info("Creating Directory: " + TRENDING_ALEX);
                RegenerationMod.LOG.info("Creating Directory: " + TRENDING_STEVE);
            }
        }
        long attr = trendingDir.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(trendingDir.list()).length == 0) {
            FileUtils.cleanDirectory(trendingDir);
            RegenerationMod.LOG.warn("Refreshing Trending skins");

            int randomPage = RegenUtil.rand.nextInt(7800);

            for (int i = 3; i > 0; i--) {
                for (String skin : MineSkin.getSkinsFromPage(randomPage + i)) {
                    FileUtil.downloadAsPng(new URL(skin), "mk_" + RandomStringUtils.random(5, true, false), TRENDING_ALEX, TRENDING_STEVE);
                }
            }
        }
    }

    public static void skinpacks() throws IOException {
        if (!RegenConfig.skins.downloadInternalSkins) return;

        File drWhoDir = new File(SKIN_DIRECTORY_ALEX + "/doctor_who");

        long attr = drWhoDir.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(drWhoDir.list()).length == 0) {
            RegenerationMod.LOG.info("Downloading Remote Skinpacks");
            String packsUrl = "https://raw.githubusercontent.com/WhoCraft/Regeneration/skins/skinpacks/skinpacks.json";
            JsonObject links = MineSkin.getApiResponse(new URL(packsUrl));

            for (int skins = links.getAsJsonArray("packs").size() - 1; skins >= 0; skins--) {
                JsonArray packs = links.getAsJsonArray("packs");
                JsonObject currentPack = packs.get(skins).getAsJsonObject();
                String packName = currentPack.get("name").getAsString();
                String packCredit = currentPack.get("credits").getAsString();
                String downloadLink = currentPack.get("download_url").getAsString();
                String desc = currentPack.get("description").getAsString();

                RegenerationMod.LOG.info("Downloading " + packName + " by " + packCredit + " " + desc);
                FileUtil.unzipSkinPack(downloadLink);
            }
        }
    }


}
