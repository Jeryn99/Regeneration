package me.swirtzly.regeneration.client.skinhandling;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class SkinInfo {

	private final SkinType SKIN_TYPE;
    private final ResourceLocation TEXTURE_LOCATION;
    private final PlayerEntity PLAYER;

    public SkinInfo(PlayerEntity playerEntity, ResourceLocation resourceLocation, SkinType skinType) {
		this.SKIN_TYPE = skinType;
        this.TEXTURE_LOCATION = resourceLocation;
        this.PLAYER = playerEntity;
    }


    public ResourceLocation getTextureLocation() {
        return TEXTURE_LOCATION;
	}

    public SkinType getSkintype() {
		if (SKIN_TYPE != null) {
			return SKIN_TYPE;
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
