package mc.craig.software.regen.util;

import mc.craig.software.regen.client.visual.SkinRetriever;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static mc.craig.software.regen.client.visual.SkinRetriever.*;

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
            if (SkinRetriever.shouldUpdateSkins()) {
                folderSetup();
                internalSkins();
                remoteSkins();
                writeTime();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
