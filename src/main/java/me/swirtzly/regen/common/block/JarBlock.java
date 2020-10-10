package me.swirtzly.regen.common.block;

import me.swirtzly.regen.common.tiles.JarTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import org.jetbrains.annotations.Nullable;

public class JarBlock extends DirectionalBlock {

    public JarBlock() {
        super(Properties.create(Material.IRON).notSolid());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new JarTile();
    }
}
