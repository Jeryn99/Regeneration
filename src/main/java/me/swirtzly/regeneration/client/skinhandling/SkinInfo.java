package me.swirtzly.regeneration.client.skinhandling;

import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class SkinInfo {
	
	private final SkinType skintype;
    private final ResourceLocation TEXTURE_LOCATION;
    private final PlayerEntity PLAYER;

    public SkinInfo(PlayerEntity playerEntity, ResourceLocation resourceLocation, SkinType skinType) {
		this.skintype = skinType;
        this.TEXTURE_LOCATION = resourceLocation;
        this.PLAYER = playerEntity;
    }

    public ResourceLocation getTextureLocation() {
        if (TEXTURE_LOCATION != null) {
            return TEXTURE_LOCATION;
		}
		return DefaultPlayerSkin.getDefaultSkinLegacy();
	}

    public SkinType getSkintype() {
		if (skintype != null) {
			return skintype;
		}
		return SkinType.ALEX;
	}

    public PlayerEntity getPlayer() {
        return PLAYER;
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
