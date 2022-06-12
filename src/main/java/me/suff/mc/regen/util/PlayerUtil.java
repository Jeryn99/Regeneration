package me.suff.mc.regen.util;

import me.suff.mc.regen.common.advancement.TriggerManager;
import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.network.NetworkDispatcher;
import me.suff.mc.regen.network.messages.ModelMessage;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;

public class PlayerUtil {


    public static ArrayList<MobEffect> POTIONS = new ArrayList<>();

    public static void setupPotions() {
        if (!RegenConfig.COMMON.postRegenEffects.get().isEmpty()) {
            for (String name : RegenConfig.COMMON.postRegenEffects.get()) {
                for (MobEffect effect : ForgeRegistries.MOB_EFFECTS.getValues()) {
                    if (name.contentEquals(ForgeRegistries.MOB_EFFECTS.getKey(effect).toString())) {
                        POTIONS.add(effect);
                    }
                }
            }
        }
    }

    public static boolean isPlayerAboveZeroGrid(LivingEntity playerEntity) {
        BlockPos livingPos = playerEntity.blockPosition().below();
        AABB grid = new AABB(livingPos.north().west(), livingPos.south().east());
        for (Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(new BlockPos(grid.maxX, grid.maxY, grid.maxZ), new BlockPos(grid.minX, grid.minY, grid.minZ)).iterator(); iterator.hasNext(); ) {
            BlockPos pos = iterator.next();
            BlockState state = playerEntity.level.getBlockState(pos);
            if (state.getBlock() != RBlocks.ZERO_ROOM_FULL.get() && state.getBlock() != RBlocks.ZERO_ROUNDEL.get()) {
                return false;
            }
        }
        if (playerEntity instanceof ServerPlayer serverPlayer) {
            TriggerManager.ZERO_ROOM.trigger(serverPlayer);
        }
        return true;
    }

    public static void handleZeroGrid(LivingEntity playerEntity) {
        for (MobEffect effect : PlayerUtil.POTIONS) {
            if (playerEntity.hasEffect(effect)) {
                playerEntity.removeEffect(effect);
            }
        }
    }

    public static void globalChat(Component body, MinecraftServer server) {
        if (server == null) return;
        //TODO server.getPlayerList().broadcast(body, ChatType.SYSTEM, Util.NIL_UUID);
    }

    public static void sendMessage(LivingEntity livingEntity, String message, boolean hotBar) {
        if (!(livingEntity instanceof Player player)) return;
        if (!player.level.isClientSide) {
            player.displayClientMessage(Component.translatable(message), hotBar);
        }
    }

    public static void sendMessage(LivingEntity livingEntity, MutableComponent translation, boolean hotBar) {
        if (!(livingEntity instanceof Player player)) return;
        if (!player.level.isClientSide) {
            player.displayClientMessage(translation, hotBar);
        }
    }

    public static void applyPotionIfAbsent(LivingEntity player, MobEffect potion, int length, int amplifier, boolean ambient, boolean showParticles) {
        if (potion == null) return;
        if (player.getEffect(potion) == null) {
            player.addEffect(new MobEffectInstance(potion, length, amplifier, ambient, showParticles));
        }
    }

    public static AABB getReach(BlockPos pos, int range) {
        return new AABB(pos.above(range).north(range).west(range), pos.below(range).south(range).east(range));
    }

    public static void explodeKnockback(Entity exploder, Level world, BlockPos pos, double knockback, int range) {
        world.getEntities(exploder, getReach(pos, range)).forEach(entity -> {
            if (entity instanceof LivingEntity && exploder.isAlive()) {
                LivingEntity victim = (LivingEntity) entity;

                if (entity instanceof Player && !RegenConfig.COMMON.regenerationKnocksbackPlayers.get() || !victim.canChangeDimensions())
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

    public static void explodeKill(Entity exploder, Level world, BlockPos pos, int range) {
        world.getEntities(exploder, getReach(pos, range)).forEach(entity -> {
            if ((entity instanceof PathfinderMob && entity.canChangeDimensions()) || (entity instanceof Player)) // && RegenConfig.COMMON.regenerationKillsPlayers))
                entity.hurt(RegenSources.REGEN_DMG_ENERGY_EXPLOSION, 3.5F);
        });
    }

    public static void updateModel(SkinType choices) {
        NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ModelMessage(choices));
    }

    public static boolean isInHand(InteractionHand hand, LivingEntity holder, Item item) {
        ItemStack heldItem = holder.getItemInHand(hand);
        return heldItem.getItem() == item;
    }

    public static boolean isInMainHand(LivingEntity holder, Item item) {
        return isInHand(InteractionHand.MAIN_HAND, holder, item);
    }

    /**
     * Checks if player has item in offhand
     */
    public static boolean isInOffHand(LivingEntity holder, Item item) {
        return isInHand(InteractionHand.OFF_HAND, holder, item);
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
