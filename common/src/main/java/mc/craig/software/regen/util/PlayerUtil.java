package mc.craig.software.regen.util;

import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.network.messages.ModelMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
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

import static net.minecraft.core.BlockPos.betweenClosedStream;

public class PlayerUtil {

    /**
     * Checks if the given player is currently inside a Zero Room.
     *
     * @param playerEntity the player to check
     * @return true if the player is above a Zero Room, false otherwise
     */
    public static boolean isPlayerAboveZeroGrid(LivingEntity playerEntity) {
        // Get the block position below the player
        BlockPos livingPos = playerEntity.blockPosition().below();

        // Create an axis-aligned bounding box that covers the area below the player
        AABB grid = new AABB(livingPos.north().west(), livingPos.south().east());

        // Iterate over all blocks in the grid
        for (BlockPos pos : betweenClosedStream(new BlockPos((int) grid.maxX, (int) grid.maxY, (int) grid.maxZ), new BlockPos((int) grid.minX, (int) grid.minY, (int) grid.minZ)).toList()) {

            // Check if the block at this position is not a Zero Room block
            BlockState state = playerEntity.level().getBlockState(pos);
            if (state.getBlock() != RBlocks.ZERO_ROOM_FULL.get() &&
                    state.getBlock() != RBlocks.ZERO_ROUNDEL.get()) {

                // If the block is not a Zero Room block, return false
                return false;
            }
        }

        // If all blocks in the grid are Zero Room blocks, trigger the Zero Room trigger
        // and return true
        if (playerEntity instanceof ServerPlayer serverPlayer) {
            TriggerManager.ZERO_ROOM.trigger(serverPlayer);
        }
        return true;
    }

    /**
     * Handles the effects of a Zero Room on the given player.
     *
     * @param playerEntity the player to handle the effects for
     */
    public static void handleZeroGrid(LivingEntity playerEntity) {
        // Get the set of mob effects that are tagged with the POST_REGEN_POTIONS tag
        HolderSet.Named<MobEffect> mobEffects = BuiltInRegistries.MOB_EFFECT.getTag(RegenUtil.POST_REGEN_POTIONS).get();

        // Iterate over all mob effects in the set
        for (Holder<MobEffect> mobEffect : mobEffects) {
            // Check if the player has the current mob effect
            MobEffect effect = mobEffect.value();
            if (playerEntity.hasEffect(effect)) {
                // If the player has the mob effect, remove it
                playerEntity.removeEffect(effect);
            }
        }
    }

    /**
     * Sends the given message to all players on the given server.
     *
     * @param body   the message to send
     * @param server the server to send the message to
     */
    public static void globalMessage(Component body, MinecraftServer server) {
        // Return early if the server is null
        if (server == null) {
            return;
        }

        // Get the list of players on the server
        PlayerList playerList = server.getPlayerList();

        // Iterate over all players on the server
        for (ServerPlayer player : playerList.getPlayers()) {
            // Send the message to the player
            player.sendSystemMessage(body);
        }
    }

    public static void sendMessage(LivingEntity livingEntity, String message, boolean hotBar) {
        if (!(livingEntity instanceof Player player)) return;
        if (!player.level().isClientSide) {
            player.displayClientMessage(Component.translatable(message), hotBar);
        }
    }

    public static void sendMessage(LivingEntity livingEntity, MutableComponent translation, boolean hotBar) {
        if (!(livingEntity instanceof Player player)) return;
        if (!player.level().isClientSide) {
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
            if (entity instanceof LivingEntity victim && exploder.isAlive()) {

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
        explodeKnockback(player, player.level(), player.blockPosition(), RegenConfig.COMMON.regenerativeKnockback.get(), RegenConfig.COMMON.regenKnockbackRange.get());
        explodeKill(player, player.level(), player.blockPosition(), RegenConfig.COMMON.regenerativeKillRange.get());
    }

    public static void explodeKill(Entity exploder, Level world, BlockPos pos, int range) {
        world.getEntities(exploder, getReach(pos, range)).forEach(entity -> {
            if ((entity instanceof PathfinderMob && entity.canChangeDimensions()) || (entity instanceof Player)) // && RegenConfig.COMMON.regenerationKillsPlayers))
                entity.hurt(new DamageSource(RegenDamageTypes.getHolder(world, RegenDamageTypes.REGEN_DMG_ENERGY_EXPLOSION)), 3.5F);
        });
    }

    /**
     * Updates the model for the given SkinType.
     *
     * @param choices the SkinType to update the model for
     */
    public static void updateModel(SkinType choices) {
        // Create a new ModelMessage for the given SkinType and send it
        new ModelMessage(choices).send();
    }

    /**
     * Checks if the given item is in the given hand of the given entity.
     *
     * @param hand   the hand to check
     * @param holder the entity to check the hand of
     * @param item   the item to check for
     * @return true if the given item is in the given hand of the given entity, false otherwise
     */
    public static boolean isInHand(InteractionHand hand, LivingEntity holder, Item item) {
        // Get the item that the entity is currently holding in the given hand
        ItemStack heldItem = holder.getItemInHand(hand);

        // Check if the held item is the same as the given item
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
