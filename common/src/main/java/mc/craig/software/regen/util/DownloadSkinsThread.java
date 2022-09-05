package mc.craig.software.regen.util;

import mc.craig.software.regen.client.visual.SkinRetriever;

import static mc.craig.software.regen.client.visual.SkinRetriever.*;

public class DownloadSkinsThread extends Thread {

    public static boolean hasStarted = false;


    public static void setup() {
        DownloadSkinsThread thread = new DownloadSkinsThread();
        thread.setDaemon(true);
        thread.setName("Regen - Skins");

        ThreadGroup threadGroup = new ThreadGroup("Regen - Skin Threading");
        threadGroup.activeCount();

        if (threadGroup.activeCount() == 0) {
            Thread newThread = new Thread(threadGroup, thread);
            newThread.start();
        }

    }

    @Override
    public void run() {
        try {
            if (SkinRetriever.shouldUpdateSkins() && !hasStarted) {
                doDownloads();
                hasStarted = true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
