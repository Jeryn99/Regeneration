package me.suff.mc.regen.util;

import org.apache.commons.io.FileUtils;

import java.io.File;

import static me.suff.mc.regen.client.skin.CommonSkin.*;

public class DownloadSkinsThread extends Thread {

    public DownloadSkinsThread() {
    }

    public static void setup() {
        DownloadSkinsThread thread = new DownloadSkinsThread();
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
