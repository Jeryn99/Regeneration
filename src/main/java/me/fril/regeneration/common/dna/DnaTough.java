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
public class DnaTough implements DnaHandler.IDna {
	
	private ResourceLocation LOCATION = new ResourceLocation(RegenerationMod.MODID, "tough");
	
	private final UUID TOUGH_ID = UUID.fromString("b57c85ba-e5c5-4361-a2cf-3c2fb7347f16");
	private final AttributeModifier TOUGH_MODIFIER = new AttributeModifier(TOUGH_ID, "TOUGH", 0.95, 1);
	
	
	@Override
	public void onUpdate(IRegeneration cap) {
		
	}
	
	@Override
	public void onAdded(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (!player.getEntityAttribute(SharedMonsterAttributes.ARMOR).hasModifier(TOUGH_MODIFIER)) {
			player.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(TOUGH_MODIFIER);
		}
	}
	
	@Override
	public void onRemoved(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (player.getEntityAttribute(SharedMonsterAttributes.ARMOR).hasModifier(TOUGH_MODIFIER)) {
			player.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(TOUGH_MODIFIER);
		}
	}
	
	@Override
	public String getLangKey() {
		return "dna."+LOCATION.getPath()+".name";
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return LOCATION;
	}
}
