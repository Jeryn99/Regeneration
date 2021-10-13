package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.entities.Laser;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RSounds;
import me.suff.mc.regen.util.RegenSources;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/* Created by Craig on 01/03/2021 */
public class GunItem extends Item {

    private final int cooldown;
    private final float damage;

    public GunItem(int shotsPerRound, int cooldown, float damage) {
        super(new Item.Properties().tab(RItems.MAIN).durability(shotsPerRound).setNoRepair());
        this.cooldown = cooldown;
        this.damage = damage;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {

        if (entityLiving instanceof Player playerIn) {
            boolean isPistol = this == RItems.PISTOL.get();
            if (hasAmmo(entityLiving, stack) && stack.getDamageValue() < stack.getMaxDamage() && !playerIn.getCooldowns().isOnCooldown(this)) {
                worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), isPistol ? RSounds.RIFLE.get() : RSounds.STASER.get(), SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
                playerIn.getCooldowns().addCooldown(this, cooldown);
                if (worldIn.random.nextInt(40) < 10 && !playerIn.isCreative()) {
                    setDamage(stack, getDamage(stack) + 1);
                }
                if (!worldIn.isClientSide) {
                    Laser laserProjectile = new Laser(REntities.LASER.get(), playerIn, worldIn);
                    laserProjectile.setDamage(damage);
                    laserProjectile.setDamageSource(isPistol ? RegenSources.REGEN_DMG_STASER : RegenSources.REGEN_DMG_RIFLE);
                    laserProjectile.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.5F, 1.0F);
                    entityLiving.playSound(isPistol ? RSounds.STASER.get() : RSounds.RIFLE.get(), 1.0F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
                    worldIn.addFreshEntity(laserProjectile);
                }
            }
        }
    }

    private boolean hasAmmo(LivingEntity entityLiving, ItemStack gun) {
        if (entityLiving instanceof Player player) {
            if (player.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, gun) > 0) {
                return true;
            }
            for (ItemStack itemStack : player.getInventory().items) {
                if (itemStack.getItem() == RItems.PLASMA_CARTRIDGE.get()) {
                    itemStack.shrink(1);
                    return true;
                }
            }
            player.getCooldowns().addCooldown(this, 10);
            player.playSound(RSounds.GUN_EMPTY.get(), 1, 1);

        }
        return false;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand interactionHand) {
        player.startUsingItem(interactionHand);
        return super.use(worldIn, player, interactionHand);
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.NONE;
    }


    @Override
    public boolean isValidRepairItem(ItemStack p_82789_1_, ItemStack p_82789_2_) {
        return p_82789_2_.getItem() == RItems.ZINC.get();
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return false;
    }
}