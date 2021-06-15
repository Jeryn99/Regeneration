package me.swirtzly.regeneration.util.client;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.util.common.FileUtil;
import me.swirtzly.regeneration.util.common.MineSkin;
import me.swirtzly.regeneration.util.common.RegenUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static me.swirtzly.regeneration.common.skin.HandleSkins.SKIN_DIRECTORY_ALEX;
import static me.swirtzly.regeneration.common.skin.HandleSkins.SKIN_DIRECTORY_STEVE;

public class SkinDownloadManager {

    public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/mineskin");
    public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/mineskin");


    public static void downloadTrendingSkins() throws IOException {
        if (!RegenConfig.CLIENT.downloadTrendingSkins.get() || !RegenUtil.doesHaveInternet()) return;
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
            FileUtils.deleteDirectory(trendingDir);
            Regeneration.LOG.warn("Refreshing Trending skins");

            int randomPage = RegenUtil.RAND.nextInt(3000);
            for (int i = 3; i > 0; i--) {
                String cleanName = UUID.randomUUID().toString().substring(0, 6);
                ArrayList<String> skins = MineSkin.getSkinsFromPage(randomPage + i);
                for (String skin : skins) {
                    FileUtil.downloadSkins(new URL(skin), "trending_" + cleanName, TRENDING_ALEX, TRENDING_STEVE);
                }
            }

        }
    }

}
