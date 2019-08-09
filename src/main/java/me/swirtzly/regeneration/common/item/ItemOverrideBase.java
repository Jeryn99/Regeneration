package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.common.entity.EntityItemOverride;
import me.swirtzly.regeneration.common.entity.IEntityOverride;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemOverrideBase extends Item implements IEntityOverride {
    @Override
    public void update(EntityItemOverride itemOverride) {

    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        EntityItemOverride item = new EntityItemOverride(world, location.posX, location.posY, location.posZ, itemstack);
        item.setEntitySize(item.getHeight(), item.getWidth());
        item.motionX = location.motionX;
        item.motionY = location.motionY;
        item.motionZ = location.motionZ;
        return item;
    }

    @Override
    public boolean shouldDie(ItemStack stack) {
        if (stack.getTagCompound() != null) {
            return !stack.getTagCompound().hasKey("live");
        }
        return true;
    }


}
