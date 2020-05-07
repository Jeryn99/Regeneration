package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.common.entity.LaserEntity;
import me.swirtzly.regeneration.common.entity.OverrideEntity;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Swirtzly
 * on 07/05/2020 @ 12:50
 */
public class GunItem extends SolidItem {

    public GunItem(int shotsPerRound) {
        super(new Properties().group(ItemGroups.REGEN_TAB).maxDamage(shotsPerRound));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return super.onItemUse(context);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        setDamage(stack, getDamage(stack) + 1);

        if (getDamage(stack) > 0 && !playerIn.getCooldownTracker().hasCooldown(this)) {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            playerIn.getCooldownTracker().setCooldown(this, 10);

            if (!worldIn.isRemote) {
                LaserEntity entity = new LaserEntity(RegenObjects.EntityEntries.LASER.get(), playerIn, playerIn.world);
                entity.setColor(new Vec3d(1, 0, 0));
                entity.setDamage(5);
                entity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
                worldIn.addEntity(entity);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn.ticksExisted % 100 == 0) {
            if (getDamage(stack) > 0) {
                setDamage(stack, getDamage(stack) - 1);
            }
        }
    }


    @Override
    public boolean onSolidEntityItemUpdate(OverrideEntity entity) {
        return super.onSolidEntityItemUpdate(entity);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        return false;
    }
}
