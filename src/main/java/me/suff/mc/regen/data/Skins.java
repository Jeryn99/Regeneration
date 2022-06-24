package me.suff.mc.regen.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.suff.mc.regen.client.skin.CommonSkin;
import me.suff.mc.regen.util.PlayerUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Skins {

    public static void gen() throws IOException {

        JsonObject innerObject = new JsonObject();

        JsonArray jsonElements = new JsonArray();
        for (File file : CommonSkin.listAllSkins(PlayerUtil.SkinType.EITHER)) {
            if (!file.getAbsolutePath().contains("mineskin")) {
                String fileName = file.getName();
                File newFile = new File(file.getAbsolutePath().replace(" ", "_").replaceAll("&", "and").replaceAll("#", "").replaceAll("\\(", "").replaceAll("\\)", ""));
                FileUtils.createParentDirectories(newFile);
                boolean rename = file.renameTo(newFile);
                System.out.println(rename);
                JsonObject skin = new JsonObject();
                skin.addProperty("url", "https://mc.craig.software/skins/" + newFile.getAbsolutePath().replaceAll("C:\\\\Users\\\\Craig\\\\IdeaProjects\\\\Minecraft\\\\Regeneration\\\\Regeneration-1.18\\\\run\\\\Regeneration_Data\\\\skins\\\\", ""));
                skin.addProperty("name", fileName.replaceAll(".png", ""));
                skin.addProperty("destination", newFile.getAbsolutePath().replaceAll("C:\\\\Users\\\\Craig\\\\IdeaProjects\\\\Minecraft\\\\Regeneration\\\\Regeneration-1.18\\\\run\\\\Regeneration_Data\\\\skins\\\\", "").replaceAll(newFile.getName(), ""));

                jsonElements.add(skin);
            }
        }
        innerObject.add("skins", jsonElements);
        System.out.println(innerObject);
        System.exit(0);
    }

}
