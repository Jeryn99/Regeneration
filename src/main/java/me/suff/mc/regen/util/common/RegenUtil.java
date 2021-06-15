package me.suff.mc.regen.util.common;

import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.handlers.RegenObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.net.Socket;
import java.util.Random;
import java.util.UUID;

public class RegenUtil {

    public static final Random RAND = new Random();
    public static String[] TIMELORD_NAMES = new String[]{
            "Timelord"
    };
    // Constants
    public static String NO_SKIN = "no_skin";


    public static boolean isSlimSkin(UUID playerUUID) {
        return (playerUUID.hashCode() & 1) == 1;
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = RAND.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static float randFloat(float min, float max) {
        return RAND.nextFloat() * (max - min) + min;
    }

    public static void genCrater(World world, BlockPos pos, int radius) {
        for (int x = pos.getX() - radius; x < pos.getX() + radius; ++x) {
            for (int y = pos.getY() - radius; y < pos.getY() + radius; ++y) {
                for (int z = pos.getZ() - radius; z < pos.getZ() + radius; ++z) {
                    double squareDistance = Math.pow(x - pos.getX(), 2) + Math.pow(y - pos.getY(), 2) + Math.pow(z - pos.getZ(), 2);
                    if (squareDistance <= Math.pow(radius, 2)) {
                        BlockState block = world.getBlockState(new BlockPos(x, y, z));

                        if (block.getBlock() != Blocks.BEDROCK && block.getDestroySpeed(world, new BlockPos(x, y, z)) < 3.0F) {

                            if (!world.isClientSide) {

                                if (world.getBlockEntity(new BlockPos(x, y, z)) != null) {
                                    TileEntity tileEntity = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (tileEntity instanceof IInventory) {
                                        InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
                                        world.updateNeighbourForOutputSignal(pos, block.getBlock());
                                    }
                                }

                                InventoryHelper.dropItemStack(world, x, y, z, new ItemStack(block.getBlock()));
                            }
                            world.setBlockAndUpdate(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    public static void regenerationExplosion(LivingEntity player) {
        explodeKnockback(player, player.level, player.getCommandSenderBlockPosition(), RegenConfig.COMMON.regenerativeKnockback.get(), RegenConfig.COMMON.regenKnockbackRange.get());
        if (player.level.getGameTime() % 10 == 0) {
            explodeHurt(player, player.level, player.getCommandSenderBlockPosition(), RegenConfig.COMMON.regenerativeKillRange.get());
        }
    }

    public static void explodeHurt(Entity exploder, World world, BlockPos pos, int range) {
        world.getEntities(exploder, getReach(pos, range)).forEach(entity -> {
            if ((entity instanceof CreatureEntity && entity.canChangeDimensions()) || (entity instanceof PlayerEntity)) // && RegenConfig.COMMON.regenerationKillsPlayers))
                entity.hurt(RegenObjects.REGEN_DMG_ENERGY_EXPLOSION, 2F);
        });
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

                // float densMod = world.getBlockDensity(new Vec3d(pos), entity.getBoundingBox());

                float densMod = 1;

                int xr, zr;
                xr = (int) -(victim.x - exploder.x);
                zr = (int) -(victim.z - exploder.z);

                victim.knockback(exploder, (float) (knockback * densMod), xr, zr);
            }
        });
    }

    public static boolean doesHaveInternet() {
        try {
            Socket socket = new Socket("www.google.com", 80);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public interface IEnum<E extends Enum<E>> {
        int ordinal();

        default E next() {
            E[] ies = this.getAllValues();
            return this.ordinal() != ies.length - 1 ? ies[this.ordinal() + 1] : null;
        }

        default E previous() {
            return this.ordinal() != 0 ? this.getAllValues()[this.ordinal() - 1] : null;
        }

        @SuppressWarnings("unchecked")
        default E[] getAllValues() {
            IEnum[] ies = this.getClass().getEnumConstants();
            return (E[]) ies;
        }
    }
}
