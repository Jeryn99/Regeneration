package me.swirtzly.regen.util;

import me.swirtzly.regen.client.skin.ClientSkin;
import me.swirtzly.regen.client.skin.CommonSkin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static me.swirtzly.regen.client.skin.CommonSkin.*;

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
            createDefaultFolders();
            internalSkinsDownload();
            File tempZip = new File(SKIN_DIRECTORY + "/temp");
            if(tempZip.exists()) {
                FileUtils.cleanDirectory(tempZip);
            }
            CommonSkin.downloadTrendingSkins();
            CommonSkin.downloadTimelord();
            if (isClient) {
                DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientSkin::downloadPreviousSkins);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
