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

public class FileUtil {
	
	
	public static void handleDownloads() throws IOException {
		String PACKS_URL = "https://raw.githubusercontent.com/Suffril/Regeneration/skins/index.json";
		String[] links = RegenerationMod.GSON.fromJson(getJsonFromURL(PACKS_URL), String[].class);
		for (String link : links) {
			unzipPack(link);
		}
	}
	
	/**
	 * Creates skin folders
	 * Proceeds to download skins to the folders if they are empty
	 * If the download doesn't happen, NPEs will occur later on
	 */
	public static void createDefaultFolders() {
		
		if (!SkinChangingHandler.SKIN_CACHE_DIRECTORY.exists()) {
			SkinChangingHandler.SKIN_CACHE_DIRECTORY.mkdirs();
		}
		
		if (!SkinChangingHandler.SKIN_DIRECTORY.exists()) {
			SkinChangingHandler.SKIN_DIRECTORY.mkdirs();
		}
		
		SkinChangingHandler.SKIN_DIRECTORY_ALEX.mkdirs();
		SkinChangingHandler.SKIN_DIRECTORY_STEVE.mkdirs();
		
		if (Objects.requireNonNull(SkinChangingHandler.SKIN_DIRECTORY_ALEX.listFiles()).length < 1 || Objects.requireNonNull(SkinChangingHandler.SKIN_DIRECTORY_STEVE.listFiles()).length < 1) {
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
	
	
	public static void unzipPack(String url) throws IOException {
		File f = new File(SkinChangingHandler.SKIN_DIRECTORY + "/temp/" + System.currentTimeMillis() + ".zip");
		FileUtils.copyURLToFile(new URL(url), f);
		try (ZipFile file = new ZipFile(f)) {
			FileSystem fileSystem = FileSystems.getDefault();
			Enumeration<? extends ZipEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					Files.createDirectories(fileSystem.getPath(SkinChangingHandler.SKIN_DIRECTORY + File.separator + entry.getName()));
				} else {
					InputStream is = file.getInputStream(entry);
					BufferedInputStream bis = new BufferedInputStream(is);
					String uncompressedFileName = SkinChangingHandler.SKIN_DIRECTORY + File.separator + entry.getName();
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
