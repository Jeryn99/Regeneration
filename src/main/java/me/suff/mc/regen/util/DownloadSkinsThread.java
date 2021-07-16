package me.suff.mc.regen.util;

import org.apache.commons.io.FileUtils;

import java.io.File;

import static me.suff.mc.regen.client.skin.CommonSkin.*;

public class DownloadSkinsThread extends Thread {

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

    @Override
    public void run() {
        try {
            folderSetup();
            File tempZip = new File(SKIN_DIRECTORY + "/temp");
            if (tempZip.exists()) {
                FileUtils.cleanDirectory(tempZip);
            }
            trending();
            timelord();
            skinpacks();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
