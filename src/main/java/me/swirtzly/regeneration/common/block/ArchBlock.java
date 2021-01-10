package me.swirtzly.regeneration.common.block;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.tiles.ArchTile;
import me.swirtzly.regeneration.compat.ArchHelper;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.common.ICompatObject;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Swirtzly
 * on 22/04/2020 @ 11:46
 */
public class ArchBlock extends DirectionalBlock implements ICompatObject {
    public ArchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isVariableOpacity() {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) return false;
        ItemStack mainHandItem = player.getHeldItemMainhand();
        ItemStack headItem = player.getItemStackFromSlot(EquipmentSlotType.HEAD);
        IRegen cap = RegenCap.get(player).orElse(null);
        int regensLeftInHand = ArchHelper.getRegenerations(mainHandItem);

        if (mainHandItem.isEmpty()) return false;
        if (handIn == Hand.MAIN_HAND && cap.getState() == PlayerUtil.RegenState.ALIVE) {

            if (mainHandItem.getItem() == this.asItem() || mainHandItem.getItem() == RegenObjects.Blocks.HAND_JAR.get().asItem()) {
                PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.item_invalid"), true);
                return true;
            }

            if (cap.getRegenerationsLeft() > 0 && regensLeftInHand == 0) {
                int stored = cap.getRegenerationsLeft();
                ArchHelper.storeRegenerations(mainHandItem, cap.getRegenerationsLeft());
                cap.extractRegeneration(cap.getRegenerationsLeft());
                PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.stored_item", stored, new TranslationTextComponent(mainHandItem.getTranslationKey())), true);
                worldIn.removeBlock(pos, false);
                cap.synchronise();
                return true;
            }

            if (cap.getRegenerationsLeft() >= 0 && regensLeftInHand > 0) {
                int needed = RegenConfig.COMMON.regenCapacity.get() - cap.getRegenerationsLeft(), used = Math.min(regensLeftInHand, needed);
                ArchHelper.storeRegenerations(mainHandItem, regensLeftInHand - used);
                PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.item_taken_regens", used, new TranslationTextComponent(mainHandItem.getTranslationKey())), true);
                worldIn.removeBlock(pos, false);
                cap.receiveRegenerations(used);
                cap.synchronise();
                return true;
            }

            PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.regen_fail"), true);
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ArchTile();
    }


}
