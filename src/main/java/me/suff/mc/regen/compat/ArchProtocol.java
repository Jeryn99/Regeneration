package me.suff.mc.regen.compat;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
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

    private static final TranslationTextComponent ARCH = new TranslationTextComponent("tardis.protocol.arch");
    private static final AxisAlignedBB BOX = new AxisAlignedBB(-20, -20, -20, 20, 20, 20);

    public static BlockPos getGoodArchPlacement(World world, BlockPos consolePos) {
        BlockPos northPos = consolePos.north(2).above(2);
        BlockPos southPos = consolePos.south(2).above(2);
        BlockPos eastPos = consolePos.east(2).above(2);
        BlockPos westPos = consolePos.west(2).above(2);

        if (world.isEmptyBlock(northPos)) {
            return northPos;
        }

        if (world.isEmptyBlock(southPos)) {
            return southPos;
        }

        if (world.isEmptyBlock(eastPos)) {
            return eastPos;
        }

        if (world.isEmptyBlock(westPos)) {
            return westPos;
        }
        return BlockPos.ZERO;
    }

    @Override
    public void call(World world, PlayerEntity player, ConsoleTile consoleTile) { //Add a PlayerEntity parameter for Tardis Mod 1.4
        if (!world.isClientSide()) {
            consoleTile.getUpgrade(ArchUpgrade.class).ifPresent((archSubSystem -> {
                if (archSubSystem.isUsable()) {
                    BlockPos pos = consoleTile.getBlockPos();
                    BlockPos placePos = pos.north(2).above(2);
                    if (world.isEmptyBlock(placePos)) {
                        world.setBlockAndUpdate(placePos, RegenObjects.Blocks.ARCH.get().defaultBlockState());
                        if (consoleTile.getArtron() > 10) {
                            consoleTile.setArtron(consoleTile.getArtron() - 10);
                        }
                        for (PlayerEntity playerEntity : world.getEntitiesOfClass(PlayerEntity.class, BOX.move(consoleTile.getBlockPos()))) {
                            PlayerUtil.sendMessage(playerEntity, "message.regeneration.arch_placed", true);
                        }
                    } else {
                        world.setBlockAndUpdate(placePos, Blocks.AIR.defaultBlockState());
                        TardisCompat.damageSubsystem(world); //Only damage after you retract the arch
                        for (PlayerEntity playerEntity : world.getEntitiesOfClass(PlayerEntity.class, BOX.move(consoleTile.getBlockPos()))) {
                            PlayerUtil.sendMessage(playerEntity, "message.regeneration.arch_removed", true);
                        }
                    }
                } else {
                    for (PlayerEntity playerEntity : world.getEntitiesOfClass(PlayerEntity.class, BOX.move(consoleTile.getBlockPos()))) {
                        PlayerUtil.sendMessage(playerEntity, new TranslationTextComponent("message.regeneration.arch_system_dead"), true);
                    }
                }
            }));
        } else Regeneration.proxy.closeGui();
    }

    @Override
    public String getSubmenu() {
        return "interior";
    }

    //Left here
    public String getDisplayName() {
        return ARCH.getContents();
    }

    @Override
    public String getDisplayName(ConsoleTile consoleTile) {
        return ARCH.getContents();
    }
}
