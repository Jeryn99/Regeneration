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
public class DnaLucky implements DnaHandler.IDna {
	
	private final UUID LUCKY_ID = UUID.fromString("9aaf3f7c-264e-4c19-8485-49503b6940b7");
	private final AttributeModifier LUCKY_MODIFIER = new AttributeModifier(LUCKY_ID, "LUCK", 0.95, 2);
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "lucky");
	
	@Override
	public void onUpdate(IRegeneration cap) {
		
	}
	
	@Override
	public void onAdded(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (!player.getAttribute(SharedMonsterAttributes.LUCK).hasModifier(LUCKY_MODIFIER)) {
			player.getAttribute(SharedMonsterAttributes.LUCK).applyModifier(LUCKY_MODIFIER);
		}
	}
	
	@Override
	public void onRemoved(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (player.getAttribute(SharedMonsterAttributes.LUCK).hasModifier(LUCKY_MODIFIER)) {
			player.getAttribute(SharedMonsterAttributes.LUCK).removeModifier(LUCKY_MODIFIER);
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
