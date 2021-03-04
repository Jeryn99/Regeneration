package me.suff.mc.regen.client.skin;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static me.suff.mc.regen.client.skin.CommonSkin.*;

public class ClientSkin {

    public static File USER_ALEX = new File(SKIN_DIRECTORY_ALEX + "/the_past");
    public static File USER_STEVE = new File(SKIN_DIRECTORY_STEVE + "/the_past");

    public static void downloadPreviousSkins() {
        if (!RegenConfig.SKIN.downloadPreviousSkins.get() || !RegenUtil.doesHaveInternet()) return;
        Regeneration.LOG.warn("Refreshing users past skins for {}", Minecraft.getInstance().getSession().getUsername());

        if (!USER_ALEX.exists()) {
            if (USER_ALEX.mkdirs()) {
                Regeneration.LOG.info("Creating Directory: " + USER_ALEX);
            }
        }

        if (!USER_STEVE.exists()) {
            if (USER_STEVE.mkdirs()) {
                Regeneration.LOG.info("Creating Directory: " + USER_STEVE);
            }
        }

        long attr = USER_ALEX.lastModified();

        if (System.currentTimeMillis() - attr >= 86400000 || Objects.requireNonNull(USER_ALEX.list()).length == 0) {
            try {
                FileUtils.cleanDirectory(USER_ALEX);
                FileUtils.cleanDirectory(USER_STEVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = "https://namemc.com/minecraft-skins/profile/" + Minecraft.getInstance().getSession().getPlayerID();
            try {
                for (String skin : getSkins(url)) {
                    downloadSkins(new URL(skin), Minecraft.getInstance().getSession().getUsername() + "_" + System.currentTimeMillis(), USER_ALEX, USER_STEVE);
                }
            } catch (IOException e) {
                Regeneration.LOG.warn("Could not download player skins for {}, Are you a legitimate user? Are you online?", Minecraft.getInstance().getSession().getUsername());
                e.printStackTrace();
            }
        }
    }


}


