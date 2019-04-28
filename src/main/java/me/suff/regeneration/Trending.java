package me.suff.regeneration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.util.FileUtil;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class Trending {
	
	public static void downloadTrendingSkins() throws IOException {
		if (!RegenConfig.CLIENT.downloadTrendingSkins.get()) return;
		File trendingDir = new File(SkinChangingHandler.SKIN_DIRECTORY_ALEX.toPath().toString() + "/namemc_trending/");
		if (!trendingDir.exists()) {
			trendingDir.mkdirs();
		}
		
		long attr = trendingDir.lastModified();
		
		if (System.currentTimeMillis() - attr <= 86400000) {
			FileUtils.deleteDirectory(trendingDir);
			RegenerationMod.LOG.warn("Refreshing Trending skins");
			try {
				String url = "https://namemc.com/minecraft-skins";
				Document doc = Jsoup.connect(url).get();
				Elements scripts = doc.getElementsByTag("script");
				String jsonText = scripts.get(2).data();
				
				JsonParser parser = new JsonParser();
				JsonObject rootObj = parser.parse(jsonText).getAsJsonObject();
				JsonObject locObj = rootObj.getAsJsonObject("mainEntityOfPage");
				JsonArray imagesUrl = locObj.getAsJsonArray("image");
				
				
				imagesUrl.iterator().forEachRemaining(jsonElement -> {
					try {
						FileUtil.downloadSkinImage(new URL(jsonElement.getAsJsonObject().get("sameAs").getAsString().replace("https://namemc.com/skin/", "https://namemc.com/texture/") + ".png"), trendingDir, "namemc_" + System.currentTimeMillis());
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