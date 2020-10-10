package me.swirtzly.regen.common.block;

import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;

public class JarBlock extends DirectionalBlock {

    public JarBlock() {
        super(Properties.create(Material.IRON).notSolid());
    }


}
