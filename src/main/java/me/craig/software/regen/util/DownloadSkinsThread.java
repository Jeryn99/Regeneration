package me.craig.software.regen.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import me.craig.software.regen.Regeneration;
import me.craig.software.regen.client.skin.SkinHandler;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static me.craig.software.regen.client.skin.CommonSkin.*;

public class DownloadSkinsThread extends Thread {

    private boolean hasStarted;

    public static void setup() {
        DownloadSkinsThread thread = new DownloadSkinsThread();
        thread.setDaemon(true);
        thread.setName("Regen - Skins");
        thread.start();
    }

    @Override
    public void run() {
        try {
            if (shouldUpdateSkins() && !hasStarted) {
                doDownloads();
                hasStarted = true;
                stop();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public static void doDownloads() throws IOException {
        folderSetup();
        File tempZip = new File(SKIN_DIRECTORY + "/temp");
        if (tempZip.exists()) {
            FileUtils.cleanDirectory(tempZip);
        }
        trending();
        timelord();
        skinpacks();
        writeTime();
    }

    public static void writeTime() throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("last_downloaded", new JsonPrimitive(System.currentTimeMillis()));

        try (FileWriter writer = new FileWriter(new File(SKIN_DIRECTORY, "cache_tracker.json"))) {
            Regeneration.GSON.toJson(jsonObject, writer);
            writer.flush();
        }
    }

    public static boolean shouldUpdateSkins() throws FileNotFoundException {
        File cacheFile = new File(SKIN_DIRECTORY, "cache_tracker.json");
        if (!cacheFile.exists()) {
            Regeneration.LOG.info("Looks like no skins have been downloaded! Commencing first time set up!");
            return true;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cacheFile)));
        JsonObject json = JSONUtils.parse(br);
        long timeSinceDownloaded = json.getAsJsonPrimitive("last_downloaded").getAsLong();

        long minutesSince = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - timeSinceDownloaded);
        boolean shouldDownload = minutesSince > 1440;
        Regeneration.LOG.info("It has been {} minutes since last skin update! {}", minutesSince, shouldDownload ? "A Skin update will commence!" : "A Skin update will not commence just now!");
        return shouldDownload;
    }
}
