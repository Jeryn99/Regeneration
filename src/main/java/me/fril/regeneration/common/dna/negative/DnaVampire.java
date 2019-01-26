package me.fril.regeneration.common.dna.negative;

import java.util.UUID;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.common.dna.DnaHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
public class DnaVampire implements DnaHandler.IDna {
	
	private final UUID SPEED_ID = UUID.fromString("a22a9515-90d7-479d-9153-07268f2a1714");
	private final AttributeModifier SPEED_MODIFIER = new AttributeModifier(SPEED_ID, "SANIC_FAST", 0.95, 1);
	private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "vampire");
	
	@Override
	public void onUpdate(IRegeneration cap) {
		World world = cap.getPlayer().world;
		EntityPlayer player = cap.getPlayer();
		if (cap.dnaAlive()) {
			if (player.world.canSeeSky(new BlockPos(player.posX, player.posY + player.getEyeHeight(), player.posZ)) && cap.getPlayer().world.isDaytime()) {
				cap.getPlayer().setFire(1);
			}
		}
		
		if (world.isDaytime()) {
			onRemoved(cap);
		} else {
			onAdded(cap);
		}
		
	}
	
	@Override
	public void onAdded(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (!player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SPEED_MODIFIER)) {
			player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(SPEED_MODIFIER);
		}
	}
	
	@Override
	public void onRemoved(IRegeneration cap) {
		EntityPlayer player = cap.getPlayer();
		if (player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SPEED_MODIFIER)) {
			player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER);
		}
	}
	
	@Override
	public String getLangKey() {
		return "dna." + getRegistryName().getPath() + ".name";
	}
	
	@Override
	public ResourceLocation getRegistryName() {
		return ID;
	}
}
