package me.suff.regeneration.common.dna.positive;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.dna.DnaHandler;
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
	
	private final UUID SPEED_ID = UUID.fromString("a22a9515-90d7-479d-9153-07268f2a1714");
	private final AttributeModifier SPEED_MODIFIER = new AttributeModifier(SPEED_ID, "SANIC_FAST", 0.95, 1);
	private final UUID KNOCKBACK_ID = UUID.fromString("49906f69-7b9d-4967-aba8-901621ee76a5");
	private final AttributeModifier KNOCKBACK_MODIFIER = new AttributeModifier(KNOCKBACK_ID, "JUMPY", 0.95, 1);
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "athlete");
	
	@Override
	public void onUpdate(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (player.isSprinting()) {
			onAdded(cap);
		} else {
			onRemoved(cap);
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
		return "dna." + ID.getPath() + ".name";
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return ID;
	}
}
