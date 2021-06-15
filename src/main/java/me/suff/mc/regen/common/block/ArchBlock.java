package me.suff.mc.regen.common.block;

import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.tiles.ArchTile;
import me.suff.mc.regen.compat.ArchHelper;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.ICompatObject;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItemUseContext;
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
    public boolean hasDynamicShape() {
        return true;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide) return false;
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack headItem = player.getItemBySlot(EquipmentSlotType.HEAD);
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
                PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.stored_item", stored, new TranslationTextComponent(mainHandItem.getDescriptionId())), true);
                worldIn.removeBlock(pos, false);
                cap.synchronise();
                return true;
            }

            if (cap.getRegenerationsLeft() >= 0 && regensLeftInHand > 0) {
                int needed = RegenConfig.COMMON.regenCapacity.get() - cap.getRegenerationsLeft(), used = Math.min(regensLeftInHand, needed);
                ArchHelper.storeRegenerations(mainHandItem, regensLeftInHand - used);
                PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.item_taken_regens", used, new TranslationTextComponent(mainHandItem.getDescriptionId())), true);
                worldIn.removeBlock(pos, false);
                cap.receiveRegenerations(used);
                cap.synchronise();
                return true;
            }

            PlayerUtil.sendMessage(player, new TranslationTextComponent("regeneration.messages.regen_fail"), true);
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
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
