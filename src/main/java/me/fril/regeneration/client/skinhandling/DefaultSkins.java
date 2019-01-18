package me.fril.regeneration.client.skinhandling;

public enum DefaultSkins {
	SIXTH(true, "https://github.com/Suffril/Regeneration/blob/skins/skins/alex/six_doctor.png?raw=true"),
	ELEVENTH(true, "https://github.com/Suffril/Regeneration/blob/skins/alex/eleventh_doctor.png?raw=true"),
	GORILLAZ(true, "https://github.com/Suffril/Regeneration/blob/skins/alex/gorillaz.png?raw=true"),
	JERRY(false, "https://github.com/Suffril/Regeneration/blob/skins/steve/jerry.png?raw=true"),
	AGRENT(false, "https://github.com/Suffril/Regeneration/blob/skins/steve/agent.png?raw=true"),
	DOC_GREEN(false, "https://github.com/Suffril/Regeneration/blob/skins/steve/doc_green.png?raw=true"),
	FRENCH(false, "https://github.com/Suffril/Regeneration/blob/skins/steve/the_french.png?raw=true"),
	JULI(false, "https://github.com/Suffril/Regeneration/blob/skins/steve/juli.png?raw=true"),
	MARTY(false, "https://github.com/Suffril/Regeneration/blob/skins/steve/marty.png?raw=true"),
	THATGUY(false, "https://github.com/Suffril/Regeneration/blob/skins/steve/thatguy.png?raw=true"),
	ANCIENT_EMO(false, "https://github.com/Suffril/Regeneration/blob/skins/steve/ancient_emo.png?raw=true"),
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
