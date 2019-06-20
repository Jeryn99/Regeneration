package me.swirtzly.regeneration.common.tiles;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.util.RegenState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileEntityHandInJar extends TileEntity implements ITickable {
	
	public boolean isInUse = false;
	public int lindosAmont = 0;
	
	
	public int getLindosAmont() {
		return lindosAmont;
	}
	
	public void setLindosAmont(int lindosAmont) {
		this.lindosAmont = lindosAmont;
	}
	
	public boolean isInUse() {
		return isInUse;
	}
	
	public void setInUse(boolean inUse) {
		isInUse = inUse;
	}
	
	@Override
	public void update() {
		
		if (world != null) {
			List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, getRenderBoundingBox().expand(12, 12, 12));
			for (EntityPlayer player : players) {
				
				IRegeneration data = CapabilityRegeneration.getForPlayer(player);
				
				if (!isInUse && data.getState() == RegenState.REGENERATING) {
					lindosAmont += 1;
					isInUse = true;
				}
				
			}
			
			isInUse = false;
		}
	}
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("inUse", isInUse);
		compound.setFloat("lindos", lindosAmont);
		return super.writeToNBT(compound);
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		isInUse = compound.getBoolean("inUse");
		lindosAmont = compound.getInteger("lindos");
		super.readFromNBT(compound);
	}
	
	
}
