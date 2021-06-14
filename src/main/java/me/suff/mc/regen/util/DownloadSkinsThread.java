package me.suff.mc.regen.util;

import static me.suff.mc.regen.client.skin.CommonSkin.SKIN_DIRECTORY;
import static me.suff.mc.regen.client.skin.CommonSkin.folderSetup;
import static me.suff.mc.regen.client.skin.CommonSkin.skinpacks;
import static me.suff.mc.regen.client.skin.CommonSkin.timelord;
import static me.suff.mc.regen.client.skin.CommonSkin.trending;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;

import me.suff.mc.regen.Regeneration;

public class DownloadSkinsThread extends Thread {

    public static boolean forceStop = false;
    private final boolean isClient;

    public DownloadSkinsThread(boolean isClient) {
        this.isClient = isClient;
    }

    public static void setup(boolean isClient) {
        DownloadSkinsThread thread = new DownloadSkinsThread(isClient);
        thread.setDaemon(true);
        thread.setName("Regen - Skins");
        thread.start();
    }

    private static void unPack(String s) throws IOException {
        File tempZip = new File("./gallifrey/gallifrey" + ".zip");

        FileUtils.copyURLToFile(new URL(s), tempZip);
        try (ZipFile file = new ZipFile(tempZip)) {
            FileSystem fileSystem = FileSystems.getDefault();
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    Files.createDirectories(fileSystem.getPath("./gallifrey/" + File.separator + entry.getName()));
                } else {
                    InputStream is = file.getInputStream(entry);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    String uncompressedFileName = "./gallifrey/" + File.separator + entry.getName();
                    Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
                    Regeneration.LOG.info(entry.getName());
                    File temp = uncompressedFilePath.toFile();
                    if (temp.exists()) {
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
    }

    @Override
    public void run() {
        try {
            // downloadGallifrey();
            folderSetup();
            File tempZip = new File(SKIN_DIRECTORY + "/temp");
            if (tempZip.exists()) {
                FileUtils.cleanDirectory(tempZip);
            }
            trending();
            timelord();
            skinpacks();
            if (forceStop) {
                stop();
                forceStop = false;
            }

        } catch (IOException exception) {
            exception.printStackTrace();
            stop();
            forceStop = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
