package me.swirtzly.regeneration.compat;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.tardis.mod.protocols.Protocol;
import net.tardis.mod.tileentities.ConsoleTile;

/**
 * Created by Swirtzly
 * on 22/04/2020 @ 11:07
 */
public class ArchProtocol extends Protocol {

    private static TranslationTextComponent ARCH = new TranslationTextComponent("tardis.protocol.arch");


    @Override
    public void call(World world, ConsoleTile consoleTile) {
        consoleTile.getSubsystem(ArchSubSystem.class).ifPresent((archSubSystem -> {
            if (archSubSystem.canBeUsed()) {
                BlockPos pos = consoleTile.getPos();
                BlockPos placePos = pos.north(2).up(2);
                if (world.isAirBlock(placePos)) {
                    world.setBlockState(pos.north(2).up(2), RegenObjects.Blocks.ARCH.getDefaultState());
                    TardisCompat.damageSubsystem(world);
                    if (consoleTile.getArtron() > 10) {
                        consoleTile.setArtron(consoleTile.getArtron() - 10);
                    }
                }
            }
        }));
    }

    @Override
    public String getSubmenu() {
        return "interior";
    }

    @Override
    public String getDisplayName() {
        return ARCH.getUnformattedComponentText();
    }
}
