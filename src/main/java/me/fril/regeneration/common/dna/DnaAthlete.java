package me.fril.regeneration.common.dna;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
public class DnaAthlete implements DnaHandler.IDna {
	
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "athlete");
	
	private final UUID SPEED_ID = UUID.fromString("a22a9515-90d7-479d-9153-07268f2a1714");
	private final AttributeModifier SPEED_MODIFIER = new AttributeModifier(SPEED_ID, "SANIC_FAST", 0.95, 1);
	
	private final UUID KNOCKBACK_ID = UUID.fromString("49906f69-7b9d-4967-aba8-901621ee76a5");
	private final AttributeModifier KNOCKBACK_MODIFIER = new AttributeModifier(KNOCKBACK_ID, "JUMPY", 0.95, 1);
	
	
	@Override
	public void onUpdate(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if(player.isJumping) {
			player.motionY += 0.1D;
			player.velocityChanged = true;
		}
	}
	
	@Override
	public void onAdded(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (!player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SPEED_MODIFIER)) {
			player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(SPEED_MODIFIER);
		}
		
		if (!player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).hasModifier(KNOCKBACK_MODIFIER)) {
			player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(KNOCKBACK_MODIFIER);
		}
	}
	
	@Override
	public void onRemoved(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SPEED_MODIFIER)) {
			player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER);
		}
		
		if (player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).hasModifier(KNOCKBACK_MODIFIER)) {
			player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).removeModifier(KNOCKBACK_MODIFIER);
		}
	}
	
	@Override
	public String getLangKey() {
		return "dna."+ID.getPath()+".name";
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return ID;
	}
}
