package me.swirtzly.regeneration.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler.SKIN_DIRECTORY_ALEX;
import static me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler.SKIN_DIRECTORY_STEVE;


public class Trending {

    public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/namemc");
    public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/namemc");

    public static File USER_ALEX = new File(SKIN_DIRECTORY_ALEX + "/the_past");
    public static File USER_STEVE = new File(SKIN_DIRECTORY_STEVE + "/the_past");

    public static void downloadPreviousSkins() {
        RegenerationMod.LOG.warn("Refreshing users past skins");

        if (!USER_ALEX.exists()) {
            USER_ALEX.mkdirs();
        }

        long attr = USER_ALEX.lastModified();

        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(USER_ALEX.list()).length == 0) {
            for (int i = 0; i < 5; i++) {
                try {
                    String url = "https://namemc.com/minecraft-skins/profile/" + Minecraft.getMinecraft().getSession().getPlayerID() + "?page=" + i;
                    getListOfSkns(url).iterator().forEachRemaining(jsonElement -> {
                        try {
                            String trendingUrl = jsonElement.getAsJsonObject().get("sameAs").getAsString();
                            FileUtil.downloadSkins(new URL(trendingUrl.replace("https://namemc.com/skin/", "https://namemc.com/texture/") + ".png"), Minecraft.getMinecraft().getSession().getUsername() + "_" + trendingUrl.replaceAll("https://namemc.com/skin/", ""), USER_ALEX, USER_STEVE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static JsonArray getListOfSkns(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements scripts = doc.getElementsByTag("script");
        String jsonText = scripts.get(2).data();

        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(jsonText).getAsJsonObject();
        JsonObject locObj = rootObj.getAsJsonObject("mainEntityOfPage");
        return locObj.getAsJsonArray("image");
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
			try {
                String url = "https://namemc.com/minecraft-skins/trending/";
                getListOfSkns(url).iterator().forEachRemaining(jsonElement -> {
					try {
                        String trendingUrl = jsonElement.getAsJsonObject().get("sameAs").getAsString();
                        FileUtil.downloadSkins(new URL(trendingUrl.replace("https://namemc.com/skin/", "https://namemc.com/texture/") + ".png"), "namemc_" + trendingUrl.replaceAll("https://namemc.com/skin/", ""), TRENDING_ALEX, TRENDING_STEVE);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

            } catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}