package me.fril.regeneration.common;

import me.fril.regeneration.handlers.RegenObjects;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class EntityFobWatch extends EntityItem {
	public EntityFobWatch(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public EntityFobWatch(World worldIn, double x, double y, double z, ItemStack stack) {
		super(worldIn, x, y, z, stack);
	}

	public EntityFobWatch(World worldIn) {
		super(worldIn);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.ticksExisted % 100 == 0 || ticksExisted == 2) {
			world.playSound(null, getPosition(), RegenObjects.Sounds.FOB_WATCH, SoundCategory.AMBIENT, 0.3F, 1);
		}
	}
}
