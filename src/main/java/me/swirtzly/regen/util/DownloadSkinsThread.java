package me.swirtzly.regen.util;

import me.swirtzly.regen.client.skin.ClientSkin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.io.IOException;

import static me.swirtzly.regen.client.skin.CommonSkin.createDefaultFolders;
import static me.swirtzly.regen.client.skin.CommonSkin.internalSkinsDownload;

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
            if (isClient) {
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
                    try {
                        ClientSkin.downloadTrendingSkins();
                        ClientSkin.downloadPreviousSkins();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
