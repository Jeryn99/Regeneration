package me.suff.mc.regen.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import static me.suff.mc.regen.RegenerationMod.GSON;
import static net.minecraft.util.JsonUtils.fromJson;

public class MineSkin {

    private static final String USER_AGENT = "Regeneration MC Mod/1.0";
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

    /**
     * @param uuid UUID of User to validate
     * @return Returns a boolean declaring whether a user is premium or not
     * @author Craig
     */
    public static boolean isUserValid(UUID uuid) throws IOException {
        JsonObject response = getApiResponse(new URL(URL + "/validate/uuid/" + uuid));
        return response.get("valid").getAsBoolean() && response.has("uuid");
    }

    /**
     * @param name Name of User to validate
     * @param uuid UUID of User to validate. UUID is used to confirm the local ID matches the remote one
     * @return Returns a boolean declaring whether a user is premium or not
     * @author Craig
     */
    public static boolean isUserValid(String name, UUID uuid) throws IOException {
        JsonObject response = getApiResponse(new URL(URL + "/validate/name/" + name));

        //If the response doesn't contain a uuid, it is not a premium account
        if (!response.has("uuid")) {
            return false;
        } else {
            String uuidResponse = response.get("uuid").getAsString();
            return uuidResponse.equals(uuid.toString()) && response.get("valid").getAsBoolean();
        }
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

    /**
     * @param url URL
     * @return Returns a JsonObject of the response from the site
     * @author Craig
     */
    public static JsonObject getApiResponse(URL url) throws IOException {
        HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
        uc.connect();
        uc = (HttpsURLConnection) url.openConnection();
        uc.addRequestProperty("User-Agent", USER_AGENT);
        InputStream inputStream = uc.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        return MineSkin.parse(br);
    }

    public static JsonObject parse(String p_212746_0_, boolean p_212746_1_) {
        return parse(new StringReader(p_212746_0_), p_212746_1_);
    }

    public static JsonObject parse(Reader p_212744_0_, boolean p_212744_1_) {
        return fromJson(GSON, p_212744_0_, JsonObject.class, p_212744_1_);
    }

    public static JsonObject parse(String p_212745_0_) {
        return parse(p_212745_0_, false);
    }

    public static JsonObject parse(Reader p_212743_0_) {
        return parse(p_212743_0_, false);
    }


}
