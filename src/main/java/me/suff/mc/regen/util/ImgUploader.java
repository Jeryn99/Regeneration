package me.suff.mc.regen.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.suff.mc.regen.config.RegenConfig;
import net.minecraft.util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

public class ImgUploader implements Runnable {

    BufferedImage img;
    private final boolean isAlex;

    public ImgUploader(BufferedImage img, boolean isAlex) {
        this.img = img;
        this.isAlex = isAlex;
    }

    @Override
    public void run() {
        try {
            JsonElement jelement = new JsonParser().parse(getImgurContent(RegenConfig.CLIENT.imgurClientID.get(), img));
            JsonObject jobject = jelement.getAsJsonObject();
            jobject = jobject.getAsJsonObject("data");
            String end = jobject.get("link").toString().replaceAll("\"", "");
            String url = "https://www.minecraft.net/en-us/profile/skin/remote?url=" + end + "&model=" + (isAlex ? "slim" : "classic");
            Util.getPlatform().openUri(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String getImgurContent(String clientID, BufferedImage image) throws IOException {
        URL url;
        url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        String dataImage = Base64.getEncoder().encodeToString(byteImage);
        String data = URLEncoder.encode("image", "UTF-8") + "="
                + URLEncoder.encode(dataImage, "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();

        return stb.toString();
    }
}