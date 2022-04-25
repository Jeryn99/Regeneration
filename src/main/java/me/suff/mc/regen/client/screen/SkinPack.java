package me.suff.mc.regen.client.screen;

import me.suff.mc.regen.Regeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SkinPack {

    public static final HashMap<ResourceLocation, ResourceLocation> THUMBNAIL = new HashMap<>();
    private static final ArrayList<SkinPack> skins = new ArrayList<>();
    private final ResourceLocation namespace;
    private String name;
    private ArrayList<String> authors;
    private String downloadUrl;

    public SkinPack(String name, ArrayList<String> authors, String downloadUrl, String thumbnail, ResourceLocation namespace) {
        this.name = name;
        this.authors = authors;
        this.downloadUrl = downloadUrl;
        this.namespace = namespace;

       /* if (!thumbnail.isEmpty()) {
            Thread thread = new Thread(() -> {
                try {
                    THUMBNAIL.put(namespace, urlToTexture(new URL(thumbnail)));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } else {
            THUMBNAIL.put(namespace, null);
        }*/
    }

    public static ResourceLocation urlToTexture(URL url) {
        URLConnection uc = null;
        NativeImage image = null;
        try {
            uc = url.openConnection();
            uc.connect();
            uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36");
            Regeneration.LOG.warn("Downloading Image from: {}", url.toString());
            image = NativeImage.read(uc.getInputStream());
            return Minecraft.getInstance().getTextureManager().register("thumbnail_", new DynamicTexture(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DefaultPlayerSkin.getDefaultSkin();
    }

    public static Collection<? extends SkinPack> getAll() {
        return skins;
    }

    public static void add(SkinPack skinPack) {
        skins.add(skinPack);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public ResourceLocation getThumbnail() {
        return THUMBNAIL.get(namespace);
    }

    public ResourceLocation location() {
        return namespace;
    }

    public boolean hasThumbnail() {
        return getThumbnail() != null;
    }
}
