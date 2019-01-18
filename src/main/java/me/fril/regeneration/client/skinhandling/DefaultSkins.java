package me.fril.regeneration.client.skinhandling;

public enum DefaultSkins {
	SIXTH(true, "https://github.com/Suffril/Regeneration/blob/skins/skins/alex/six_doctor.png?raw=true"),
	ELEVENTH(true, "https://github.com/Suffril/Regeneration/blob/skins/alex/eleventh_doctor.png?raw=true"),
	HEROBRINE(false, "https://github.com/Suffril/Regeneration/blob/skins/skins/steve/herobrine.png?raw=true");
	
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
