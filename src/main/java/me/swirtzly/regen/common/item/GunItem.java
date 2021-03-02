package me.swirtzly.regen.common.item;

import me.swirtzly.regen.common.entities.LaserProjectile;
import me.swirtzly.regen.common.objects.REntities;
import me.swirtzly.regen.common.objects.RItems;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.util.RegenSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;


/* Created by Craig on 01/03/2021 */
public class GunItem extends Item {

    private final int cooldown;
    private final float damage;

    public GunItem(int shotsPerRound, int cooldown, float damage) {
        super(new Properties().group(RItems.MAIN).maxDamage(shotsPerRound).setNoRepair());
        this.cooldown = cooldown;
        this.damage = damage;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return super.onItemUse(context);
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
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {

        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerIn = (PlayerEntity) entityLiving;
            boolean isPistol = this == RItems.PISTOL.get();
            if (stack.getDamage() < stack.getMaxDamage() && !playerIn.getCooldownTracker().hasCooldown(this)) {
                worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), isPistol ? RSounds.RIFLE.get() : RSounds.STASER.get(), SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                playerIn.getCooldownTracker().setCooldown(this, cooldown);
                setDamage(stack, getDamage(stack) + 1);
                if (!worldIn.isRemote) {
                    LaserProjectile laserProjectile = new LaserProjectile(REntities.LASER.get(), playerIn, worldIn);
                    laserProjectile.setDamage(damage);
                    laserProjectile.setDamageSource(isPistol ? RegenSources.REGEN_DMG_STASER : RegenSources.REGEN_DMG_RIFLE);
                    laserProjectile.func_234612_a_(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
                    entityLiving.playSound(isPistol ? RSounds.STASER.get() : RSounds.RIFLE.get(), 1.0F, 0.4F / (worldIn.rand.nextFloat() * 0.4F + 0.8F));
                    worldIn.addEntity(laserProjectile);
                }
            }
        }
    }

    @Override
    public ActionResult< ItemStack > onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setActiveHand(handIn);
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

        if(entityIn instanceof PlayerEntity) {
         PlayerEntity playerEntity = (PlayerEntity) entityIn;
            if (getDamage(stack) == getMaxDamage(stack)) {
                playerEntity.getCooldownTracker().setCooldown(this, 100 * getMaxDamage(stack));
            }
        }
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }
}
