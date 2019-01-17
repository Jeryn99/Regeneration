package me.fril.regeneration.client.skinhandling;

public enum DefaultSkins {
    FIFTH(true, "https://i.imgur.com/KY3uHvR.png"),
    HEROBRINE(false, "https://i.imgur.com/I0EzTrk.png");

    private final String url;
    private final boolean alexDir;

    DefaultSkins(boolean alexDir, String base64) {
        this.url = base64;
        this.alexDir = alexDir;
    }

    public String getURL() {
        return url;
    }

    public boolean isAlexDir() {
        return alexDir;
    }
}
