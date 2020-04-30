package me.swirtzly.regeneration.compat;

import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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


    public static BlockPos getGoodArchPlacement(World world, BlockPos consolePos) {
        BlockPos northPos = consolePos.north(2).up(2);
        BlockPos southPos = consolePos.south(2).up(2);
        BlockPos eastPos = consolePos.east(2).up(2);
        BlockPos westPos = consolePos.west(2).up(2);

        if (world.isAirBlock(northPos)) {
            return northPos;
        }

        if (world.isAirBlock(southPos)) {
            return southPos;
        }

        if (world.isAirBlock(eastPos)) {
            return eastPos;
        }

        if (world.isAirBlock(westPos)) {
            return westPos;
        }
        return BlockPos.ZERO;
    }

    @Override
    public void call(World world, ConsoleTile consoleTile) {
        consoleTile.getSubsystem(ArchSubSystem.class).ifPresent((archSubSystem -> {
            if (archSubSystem.canBeUsed()) {
                BlockPos pos = consoleTile.getPos();
                BlockPos placePos = pos.north(2).up(2);

                if (world.isAirBlock(placePos)) {
                    world.setBlockState(placePos, RegenObjects.Blocks.ARCH.get().getDefaultState());
                    TardisCompat.damageSubsystem(world);
                    if (consoleTile.getArtron() > 10) {
                        consoleTile.setArtron(consoleTile.getArtron() - 10);
                    }
                } else {
                    BlockState console = world.getBlockState(pos);
                    for (PlayerEntity playerEntity : world.getEntitiesWithinAABB(PlayerEntity.class, console.getCollisionShape(world, pos).getBoundingBox().grow(25))) {
                        PlayerUtil.sendMessage(playerEntity, new TranslationTextComponent("message.regeneration.arch_no_space"), false);
                    }
                }
            } else {
                BlockState console = world.getBlockState(consoleTile.getPos());
                for (PlayerEntity playerEntity : world.getEntitiesWithinAABB(PlayerEntity.class, console.getCollisionShape(world, consoleTile.getPos()).getBoundingBox().grow(25))) {
                    PlayerUtil.sendMessage(playerEntity, new TranslationTextComponent("message.regeneration.arch_system_dead"), false);
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
