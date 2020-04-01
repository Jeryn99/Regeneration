package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.traits.TraitManager;
import net.minecraft.entity.player.PlayerEntity;

public class FireResistantTrait extends TraitManager.IDna {

    public FireResistantTrait() {
        super("fire");
    }
	
	@Override
    public void onUpdate(IRegen cap) {
		PlayerEntity player = cap.getPlayer();
		if (player.isBurning() && cap.isDnaActive()) {
			player.extinguish();
		}
	}
	
	@Override
    public void onAdded(IRegen cap) {
		
	}
	
	@Override
    public void onRemoved(IRegen cap) {

    }
	
}
