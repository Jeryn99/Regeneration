package me.swirtzly.regen.common.item;

import me.swirtzly.regen.common.entities.OverrideEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
public class SolidItem extends Item {

    public SolidItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        OverrideEntity entity = new OverrideEntity(world, location.getPosX(), location.getPosY(), location.getPosZ(), itemstack);
        entity.setMotion(location.getMotion());
        if (location instanceof ItemEntity) {
            entity.setOwnerId(((ItemEntity) location).getOwnerId());
            entity.setThrowerId(((ItemEntity) location).getThrowerId());
        }
        return entity;
    }

    @Nullable
    public EntitySize getEntitySize(OverrideEntity entity, ItemStack stack) {
        return null;
    }

    public boolean onSolidEntityItemUpdate(OverrideEntity entity) {
        return false;
    }

}
