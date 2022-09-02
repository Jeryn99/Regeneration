package mc.craig.software.regen.util;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class RegenUtil {

    private static final String USER_AGENT = "Regeneration MC Mod/1.0";

    /**
     * @param url URL
     * @return Returns a JsonObject of the response from the site
     * @author Craig
     */
    public static JsonObject getApiResponse(URL url) throws IOException {
        HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
        uc.connect();
        uc = (HttpsURLConnection) url.openConnection();
        uc.setConnectTimeout(0);
        uc.addRequestProperty("User-Agent", USER_AGENT);
        InputStream inputStream = uc.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        return GsonHelper.parse(br);
    }

}
