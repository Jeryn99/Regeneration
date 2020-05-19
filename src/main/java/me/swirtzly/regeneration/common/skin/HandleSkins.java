package me.swirtzly.regeneration.common.skin;

import me.swirtzly.regeneration.Regeneration;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.ArrayList;

import static me.swirtzly.regeneration.util.FileUtil.getJsonFromURL;
import static me.swirtzly.regeneration.util.client.TrendingManager.getSkins;

/**
 * Created by Swirtzly
 * on 18/05/2020 @ 10:24
 */
@Mod.EventBusSubscriber
public class HandleSkins {

    public static ArrayList<String> SKINS = new ArrayList<>();


    public static void downloadSkins() {
        SKINS.clear();

        try {
            String[] uuids = Regeneration.GSON.fromJson(getJsonFromURL("https://raw.githubusercontent.com/Swirtzly/Regeneration/skins/donators.json"), String[].class);

            for (String uuid : uuids) {
                SKINS.addAll(getSkins("https://namemc.com/minecraft-skins/profile/" + uuid));
            }

            SKINS.addAll(getSkins("https://namemc.com/minecraft-skins"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String skin : SKINS) {
            System.out.println(skin);
        }
    }

}
