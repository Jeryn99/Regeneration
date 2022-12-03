package mc.craig.software.regen.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import static mc.craig.software.regen.util.RegenUtil.GSON;

public class SkinApi {

    private static final String USER_AGENT = "Regeneration MC Mod/1.0";
    private static final String URL = "https://api.mineskin.org";
    private static final String API_ENDPOINT = "https://mc.craig.software/api/skin/random-skins";

    /**
     * Retrieves a list of skin URLs from a given page number.
     *
     * @param page the page number to retrieve the skins from
     * @return an ArrayList containing the URLs of the skins on the given page
     */
    public static ArrayList<String> getSkinsFromPage(int page) throws IOException {
        ArrayList<String> foundSkins = new ArrayList<>();

        // Fetch the JSON data from the API
        JsonElement json = getApiData(URL + "/get/list/" + page);

        // Check if the JSON data is a JSON array
        if (json.isJsonArray()) {
            // Extract the skin URLs from the JSON data and add them to the foundSkins list
            for (JsonElement jsonElement : json.getAsJsonArray()) {
                JsonElement t = jsonElement.getAsJsonObject().get("url");
                foundSkins.add(t.getAsString());
            }
        }

        return foundSkins;
    }

    /**
     * Retrieves a list of JSON elements from the internal API.
     *
     * @return an ArrayList containing the JSON elements returned by the API
     */
    public static ArrayList<JsonElement> interalApiSkins() throws IOException {
        ArrayList<JsonElement> foundSkins = new ArrayList<>();

        // Fetch the JSON data from the API
        JsonArray json = getApiData(API_ENDPOINT).getAsJsonArray();

        // Add each element of the JSON array to the foundSkins list
        for (JsonElement jsonElement : json) {
            JsonObject jsonData = jsonElement.getAsJsonObject();
            foundSkins.add(jsonData);
        }

        return foundSkins;
    }

    /**
     * Validates a user with the given UUID.
     *
     * @param uuid the UUID of the user to validate
     * @return true if the user is valid, false otherwise
     */
    public static boolean isUserValid(UUID uuid) throws IOException {
        // Fetch the JSON data from the API
        JsonElement response = getApiData(URL + "/validate/uuid/" + uuid);

        if (response.isJsonObject() && response.getAsJsonObject().has("valid") && response.getAsJsonObject().has("uuid")) {
            return response.getAsJsonObject().get("valid").getAsBoolean();
        } else {
            return false;
        }
    }

    /**
     * Searches for skins with the given search term.
     *
     * @param searchTerm the term to search for
     * @return an ArrayList containing the URLs of the skins that match the search term
     */
    public static ArrayList<String> searchSkins(String searchTerm) throws IOException {
        // Fetch the JSON data from the API
        JsonElement response = getApiData(URL + "/get/list?filter=" + searchTerm);

        // Create a list to hold the found skins
        ArrayList<String> foundSkins = new ArrayList<>();

        if (response.isJsonArray()) {
            JsonArray skinData = response.getAsJsonObject().get("skins").getAsJsonArray();
            for (int skinIndex = skinData.size() - 1; skinIndex >= 0; skinIndex--) {
                JsonElement skin = skinData.get(skinIndex).getAsJsonObject().get("url");
                foundSkins.add(skin.getAsString());
            }
        }

        return foundSkins;
    }


    /**
     * Helper method to fetch JSON data from a given URL.
     *
     * @param url the URL to fetch the JSON data from
     * @return a JsonObject containing the JSON data
     */
    public static JsonElement getApiData(String url) throws IOException {
        // Create a connection to the given URL
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();

        // Set the user agent to identify the request
        connection.setRequestProperty("User-Agent", USER_AGENT);

        // Set the request method to GET
        connection.setRequestMethod("GET");

        // Set the connection timeout to 5 seconds
        connection.setConnectTimeout(5000);

        // Set the read timeout to 5 seconds
        connection.setReadTimeout(5000);

        // Connect to the URL
        connection.connect();

        // Check if the response code is OK (200)
        if (connection.getResponseCode() == 200) {
            // Read the JSON data from the response
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            // Parse the JSON data into a JsonObject
            return GSON.fromJson(stringBuilder.toString(), JsonElement.class);
        } else {
            // If the response code is not OK, throw an exception
            throw new IOException("Failed to connect to API. Response code: " + connection.getResponseCode());
        }
    }



}
