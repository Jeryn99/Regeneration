package me.suff.mc.regen.common.item;

import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.ICompatObject;
import me.suff.mc.regen.util.common.Worldutil;
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
        super(new Item.Properties().rarity(Rarity.UNCOMMON).tab(ItemGroups.REGEN_TAB).durability(10).setNoRepair());
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {

        ItemStack stack = playerIn.getItemInHand(handIn);

        if (playerIn instanceof ServerPlayerEntity && !playerIn.getCooldowns().isOnCooldown(stack.getItem())) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) playerIn;
            ServerWorld gallifrey = DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), RegenObjects.GALLIFREY_TYPE, false, true);
            ServerWorld overworld = DimensionManager.getWorld(ServerLifecycleHooks.getCurrentServer(), DimensionType.OVERWORLD, false, false);
            if (gallifrey != null && overworld != null) {
                setDamage(stack, getDamage(stack) + 1);

                if (getDamage(stack) == getMaxDamage(stack)) {
                    stack.setCount(0);
                }
                ServerWorld targetDimension = playerEntity.dimension == RegenObjects.GALLIFREY_TYPE ? overworld : gallifrey;
                playerEntity.teleportTo(targetDimension, playerEntity.x, Worldutil.getTopBlockForPos(targetDimension, new BlockPos(playerEntity.x, 35, playerEntity.z)), playerEntity.z, playerEntity.yRot, playerEntity.xRot);
                playerEntity.getCooldowns().addCooldown(stack.getItem().getItem(), 200);
            }
        }

        return super.use(worldIn, playerIn, handIn);
    }
}
