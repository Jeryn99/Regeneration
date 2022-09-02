package mc.craig.software.regen.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static mc.craig.software.regen.util.RegenUtil.getApiResponse;

public class MineSkin {

    private static final String URL = "https://api.mineskin.org";

    /**
     * @param page Specify what page you want a list of skins from
     * @return Returns a ArrayList of URLS as Strings
     * @author Craig
     */
    public static ArrayList<String> getSkinsFromPage(int page) throws IOException {
        ArrayList<String> foundSkins = new ArrayList<>();
        String urlString = URL + "/get/list/" + page;
        JsonObject json = getApiResponse(new URL(urlString));
        for (int skins = json.getAsJsonArray("skins").size() - 1; skins >= 0; skins--) {
            JsonElement t = json.getAsJsonArray("skins").get(skins).getAsJsonObject().get("url");
            foundSkins.add(t.getAsString());
        }
        return foundSkins;
    }


    public static ArrayList<String> searchSkins(String searchTerm) throws IOException {
        JsonObject response = getApiResponse(new URL(URL + "/get/list?filter=" + searchTerm));
        ArrayList<String> foundSkins = new ArrayList<>();
        for (int skins = response.getAsJsonArray("skins").size() - 1; skins >= 0; skins--) {
            JsonElement t = response.getAsJsonArray("skins").get(skins).getAsJsonObject().get("url");
            foundSkins.add(t.getAsString());
        }
        return foundSkins;
    }


}