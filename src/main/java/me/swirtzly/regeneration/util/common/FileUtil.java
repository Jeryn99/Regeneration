package me.swirtzly.regeneration.util.common;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.client.image.ImageDownloader;
import me.swirtzly.regeneration.util.client.ClientUtil;
import me.swirtzly.regeneration.util.client.SkinDownloadManager;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static me.swirtzly.regeneration.client.skinhandling.SkinManipulation.*;

public class FileUtil {


    public static void handleDownloads() throws IOException {
		if (!RegenConfig.CLIENT.downloadInteralSkins.get()) return;


		String PACKS_URL = "https://raw.githubusercontent.com/Swirtzly/Regeneration/skins/index.json";
		String[] links = Regeneration.GSON.fromJson(getJsonFromURL(PACKS_URL), String[].class);
		for (String link : links) {
			unzipSkinPack(link);
		}
	}
	
	/**
	 * Creates skin folders Proceeds to download skins to the folders if they are empty If the download doesn't happen, NPEs will occur later on
	 */
	public static void createDefaultFolders() throws IOException {
		
		if (!SKIN_DIRECTORY.exists()) {
			FileUtils.forceMkdir(SKIN_DIRECTORY);
		}
		
		if (!SKIN_DIRECTORY_ALEX.exists()) {
			FileUtils.forceMkdir(SKIN_DIRECTORY_ALEX);
		}
		
		if (!SKIN_DIRECTORY_STEVE.exists()) {
			FileUtils.forceMkdir(SKIN_DIRECTORY_STEVE);
		}

	}
	
	/**
	 * @param url - URL to download image from
	 * @param filename - Filename of the image [SHOULD NOT CONTAIN FILE EXTENSION, PNG IS SUFFIXED FOR YOU]
	 * @throws IOException
	 */
	public static void downloadSkins(URL url, String filename, File alexDir, File steveDir) throws IOException {

		URLConnection uc = url.openConnection();
		uc.connect();
		uc = url.openConnection();
		uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
		Regeneration.LOG.warn("Downloading Skin from: {}", url.toString());
		BufferedImage img = ImageIO.read(uc.getInputStream());
		img = ClientUtil.ImageFixer.convertSkinTo64x64(img);

		File file = ImageDownloader.isAlexSkin(img) ? alexDir : steveDir;

		if (!file.exists()) {
			file.mkdirs();
		}

		if (!steveDir.exists()) {
			steveDir.mkdirs();
		}

		if (!alexDir.exists()) {
			alexDir.mkdirs();
		}
		ImageIO.write(img, "png", new File(file, filename + ".png"));
	}
	
	public static void doSetupOnThread() {
		AtomicBoolean notDownloaded = new AtomicBoolean(true);
		new Thread(() -> {
			while (notDownloaded.get()) {
				try {
					createDefaultFolders();
					handleDownloads();
                    SkinDownloadManager.downloadTrendingSkins();
                    SkinDownloadManager.downloadPreviousSkins();
					notDownloaded.set(false);
				} catch (Exception e) {
					Regeneration.LOG.error("Regeneration Mod: Failed to download skins! Check your internet connection and ensure you are playing in online mode!");
					throw new RuntimeException(e);
				}
			}
		}, Regeneration.NAME + " Download Daemon").start();
	}

	public static void unzipSkinPack(String url) throws IOException {
		File tempZip = new File(SKIN_DIRECTORY + "/temp/" + System.currentTimeMillis() + ".zip");
		Regeneration.LOG.info("Downloading " + url + " to " + tempZip.getAbsolutePath());
		FileUtils.copyURLToFile(new URL(url), tempZip);
		try (ZipFile file = new ZipFile(tempZip)) {
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
					Regeneration.LOG.info("Extracting file: " + uncompressedFilePath);
					File temp = uncompressedFilePath.toFile();
					if (temp.exists()) {
						Regeneration.LOG.info("Recreating: " + uncompressedFilePath);
						temp.delete();
					}
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

		if (tempZip.exists()) {
			FileUtils.forceDelete(tempZip.getParentFile());
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
