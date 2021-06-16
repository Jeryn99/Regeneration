package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.entity.LaserEntity;
import me.suff.mc.regen.common.entity.OverrideEntity;
import me.suff.mc.regen.handlers.RegenObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Craig
 * on 07/05/2020 @ 12:50
 */
public class GunItem extends SolidItem {

    private final int cooldown;
    private final float damage;

    public GunItem(int shotsPerRound, int cooldown, float damage) {
        super(new Properties().tab(ItemGroups.REGEN_TAB).durability(shotsPerRound).setNoRepair());
        this.cooldown = cooldown;
        this.damage = damage;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        return super.useOn(context);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {

        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerIn = (PlayerEntity) entityLiving;
            if (stack.getDamageValue() < stack.getMaxDamage() && !playerIn.getCooldowns().isOnCooldown(this)) {
                worldIn.playSound(null, playerIn.x, playerIn.y, playerIn.z, this == RegenObjects.Items.PISTOL.get() ? RegenObjects.Sounds.STASER.get() : RegenObjects.Sounds.RIFLE.get(), SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                playerIn.getCooldowns().addCooldown(this, cooldown);
                setDamage(stack, getDamage(stack) + 1);
                if (!worldIn.isClientSide) {
                    LaserEntity entity = new LaserEntity(RegenObjects.EntityEntries.LASER.get(), playerIn, playerIn.level);
                    entity.setColor(new Vec3d(1, 0, 0));
                    entity.setDamage(damage);
                    entity.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 1.5F, 1.0F);
                    worldIn.addFreshEntity(entity);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.startUsingItem(handIn);
        return super.use(worldIn, playerIn, handIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn.tickCount % 100 == 0) {
            if (getDamage(stack) > 0) {
                setDamage(stack, getDamage(stack) - 1);
            }
        }
        stack.getOrCreateTag().putBoolean("live", true);
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
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
