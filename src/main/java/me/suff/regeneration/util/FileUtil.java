package me.suff.regeneration.util;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static me.suff.regeneration.client.skinhandling.SkinChangingHandler.*;

public class FileUtil {
	
	
	public static void handleDownloads() throws IOException {
		String PACKS_URL = "https://raw.githubusercontent.com/Suffril/Regeneration/skins/index.json";
		String[] links = RegenerationMod.GSON.fromJson(getJsonFromURL(PACKS_URL), String[].class);
		for (String link : links) {
			unzipSkinPack(link);
		}
	}
	
	/**
	 * Creates skin folders
	 * Proceeds to download skins to the folders if they are empty
	 * If the download doesn't happen, NPEs will occur later on
	 */
	public static void createDefaultFolders() throws IOException {
		
		if (!SKIN_CACHE_DIRECTORY.exists()) {
			FileUtils.forceMkdir(SKIN_CACHE_DIRECTORY);
		}
		
		if (!SKIN_DIRECTORY.exists()) {
			FileUtils.forceMkdir(SKIN_DIRECTORY);
		}
		
		if (!SKIN_DIRECTORY_ALEX.exists()) {
			FileUtils.forceMkdir(SKIN_DIRECTORY_ALEX);
		}
		
		if (!SKIN_DIRECTORY_STEVE.exists()) {
			FileUtils.forceMkdir(SKIN_DIRECTORY_STEVE);
		}
		
		
		if (Objects.requireNonNull(SKIN_DIRECTORY_ALEX.list()).length == 0 || Objects.requireNonNull(SKIN_DIRECTORY_STEVE.list()).length == 0) {
			RegenerationMod.LOG.warn("One of the skin directories is empty, so we're going to fill both.");
			try {
				handleDownloads();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param url      - URL to download image from
	 * @param file     - Directory of where to save the image to [SHOULD NOT CONTAIN THE FILES NAME]
	 * @param filename - Filename of the image [SHOULD NOT CONTAIN FILE EXTENSION, PNG IS SUFFIXED FOR YOU]
	 * @throws IOException
	 */
	public static void downloadImage(URL url, File file, String filename) throws IOException {
		SkinChangingHandler.SKIN_LOG.info("Downloading Skin from: {}", url.toString());
		BufferedImage img = ImageIO.read(url);
		ImageIO.write(img, "png", new File(file, filename + ".png"));
	}
	
	
	public static void unzipSkinPack(String url) throws IOException {
		File f = new File(SKIN_DIRECTORY + "/temp/" + System.currentTimeMillis() + ".zip");
		FileUtils.copyURLToFile(new URL(url), f);
		try (ZipFile file = new ZipFile(f)) {
			FileSystem fileSystem = FileSystems.getDefault();
			Enumeration<? extends ZipEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					Files.createDirectories(fileSystem.getPath(SKIN_DIRECTORY + File.separator + entry.getName()));
				} else {
					InputStream is = file.getInputStream(entry);
					BufferedInputStream bis = new BufferedInputStream(is);
					String uncompressedFileName = SKIN_DIRECTORY + File.separator + entry.getName();
					Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
					Files.createFile(uncompressedFilePath);
					FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
					while (bis.available() > 0) {
						fileOutput.write(bis.read());
					}
					fileOutput.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (f.exists()) {
			FileUtils.forceDelete(f.getParentFile());
		}
	}
	
	public static String getJsonFromURL(String URL) {
		URL url = null;
		try {
			url = new URL(URL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line = bufferedReaderToString(in);
			line = line.replace("<pre>", "");
			line = line.replace("</pre>", "");
			return line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String bufferedReaderToString(BufferedReader e) {
		StringBuilder builder = new StringBuilder();
		String aux = "";
		
		try {
			while ((aux = e.readLine()) != null) {
				builder.append(aux);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return builder.toString();
	}
}
