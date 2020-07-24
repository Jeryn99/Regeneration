package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.common.ICompatObject;
import me.swirtzly.regeneration.util.common.Worldutil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

/**
 * Created by Swirtzly
 * on 20/05/2020 @ 20:39
 */
public class ConfessionDialItem extends Item implements ICompatObject {

    public ConfessionDialItem() {
        super(new Item.Properties().rarity(Rarity.UNCOMMON).group(ItemGroups.REGEN_TAB).maxDamage(10).setNoRepair());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        ItemStack stack = playerIn.getHeldItem(handIn);

        if (playerIn instanceof ServerPlayerEntity && !playerIn.getCooldownTracker().hasCooldown(stack.getItem())) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) playerIn;
            ServerWorld gallifrey = DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), RegenObjects.GALLIFREY_TYPE, false, true);
            ServerWorld overworld = DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), DimensionType.OVERWORLD, false, false);
            if (gallifrey != null && overworld != null) {
                setDamage(stack, getDamage(stack) + 1);

                if (getDamage(stack) == getMaxDamage(stack)) {
                    stack.setCount(0);
                }
                ServerWorld targetDimension = playerEntity.dimension == RegenObjects.GALLIFREY_TYPE ? overworld : gallifrey;
                playerEntity.teleport(targetDimension, playerEntity.posX, Worldutil.getTopBlockForPos(targetDimension, new BlockPos(playerEntity.posX, 35, playerEntity.posZ)), playerEntity.posZ, playerEntity.rotationYaw, playerEntity.rotationPitch);
                playerEntity.getCooldownTracker().setCooldown(stack.getItem().getItem(), 200);
            }
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
