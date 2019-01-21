package me.fril.regeneration.client.skinhandling;

import net.minecraft.util.ResourceLocation;

public class SkinInfo {
	
	private final SkinType skintype;
	private final ResourceLocation textureLocation;
	
	public SkinInfo(ResourceLocation resourceLocation, SkinType skinType) {
		this.skintype = skinType;
		this.textureLocation = resourceLocation;
	}
	
	public ResourceLocation getTextureLocation() {
		return textureLocation;
	}
	
	public SkinType getSkintype() {
		return skintype;
	}
	
	public enum SkinType {
		ALEX, STEVE
	}
	
}
