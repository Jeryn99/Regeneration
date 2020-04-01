package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.common.entity.OverrideEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class OverrideItem extends Item implements OverrideEntity.IEntityOverride {

    public OverrideItem(Properties p_i48487_1_) {
		super(p_i48487_1_);
	}

    @Override
	public void update(OverrideEntity itemOverride) {

    }
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

    @Nullable
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		OverrideEntity item = new OverrideEntity(world, location.posX, location.posY, location.posZ, itemstack);
		item.setEntitySize(item.getHeight(), item.getWidth());
		item.setMotion(location.getMotion());
		return item;
	}

    @Override
	public boolean shouldDie(ItemStack stack) {
		if (stack.getTag() != null) {
			return !stack.getTag().contains("live");
		}
		return true;
	}

}
