package mc.craig.software.regen.client.skin;

import static mc.craig.software.regen.client.skin.SkinRetriever.doDownloads;

public class DownloadSkinsThread extends Thread {

    public static boolean hasStarted = false;
    public static boolean isClient = false;

    public DownloadSkinsThread(boolean isClient){
        DownloadSkinsThread.isClient = isClient;
    }

    @Override
    public void run() {
        try {
            if (SkinRetriever.shouldUpdateSkins() && !hasStarted) {
                doDownloads(isClient);
                hasStarted = true;
                stop();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
