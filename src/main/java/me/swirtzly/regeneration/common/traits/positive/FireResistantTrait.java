package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.traits.TraitManager;
import net.minecraft.entity.player.PlayerEntity;

public class FireResistantTrait extends TraitManager.IDna {

    public FireResistantTrait() {
        super("fire");
    }

	@Override
	public void onUpdate(IRegeneration cap) {
		PlayerEntity player = cap.getPlayer();
		if (player.isBurning() && cap.isDnaActive()) {
			player.extinguish();
		}
	}
	
	@Override
	public void onAdded(IRegeneration cap) {
	
	}
	
	@Override
	public void onRemoved(IRegeneration cap) {
	
	}

}
