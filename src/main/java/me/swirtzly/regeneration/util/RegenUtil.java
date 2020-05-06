package me.swirtzly.regeneration.util;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.handlers.RegenObjects;
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

import java.util.Random;
import java.util.UUID;

import static me.swirtzly.regeneration.util.FileUtil.getJsonFromURL;

public class RegenUtil {

	public static String[] TIMELORD_NAMES = new String[]{
			"Timelord"
	};

	public static String[] downloadNames() {
		String[] names = RegenerationMod.GSON.fromJson(getJsonFromURL("https://raw.githubusercontent.com/Swirtzly/Regeneration/skins/timelord-names.json"), String[].class);
		for (String name : names) {
			System.out.println(name);
		}
		return names;
	}

	private static Random rand = new Random();

	public static boolean isSlimSkin(UUID playerUUID) {
		return (playerUUID.hashCode() & 1) == 1;
	}

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = rand.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	public static float randFloat(float min, float max) {
		return rand.nextFloat() * (max - min) + min;
	}

	public static void genCrater(World world, BlockPos pos, int radius) {
		for (int x = pos.getX() - radius; x < pos.getX() + radius; ++x) {
			for (int y = pos.getY() - radius; y < pos.getY() + radius; ++y) {
				for (int z = pos.getZ() - radius; z < pos.getZ() + radius; ++z) {
					double squareDistance = Math.pow(x - pos.getX(), 2) + Math.pow(y - pos.getY(), 2) + Math.pow(z - pos.getZ(), 2);
					if (squareDistance <= Math.pow(radius, 2)) {
						BlockState block = world.getBlockState(new BlockPos(x, y, z));

						if (block.getBlock() != Blocks.BEDROCK && block.getBlockHardness(world, new BlockPos(x, y, z)) < 3.0F) {

							if (!world.isRemote) {

								if (world.getTileEntity(new BlockPos(x, y, z)) != null) {
									TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
									if (tileEntity instanceof IInventory) {
										InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
										world.updateComparatorOutputLevel(pos, block.getBlock());
									}
								}

								InventoryHelper.spawnItemStack(world, x, y, z, new ItemStack(block.getBlock()));
							}
							world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState());
						}
					}
				}
			}
		}
	}

    public static void regenerationExplosion(LivingEntity player) {
		explodeKnockback(player, player.world, player.getPosition(), RegenConfig.COMMON.regenerativeKnockback.get(), RegenConfig.COMMON.regenKnockbackRange.get());
		explodeKill(player, player.world, player.getPosition(), RegenConfig.COMMON.regenerativeKillRange.get());
	}

    // Constants
    public static String NO_SKIN = "no_skin";

    public static void explodeKill(Entity exploder, World world, BlockPos pos, int range) {
        world.getEntitiesWithinAABBExcludingEntity(exploder, getReach(pos, range)).forEach(entity -> {
            if ((entity instanceof CreatureEntity && entity.isNonBoss()) || (entity instanceof PlayerEntity)) // && RegenConfig.COMMON.regenerationKillsPlayers))
                entity.attackEntityFrom(RegenObjects.REGEN_DMG_ENERGY_EXPLOSION, Float.MAX_VALUE);
        });
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

                // float densMod = world.getBlockDensity(new Vec3d(pos), entity.getBoundingBox());

				float densMod = 1;

                int xr, zr;
				xr = (int) -(victim.posX - exploder.posX);
				zr = (int) -(victim.posZ - exploder.posZ);

                victim.knockBack(exploder, (float) (knockback * densMod), xr, zr);
            }
		});
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
