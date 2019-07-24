package me.swirtzly.regeneration.common.tiles;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityHandInJar extends TileEntity implements ITickable {
	
	public boolean isInUse = false;
	public int lindosAmont = 0;
	private AxisAlignedBB AABB = new AxisAlignedBB(0.2, 0, 0, 0.8, 2, 0.1);

	public TileEntityHandInJar(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

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
	public void tick() {
		
		if (world != null) {

			if (world.getGameTime() % 35 == 0 && getLindosAmont() > 0) {
				world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), RegenObjects.Sounds.JAR_BUBBLES, SoundCategory.PLAYERS, 1.0F, 0.3F);
			}

			isInUse = false;
			List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, AABB.grow(12, 12, 12));
			for (PlayerEntity player : players) {
				CapabilityRegeneration.getForPlayer(player).ifPresent((data) -> {
					if (!isInUse && data.getState() == PlayerUtil.RegenState.REGENERATING && player.world.getGameTime() % 30 == 0) {
						lindosAmont = lindosAmont + 1;
						isInUse = true;
					}
				});
			}
		}
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("inUse", isInUse);
		nbt.putInt("lindos", lindosAmont);
		return null;
	}

	@Override
	public void deserializeNBT(CompoundNBT compound) {
		isInUse = compound.getBoolean("inUse");
		lindosAmont = compound.getInt("lindos");
		super.deserializeNBT(compound);
	}


	
	
}
