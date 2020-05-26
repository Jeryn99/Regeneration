package me.swirtzly.regeneration.util.client;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.util.FileUtil;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static me.swirtzly.regeneration.client.skinhandling.SkinManipulation.SKIN_DIRECTORY_ALEX;
import static me.swirtzly.regeneration.client.skinhandling.SkinManipulation.SKIN_DIRECTORY_STEVE;
import static me.swirtzly.regeneration.common.skin.HandleSkins.getSkins;

public class TrendingManager {

	public static File TRENDING_ALEX = new File(SKIN_DIRECTORY_ALEX + "/namemc");
	public static File TRENDING_STEVE = new File(SKIN_DIRECTORY_STEVE + "/namemc");

	public static File USER_ALEX = new File(SKIN_DIRECTORY_ALEX + "/the_past");
	public static File USER_STEVE = new File(SKIN_DIRECTORY_STEVE + "/the_past");

	public static void downloadPreviousSkins() {
		if (!RegenConfig.CLIENT.downloadPreviousSkins.get()) return;
		Regeneration.LOG.warn("Refreshing users past skins");

		if (!USER_ALEX.exists()) {
			USER_ALEX.mkdirs();
		}

		long attr = USER_ALEX.lastModified();

		if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(USER_ALEX.list()).length == 0) {
			String url = "https://namemc.com/minecraft-skins/profile/" + Minecraft.getInstance().getSession().getPlayerID();
			try {
				for (String skin : getSkins(url)) {
					FileUtil.downloadSkins(new URL(skin), Minecraft.getInstance().getSession().getUsername() + "_" + System.currentTimeMillis(), USER_ALEX, USER_STEVE);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static void downloadTrendingSkins() throws IOException {
		if (!RegenConfig.CLIENT.downloadTrendingSkins.get()) return;
		File trendingDir = TRENDING_ALEX;
		if (!trendingDir.exists()) {
			trendingDir.mkdirs();
		}
		long attr = trendingDir.lastModified();
		if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(trendingDir.list()).length == 0) {
			FileUtils.deleteDirectory(trendingDir);
			Regeneration.LOG.warn("Refreshing Trending skins");
			for (String skin : getSkins("https://namemc.com/minecraft-skins")) {
				String cleanName = skin.replaceAll("https://namemc.com/texture/", "").replaceAll(".png", "");
				FileUtil.downloadSkins(new URL(skin), "trending_" + cleanName, TRENDING_ALEX, TRENDING_STEVE);
			}
		}
	}

}
