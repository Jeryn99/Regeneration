package me.suff.mc.regen.common.traits.positive;

import me.suff.mc.regen.common.capability.IRegeneration;
import me.suff.mc.regen.common.traits.DnaHandler;
import net.minecraft.entity.player.EntityPlayer;

public class DnaWallClimbing extends DnaHandler.IDna {

    public DnaWallClimbing() {
        super("wallclimbing");
    }

    @Override
    public void onUpdate(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
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
