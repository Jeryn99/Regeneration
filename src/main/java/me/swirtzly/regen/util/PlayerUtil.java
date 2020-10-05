package me.swirtzly.regen.util;

import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.network.NetworkDispatcher;
import me.swirtzly.regen.network.messages.ModelMessage;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {


    public static ArrayList<Effect> POTIONS = new ArrayList();

    public static void setupPotions() {
        for (String name : RegenConfig.COMMON.postRegenEffects.get()) {
            for (Effect effect : ForgeRegistries.POTIONS.getValues()) {
                if (name.contentEquals(effect.getRegistryName().toString())) {
                    POTIONS.add(effect);
                }
            }
        }
    }


    public static void sendMessage(LivingEntity livingEntity, String message, boolean hotBar) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) livingEntity;
        if (!player.world.isRemote) {
            player.sendStatusMessage(new TranslationTextComponent(message), hotBar);
        }
    }

    public static void sendMessage(LivingEntity livingEntity, TranslationTextComponent translation, boolean hotBar) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) livingEntity;
        if (!player.world.isRemote) {
            player.sendStatusMessage(translation, hotBar);
        }
    }

    public static void sendMessageToAll(TranslationTextComponent translation) {
        List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        players.forEach(playerMP -> sendMessage(playerMP, translation, false));
    }

    public static boolean applyPotionIfAbsent(LivingEntity player, Effect potion, int length, int amplifier, boolean ambient, boolean showParticles) {
        if (potion == null) return false;
        if (player.getActivePotionEffect(potion) == null) {
            player.addPotionEffect(new EffectInstance(potion, length, amplifier, ambient, showParticles));
            return true;
        }
        return false;
    }

    public static AxisAlignedBB getReach(BlockPos pos, int range) {
        return new AxisAlignedBB(pos.up(range).north(range).west(range), pos.down(range).south(range).east(range));
    }

    public static void explodeKnockback(Entity exploder, World world, BlockPos pos, double knockback, int range) {
        world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
            if (entity instanceof LivingEntity && exploder.isAlive()) {
                LivingEntity victim = (LivingEntity) entity;

                if (entity instanceof PlayerEntity && !RegenConfig.COMMON.regenerationKnocksbackPlayers.get() || !victim.isNonBoss())
                    return;

                float densMod = Explosion.getBlockDensity(entity.getPositionVec(), entity);

                int xr, zr;
                xr = (int) -(victim.getPosX() - exploder.getPosX());
                zr = (int) -(victim.getPosZ() - exploder.getPosZ());
                victim.setMotion(victim.getMotion().mul((knockback * densMod), xr, zr));
            }
        });
    }

    public static void regenerationExplosion(LivingEntity player) {
        explodeKnockback(player, player.world, new BlockPos(player.getPositionVec()), RegenConfig.COMMON.regenerativeKnockback.get(), RegenConfig.COMMON.regenKnockbackRange.get());
        explodeKill(player, player.world, new BlockPos(player.getPositionVec()), RegenConfig.COMMON.regenerativeKillRange.get());
    }

    public static void explodeKill(Entity exploder, World world, BlockPos pos, int range) {
        world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
            if ((entity instanceof CreatureEntity && entity.isNonBoss()) || (entity instanceof PlayerEntity)) // && RegenConfig.COMMON.regenerationKillsPlayers))
                entity.attackEntityFrom(RegenSources.REGEN_DMG_ENERGY_EXPLOSION, 3.5F);
        });
    }

    public static void updateModel(SkinType choices) {
        NetworkDispatcher.NETWORK_CHANNEL.sendToServer(new ModelMessage(choices));
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
