package me.fril.regeneration.util;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static me.fril.regeneration.client.skinhandling.SkinChangingHandler.*;

public class FileUtil {
	
	/**
	 * Creates skin folders
	 * Proceeds to download skins to the folders if they are empty
	 * If the download doesn't happen, NPEs will occur later on
	 */
	public static void createDefaultFolders() {
		
		if (!SKIN_CACHE_DIRECTORY.exists()) {
			SKIN_CACHE_DIRECTORY.mkdirs();
		}
		
		if (!SKIN_DIRECTORY.exists()) {
			SKIN_DIRECTORY.mkdirs();
		}
		
		SKIN_DIRECTORY_ALEX.mkdirs();
		SKIN_DIRECTORY_STEVE.mkdirs();
		
		if (Objects.requireNonNull(SKIN_DIRECTORY_ALEX.listFiles()).length < 1 || Objects.requireNonNull(SKIN_DIRECTORY_STEVE.listFiles()).length < 1) {
			try {
				createDefaultImages();
				unzipPack("https://github.com/Suffril/Regeneration/blob/skins/doctor_skins.zip?raw=true");
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
		SKIN_LOG.info("Downloading Skin from: {}", url.toString());
		BufferedImage img = ImageIO.read(url);
		ImageIO.write(img, "png", new File(file, filename + ".png"));
	}
	
	
	public static void unzipPack(String url) throws IOException {
		File f = new File(SKIN_DIRECTORY + "/temp/skins.zip");
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
			f.delete();
		}
	}
	
}
