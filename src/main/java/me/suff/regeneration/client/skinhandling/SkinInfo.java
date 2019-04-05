package me.suff.regeneration.client.skinhandling;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class SkinInfo {
	
	private final SkinType skintype;
	private final ResourceLocation textureLocation;
	
	public SkinInfo(ResourceLocation resourceLocation, SkinType skinType) {
		this.skintype = skinType;
		this.textureLocation = resourceLocation;
	}
	
	public ResourceLocation getSkinTextureLocation() {
		if (textureLocation != null) {
			return textureLocation;
		}
		return DefaultPlayerSkin.getDefaultSkinLegacy();
	}
	
	public SkinType getSkintype() {
		if (skintype != null) {
			return skintype;
		}
		return SkinType.ALEX;
	}
	
	public void dispose() {
		TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		ITextureObject texture = textureManager.getTexture(getSkinTextureLocation());
		if (texture != null) {
			textureManager.deleteTexture(getSkinTextureLocation());
		}
	}
	
	public enum SkinType {
		ALEX("slim"), STEVE("default");
		
		private final String type;
		
		SkinType(String type) {
			this.type = type;
		}
		
		public String getMojangType() {
			return type;
		}
	}
	
}
