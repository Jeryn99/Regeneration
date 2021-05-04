package me.suff.mc.regen.common.item;

import me.suff.mc.regen.util.RConstants;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

/* Created by Craig on 18/03/2021 */
public class DimensionCanonItem extends Item {

    public DimensionCanonItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getItemInHand(hand);

        if (playerEntity instanceof ServerPlayerEntity && !playerEntity.getCooldowns().isOnCooldown(stack.getItem())) {

            ThreadTaskExecutor<Runnable> executor = LogicalSidedProvider.WORKQUEUE.get(world.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER);
            executor.tell(new TickDelayedTask(0, () -> {
                teleport((ServerPlayerEntity) playerEntity);

                setDamage(stack, getDamage(stack) + 1);
                if (getDamage(stack) == getMaxDamage(stack)) {
                    stack.setCount(0);
                }
                playerEntity.getCooldowns().addCooldown(stack.getItem().getItem(), 200);
            }));
        }

        return super.use(world, playerEntity, hand);
    }

    public void teleport(ServerPlayerEntity playerEntity) {
        World currentWorld = playerEntity.level;
        boolean isOnOverworld = currentWorld.dimension() == World.OVERWORLD;
        ServerWorld newWorld = currentWorld.getServer().getLevel(isOnOverworld ? RConstants.GALLIFREY : World.OVERWORLD);
        BlockPos tpPos = getTopBlockForPos(playerEntity, newWorld, playerEntity.blockPosition());
        playerEntity.teleportTo(newWorld, tpPos.getX(), tpPos.getY(), tpPos.getZ(), playerEntity.yRot, playerEntity.xRot);
        playerEntity.randomTeleport(playerEntity.blockPosition().getX(), playerEntity.blockPosition().getY(), playerEntity.blockPosition().getZ(), false);
    }

    private BlockPos getTopBlockForPos(PlayerEntity playerEntity, ServerWorld newWorld, BlockPos pos) {
        for (int i = 5; i > 0; i--) {
            for (int y = 0; y < newWorld.getMaxBuildHeight(); y++) {
                BlockPos newPos = new BlockPos(pos.getX() + i * 20, y, pos.getZ() + i * 20);
                if (isTeleportFriendlyBlock(newWorld, pos, playerEntity) && !isPosBelowOrAboveWorld(newWorld, newPos.getY())) {
                    return newPos;
                }
            }
        }
        return newWorld.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos);
    }

    public static boolean isPosBelowOrAboveWorld(World dim, int y) {
        if (dim.dimension() == World.NETHER) {
            return y <= 0 || y >= 126;
        }
        return y <= 0 || y >= 256;
    }

    private static boolean isTeleportFriendlyBlock(World world, BlockPos pos, PlayerEntity playerEntity) {
        BlockState state = world.getBlockState(pos);
        BlockPos blockpos = pos.subtract(playerEntity.blockPosition());
        return state.getMaterial().blocksMotion() && world.noCollision(playerEntity, playerEntity.getBoundingBox().move(blockpos));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeDepleted() {
        return super.canBeDepleted();
    }

    @Override
    public boolean shouldOverrideMultiplayerNbt() {
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 12;
    }
}