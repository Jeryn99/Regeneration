package me.suff.mc.regen.util;

import me.suff.mc.regen.common.advancement.TriggerManager;
import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.ModelMessage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerUtil {


    public static ArrayList<Effect> POTIONS = new ArrayList();

    public static void setupPotions() {
        if (!RegenConfig.COMMON.postRegenEffects.get().isEmpty()) {
            for (String name : RegenConfig.COMMON.postRegenEffects.get()) {
                for (Effect effect : ForgeRegistries.POTIONS.getValues()) {
                    if (name.contentEquals(effect.getRegistryName().toString())) {
                        POTIONS.add(effect);
                    }
                }
            }
        }
    }

    public static boolean isPlayerAboveZeroGrid(LivingEntity playerEntity) {
        BlockPos livingPos = playerEntity.blockPosition().below();
        AxisAlignedBB grid = new AxisAlignedBB(livingPos.north().west(), livingPos.south().east());
        for (Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(new BlockPos(grid.maxX, grid.maxY, grid.maxZ), new BlockPos(grid.minX, grid.minY, grid.minZ)).iterator(); iterator.hasNext(); ) {
            BlockPos pos = iterator.next();
            BlockState state = playerEntity.level.getBlockState(pos);
            if (state.getBlock() != RBlocks.ZERO_ROOM_FULL.get() && state.getBlock() != RBlocks.ZERO_ROUNDEL.get()) {
                return false;
            }
        }
        if (playerEntity instanceof ServerPlayerEntity) {
            TriggerManager.ZERO_ROOM.trigger((ServerPlayerEntity) playerEntity);
        }
        return true;
    }

    public static void handleZeroGrid(LivingEntity playerEntity) {
        for (Effect effect : PlayerUtil.POTIONS) {
            if (playerEntity.hasEffect(effect)) {
                playerEntity.removeEffect(effect);
            }
        }
    }


    public static void sendMessage(LivingEntity livingEntity, String message, boolean hotBar) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) livingEntity;
        if (!player.level.isClientSide) {
            player.displayClientMessage(new TranslationTextComponent(message), hotBar);
        }
    }

    public static void sendMessage(LivingEntity livingEntity, IFormattableTextComponent translation, boolean hotBar) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) livingEntity;
        if (!player.level.isClientSide) {
            player.displayClientMessage(translation, hotBar);
        }
    }

    public static void sendMessageToAll(TranslationTextComponent translation) {
        List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        players.forEach(playerMP -> sendMessage(playerMP, translation, false));
    }

    public static void applyPotionIfAbsent(LivingEntity player, Effect potion, int length, int amplifier, boolean ambient, boolean showParticles) {
        if (potion == null) return;
        if (player.getEffect(potion) == null) {
            player.addEffect(new EffectInstance(potion, length, amplifier, ambient, showParticles));
        }
    }

    public static AxisAlignedBB getReach(BlockPos pos, int range) {
        return new AxisAlignedBB(pos.above(range).north(range).west(range), pos.below(range).south(range).east(range));
    }

    public static void explodeKnockback(Entity exploder, World world, BlockPos pos, double knockback, int range) {
        world.getEntities(exploder, getReach(pos, range)).forEach(entity -> {
            if (entity instanceof LivingEntity && exploder.isAlive()) {
                LivingEntity victim = (LivingEntity) entity;

                if (entity instanceof PlayerEntity && !RegenConfig.COMMON.regenerationKnocksbackPlayers.get() || !victim.canChangeDimensions())
                    return;

                float densMod = Explosion.getSeenPercent(entity.position(), entity);

                int xr, zr;
                xr = (int) -(victim.getX() - exploder.getX());
                zr = (int) -(victim.getZ() - exploder.getZ());
                victim.setDeltaMovement(victim.getDeltaMovement().multiply((knockback * densMod), xr, zr));
            }
        });
    }

    public static void regenerationExplosion(LivingEntity player) {
        explodeKnockback(player, player.level, new BlockPos(player.position()), RegenConfig.COMMON.regenerativeKnockback.get(), RegenConfig.COMMON.regenKnockbackRange.get());
        explodeKill(player, player.level, new BlockPos(player.position()), RegenConfig.COMMON.regenerativeKillRange.get());
    }

    public static void explodeKill(Entity exploder, World world, BlockPos pos, int range) {
        world.getEntities(exploder, getReach(pos, range)).forEach(entity -> {
            if ((entity instanceof CreatureEntity && entity.canChangeDimensions()) || (entity instanceof PlayerEntity)) // && RegenConfig.COMMON.regenerationKillsPlayers))
                entity.hurt(RegenSources.REGEN_DMG_ENERGY_EXPLOSION, 3.5F);
        });
    }

    public static void updateModel(SkinType choices) {
        NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ModelMessage(choices));
    }

    public static boolean isInHand(Hand hand, LivingEntity holder, Item item) {
        ItemStack heldItem = holder.getItemInHand(hand);
        return heldItem.getItem() == item;
    }

    public static boolean isInMainHand(LivingEntity holder, Item item) {
        return isInHand(Hand.MAIN_HAND, holder, item);
    }

    /**
     * Checks if player has item in offhand
     */
    public static boolean isInOffHand(LivingEntity holder, Item item) {
        return isInHand(Hand.OFF_HAND, holder, item);
    }

    /**
     * Checks if player has item in either hand
     */
    public static boolean isInEitherHand(LivingEntity holder, Item item) {
        return isInMainHand(holder, item) || isInOffHand(holder, item);
    }

    // MAIN_HAND xor OFF_HAND
    public static boolean isInOneHand(LivingEntity holder, Item item) {
        boolean mainHand = (isInMainHand(holder, item) && !isInOffHand(holder, item));
        boolean offHand = (isInOffHand(holder, item) && !isInMainHand(holder, item));
        return mainHand || offHand;
    }

    public enum SkinType implements RegenUtil.IEnum<SkinType> {
        ALEX, STEVE, EITHER;

        public boolean isAlex() {

            if (this == EITHER) {
                return RegenUtil.RAND.nextBoolean();
            }
            return this == ALEX;
        }
    }

}
