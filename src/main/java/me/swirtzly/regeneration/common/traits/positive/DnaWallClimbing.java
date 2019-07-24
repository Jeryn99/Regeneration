package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import net.minecraft.entity.player.PlayerEntity;

public class DnaWallClimbing extends DnaHandler.IDna {

    public DnaWallClimbing() {
        super("wallclimbing");
    }

    @Override
    public void onUpdate(IRegeneration cap) {
        PlayerEntity player = cap.getPlayer();
		if (player.collidedHorizontally && player.moveForward > 0) {
            player.motionY = 0.2D;
        }
    }

    @Override
    public void onAdded(IRegeneration cap) {

    }

    @Override
    public void onRemoved(IRegeneration cap) {

    }

}
