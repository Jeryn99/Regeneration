package me.swirtzly.regen.client.skin;

import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.util.RegenUtil;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static me.swirtzly.regen.client.skin.CommonSkin.*;

public class ClientSkin {

    public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/namemc");
    public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/namemc");

    public static File USER_ALEX = new File(SKIN_DIRECTORY_ALEX + "/the_past");
    public static File USER_STEVE = new File(SKIN_DIRECTORY_STEVE + "/the_past");

    public static void downloadPreviousSkins() {
        if (!RegenConfig.SKIN.downloadPreviousSkins.get() || !RegenUtil.doesHaveInternet()) return;
        Regeneration.LOG.warn("Refreshing users past skins");

        if (!USER_ALEX.exists()) {
            if (USER_ALEX.mkdirs()) {
                Regeneration.LOG.info("Creating Directory: " + USER_ALEX);
            }
        }

        if (!USER_STEVE.exists()) {
            if (USER_STEVE.mkdirs()) {
                Regeneration.LOG.info("Creating Directory: " + USER_STEVE);
            }
        }

        long attr = USER_ALEX.lastModified();

        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(USER_ALEX.list()).length == 0) {
            try {
                FileUtils.cleanDirectory(USER_ALEX);
                FileUtils.cleanDirectory(USER_STEVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = "https://namemc.com/minecraft-skins/profile/" + Minecraft.getInstance().getSession().getPlayerID();
            try {
                for (String skin : getSkins(url)) {
                    downloadSkins(new URL(skin), Minecraft.getInstance().getSession().getUsername() + "_" + System.currentTimeMillis(), USER_ALEX, USER_STEVE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void downloadTrendingSkins() throws IOException {
        if (!RegenConfig.SKIN.downloadTrendingSkins.get() || !RegenUtil.doesHaveInternet()) return;
        File trendingDir = TRENDING_ALEX;
        if (!trendingDir.exists()) {
            if (trendingDir.mkdirs()) {
                Regeneration.LOG.info("Creating Directory: " + trendingDir);
                Regeneration.LOG.info("Creating Directory: " + TRENDING_ALEX);
                Regeneration.LOG.info("Creating Directory: " + TRENDING_STEVE);
            }
        }
        long attr = trendingDir.lastModified();
        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(trendingDir.list()).length == 0) {
            FileUtils.cleanDirectory(trendingDir);
            Regeneration.LOG.warn("Refreshing Trending skins");
            for (String skin : getSkins("https://namemc.com/minecraft-skins")) {
                String cleanName = skin.replaceAll("https://namemc.com/texture/", "").replaceAll(".png", "");
                downloadSkins(new URL(skin), "trending_" + cleanName, TRENDING_ALEX, TRENDING_STEVE);
            }
        }
    }

}


