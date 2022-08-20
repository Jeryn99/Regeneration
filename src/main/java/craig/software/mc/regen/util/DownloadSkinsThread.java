package craig.software.mc.regen.util;

import craig.software.mc.regen.client.skin.CommonSkin;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class DownloadSkinsThread extends Thread {

    public static void setup() {
        DownloadSkinsThread thread = new DownloadSkinsThread();
        thread.setDaemon(true);
        thread.setName("Regen - Skins");
        thread.start();
    }

    @Override
    public void run() {
        try {
            CommonSkin.folderSetup();
            File tempZip = new File(CommonSkin.SKIN_DIRECTORY + "/temp");
            if (tempZip.exists()) {
                FileUtils.cleanDirectory(tempZip);
            }
            CommonSkin.trending();
            CommonSkin.timelord();
            CommonSkin.skinpacks();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
