package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.entities.LaserProjectile;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.util.RegenSources;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/* Created by Craig on 01/03/2021 */
public class GunItem extends Item {

    private final int cooldown;
    private final float damage;

    public GunItem(int shotsPerRound, int cooldown, float damage) {
        super(new Properties().tab(RItems.MAIN).durability(shotsPerRound).setNoRepair());
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
            boolean isPistol = this == RItems.PISTOL.get();
            if (hasAmmo(entityLiving, stack) && stack.getDamageValue() < stack.getMaxDamage() && !playerIn.getCooldowns().isOnCooldown(this)) {
                worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), isPistol ? RSounds.RIFLE.get() : RSounds.STASER.get(), SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
                playerIn.getCooldowns().addCooldown(this, cooldown);
                if (worldIn.random.nextInt(40) == 0 && !playerIn.isCreative()) {
                    setDamage(stack, getDamage(stack) + 1);
                }
                if (!worldIn.isClientSide) {
                    LaserProjectile laserProjectile = new LaserProjectile(REntities.LASER.get(), playerIn, worldIn);
                    laserProjectile.setDamage(damage);
                    laserProjectile.setDamageSource(isPistol ? RegenSources.REGEN_DMG_STASER : RegenSources.REGEN_DMG_RIFLE);
                    laserProjectile.shootFromRotation(playerIn, playerIn.xRot, playerIn.yRot, 0.0F, 1.5F, 1.0F);
                    entityLiving.playSound(isPistol ? RSounds.STASER.get() : RSounds.RIFLE.get(), 1.0F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
                    worldIn.addFreshEntity(laserProjectile);
                }
            }
        }
    }

    private boolean hasAmmo(LivingEntity entityLiving, ItemStack gun) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            if (playerEntity.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, gun) > 0) {
                return true;
            }
            for (ItemStack itemStack : playerEntity.inventory.items) {
                if (itemStack.getItem() == RItems.PLASMA_CARTRIDGE.get()) {
                    itemStack.shrink(1);
                    return true;
                }
            }
            playerEntity.getCooldowns().addCooldown(this, 10);
            playerEntity.playSound(RSounds.GUN_EMPTY.get(), 1,1);

        }
        return false;
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.startUsingItem(handIn);
        return super.use(worldIn, playerIn, handIn);
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return false;
    }
}
